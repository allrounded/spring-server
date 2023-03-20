package hufs.team.mogong.team.exception;

import hufs.team.mogong.common.exception.BaseException;
import hufs.team.mogong.common.exception.ErrorCodeAndMessages;

public class NotFoundTeamIdException extends BaseException {

	public NotFoundTeamIdException() {
		super(ErrorCodeAndMessages.NOT_FOUND_TEAM_ID);
	}
}
