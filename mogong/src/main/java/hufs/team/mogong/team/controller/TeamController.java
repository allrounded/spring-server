package hufs.team.mogong.team.controller;

import static hufs.team.mogong.common.response.ResponseCodeAndMessages.CREATE_TEAM_SUCCESS;
import static hufs.team.mogong.common.response.ResponseCodeAndMessages.GENERATE_TEAM_RESULT_SUCCESS;
import static hufs.team.mogong.common.response.ResponseCodeAndMessages.UPLOAD_SINGLE_MEMBER_IMAGE_SUCCESS;

import hufs.team.mogong.common.response.BaseResponse;
import hufs.team.mogong.team.service.TeamService;
import hufs.team.mogong.team.service.dto.request.CreateTeamRequest;
import hufs.team.mogong.team.service.dto.request.UploadTeamRequest;
import hufs.team.mogong.team.service.dto.response.CreateTeamResponse;
import hufs.team.mogong.team.service.dto.response.UploadTeamResponse;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
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
	public BaseResponse<CreateTeamResponse> create(final @Valid @RequestBody CreateTeamRequest request) {
		CreateTeamResponse response = teamService.create(request);
		return new BaseResponse<>(CREATE_TEAM_SUCCESS, response);
	}

	@PostMapping("/{teamId}")
	public BaseResponse<UploadTeamResponse> upload(final @PathVariable String teamId,
		final @Valid @RequestBody UploadTeamRequest request) {
		UploadTeamResponse response = teamService.upload(teamId, request);
		if (response.hasResult()) {
			return new BaseResponse<>(GENERATE_TEAM_RESULT_SUCCESS, response);
		}
		return new BaseResponse<>(UPLOAD_SINGLE_MEMBER_IMAGE_SUCCESS, response);
	}
}
