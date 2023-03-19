package hufs.team.mogong.team.service;

import hufs.team.mogong.team.service.dto.request.UploadTeamRequest;
import hufs.team.mogong.team.service.dto.request.CreateTeamRequest;
import hufs.team.mogong.team.service.dto.response.CreateTeamResponse;
import hufs.team.mogong.team.service.dto.response.TimeResponses;
import hufs.team.mogong.team.service.dto.response.UploadTeamResponse;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class TeamService {

	@Transactional
	public CreateTeamResponse create(CreateTeamRequest request) {
		return new CreateTeamResponse("sample-team-id", 5, 0);
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
