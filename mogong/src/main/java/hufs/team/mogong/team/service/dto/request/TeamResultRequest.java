package hufs.team.mogong.team.service.dto.request;

import hufs.team.mogong.team.service.dto.response.ResultTimeResponses;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TeamResultRequest {

	private Long teamId;
	private String teamName;
	private ResultTimeResponses timeResponses;

}
