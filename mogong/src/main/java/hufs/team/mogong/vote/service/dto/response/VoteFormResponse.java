package hufs.team.mogong.vote.service.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class VoteFormResponse {

	private final Long teamId;
	private final Integer divisorMinutes;
	private final boolean duplicate;
}
