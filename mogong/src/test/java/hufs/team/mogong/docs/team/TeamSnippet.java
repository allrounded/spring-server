package hufs.team.mogong.docs.team;


import static hufs.team.mogong.docs.DocumentFormatGenerator.createResponseSnippetWithFields;
import static hufs.team.mogong.docs.DocumentFormatGenerator.requestSnippetWithConstraintsAndFields;
import static hufs.team.mogong.docs.DocumentFormatGenerator.responseFieldsOfCommon;
import static hufs.team.mogong.docs.DocumentFormatGenerator.responseFieldsOfObjectWithConstraintsAndFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import hufs.team.mogong.team.service.dto.TeamRequest;
import hufs.team.mogong.team.service.dto.TeamResponse;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;

public interface TeamSnippet {

	Snippet CREATE_TEAM_REQUEST_BODY_SNIPPET = requestSnippetWithConstraintsAndFields(
		TeamRequest.class,
		fieldWithPath("numberOfTeam").type(JsonFieldType.NUMBER).description("팀 구성원 수")
	);

	Snippet CREATE_TEAM_RESPONSE_SNIPPET = createResponseSnippetWithFields(
		responseFieldsOfCommon(),
		responseFieldsOfObjectWithConstraintsAndFields(
			TeamResponse.class,
			fieldWithPath("teamId").type(JsonFieldType.STRING).description("생성된 팀 아이디"),
			fieldWithPath("numberOfTeam").type(JsonFieldType.NUMBER).description("팀 구성원 수"),
			fieldWithPath("submit").type(JsonFieldType.NUMBER).description("이미지를 제출한 구성원 수")
		)
	);

}
