package hufs.team.mogong.team.exception;

import hufs.team.mogong.common.exception.BaseException;
import hufs.team.mogong.common.exception.ErrorCodeAndMessages;

public class NotFoundTeamNameException extends BaseException {


	public NotFoundTeamNameException() {
		super(ErrorCodeAndMessages.NOT_FOUND_TEAM_NAME);
	}
}
