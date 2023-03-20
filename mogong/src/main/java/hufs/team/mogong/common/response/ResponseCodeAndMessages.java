package hufs.team.mogong.common.response;

import hufs.team.mogong.common.CodeAndMessages;
import lombok.Getter;

@Getter
public enum ResponseCodeAndMessages implements CodeAndMessages {

	/**
	 * IMAGE
	 */
	GENERATE_IMAGE_PRESIGNED_URL_SUCCESS("I-S001", "이미지 PreSigned URL 생성에 성공했습니다."),

	/**
	 * Team
	 */
	CREATE_TEAM_SUCCESS("T-S001", "팀 생성에 성공했습니다."),
	UPLOAD_SINGLE_MEMBER_IMAGE_SUCCESS("T-S002", "팀 멤버의 이미지 등록에 성공했습니다."),
	UPLOAD_ALL_MEMBER_IMAGE_SUCCESS("T-S003", "팀 멤버의 모든 이미지가 등록되었습니다. 결과를 요청해주세요"),
	GENERATE_TEAM_RESULT_SUCCESS("T-S004", "팀 결과 생성에 성공했습니다."),
	;

	private final String code;
	private final String message;


	ResponseCodeAndMessages(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
