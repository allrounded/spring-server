package hufs.team.mogong.image.exception;

import hufs.team.mogong.common.exception.BaseException;
import hufs.team.mogong.common.exception.ErrorCodeAndMessages;

public class NotFoundImageException extends BaseException {

	public NotFoundImageException() {
		super(ErrorCodeAndMessages.NOT_FOUND_IMAGE);
	}
}
