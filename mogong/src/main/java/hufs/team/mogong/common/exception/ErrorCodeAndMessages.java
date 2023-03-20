package hufs.team.mogong.common.exception;

import hufs.team.mogong.common.CodeAndMessages;
import lombok.Getter;

@Getter
public enum ErrorCodeAndMessages implements CodeAndMessages {

	/**
	 * Image
	 */
	GENERATE_IMAGE_PRESIGNED_URL_FAILED("I-F001", "이미지 PreSigned URL 생성에 성공했습니다."),

	/**
	 * Team
	 */
	NOT_FOUND_TEAM_ID("T-F001", "해당 팀 ID를 찾을 수 없습니다."),
	;
	private final String code;
	private final String message;

	ErrorCodeAndMessages(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
