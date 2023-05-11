package hufs.team.mogong.team.service.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CreateTeamResponse {

	private final Long teamId;
	private final String teamName;
	private final Integer numberOfMember;
	private final Integer numberOfSubmit;
	private final String authCode;
	private final String imageUrl;
}
