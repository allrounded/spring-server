package hufs.team.mogong.common.exception;

import hufs.team.mogong.common.CodeAndMessages;
import lombok.Getter;

@Getter
public enum ErrorCodeAndMessages implements CodeAndMessages {

	/**
	 * Image
	 */
	GENERATE_IMAGE_PRESIGNED_URL_FAILED("I-F001", "이미지 PreSigned URL 생성에 실패했습니다."),
	NOT_FOUND_MEMBER_IMAGE("I-F002", "팀원 이미지 조회에 실패했습니다."),
	NOT_FOUND_TEAM_IMAGE("I-F003", "팀 이미지 조회에 실패했습니다."),

	/**
	 * Team
	 */
	NOT_FOUND_TEAM_ID("T-F001", "해당 팀 ID를 찾을 수 없습니다."),
	NOT_COMPLETED_SUBMIT("T-F002", "현재 등록된 이미지 수가 지정된 멤버 수보다 적습니다."),
	NOT_MATCH_AUTH_CODE("T-F003", "AUTH CODE가 일치하지 않습니다."),
	TEAM_RESULT_BADREQUEST("T-F004", "시간표 이미지가 아닙니다. 이미지를 다시 확인해주세요."),
	NOT_FOUND_TEAM_NAME("T-F005", "해당 팀 NAME을 찾을 수 없습니다."),
	OVER_IMAGE_SUBMIT("T-F006", "팀 이미지 제출 수가 멤버 수를 초과했습니다."),

	/**
	 * Member
	 */
	NOT_FOUND_MEMBER_ID("M-F001", "해당 팀원 ID를 찾을 수 없습니다."),

	/**
	 * Image Server
	 */
	IMAGE_SERVER_ERROR("IS-F001", "IMAGE SERVER의 에러로 인해 요청이 제대로 수행되지 못했습니다."),

	/**
	 * Vote
	 */
	NOT_FOUND_TEAM_VOTE("V-F001", "해당 팀 투표가 아직 완성되지 않았습니다."),
	ALREADY_EXISTS_TEAM_VOTE("V-F002", "해당 팀 투표는 이미 생성되었습니다."),
	;
	private final String code;
	private final String message;

	ErrorCodeAndMessages(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
