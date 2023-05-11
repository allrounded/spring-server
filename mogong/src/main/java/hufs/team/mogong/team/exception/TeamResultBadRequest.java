package hufs.team.mogong.team.exception;

import hufs.team.mogong.common.exception.BaseException;
import hufs.team.mogong.common.exception.ErrorCodeAndMessages;

public class TeamResultBadRequest extends BaseException {

	public TeamResultBadRequest() {
		super(ErrorCodeAndMessages.TEAM_RESULT_BADREQUEST);
	}
}
