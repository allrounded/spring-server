package hufs.team.mogong.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{

	private final String code;

	public BaseException(ErrorCodeAndMessages errorCodeAndMessages) {
		super(errorCodeAndMessages.getMessage());
		this.code = errorCodeAndMessages.getCode();
	}

}
