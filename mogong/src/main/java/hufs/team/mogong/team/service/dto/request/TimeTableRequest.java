package hufs.team.mogong.team.service.dto.request;

import hufs.team.mogong.team.DayOfWeek;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TimeTableRequest {

	private DayOfWeek dayOfWeek;
	private int[] time;

}
