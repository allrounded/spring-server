package hufs.team.mogong.team.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TeamResponse {

	private final String teamId;
	private final Integer numberOfTeam;
	private final Integer submit;
}
