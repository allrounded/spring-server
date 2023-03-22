package hufs.team.mogong.team.exception;

import hufs.team.mogong.common.exception.BaseException;
import hufs.team.mogong.common.exception.ErrorCodeAndMessages;

public class NotMatchAuthCode extends BaseException {

	public NotMatchAuthCode() {
		super(ErrorCodeAndMessages.NOT_MATCH_AUTH_CODE);
	}
}
