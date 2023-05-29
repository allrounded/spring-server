package hufs.team.mogong.vote.exception;

import hufs.team.mogong.common.exception.BaseException;
import hufs.team.mogong.common.exception.ErrorCodeAndMessages;

public class AlreadyExistsTeamVote extends BaseException {

	public AlreadyExistsTeamVote() {
		super(ErrorCodeAndMessages.ALREADY_EXISTS_TEAM_VOTE);
	}
}
