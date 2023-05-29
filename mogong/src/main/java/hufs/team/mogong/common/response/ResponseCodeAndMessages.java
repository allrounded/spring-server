package hufs.team.mogong.common.response;

import hufs.team.mogong.common.CodeAndMessages;
import lombok.Getter;

@Getter
public enum ResponseCodeAndMessages implements CodeAndMessages {

	/**
	 * Image
	 */
	GENERATE_IMAGE_PRESIGNED_URL_SUCCESS("I-S001", "이미지 PreSigned URL 생성에 성공했습니다."),

	/**
	 * Team
	 */
	CREATE_TEAM_SUCCESS("T-S001", "팀 생성에 성공했습니다."),
	FIND_TEAM_ID_SUCCESS("T-S005", "팀 아이디 조회에 성공했습니다."),
	FIND_TEAM_SUCCESS("T-S006", "팀 현황 조회에 성공했습니다."),

	/**
	 * Member
	 */
	CREATE_MEMBER_SUCCESS("M-S001", "팀원 생성이 완료되었습니다."),
	FIND_MEMBER_SUCCESS("M-S002", "팀원 조회에 성공했습니다."),
	UPLOAD_MEMBER_IMAGE_SUCCESS("M-S003", "팀원의 이미지 업로드에 성공했습니다."),

	/**
	 * Vote
	 */
	CREATE_VOTE_FORM_SUCCESS("V-S001", "팀 투표 폼 생성이 완료되었습니다."),
	FIND_VOTE_FORM_SUCCESS("V-S002", "팀 투표 폼 조회에 성공했습니다."),
	;

	private final String code;
	private final String message;


	ResponseCodeAndMessages(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
