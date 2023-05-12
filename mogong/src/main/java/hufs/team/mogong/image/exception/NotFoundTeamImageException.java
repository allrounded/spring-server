package hufs.team.mogong.image.exception;

import hufs.team.mogong.common.exception.BaseException;
import hufs.team.mogong.common.exception.ErrorCodeAndMessages;

public class NotFoundTeamImageException extends BaseException {

	public NotFoundTeamImageException() {
		super(ErrorCodeAndMessages.NOT_FOUND_TEAM_IMAGE);
	}
}
