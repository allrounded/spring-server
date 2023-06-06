package hufs.team.mogong.timetable.exception;

import hufs.team.mogong.common.exception.BaseException;
import hufs.team.mogong.common.exception.ErrorCodeAndMessages;

public class NotFoundTeamTimeTableException extends BaseException {

	public NotFoundTeamTimeTableException() {
		super(ErrorCodeAndMessages.NOT_FOUND_TEAM_TIME_TABLE);
	}
}
