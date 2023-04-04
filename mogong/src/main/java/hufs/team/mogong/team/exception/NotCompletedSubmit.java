package hufs.team.mogong.team.exception;

import hufs.team.mogong.common.exception.BaseException;
import hufs.team.mogong.common.exception.ErrorCodeAndMessages;
import lombok.Getter;

@Getter
public class NotCompletedSubmit extends BaseException {

	private final int sizeOfImages;
	private final int numberOfTeam;

	public NotCompletedSubmit(int sizeOfImages, int numberOfTeam) {
		super(ErrorCodeAndMessages.NOT_COMPLETED_SUBMIT);
		this.sizeOfImages = sizeOfImages;
		this.numberOfTeam = numberOfTeam;
	}
}
