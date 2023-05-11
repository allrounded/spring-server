package hufs.team.mogong.team.controller;

import static hufs.team.mogong.common.response.ResponseCodeAndMessages.CREATE_TEAM_SUCCESS;
import static hufs.team.mogong.common.response.ResponseCodeAndMessages.FIND_TEAM_ID_SUCCESS;
import static hufs.team.mogong.common.response.ResponseCodeAndMessages.FIND_TEAM_SUCCESS;

import hufs.team.mogong.common.response.BaseResponse;
import hufs.team.mogong.team.service.TeamService;
import hufs.team.mogong.team.service.dto.request.CreateTeamRequest;
import hufs.team.mogong.team.service.dto.response.CreateTeamResponse;
import hufs.team.mogong.team.service.dto.response.TeamIdResponse;
import hufs.team.mogong.team.service.dto.response.TeamResponse;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teams")
public class TeamController {

	private final TeamService teamService;

	public TeamController(TeamService teamService) {
		this.teamService = teamService;
	}

	@PostMapping
	public BaseResponse<CreateTeamResponse> create(final @Valid @RequestBody CreateTeamRequest request) {
		CreateTeamResponse response = teamService.create(request);
		return new BaseResponse<>(CREATE_TEAM_SUCCESS, response);
	}

	@GetMapping("/{teamId}/v1")
	public BaseResponse<TeamResponse> findByIdV1(final @PathVariable Long teamId,
		final @RequestParam(name = "auth_code", required = false) String authCode) {
		TeamResponse response = teamService.findById(teamId, true, authCode);
		return new BaseResponse<>(FIND_TEAM_SUCCESS, response);
	}

	@GetMapping("/{teamId}/v2")
	public BaseResponse<TeamResponse> findByIdV2(final @PathVariable Long teamId,
		final @RequestParam(name = "auth_code", required = false) String authCode) {
		TeamResponse response = teamService.findById(teamId, false, authCode);
		return new BaseResponse<>(FIND_TEAM_SUCCESS, response);
	}

	@GetMapping("/names/{teamName}")
	public BaseResponse<TeamIdResponse> findId(final @PathVariable String teamName) {
		TeamIdResponse response = teamService.findId(teamName);
		return new BaseResponse<>(FIND_TEAM_ID_SUCCESS, response);
	}

	/**
	 * Team 삭제
	 */

	/**
	 * Team 인원 수정하기
	 */
}
