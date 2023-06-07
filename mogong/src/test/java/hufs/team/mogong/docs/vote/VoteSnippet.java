package hufs.team.mogong.docs.vote;

import static hufs.team.mogong.docs.DocumentFormatGenerator.createResponseSnippetWithFields;
import static hufs.team.mogong.docs.DocumentFormatGenerator.requestSnippetWithConstraintsAndFields;
import static hufs.team.mogong.docs.DocumentFormatGenerator.responseFieldsOfCommon;
import static hufs.team.mogong.docs.DocumentFormatGenerator.responseFieldsOfCommonNonData;
import static hufs.team.mogong.docs.DocumentFormatGenerator.responseFieldsOfObjectWithConstraintsAndFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import hufs.team.mogong.vote.service.dto.request.MemberVoteRequest;
import hufs.team.mogong.vote.service.dto.request.VoteFormRequest;
import hufs.team.mogong.vote.service.dto.response.MemberVoteResponse;
import hufs.team.mogong.vote.service.dto.response.VoteFormResponse;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;

public interface VoteSnippet {

	Snippet CREATE_VOTE_FORM_REQUEST_BODY_SNIPPET = requestSnippetWithConstraintsAndFields(
		VoteFormRequest.class,
		fieldWithPath("divisorMinutes").type(JsonFieldType.NUMBER).description("투표 분할 시간(30분, 60분, 90분)"),
		fieldWithPath("duplicate").type(JsonFieldType.BOOLEAN).description("중복 항목 투표 가능 여부")
	);

	Snippet CREATE_VOTE_FORM_SUCCESS_RESPONSE_SNIPPET = createResponseSnippetWithFields(
		responseFieldsOfCommon(),
		responseFieldsOfObjectWithConstraintsAndFields(
			VoteFormResponse.class,
			fieldWithPath("teamId").type(JsonFieldType.NUMBER).description("팀 아이디"),
			fieldWithPath("divisorMinutes").type(JsonFieldType.NUMBER).description("투표 폼 시간 단위(30분, 60분, 90분)"),
			fieldWithPath("duplicate").type(JsonFieldType.BOOLEAN).description("중복 투표 가능 여부")
		)
	);

	Snippet CREATE_VOTE_FORM_FAIL_RESPONSE_SNIPPET = createResponseSnippetWithFields(
		responseFieldsOfCommonNonData()
	);

	Snippet CREATE_MEMBER_VOTE_REQUEST_BODY_SNIPPET = requestSnippetWithConstraintsAndFields(
		MemberVoteRequest.class,
		fieldWithPath("timeRequests").type(JsonFieldType.OBJECT).description("멤버가 투표한 시간표"),
		fieldWithPath("timeRequests.divisorMinutes").type(JsonFieldType.NUMBER).description("투표 폼 시간 단위(30분, 60분, 90분)"),
		fieldWithPath("timeRequests.times[]").type(JsonFieldType.ARRAY).description("시간표 배열 객체"),
		fieldWithPath("timeRequests.times[].dayOfWeek").type(JsonFieldType.STRING).description("시간표 요일(MON, TUE, WED, THU, FRI, SAT, SUN)"),
		fieldWithPath("timeRequests.times[].time[]").type(JsonFieldType.ARRAY).description("시간표 배열(0, 1로 이루어진 배열)")
	);

	Snippet CREATE_MEMBER_VOTE_SUCCESS_RESPONSE_SNIPPET = createResponseSnippetWithFields(
		responseFieldsOfCommon(),
		responseFieldsOfObjectWithConstraintsAndFields(
			MemberVoteResponse.class,
			fieldWithPath("teamTotalVotesResponse").type(JsonFieldType.OBJECT).description("팀 공강 시간표 데이터 및 팀원 투표 총 합"),
			fieldWithPath("teamTotalVotesResponse.teamId").type(JsonFieldType.NUMBER).description("팀 아이디"),
			fieldWithPath("teamTotalVotesResponse.teamTimeTable").type(JsonFieldType.OBJECT).description("팀 공강 시간표 데이터"),
			fieldWithPath("teamTotalVotesResponse.teamTimeTable.divisorMinutes").type(JsonFieldType.NUMBER).description("팀 공강 시간표 시간 단위(30분, 60분, 90분)"),
			fieldWithPath("teamTotalVotesResponse.teamTimeTable.times[]").type(JsonFieldType.ARRAY).description("팀 공강 시간표 배열"),
			fieldWithPath("teamTotalVotesResponse.teamTimeTable.times[].dayOfWeek").type(JsonFieldType.STRING).description("팀 공강 시간표 요일(MON, TUE, WED, THU, FRI, SAT, SUN)"),
			fieldWithPath("teamTotalVotesResponse.teamTimeTable.times[].time[]").type(JsonFieldType.ARRAY).description("팀 공강 시간표 배열(0, 1로 이루어진 배열)"),
			fieldWithPath("teamTotalVotesResponse.teamVote").type(JsonFieldType.OBJECT).description("팀원 투표 총합"),
			fieldWithPath("teamTotalVotesResponse.teamVote.divisorMinutes").type(JsonFieldType.NUMBER).description("팀원 투표 총합 시간 단위(30분, 60분, 90분)"),
			fieldWithPath("teamTotalVotesResponse.teamVote.times[]").type(JsonFieldType.ARRAY).description("팀원 투표 총합 배열"),
			fieldWithPath("teamTotalVotesResponse.teamVote.times[].dayOfWeek").type(JsonFieldType.STRING).description("팀원 투표 총합 요일(MON, TUE, WED, THU, FRI, SAT, SUN)"),
			fieldWithPath("teamTotalVotesResponse.teamVote.times[].time[]").type(JsonFieldType.ARRAY).description("팀원 투표 총합 배열(0, 1로 이루어진 배열)"),
			fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("투표하는 해당 팀원 아이디"),
			fieldWithPath("memberVote").type(JsonFieldType.OBJECT).description("팀원 투표 현황"),
			fieldWithPath("memberVote.divisorMinutes").type(JsonFieldType.NUMBER).description("투표 폼 시간 단위(30분, 60분, 90분)"),
			fieldWithPath("memberVote.times[]").type(JsonFieldType.ARRAY).description("팀원 투표 현황 배열 객체"),
			fieldWithPath("memberVote.times[].dayOfWeek").type(JsonFieldType.STRING).description("시간표 요일(MON, TUE, WED, THU, FRI, SAT, SUN)"),
			fieldWithPath("memberVote.times[].time[]").type(JsonFieldType.ARRAY).description("시간표 배열(0, 1로 이루어진 배열)")
		)
	);

	Snippet FIND_TEAM_TOTAL_VOTES_SUCCESS_RESPONSE_SNIPPET = createResponseSnippetWithFields(
		responseFieldsOfCommon(),
		responseFieldsOfObjectWithConstraintsAndFields(
			MemberVoteResponse.class,
			fieldWithPath("teamId").type(JsonFieldType.NUMBER).description("팀 아이디"),
			fieldWithPath("teamTimeTable").type(JsonFieldType.OBJECT).description("팀 공강 시간표 데이터"),
			fieldWithPath("teamTimeTable.divisorMinutes").type(JsonFieldType.NUMBER).description("팀 공강 시간표 시간 단위(30분, 60분, 90분)"),
			fieldWithPath("teamTimeTable.times[]").type(JsonFieldType.ARRAY).description("팀 공강 시간표 배열"),
			fieldWithPath("teamTimeTable.times[].dayOfWeek").type(JsonFieldType.STRING).description("팀 공강 시간표 요일(MON, TUE, WED, THU, FRI, SAT, SUN)"),
			fieldWithPath("teamTimeTable.times[].time[]").type(JsonFieldType.ARRAY).description("팀 공강 시간표 배열(0, 1로 이루어진 배열)"),
			fieldWithPath("teamVote").type(JsonFieldType.OBJECT).description("팀원 투표 총합"),
			fieldWithPath("teamVote.divisorMinutes").type(JsonFieldType.NUMBER).description("팀원 투표 총합 시간 단위(30분, 60분, 90분)"),
			fieldWithPath("teamVote.times[]").type(JsonFieldType.ARRAY).description("팀원 투표 총합 배열"),
			fieldWithPath("teamVote.times[].dayOfWeek").type(JsonFieldType.STRING).description("팀원 투표 총합 요일(MON, TUE, WED, THU, FRI, SAT, SUN)"),
			fieldWithPath("teamVote.times[].time[]").type(JsonFieldType.ARRAY).description("팀원 투표 총합 배열(0, 1로 이루어진 배열)")
		)
	);

}
