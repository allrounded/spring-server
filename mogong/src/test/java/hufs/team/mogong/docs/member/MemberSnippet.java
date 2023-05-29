package hufs.team.mogong.docs.member;

import static hufs.team.mogong.docs.DocumentFormatGenerator.createResponseSnippetWithFields;
import static hufs.team.mogong.docs.DocumentFormatGenerator.requestSnippetWithConstraintsAndFields;
import static hufs.team.mogong.docs.DocumentFormatGenerator.responseFieldsOfCommon;
import static hufs.team.mogong.docs.DocumentFormatGenerator.responseFieldsOfObjectWithConstraintsAndFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import hufs.team.mogong.member.service.dto.request.CreateMemberRequest;
import hufs.team.mogong.member.service.dto.response.MemberImageUploadResponse;
import hufs.team.mogong.member.service.dto.response.MemberResponse;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;

public interface MemberSnippet {

	Snippet CREATE_MEMBER_REQUEST_BODY_SNIPPET = requestSnippetWithConstraintsAndFields(
		CreateMemberRequest.class,
		fieldWithPath("nickName").type(JsonFieldType.STRING).description("생성할 팀원 이름"),
		fieldWithPath("leader").type(JsonFieldType.BOOLEAN).description("팀장 여부")
	);

	Snippet MEMBER_RESPONSE_SNIPPET = createResponseSnippetWithFields(
		responseFieldsOfCommon(),
		responseFieldsOfObjectWithConstraintsAndFields(
			MemberResponse.class,
			fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("생성된 팀원 아이디"),
			fieldWithPath("nickName").type(JsonFieldType.STRING).description("생성된 팀원 이름"),
			fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("생성된 팀원의 이미지 URL"),
			fieldWithPath("submit").type(JsonFieldType.BOOLEAN).description("생성된 팀원의 이미지 업로드 여부(default: false)")
		)
	);

	Snippet MEMBER_IMAGE_UPLOAD_RESPONSE_SNIPPET = createResponseSnippetWithFields(
		responseFieldsOfCommon(),
		responseFieldsOfObjectWithConstraintsAndFields(
			MemberImageUploadResponse.class,
			fieldWithPath("teamName").type(JsonFieldType.STRING).description("팀 이름"),
			fieldWithPath("numberOfMember").type(JsonFieldType.NUMBER).description("팀원 수"),
			fieldWithPath("numberOfSubmit").type(JsonFieldType.NUMBER).description("이미지 업로드 수"),
			fieldWithPath("nickName").type(JsonFieldType.STRING).description("팀원 이름"),
			fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("팀원 이미지 URL"),
			fieldWithPath("submit").type(JsonFieldType.BOOLEAN).description("팀원의 이미지 업로드 여부")
		)
	);

}
