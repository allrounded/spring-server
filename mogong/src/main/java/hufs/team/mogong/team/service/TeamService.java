package hufs.team.mogong.team.service;

import hufs.team.mogong.team.service.dto.TeamRequest;
import hufs.team.mogong.team.service.dto.TeamResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class TeamService {

	@Transactional
	public TeamResponse create(TeamRequest request) {
		return new TeamResponse("sample-team-id", 5, 0);
	}
}
