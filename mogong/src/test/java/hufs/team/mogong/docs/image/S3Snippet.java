package hufs.team.mogong.docs.image;

import static hufs.team.mogong.docs.DocumentFormatGenerator.createResponseSnippetWithFields;
import static hufs.team.mogong.docs.DocumentFormatGenerator.responseFieldsOfCommon;
import static hufs.team.mogong.docs.DocumentFormatGenerator.responseFieldsOfObjectWithConstraintsAndFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

import hufs.team.mogong.service.dto.PreSignedUrlResponse;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.snippet.Snippet;

public interface S3Snippet {

	Snippet PRESIGNED_URL_GENERATE_REQUEST_PARAM_SNIPPET = requestParameters(
		parameterWithName("extension").description("이미지 파일 확장자"));

	Snippet PRESIGNED_URL_GENERATE_RESPONSE_SNIPPET = createResponseSnippetWithFields(
		responseFieldsOfCommon(),
		responseFieldsOfObjectWithConstraintsAndFields(
			PreSignedUrlResponse.class,
			fieldWithPath("preSignedUrl").type(JsonFieldType.STRING).description("PUT S3 PreSigned URL"),
			fieldWithPath("fileName").type(JsonFieldType.STRING).description("S3에 저장될 파일 이름"))
	);
}
