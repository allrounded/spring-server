package hufs.team.mogong.team.exception;

import hufs.team.mogong.common.exception.BaseException;
import hufs.team.mogong.common.exception.ErrorCodeAndMessages;

public class NotCompletedSubmit extends BaseException {

	public NotCompletedSubmit() {
		super(ErrorCodeAndMessages.NOT_COMPLETED_SUBMIT);
	}
}
