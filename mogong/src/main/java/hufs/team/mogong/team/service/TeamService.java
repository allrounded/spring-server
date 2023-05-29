package hufs.team.mogong.team.service;

import hufs.team.mogong.image.TeamImage;
import hufs.team.mogong.image.exception.NotFoundTeamImageException;
import hufs.team.mogong.image.repository.MemberImageRepository;
import hufs.team.mogong.image.repository.TeamImageRepository;
import hufs.team.mogong.image.service.ImageUploadService;
import hufs.team.mogong.image.service.dto.PreSignedUrlRequest;
import hufs.team.mogong.member.Member;
import hufs.team.mogong.member.repository.MemberRepository;
import hufs.team.mogong.member.service.dto.response.MemberResultResponse;
import hufs.team.mogong.team.DayOfWeek;
import hufs.team.mogong.team.Team;
import hufs.team.mogong.team.exception.ImageServerError;
import hufs.team.mogong.team.exception.NotCompletedSubmit;
import hufs.team.mogong.team.exception.NotFoundTeamIdException;
import hufs.team.mogong.team.exception.NotFoundTeamNameException;
import hufs.team.mogong.team.repository.TeamRepository;
import hufs.team.mogong.team.service.dto.request.CreateTeamRequest;
import hufs.team.mogong.team.service.dto.request.TeamResultRequest;
import hufs.team.mogong.team.service.dto.request.TimeRequests;
import hufs.team.mogong.team.service.dto.request.TimeTableRequest;
import hufs.team.mogong.team.service.dto.response.CreateTeamResponse;
import hufs.team.mogong.team.service.dto.response.ImageServerResultResponse;
import hufs.team.mogong.team.service.dto.response.TeamIdResponse;
import hufs.team.mogong.team.service.dto.response.TeamResponse;
import hufs.team.mogong.team.service.dto.response.TimeResponses;
import hufs.team.mogong.team.service.dto.response.TimeTableResponse;
import hufs.team.mogong.timetable.MemberTimeTableV1;
import hufs.team.mogong.timetable.MemberTimeTableV2;
import hufs.team.mogong.timetable.TeamTimeTable;
import hufs.team.mogong.timetable.repository.MemberTimeTableV1Repository;
import hufs.team.mogong.timetable.repository.MemberTimeTableV2Repository;
import hufs.team.mogong.timetable.repository.TeamTimeTableRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
	private static final String JPEG = ".jpeg";
	private static final String DEFAULT_TEAM_IMAGE_PATH = "image/teams/";
	private static final int ZERO_NUMERIC_VALUE = Character.getNumericValue('0');

	@Value("${image-server.result}")
	private String imageResultUrl;
	@Value("${image-server.url}")
	private String imageServerUrl;

	private final TeamRepository teamRepository;
	private final TeamImageRepository teamImageRepository;
	private final MemberRepository memberRepository;
	private final MemberTimeTableV1Repository memberTimeTableV1Repository;
	private final MemberTimeTableV2Repository memberTimeTableV2Repository;
	private final TeamTimeTableRepository teamTimeTableRepository;

	private final RestTemplate restTemplate;
	private final ImageUploadService imageUploadService;


	public TeamService(TeamRepository teamRepository, TeamImageRepository teamImageRepository,
		MemberRepository memberRepository, MemberTimeTableV1Repository memberTimeTableV1Repository,
		MemberTimeTableV2Repository memberTimeTableV2Repository,
		TeamTimeTableRepository teamTimeTableRepository, RestTemplate restTemplate,
		ImageUploadService imageUploadService) {
		this.teamRepository = teamRepository;
		this.teamImageRepository = teamImageRepository;
		this.memberRepository = memberRepository;
		this.memberTimeTableV1Repository = memberTimeTableV1Repository;
		this.memberTimeTableV2Repository = memberTimeTableV2Repository;
		this.teamTimeTableRepository = teamTimeTableRepository;
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
		TeamImage image = teamImageRepository.save(new TeamImage(team, getPreSignedUrl(team)));

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

	private String getPreSignedUrl(Team team) {
		return imageUploadService
			.generateTeamUrl(new PreSignedUrlRequest(JPEG),
				DEFAULT_TEAM_IMAGE_PATH+team.getTeamId()+"/"+team.getTeamName())
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

		List<MemberResultResponse> memberResultResponses = members.stream()
			.map(m -> {log.debug("MEMBER ID={}의 이미지 조회", m.getMemberId());
				return new MemberResultResponse(m.getMemberId(), m.getNickName(), m.isSubmit(), m.isLeader());})
			.collect(Collectors.toList());

		TimeResponses timeResponses = getTeamResult(team, members, isV1, authCode);

		return new TeamResponse(
			team.getTeamId(),
			team.getTeamName(),
			team.getNumberOfMember(),
			team.getNumberOfSubmit(),
			memberResultResponses,
			teamImage.getUrl().split("\\?")[0],
			timeResponses);
	}

	private TimeResponses getTeamResult(Team team, List<Member> members, boolean isV1, String authCode) {
		if (isV1) {
			log.debug("[V1 Result]");
			return getTeamResultV1(team, members, authCode);
		}
		log.debug("[V2 Result]");
		return getTeamResultV2(team, members, authCode);
	}

	private TimeResponses getTeamResultV1(Team team, List<Member> members, String authCode) {
		if (checkSubmit(team.getNumberOfMember(), team.getNumberOfSubmit())
			|| validateAuthCode(team, authCode)) {
			int[][] times = new int[DAY_OF_WEEK][TIME_LENGTH];
			for (Member member : members) {
				MemberTimeTableV1 timeTable = findTimeTableV1ByMemberId(member, team, authCode);
				timeTable.accumulate(times, TIME_LENGTH);
			}
			TimeTableRequest[] timeTableResponses = getTimeTableRequests(times);
			ImageServerResultResponse resultResponse = requestTeamResult(getTeamResultRequest(team, timeTableResponses));
			if (resultResponse == null) {
				throw new ImageServerError();
			}
			resultResponse.checkStatusCode();

			return new TimeResponses(DIVISOR_MINUTES, getTimeTableResponses(times));
		}

		throw new NotCompletedSubmit(team.getNumberOfMember(), team.getNumberOfSubmit());
	}

	private TimeResponses getTeamResultV2(Team team, List<Member> members, String authCode) {
		if (checkSubmit(team.getNumberOfMember(), team.getNumberOfSubmit())
			|| validateAuthCode(team, authCode)) {
			long[] times = new long[DAY_OF_WEEK];
			for (Member member : members) {
				MemberTimeTableV2 timeTable = findTimeTableV2ByMemberId(member, team, authCode);
				timeTable.accumulate(times);
			}
			upsertTeamTimeTable(times, team);

			int[][] renewalTimes = changeTimesToIntArray(times);
			TimeTableRequest[] timeTableResponses = getTimeTableRequests(renewalTimes);
			ImageServerResultResponse resultResponse = requestTeamResult(getTeamResultRequest(team, timeTableResponses));
			if (resultResponse == null) {
				throw new ImageServerError();
			}
			resultResponse.checkStatusCode();

			return new TimeResponses(DIVISOR_MINUTES, getTimeTableResponses(renewalTimes));
		}

		throw new NotCompletedSubmit(team.getNumberOfMember(), team.getNumberOfSubmit());
	}

	private void upsertTeamTimeTable(long[] times, Team team) {
		Optional<TeamTimeTable> teamTimeTable = teamTimeTableRepository.findByTeam_TeamId(team.getTeamId());
		if (teamTimeTable.isPresent()) {
			log.debug("TEAM TIME TABLE UPDATE : teamId = {}", team.getTeamId());
			TeamTimeTable timeTable = teamTimeTable.get();
			timeTable.update(times);
			return;
		}
		log.debug("TEAM TIME TABLE CREATE : teamId = {}", team.getTeamId());
		teamTimeTableRepository.save(new TeamTimeTable(team, times));
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

	private TimeTableRequest[] getTimeTableRequests(int[][] times) {
		TimeTableRequest[] timeTables = new TimeTableRequest[DAY_OF_WEEK];
		timeTables[0] = new TimeTableRequest(DayOfWeek.MON, times[0]);
		timeTables[1] = new TimeTableRequest(DayOfWeek.TUE, times[1]);
		timeTables[2] = new TimeTableRequest(DayOfWeek.WED, times[2]);
		timeTables[3] = new TimeTableRequest(DayOfWeek.THU, times[3]);
		timeTables[4] = new TimeTableRequest(DayOfWeek.FRI, times[4]);
		timeTables[5] = new TimeTableRequest(DayOfWeek.SAT, times[5]);
		timeTables[6] = new TimeTableRequest(DayOfWeek.SUN, times[6]);
		return timeTables;
	}

	private TimeTableResponse[] getTimeTableResponses(int[][] times) {
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

	private TeamResultRequest getTeamResultRequest(Team team, TimeTableRequest[] timeTableRequests) {
		return new TeamResultRequest(
			team.getTeamId(),
			team.getTeamName(),
			new TimeRequests(DIVISOR_MINUTES, timeTableRequests)
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

	private MemberTimeTableV1 findTimeTableV1ByMemberId(Member member, Team team, String authCode) {
		if (validateAuthCode(team, authCode)) {
			return memberTimeTableV1Repository.findByMember_MemberId(member.getMemberId())
				.orElse(MemberTimeTableV1.getDefault(member, TIME_LENGTH));
		}

		return memberTimeTableV1Repository.findByMember_MemberId(member.getMemberId())
			.orElseThrow(() -> new NotCompletedSubmit(team.getNumberOfMember(), team.getNumberOfSubmit()));
	}

	private MemberTimeTableV2 findTimeTableV2ByMemberId(Member member, Team team, String authCode) {
		if (validateAuthCode(team, authCode)) {
			return memberTimeTableV2Repository.findByMember_MemberId(member.getMemberId())
				.orElse(MemberTimeTableV2.getDefault(member));
		}
		return memberTimeTableV2Repository.findByMember_MemberId(member.getMemberId())
			.orElseThrow(() -> new NotCompletedSubmit(team.getNumberOfMember(), team.getNumberOfSubmit()));
	}

	private Team findTeamByTeamId(Long teamId){
		return teamRepository.findById(teamId)
			.orElseThrow(NotFoundTeamIdException::new);
	}

	private TeamImage findTeamImageByTeamId(Long teamId) {
		return teamImageRepository.findByTeam_TeamId(teamId)
			.orElseThrow(NotFoundTeamImageException::new);
	}
}
