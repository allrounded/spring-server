package hufs.team.mogong.team.service.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ImageServerTimeResponses {

	private Integer code;

	private Integer divisorMinutes;
	private TimeTableResponse[] times;
}
