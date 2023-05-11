package hufs.team.mogong.team.service.dto.response;

import hufs.team.mogong.team.service.dto.response.TimeTableResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ResultTimeResponses {

	private Integer divisorMinutes;
	private TimeTableResponse[] times;
}

