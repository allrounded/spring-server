package hufs.team.mogong.member.exception;

import hufs.team.mogong.common.exception.BaseException;
import hufs.team.mogong.common.exception.ErrorCodeAndMessages;

public class NotFoundMemberIdException extends BaseException {

	public NotFoundMemberIdException() {
		super(ErrorCodeAndMessages.NOT_FOUND_MEMBER_ID);
	}
}
