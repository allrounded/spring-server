package hufs.team.mogong.team.exception;

import hufs.team.mogong.common.exception.BaseException;
import hufs.team.mogong.common.exception.ErrorCodeAndMessages;
import lombok.Getter;

@Getter
public class NotCompletedSubmit extends BaseException {

	private final int numberOfMember;
	private final int numberOfSubmit;

	public NotCompletedSubmit(int numberOfMember, int numberOfSubmit) {
		super(ErrorCodeAndMessages.NOT_COMPLETED_SUBMIT);
		this.numberOfMember = numberOfMember;
		this.numberOfSubmit = numberOfSubmit;
	}
}
