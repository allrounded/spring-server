package hufs.team.mogong.common.response;

import hufs.team.mogong.common.CodeAndMessages;
import lombok.Getter;

@Getter
public enum ResponseCodeAndMessages implements CodeAndMessages {

	/**
	 * IMAGE
	 */
	GENERATE_IMAGE_PRESIGNED_URL_SUCCESS("I-S001", "이미지 PreSigned URL 생성에 성공했습니다.")
	;

	private final String code;
	private final String message;


	ResponseCodeAndMessages(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
