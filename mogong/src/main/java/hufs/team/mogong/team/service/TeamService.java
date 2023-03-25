package hufs.team.mogong.team.service;

import hufs.team.mogong.image.Image;
import hufs.team.mogong.image.repository.ImageRepository;
import hufs.team.mogong.image.service.ImageUploadService;
import hufs.team.mogong.image.service.dto.PreSignedUrlRequest;
import hufs.team.mogong.image.service.dto.PreSignedUrlResponse;
import hufs.team.mogong.team.DayOfWeek;
import hufs.team.mogong.team.Team;
import hufs.team.mogong.team.exception.NotCompletedSubmit;
import hufs.team.mogong.team.exception.NotFoundTeamIdException;
import hufs.team.mogong.team.exception.NotMatchAuthCode;
import hufs.team.mogong.team.repository.TeamRepository;
import hufs.team.mogong.team.service.dto.request.CreateTeamRequest;
import hufs.team.mogong.team.service.dto.request.ImageUrlRequest;
import hufs.team.mogong.team.service.dto.request.TeamResultRequest;
import hufs.team.mogong.team.service.dto.request.UploadTeamRequest;
import hufs.team.mogong.team.service.dto.response.CreateTeamResponse;
import hufs.team.mogong.team.service.dto.response.TeamResultResponse;
import hufs.team.mogong.team.service.dto.response.TimeDetailResponse;
import hufs.team.mogong.team.service.dto.response.TimeResponses;
import hufs.team.mogong.team.service.dto.response.UploadTeamResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
	@Value("${image-server.url}")
	private String imageServerUrl;

	private final TeamRepository teamRepository;
	private final ImageRepository imageRepository;
	private final RestTemplate restTemplate;
	private final ImageUploadService imageUploadService;


	public TeamService(TeamRepository teamRepository, ImageRepository imageRepository,
		RestTemplate restTemplate, ImageUploadService imageUploadService) {
		this.teamRepository = teamRepository;
		this.imageRepository = imageRepository;
		this.restTemplate = restTemplate;
		this.imageUploadService = imageUploadService;
	}

	@Transactional
	public CreateTeamResponse create(CreateTeamRequest request) {
		log.debug("REQUEST NUMBER OF TEAM = {}", request.getNumberOfTeam());
		Team team = teamRepository.save(
			new Team(UUID.randomUUID().toString(), request.getNumberOfTeam(), request.getAuthCode()));
		log.debug("[SAVE TEAM] teamId ={}, name ={}, numberOfMember = {}", team.getTeamId(), team.getTeamName(), team.getNumberOfMember());
		return new CreateTeamResponse(team.getTeamId(), team.getTeamName(), team.getNumberOfMember(), ZERO, team.getAuthCode());
	}

	@Transactional
	public UploadTeamResponse upload(Long teamId, UploadTeamRequest request) {
		Team team = teamRepository.findById(teamId)
			.orElseThrow(NotFoundTeamIdException::new);
		List<Image> images = imageRepository.findAllByTeam_TeamId(teamId);
		int imageSize = images.size();

		log.debug("[요청 전 IMAGE 제출 수] {}", imageSize);
		if (!checkImageSize(team, images)) {
			imageRepository.save(new Image(team, request.getImageUrl()));
			imageSize++;
		}

		log.debug("[요청 후 IMAGE 제출 수] {}", imageSize);
		return new UploadTeamResponse(team.getTeamId(),
			team.getTeamName(),
			team.getNumberOfMember(),
			imageSize);
	}

	public TeamResultResponse getResult(Long teamId, String authCode) {
		Team team = teamRepository.findById(teamId)
			.orElseThrow(NotFoundTeamIdException::new);
		List<Image> images = imageRepository.findAllByTeam_TeamId(teamId);

		if (isNull(authCode) && !checkImageSize(team, images)) {
			throw new NotCompletedSubmit();
		}

		if (!isNull(authCode) && !Objects.equals(team.getAuthCode(), authCode)) {
			throw new NotMatchAuthCode();
		}
		// Flask 호출
		log.debug("[Flask 호출]");
		/**
		TeamResultRequest request = createTeamResultRequest(team, images);
		TeamResultResponse response = createTeamResultResponse(request, team);
		log.debug("TeamResultResponse FROM Flask Server = {}", response);
		 */

		List<TimeDetailResponse> times = new ArrayList<>();
		times.add(new TimeDetailResponse(DayOfWeek.MON, "09:00~09:30"));
		times.add(new TimeDetailResponse(DayOfWeek.MON, "09:30~10:00"));
		times.add(new TimeDetailResponse(DayOfWeek.SUN, "09:00~09:30"));
		return new TeamResultResponse(
			"https://mogong.s3.ap-northeast-2.amazonaws.com/image/sample_5.JPG",
			new TimeResponses(30, times));
	}

	private boolean checkImageSize(Team team, List<Image> images) {
		return team.getNumberOfMember() == images.size();
	}

	private boolean isNull(String authCode){
		return authCode == null;
	}

	private ImageUrlRequest getResultPreSignedUrl() {
		String resultPreSignedUrl = imageUploadService
			.generate(new PreSignedUrlRequest(".jpeg"))
			.getPreSignedUrl();
		log.debug("Result PreSignedUrl = {}", resultPreSignedUrl);
		return new ImageUrlRequest(resultPreSignedUrl);
	}

	private TeamResultRequest createTeamResultRequest(Team team,  List<Image> images) {
		return new TeamResultRequest(
			team.getTeamName(),
			team.getNumberOfMember(),
			images.stream()
				.map(ImageUrlRequest::from)
				.collect(Collectors.toList()),
			getResultPreSignedUrl()
		);
	}

	private TeamResultResponse createTeamResultResponse(TeamResultRequest request, Team team) {
		return restTemplate.postForEntity(
			imageServerUrl,
			request,
			TeamResultResponse.class,
			team.getTeamId()
		).getBody();
	}
}
