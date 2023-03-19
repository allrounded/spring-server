package hufs.team.mogong.team.service.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TimeResponses {

	private Integer divisorMinutes;
	private List<TimeDetailResponse> times;
}
