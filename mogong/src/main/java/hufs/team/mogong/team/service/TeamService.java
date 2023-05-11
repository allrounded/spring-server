package hufs.team.mogong.team.service;

import hufs.team.mogong.image.MemberImage;
import hufs.team.mogong.image.TeamImage;
import hufs.team.mogong.image.exception.NotFoundImageException;
import hufs.team.mogong.image.repository.MemberImageRepository;
import hufs.team.mogong.image.repository.TeamImageRepository;
import hufs.team.mogong.image.service.ImageUploadService;
import hufs.team.mogong.image.service.dto.PreSignedUrlRequest;
import hufs.team.mogong.member.Member;
import hufs.team.mogong.member.repository.MemberRepository;
import hufs.team.mogong.member.service.dto.response.MemberResponse;
import hufs.team.mogong.team.DayOfWeek;
import hufs.team.mogong.team.Team;
import hufs.team.mogong.team.exception.ImageServerError;
import hufs.team.mogong.team.exception.NotCompletedSubmit;
import hufs.team.mogong.team.exception.NotFoundTeamIdException;
import hufs.team.mogong.team.exception.NotFoundTeamNameException;
import hufs.team.mogong.team.repository.TeamRepository;
import hufs.team.mogong.team.service.dto.request.CreateTeamRequest;
import hufs.team.mogong.team.service.dto.response.ImageServerTimeResponses;
import hufs.team.mogong.team.service.dto.response.ResultTimeResponses;
import hufs.team.mogong.team.service.dto.request.TeamResultRequest;
import hufs.team.mogong.team.service.dto.response.CreateTeamResponse;
import hufs.team.mogong.team.service.dto.response.ImageServerResultResponse;
import hufs.team.mogong.team.service.dto.response.TeamIdResponse;
import hufs.team.mogong.team.service.dto.response.TeamResponse;
import hufs.team.mogong.team.service.dto.response.TimeResponses;
import hufs.team.mogong.team.service.dto.response.TimeTableResponse;
import hufs.team.mogong.timetable.TimeTableV1;
import hufs.team.mogong.timetable.TimeTableV2;
import hufs.team.mogong.timetable.repository.TimeTableV1Repository;
import hufs.team.mogong.timetable.repository.TimeTableV2Repository;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@Transactional(readOnly = true)
public class TeamService {

	private static final int ZERO = 0;
	private static final int DIVISOR_MINUTES = 30;
	private static final int DAY_OF_WEEK = 7;
	private static final int TIME_LENGTH = 30;
	private static final int AUTH_CODE_LENGTH = 4;
	private static final String JPEG = ".jpeg";
	private static final String DEFAULT_TEAM_IMAGE_PATH = "image/teams/";
	private static final int ZERO_NUMERIC_VALUE = Character.getNumericValue('0');

	@Value("${image-server.result}")
	private String imageResultUrl;
	@Value("${image-server.url}")
	private String imageServerUrl;

	private final TeamRepository teamRepository;
	private final MemberImageRepository memberImageRepository;
	private final TeamImageRepository teamImageRepository;
	private final MemberRepository memberRepository;
	private final TimeTableV1Repository timeTableV1Repository;
	private final TimeTableV2Repository timeTableV2Repository;

	private final RestTemplate restTemplate;
	private final ImageUploadService imageUploadService;


	public TeamService(TeamRepository teamRepository, MemberImageRepository memberImageRepository,
		TeamImageRepository teamImageRepository, MemberRepository memberRepository,
		TimeTableV1Repository timeTableV1Repository, TimeTableV2Repository timeTableV2Repository,
		RestTemplate restTemplate, ImageUploadService imageUploadService) {
		this.teamRepository = teamRepository;
		this.memberImageRepository = memberImageRepository;
		this.teamImageRepository = teamImageRepository;
		this.memberRepository = memberRepository;
		this.timeTableV1Repository = timeTableV1Repository;
		this.timeTableV2Repository = timeTableV2Repository;
		this.restTemplate = restTemplate;
		this.imageUploadService = imageUploadService;
	}

	@Transactional
	public CreateTeamResponse create(CreateTeamRequest request) {
		log.debug("[TEAM] REQUEST NUMBER OF TEAM = {}", request.getNumberOfTeam());
		Team team = teamRepository.save(
			new Team(
				UUID.randomUUID().toString(),
				request.getNumberOfTeam(),
				ZERO,
				request.getAuthCode()));
		TeamImage image = teamImageRepository.save(
			new TeamImage(team, getPreSignedUrl(team.getTeamId()))
		);

		log.debug("[SAVE TEAM] teamId ={}, name ={}, numberOfMember = {}, imageUrl={}",
			team.getTeamId(), team.getTeamName(), team.getNumberOfMember(), image.getUrl());

		return new CreateTeamResponse(
			team.getTeamId(),
			team.getTeamName(),
			team.getNumberOfMember(),
			ZERO,
			team.getAuthCode(),
			image.getUrl());
	}

	private String getPreSignedUrl(long teamId) {
		return imageUploadService
			.generate(new PreSignedUrlRequest(JPEG), DEFAULT_TEAM_IMAGE_PATH+teamId+"/")
			.getPreSignedUrl();
	}

	public TeamIdResponse findId(String teamName) {
		log.debug("[Team ID 조회] teamName = {}", teamName);
		Team team = teamRepository.findByTeamName(teamName)
			.orElseThrow(NotFoundTeamNameException::new);
		return new TeamIdResponse(team.getTeamName(), team.getTeamId());
	}

	@Transactional
	public TeamResponse findById(Long teamId, boolean isV1, String authCode) {
		log.debug("[Team 현황 조회] teamId = {}", teamId);
		Team team = findTeamByTeamId(teamId);
		TeamImage teamImage = findTeamImageByTeamId(teamId);
		List<Member> members = memberRepository.findAllByTeam_TeamId(teamId);

		List<MemberResponse> memberResponses = members.stream()
			.map(m -> new MemberResponse(m.getMemberId(), m.getNickName(),
				findMemberImageByMemberId(m.getMemberId()).getUrl(), m.isSubmit()))
			.collect(Collectors.toList());

		TimeResponses timeResponses = getTeamResult(team, members, teamImage, isV1, authCode);

		return new TeamResponse(
			team.getTeamId(),
			team.getTeamName(),
			team.getNumberOfMember(),
			team.getNumberOfSubmit(),
			memberResponses,
			teamImage.getUrl().split("\\?")[0],
			timeResponses);
	}

	private TimeResponses getTeamResult(Team team, List<Member> members, TeamImage teamImage,
		boolean isV1, String authCode) {
		if (isV1) {
			log.debug("[V1 Result]");
			return getTeamResultV1(team, members, teamImage, authCode);
		}
		log.debug("[V2 Result]");
		return getTeamResultV2(team, members, teamImage, authCode);
	}

	private TimeResponses getTeamResultV1(Team team, List<Member> members, TeamImage teamImage,
		String authCode) {
		if (checkSubmit(team.getNumberOfMember(), team.getNumberOfSubmit())) {
			int[][] times = new int[DAY_OF_WEEK][TIME_LENGTH];
			for (Member member : members) {
				TimeTableV1 timeTable = findTimeTableV1ByMemberId(member, team, authCode);
				timeTable.accumulate(times, TIME_LENGTH);
			}
			TimeTableResponse[] timeTableResponses = getTimeTables(times);
			ImageServerResultResponse resultResponse = requestTeamResult(getTeamResultRequest(team, teamImage, timeTableResponses));
			if (resultResponse == null) {
				throw new ImageServerError();
			}
			resultResponse.checkStatusCode();

			return new TimeResponses(DIVISOR_MINUTES, getTimeTables(times));
		}

		return null;
	}

	private TimeResponses getTeamResultV2(Team team, List<Member> members, TeamImage teamImage,
		String authCode) {
		if (checkSubmit(team.getNumberOfMember(), team.getNumberOfSubmit())) {
			long[] times = new long[DAY_OF_WEEK];
			for (Member member : members) {
				TimeTableV2 timeTable = findTimeTableV2ByMemberId(member, team, authCode);
				timeTable.accumulate(times);
			}
			int[][] renewalTimes = changeTimesToIntArray(times);
			TimeTableResponse[] timeTableResponses = getTimeTables(renewalTimes);
			ImageServerResultResponse resultResponse = requestTeamResult(getTeamResultRequest(team, teamImage, timeTableResponses));
			if (resultResponse == null) {
				throw new ImageServerError();
			}
			resultResponse.checkStatusCode();

			return new TimeResponses(DIVISOR_MINUTES, getTimeTables(renewalTimes));
		}

		return null;
	}

	private int[][] changeTimesToIntArray(long[] times) {
		int idx = 0;
		int[][] renewalTimes = new int[DAY_OF_WEEK][TIME_LENGTH];
		for (long time : times) {
			char[] chars = Long.toBinaryString(time).toCharArray();
			for (int i = 0; i < chars.length ; i++) {
				renewalTimes[idx][TIME_LENGTH-1-i] = Character.getNumericValue(chars[chars.length-1-i]) - ZERO_NUMERIC_VALUE;
			}
			idx++;
		}

		return renewalTimes;
	}

	private TimeTableResponse[] getTimeTables(int[][] times) {
		TimeTableResponse[] timeTables = new TimeTableResponse[DAY_OF_WEEK];
		timeTables[0] = new TimeTableResponse(DayOfWeek.MON, times[0]);
		timeTables[1] = new TimeTableResponse(DayOfWeek.TUE, times[1]);
		timeTables[2] = new TimeTableResponse(DayOfWeek.WED, times[2]);
		timeTables[3] = new TimeTableResponse(DayOfWeek.THU, times[3]);
		timeTables[4] = new TimeTableResponse(DayOfWeek.FRI, times[4]);
		timeTables[5] = new TimeTableResponse(DayOfWeek.SAT, times[5]);
		timeTables[6] = new TimeTableResponse(DayOfWeek.SUN, times[6]);
		return timeTables;
	}

	private TeamResultRequest getTeamResultRequest(Team team, TeamImage image, TimeTableResponse[] timeTableResponses) {
		return new TeamResultRequest(
			team.getTeamId(),
			team.getTeamName(),
			image.getUrl(),
			new ResultTimeResponses(DIVISOR_MINUTES, timeTableResponses)
		);
	}

	private ImageServerResultResponse requestTeamResult(TeamResultRequest request) {
		return restTemplate.postForObject(
			imageResultUrl,
			request,
			ImageServerResultResponse.class,
			request.getTeamId()
		);
	}

	private boolean checkSubmit(int numberOfMember, int numberOfSubmit) {
		log.debug("[CHECK SUBMIT] numberOfMember={}, numberOfSubmit={}", numberOfMember, numberOfSubmit);
		return numberOfMember <= numberOfSubmit;
	}

	private boolean validateAuthCode(Team team, String authCode) {
		return Objects.equals(team.getAuthCode(), authCode);
	}

	private TimeTableV1 findTimeTableV1ByMemberId(Member member, Team team, String authCode) {
		if (validateAuthCode(team, authCode)) {
			return timeTableV1Repository.findByMember_MemberId(member.getMemberId())
				.orElse(TimeTableV1.getDefault(member, TIME_LENGTH));
		}

		return timeTableV1Repository.findByMember_MemberId(member.getMemberId())
			.orElseThrow(() -> new NotCompletedSubmit(team.getNumberOfMember(), team.getNumberOfSubmit()));
	}

	private TimeTableV2 findTimeTableV2ByMemberId(Member member, Team team, String authCode) {
		if (validateAuthCode(team, authCode)) {
			return timeTableV2Repository.findByMember_MemberId(member.getMemberId())
				.orElse(TimeTableV2.getDefault(member));
		}

		return timeTableV2Repository.findByMember_MemberId(member.getMemberId())
			.orElseThrow(() -> new NotCompletedSubmit(team.getNumberOfMember(), team.getNumberOfSubmit()));
	}

	private MemberImage findMemberImageByMemberId(Long memberId) {
		return memberImageRepository.findByMember_MemberId(memberId)
			.orElseThrow(NotFoundImageException::new);
	}

	private Team findTeamByTeamId(Long teamId){
		return teamRepository.findById(teamId)
			.orElseThrow(NotFoundTeamIdException::new);
	}

	private TeamImage findTeamImageByTeamId(Long teamId) {
		return teamImageRepository.findByTeam_TeamId(teamId)
			.orElseThrow(NotFoundImageException::new);
	}
}
