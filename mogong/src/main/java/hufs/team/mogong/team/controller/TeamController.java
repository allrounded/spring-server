package hufs.team.mogong.team.controller;

import static hufs.team.mogong.common.response.ResponseCodeAndMessages.CREATE_TEAM_SUCCESS;

import hufs.team.mogong.common.response.BaseResponse;
import hufs.team.mogong.team.service.TeamService;
import hufs.team.mogong.team.service.dto.TeamRequest;
import hufs.team.mogong.team.service.dto.TeamResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teams")
public class TeamController {

	private final TeamService teamService;

	public TeamController(TeamService teamService) {
		this.teamService = teamService;
	}

	@PostMapping
	public BaseResponse<TeamResponse> create(final @RequestBody TeamRequest request) {
		TeamResponse response = teamService.create(request);
		return new BaseResponse<>(CREATE_TEAM_SUCCESS, response);
	}

}
