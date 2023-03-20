package hufs.team.mogong.team.service;

import hufs.team.mogong.image.Image;
import hufs.team.mogong.image.repository.ImageRepository;
import hufs.team.mogong.team.DayOfWeek;
import hufs.team.mogong.team.Team;
import hufs.team.mogong.team.exception.NotCompletedSubmit;
import hufs.team.mogong.team.exception.NotFoundTeamIdException;
import hufs.team.mogong.team.repository.TeamRepository;
import hufs.team.mogong.team.service.dto.request.CreateTeamRequest;
import hufs.team.mogong.team.service.dto.request.UploadTeamRequest;
import hufs.team.mogong.team.service.dto.response.CreateTeamResponse;
import hufs.team.mogong.team.service.dto.response.ResultTeamResponse;
import hufs.team.mogong.team.service.dto.response.TimeDetailResponse;
import hufs.team.mogong.team.service.dto.response.TimeResponses;
import hufs.team.mogong.team.service.dto.response.UploadTeamResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class TeamService {

	private static final int ZERO = 0;

	private final TeamRepository teamRepository;
	private final ImageRepository imageRepository;

	public TeamService(TeamRepository teamRepository, ImageRepository imageRepository) {
		this.teamRepository = teamRepository;
		this.imageRepository = imageRepository;
	}

	@Transactional
	public CreateTeamResponse create(CreateTeamRequest request) {
		log.debug("REQUEST NUMBER OF TEAM = {}", request.getNumberOfTeam());
		Team team = teamRepository.save(
			new Team(UUID.randomUUID().toString(), request.getNumberOfTeam())
		);
		log.debug("[SAVE TEAM] teamId ={}, name ={}, numberOfMember = {}", team.getTeamId(), team.getTeamName(), team.getNumberOfMember());
		return new CreateTeamResponse(team.getTeamId(), team.getTeamName(), team.getNumberOfMember(), ZERO);
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

	public ResultTeamResponse getResult(Long teamId) {
		Team team = teamRepository.findById(teamId)
			.orElseThrow(NotFoundTeamIdException::new);
		List<Image> images = imageRepository.findAllByTeam_TeamId(teamId);
		if (!checkImageSize(team, images)) {
			throw new NotCompletedSubmit();
		}
		// Flask 호출
		log.debug("[Flask 호출]");

		List<TimeDetailResponse> times = new ArrayList<>();
		times.add(new TimeDetailResponse(DayOfWeek.MON, "09:00~09:30"));
		times.add(new TimeDetailResponse(DayOfWeek.MON, "09:30~10:00"));
		times.add(new TimeDetailResponse(DayOfWeek.SUN, "09:00~09:30"));
		return new ResultTeamResponse(
			"https://mogong.s3.ap-northeast-2.amazonaws.com/image/sample_5.JPG",
			new TimeResponses(30, times));
	}

	private boolean checkImageSize(Team team, List<Image> images) {
		return team.getNumberOfMember() == images.size();
	}
}
