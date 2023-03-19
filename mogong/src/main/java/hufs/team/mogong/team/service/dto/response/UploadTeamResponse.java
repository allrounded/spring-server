package hufs.team.mogong.team.service.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UploadTeamResponse {

	private final String teamId;
	private final Integer numberOfTeam;
	private final Integer submit;
	private final String resultImageUrl;
	private final TimeResponses timeResponses;

	public boolean hasResult() {
		return resultImageUrl != null;
	}
}
