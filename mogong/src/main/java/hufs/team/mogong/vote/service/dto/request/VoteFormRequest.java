package hufs.team.mogong.vote.service.dto.request;

import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class VoteFormRequest {

	@Positive
	private Integer divisorMinutes;
	private boolean duplicate;
}
