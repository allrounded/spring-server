package hufs.team.mogong.team.service.dto.request;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CreateTeamRequest {

	@Positive
	private Integer numberOfTeam;

	@Pattern(regexp = "^\\d{4}$",
		message = "4자리의 auth code를 작성해주세요. 각 자리는 0~9까지의 정수만 가능합니다.")
	private String authCode;
}
