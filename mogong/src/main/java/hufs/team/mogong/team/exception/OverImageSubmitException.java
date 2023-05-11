package hufs.team.mogong.team.exception;

import hufs.team.mogong.common.exception.BaseException;
import hufs.team.mogong.common.exception.ErrorCodeAndMessages;

public class OverImageSubmitException extends BaseException {

	public OverImageSubmitException() {
		super(ErrorCodeAndMessages.OVER_IMAGE_SUBMIT);
	}
}
