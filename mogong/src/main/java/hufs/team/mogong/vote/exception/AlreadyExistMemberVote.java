package hufs.team.mogong.vote.exception;

import hufs.team.mogong.common.exception.BaseException;
import hufs.team.mogong.common.exception.ErrorCodeAndMessages;

public class AlreadyExistMemberVote extends BaseException {

	public AlreadyExistMemberVote() {
		super(ErrorCodeAndMessages.ALREADY_EXIST_MEMBER_VOTE);
	}
}
