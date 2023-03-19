package hufs.team.mogong.team.service;

import hufs.team.mogong.team.Team;
import hufs.team.mogong.team.repository.TeamRepository;
import hufs.team.mogong.team.service.dto.request.CreateTeamRequest;
import hufs.team.mogong.team.service.dto.request.UploadTeamRequest;
import hufs.team.mogong.team.service.dto.response.CreateTeamResponse;
import hufs.team.mogong.team.service.dto.response.TimeResponses;
import hufs.team.mogong.team.service.dto.response.UploadTeamResponse;
import java.util.ArrayList;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TeamService {

	private static final int ZERO = 0;

	private final TeamRepository teamRepository;

	public TeamService(TeamRepository teamRepository) {
		this.teamRepository = teamRepository;
	}

	@Transactional
	public CreateTeamResponse create(CreateTeamRequest request) {
		log.debug("REQUEST NUMBER OF TEAM = {}", request.getNumberOfTeam());
		Team team = teamRepository.save(
			new Team(UUID.randomUUID().toString(), request.getNumberOfTeam())
		);
		log.debug("[SAVE TEAM] teamId ={}, numberOfMember = {}", team.getTeamId(), team.getNumberOfMember());
		return new CreateTeamResponse(team.getTeamId(), team.getNumberOfMember(), ZERO);
	}

	@Transactional
	public UploadTeamResponse upload(String teamId, UploadTeamRequest request) {
		/**
		 * TODO findByTeamId -> submit 횟수 체크 -> 충족되었는지 여부에 따라 분기문 처리
		 */
		return new UploadTeamResponse(teamId, 5, 2,
			null,
			new TimeResponses(30, new ArrayList<>()));
	}
}
