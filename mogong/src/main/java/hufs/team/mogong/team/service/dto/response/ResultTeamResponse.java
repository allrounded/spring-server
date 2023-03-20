package hufs.team.mogong.team.service.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ResultTeamResponse {

	private final String resultImageUrl;
	private final TimeResponses timeResponses;

	public boolean hasResult() {
		return resultImageUrl != null;
	}
}
