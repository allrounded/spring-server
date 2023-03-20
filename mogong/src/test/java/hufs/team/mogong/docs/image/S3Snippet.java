package hufs.team.mogong.docs.image;

import static hufs.team.mogong.docs.DocumentFormatGenerator.createResponseSnippetWithFields;
import static hufs.team.mogong.docs.DocumentFormatGenerator.requestSnippetWithConstraintsAndFields;
import static hufs.team.mogong.docs.DocumentFormatGenerator.responseFieldsOfCommon;
import static hufs.team.mogong.docs.DocumentFormatGenerator.responseFieldsOfObjectWithConstraintsAndFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import hufs.team.mogong.image.service.dto.PreSignedUrlRequest;
import hufs.team.mogong.image.service.dto.PreSignedUrlResponse;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;

public interface S3Snippet {

	Snippet PRESIGNED_URL_GENERATE_REQUEST_PARAM_SNIPPET = requestSnippetWithConstraintsAndFields(
		PreSignedUrlRequest.class,
		fieldWithPath("extension").type(JsonFieldType.STRING).description("이미지 파일 확장자")
	);

	Snippet PRESIGNED_URL_GENERATE_RESPONSE_SNIPPET = createResponseSnippetWithFields(
		responseFieldsOfCommon(),
		responseFieldsOfObjectWithConstraintsAndFields(
			PreSignedUrlResponse.class,
			fieldWithPath("preSignedUrl").type(JsonFieldType.STRING).description("PUT S3 PreSigned URL"),
			fieldWithPath("fileName").type(JsonFieldType.STRING).description("S3에 저장될 파일 이름"))
	);
}
