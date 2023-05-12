package hufs.team.mogong.member.service.dto.response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class MemberResultResponse {

	private final Long memberId;
	private final String nickName;
	private final boolean submit;

}
