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
	NOT_COMPLETED_SUBMIT("T-F002", "현재 등록된 이미지 수가 지정된 멤버 수보다 적습니다."),
	NOT_MATCH_AUTH_CODE("T-F003", "AUTH CODE가 일치하지 않습니다."),
	UN_VALID_IMAGE_URL("T-F004", "시간표 이미지가 아닙니다. 이미지를 다시 확인해주세요."),
	NOT_FOUND_TEAM_NAME("T-F005", "해당 팀 NAME을 찾을 수 없습니다."),
	;
	private final String code;
	private final String message;

	ErrorCodeAndMessages(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
