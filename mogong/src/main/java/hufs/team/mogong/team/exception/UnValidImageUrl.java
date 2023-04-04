package hufs.team.mogong.team.exception;

import hufs.team.mogong.common.exception.BaseException;
import hufs.team.mogong.common.exception.ErrorCodeAndMessages;

public class UnValidImageUrl extends BaseException {

	public UnValidImageUrl() {
		super(ErrorCodeAndMessages.UN_VALID_IMAGE_URL);
	}
}
