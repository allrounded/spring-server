package hufs.team.mogong.docs.vote;

import static hufs.team.mogong.docs.DocumentFormatGenerator.createResponseSnippetWithFields;
import static hufs.team.mogong.docs.DocumentFormatGenerator.requestSnippetWithConstraintsAndFields;
import static hufs.team.mogong.docs.DocumentFormatGenerator.responseFieldsOfCommon;
import static hufs.team.mogong.docs.DocumentFormatGenerator.responseFieldsOfCommonNonData;
import static hufs.team.mogong.docs.DocumentFormatGenerator.responseFieldsOfObjectWithConstraintsAndFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import hufs.team.mogong.team.service.dto.response.CreateTeamResponse;
import hufs.team.mogong.vote.service.dto.request.VoteFormRequest;
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

}
