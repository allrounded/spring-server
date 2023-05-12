package hufs.team.mogong.image.exception;

import hufs.team.mogong.common.exception.BaseException;
import hufs.team.mogong.common.exception.ErrorCodeAndMessages;

public class NotFoundMemberImageException extends BaseException {

	public NotFoundMemberImageException() {
		super(ErrorCodeAndMessages.NOT_FOUND_MEMBER_IMAGE);
	}
}
