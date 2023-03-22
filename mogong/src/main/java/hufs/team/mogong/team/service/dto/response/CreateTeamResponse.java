package hufs.team.mogong.team.service.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CreateTeamResponse {

	private final Long teamId;
	private final String teamName;
	private final Integer numberOfTeam;
	private final Integer submit;
	private final String authCode;
}
