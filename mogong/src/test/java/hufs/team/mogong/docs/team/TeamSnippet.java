package hufs.team.mogong.docs.team;


import static hufs.team.mogong.docs.DocumentFormatGenerator.createResponseSnippetWithFields;
import static hufs.team.mogong.docs.DocumentFormatGenerator.requestSnippetWithConstraintsAndFields;
import static hufs.team.mogong.docs.DocumentFormatGenerator.responseFieldsOfCommon;
import static hufs.team.mogong.docs.DocumentFormatGenerator.responseFieldsOfObjectWithConstraintsAndFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import hufs.team.mogong.team.service.dto.request.CreateTeamRequest;
import hufs.team.mogong.team.service.dto.request.UploadTeamRequest;
import hufs.team.mogong.team.service.dto.response.CreateTeamResponse;
import hufs.team.mogong.team.service.dto.response.TeamIdResponse;
import hufs.team.mogong.team.service.dto.response.UploadTeamResponse;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;

public interface TeamSnippet {

	Snippet CREATE_TEAM_REQUEST_BODY_SNIPPET = requestSnippetWithConstraintsAndFields(
		CreateTeamRequest.class,
		fieldWithPath("numberOfTeam").type(JsonFieldType.NUMBER).description("팀 구성원 수"),
		fieldWithPath("authCode").type(JsonFieldType.STRING).description("팀장 코드(4자리, 각 자리는 0~9에 해당하는 정수여야 함.)")
	);

	Snippet CREATE_TEAM_RESPONSE_SNIPPET = createResponseSnippetWithFields(
		responseFieldsOfCommon(),
		responseFieldsOfObjectWithConstraintsAndFields(
			CreateTeamResponse.class,
			fieldWithPath("teamId").type(JsonFieldType.NUMBER).description("생성된 팀 아이디"),
			fieldWithPath("teamName").type(JsonFieldType.STRING).description("생성된 팀 이름(UUID)"),
			fieldWithPath("numberOfTeam").type(JsonFieldType.NUMBER).description("팀 구성원 수"),
			fieldWithPath("submit").type(JsonFieldType.NUMBER).description("이미지를 제출한 구성원 수"),
			fieldWithPath("authCode").type(JsonFieldType.STRING).description("팀장 코드")
		)
	);

	Snippet UPLOAD_TEAM_REQUEST_BODY_SNIPPET = requestSnippetWithConstraintsAndFields(
		UploadTeamRequest.class,
		fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("업로드가 완료된 이미지 링크")
	);

	Snippet UPLOAD_TEAM_RESPONSE_SNIPPET = createResponseSnippetWithFields(
		responseFieldsOfCommon(),
		responseFieldsOfObjectWithConstraintsAndFields(
			UploadTeamResponse.class,
			fieldWithPath("teamId").type(JsonFieldType.NUMBER).description("팀 아이디"),
			fieldWithPath("teamName").type(JsonFieldType.STRING).description("팀 이름(UUID)"),
			fieldWithPath("numberOfTeam").type(JsonFieldType.NUMBER).description("팀 구성원 수"),
			fieldWithPath("submit").type(JsonFieldType.NUMBER).description("이미지를 제출한 구성원 수")
		)
	);
	Snippet TEAM_RESULT_RESPONSE_SNIPPET =  createResponseSnippetWithFields(
		responseFieldsOfCommon(),
		responseFieldsOfObjectWithConstraintsAndFields(
			UploadTeamResponse.class,
			fieldWithPath("resultImageUrl").type(JsonFieldType.STRING).description("결과 이미지 URL"),
			fieldWithPath("timeResponses").type(JsonFieldType.OBJECT).description("N분 단위의 시간들"),
			fieldWithPath("timeResponses.divisorMinutes").type(JsonFieldType.NUMBER).description("시간 단위 (default : 30분)"),
			fieldWithPath("timeResponses.times[]").type(JsonFieldType.ARRAY).description("요일과 시간을 담은 배열"),
			fieldWithPath("timeResponses.times[].dayOfWeek").type(JsonFieldType.STRING).description("요일"),
			fieldWithPath("timeResponses.times[].time").type(JsonFieldType.STRING).description("30분 단위로 나눈 시간들 (ex. \"09:00~09:30\")")
		)
	);

	Snippet TEAM_NAME_RESPONSE_SNIPPET = createResponseSnippetWithFields(
		responseFieldsOfCommon(),
		responseFieldsOfObjectWithConstraintsAndFields(
			TeamIdResponse.class,
			fieldWithPath("teamName").type(JsonFieldType.STRING).description("Team Name"),
			fieldWithPath("teamId").type(JsonFieldType.NUMBER).description("Team Id")
		)
	);
}
