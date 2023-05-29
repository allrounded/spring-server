package hufs.team.mogong.vote.exception;

import hufs.team.mogong.common.exception.BaseException;
import hufs.team.mogong.common.exception.ErrorCodeAndMessages;

public class NotFoundTeamVoteException extends BaseException {

	public NotFoundTeamVoteException() {
		super(ErrorCodeAndMessages.NOT_FOUND_TEAM_VOTE);
	}
}
