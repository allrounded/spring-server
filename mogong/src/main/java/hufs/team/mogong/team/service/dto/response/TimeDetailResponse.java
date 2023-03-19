package hufs.team.mogong.team.service.dto.response;

import hufs.team.mogong.team.DayOfWeek;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TimeDetailResponse {

	private DayOfWeek dayOfWeek;
	private String time;

}
