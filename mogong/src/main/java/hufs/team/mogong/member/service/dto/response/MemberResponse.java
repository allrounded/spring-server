package hufs.team.mogong.member.service.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class MemberResponse {

	private final Long memberId;
	private final String nickName;
	private final String imageUrl;
	private final boolean submit;

	private final boolean leader;

}
