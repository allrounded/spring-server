package hufs.team.mogong.team.service;

import hufs.team.mogong.image.Image;
import hufs.team.mogong.image.repository.ImageRepository;
import hufs.team.mogong.team.Team;
import hufs.team.mogong.team.exception.NotFoundTeamIdException;
import hufs.team.mogong.team.repository.TeamRepository;
import hufs.team.mogong.team.service.dto.request.CreateTeamRequest;
import hufs.team.mogong.team.service.dto.request.UploadTeamRequest;
import hufs.team.mogong.team.service.dto.response.CreateTeamResponse;
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

		log.debug("[IMAGE 제출 수] {}", images.size());
		if (team.getNumberOfMember() == images.size()) {
			// Flask 호출
			log.debug("[Flask 호출]");
		}

		return new UploadTeamResponse(
			teamId,
			team.getTeamName(),
			team.getNumberOfMember(),
			images.size(),
			null,
			new TimeResponses(30, new ArrayList<>()));
	}
}
