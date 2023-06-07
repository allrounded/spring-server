package hufs.team.mogong.vote.exception;

import hufs.team.mogong.common.exception.BaseException;
import hufs.team.mogong.common.exception.ErrorCodeAndMessages;

public class NotFoundMemberVoteException extends BaseException {

	public NotFoundMemberVoteException() {
		super(ErrorCodeAndMessages.NOT_FOUND_MEMBER_VOTE);
	}
}
