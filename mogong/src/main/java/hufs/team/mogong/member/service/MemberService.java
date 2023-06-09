package hufs.team.mogong.member.service;

import hufs.team.mogong.image.MemberImage;
import hufs.team.mogong.image.exception.NotFoundMemberImageException;
import hufs.team.mogong.image.repository.MemberImageRepository;
import hufs.team.mogong.image.service.ImageUploadService;
import hufs.team.mogong.image.service.dto.PreSignedUrlRequest;
import hufs.team.mogong.member.Member;
import hufs.team.mogong.member.exception.NotFoundMemberIdException;
import hufs.team.mogong.member.repository.MemberRepository;
import hufs.team.mogong.member.service.dto.request.CreateMemberRequest;
import hufs.team.mogong.member.service.dto.request.MemberImageUploadRequest;
import hufs.team.mogong.member.service.dto.response.MemberImageUploadResponse;
import hufs.team.mogong.member.service.dto.response.MemberResponse;
import hufs.team.mogong.team.Team;
import hufs.team.mogong.team.exception.NotFoundTeamIdException;
import hufs.team.mogong.team.repository.TeamRepository;
import hufs.team.mogong.team.service.dto.response.ImageServerTimeResponses;
import hufs.team.mogong.team.service.dto.response.TimeTableResponse;
import hufs.team.mogong.timetable.MemberTimeTableV1;
import hufs.team.mogong.timetable.MemberTimeTableV2;
import hufs.team.mogong.timetable.repository.MemberTimeTableV1Repository;
import hufs.team.mogong.timetable.repository.MemberTimeTableV2Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@Transactional(readOnly = true)
public class MemberService {

	private static final String JPEG = ".jpeg";
	private static final String DEFAULT_MEMBER_IMAGE_PATH = "image/teams/";

	private final MemberRepository memberRepository;
	private final TeamRepository teamRepository;
	private final MemberImageRepository memberImageRepository;
	private final ImageUploadService imageUploadService;
	private final MemberTimeTableV1Repository memberTimeTableV1Repository;
	private final MemberTimeTableV2Repository memberTimeTableV2Repository;
	private final RestTemplate restTemplate;
	@Value("${image-server.url}")
	private String imageServerUrl;

	public MemberService(MemberRepository memberRepository, TeamRepository teamRepository,
		MemberImageRepository memberImageRepository, ImageUploadService imageUploadService,
		MemberTimeTableV1Repository memberTimeTableV1Repository, MemberTimeTableV2Repository memberTimeTableV2Repository,
		RestTemplate restTemplate) {
		this.memberRepository = memberRepository;
		this.teamRepository = teamRepository;
		this.memberImageRepository = memberImageRepository;
		this.imageUploadService = imageUploadService;
		this.memberTimeTableV1Repository = memberTimeTableV1Repository;
		this.memberTimeTableV2Repository = memberTimeTableV2Repository;
		this.restTemplate = restTemplate;
	}

	@Transactional
	public MemberResponse create(Long teamId, CreateMemberRequest request) {
		log.debug("[MEMBER CREATE REQUEST] teamId={}, nickName={}", teamId, request.getNickName());

		Team team = findTeamByTeamId(teamId);

		Member savedMember = memberRepository.save(
			new Member(request.getNickName(), team, request.isLeader()));
		log.debug("[MEMBER CREATE] MEMBER SAVE 성공");

		MemberImage savedImage = memberImageRepository.save(new MemberImage(savedMember, getPreSignedUrl(teamId)));
		log.debug("[S3] pre-signed url 생성 성공");

		return new MemberResponse(
			savedMember.getMemberId(),
			savedMember.getNickName(),
			savedImage.getUrl(),
			savedMember.isSubmit(),
			savedMember.isLeader());
	}

	private String getPreSignedUrl(long teamId) {
		return imageUploadService
			.generate(new PreSignedUrlRequest(JPEG), DEFAULT_MEMBER_IMAGE_PATH+teamId+"/")
			.getPreSignedUrl();
	}

	public MemberResponse findById(Long memberId) {
		Member member = findMemberByMemberId(memberId);
		log.debug("[MEMBER] 조회 성공 | memberId={}", member.getMemberId());

		MemberImage image = memberImageRepository.findByMember_MemberId(memberId)
			.orElseThrow(NotFoundMemberImageException::new);
		log.debug("[IMAGE] 조회 성공 | imageId={}", image.getId());

		return new MemberResponse(
			member.getMemberId(),
			member.getNickName(),
			image.getUrl(),
			member.isSubmit(),
			member.isLeader()
		);
	}

	@Transactional
	public MemberImageUploadResponse upsertV1(Long teamId, Long memberId) {
		Team team = findTeamByTeamId(teamId);
		Member member = findMemberByMemberId(memberId);
		MemberImage image = findImageByMemberId(memberId);
		List<String> times = requestImageProcessingV1(team, member, image);

		Optional<MemberTimeTableV1> timeTableV1 = memberTimeTableV1Repository.findByMember_MemberId(memberId);

		if (timeTableV1.isEmpty()) {
			memberTimeTableV1Repository.save(new MemberTimeTableV1(member, times));
			team.addSubmit();
			member.submit();
			log.debug("[MEMBER IMAGE FIRST UPLOAD] team numberOfSubmit={}", team.getNumberOfSubmit());
			return new MemberImageUploadResponse(
				team.getTeamName(),
				team.getNumberOfMember(),
				team.getNumberOfSubmit(),
				member.getNickName(),
				image.getUrl(),
				Boolean.TRUE,
				member.isLeader());
		}

		timeTableV1.get().update(times);
		log.debug("[MEMBER IMAGE SECOND UPLOAD] team numberOfSubmit={}", team.getNumberOfSubmit());
		return new MemberImageUploadResponse(
			team.getTeamName(),
			team.getNumberOfMember(),
			team.getNumberOfSubmit(),
			member.getNickName(),
			image.getUrl(),
			Boolean.TRUE,
			member.isLeader());
	}

	@Transactional
	public MemberImageUploadResponse upsertV2(Long teamId, Long memberId) {
		Team team = findTeamByTeamId(teamId);
		Member member = findMemberByMemberId(memberId);
		MemberImage image = findImageByMemberId(memberId);
		List<Long> times = requestImageProcessingV2(team, member, image);

		Optional<MemberTimeTableV2> timeTableV2 = memberTimeTableV2Repository.findByMember_MemberId(memberId);

		if (timeTableV2.isEmpty()) {
			memberTimeTableV2Repository.save(new MemberTimeTableV2(member, times));
			member.submit();
			team.addSubmit();
			log.debug("[MEMBER IMAGE FIRST UPLOAD] team numberOfSubmit={}", team.getNumberOfSubmit());
			return new MemberImageUploadResponse(
				team.getTeamName(),
				team.getNumberOfMember(),
				team.getNumberOfSubmit(),
				member.getNickName(),
				image.getUrl().split("\\?")[0],
				Boolean.TRUE,
				member.isLeader());
		}

		timeTableV2.get().update(times);
		log.debug("[MEMBER IMAGE SECOND UPLOAD] team numberOfSubmit={}", team.getNumberOfSubmit());
		return new MemberImageUploadResponse(
			team.getTeamName(),
			team.getNumberOfMember(),
			team.getNumberOfSubmit(),
			member.getNickName(),
			image.getUrl(),
			Boolean.TRUE,
			member.isLeader());
	}

	private List<String> requestImageProcessingV1(Team team, Member member, MemberImage image) {
		ImageServerTimeResponses timeResponses = getTimeResponses(team, member, image);
		TimeTableResponse[] times = timeResponses.getTimes();
		List<String> timeResults = new ArrayList<>();

		StringBuilder sb = new StringBuilder();

		for (TimeTableResponse timeTable : times) {
			int[] time = timeTable.getTime();
			sb.setLength(0);
			for (int t : time) {
				sb.append(t);
			}
			timeResults.add(sb.toString());
		}

		return timeResults;
	}

	private List<Long> requestImageProcessingV2(Team team, Member member, MemberImage image) {
		ImageServerTimeResponses timeResponses = getTimeResponses(team, member, image);
		log.debug("TIME RESPONSES={}", timeResponses.toString());
		TimeTableResponse[] times = timeResponses.getTimes();
		List<Long> timeResults = new ArrayList<>();
		StringBuilder sb = new StringBuilder();

		for (TimeTableResponse timeTable : times) {
			int[] time = timeTable.getTime();
			sb.setLength(0);
			for (int t : time) {
				sb.append(t);
			}
			timeResults.add(Long.parseLong(sb.toString(), 2));
		}

		return timeResults;
	}

	private ImageServerTimeResponses getTimeResponses(Team team, Member member, MemberImage image) {
		return restTemplate.postForObject(
			imageServerUrl,
			new MemberImageUploadRequest(team.getTeamName(),
//				"https://mogong.s3.ap-northeast-2.amazonaws.com/image/sample_3.JPG"
				image.getUrl().split("\\?")[0]
			),
			ImageServerTimeResponses.class,
			team.getTeamId(), member.getMemberId()
		);
	}

	private Member findMemberByMemberId(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(NotFoundMemberIdException::new);
	}

	private Team findTeamByTeamId(Long teamId) {
		return teamRepository.findById(teamId)
			.orElseThrow(NotFoundTeamIdException::new);
	}

	private MemberImage findImageByMemberId(Long memberId) {
		return memberImageRepository.findByMember_MemberId(memberId)
			.orElseThrow(NotFoundMemberImageException::new);
	}
}
