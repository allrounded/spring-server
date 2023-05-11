package hufs.team.mogong.team.service.dto.response;

import hufs.team.mogong.member.service.dto.response.MemberResponse;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TeamResponse {

	private final Long teamId;
	private final String teamName;
	private final Integer numberOfMember;
	private final Integer numberOfSubmit;
	private final List<MemberResponse> members;
	private final String resultImageUrl;
	private final TimeResponses timeResponses;
}
