package hufs.team.mogong.team.exception;

import hufs.team.mogong.common.exception.BaseException;
import hufs.team.mogong.common.exception.ErrorCodeAndMessages;

public class ImageServerError extends BaseException {

	public ImageServerError() {
		super(ErrorCodeAndMessages.IMAGE_SERVER_ERROR);
	}
}
