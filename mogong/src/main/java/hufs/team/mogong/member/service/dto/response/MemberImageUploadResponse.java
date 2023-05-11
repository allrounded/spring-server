package hufs.team.mogong.member.service.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class MemberImageUploadResponse {

	private final String teamName;
	private final Integer numberOfMember;
	private final Integer numberOfSubmit;
	private final String nickName;
	private final String imageUrl;
	private final boolean submit;
}
