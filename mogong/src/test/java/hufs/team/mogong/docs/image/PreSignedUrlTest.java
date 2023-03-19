package hufs.team.mogong.docs.image;

import static hufs.team.mogong.common.response.ResponseCodeAndMessages.GENERATE_IMAGE_PRESIGNED_URL_SUCCESS;
import static hufs.team.mogong.docs.image.S3Snippet.PRESIGNED_URL_GENERATE_REQUEST_PARAM_SNIPPET;
import static hufs.team.mogong.docs.image.S3Snippet.PRESIGNED_URL_GENERATE_RESPONSE_SNIPPET;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import hufs.team.mogong.docs.InitDocumentationTest;
import hufs.team.mogong.image.service.dto.PreSignedUrlRequest;
import hufs.team.mogong.image.service.dto.PreSignedUrlResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("[API DOCS] AWS S3 PreSigned URL 생성")
class PreSignedUrlTest extends InitDocumentationTest {
	@Test
	@DisplayName("PreSigned URL 생성 성공")
	void generate_pre_signed_url_success(){
		PreSignedUrlRequest request = new PreSignedUrlRequest(".JPG");

		//given
		given(this.spec)
			.filter(document(DEFAULT_RESTDOCS_PATH,
				PRESIGNED_URL_GENERATE_REQUEST_PARAM_SNIPPET,
				PRESIGNED_URL_GENERATE_RESPONSE_SNIPPET))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(CONTENT_TYPE, APPLICATION_JSON)
			.body(request)

		//when
		.when()
			.post("/images")

		//then
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("code", Matchers.equalTo(GENERATE_IMAGE_PRESIGNED_URL_SUCCESS.getCode()))
			.body("message", Matchers.equalTo(GENERATE_IMAGE_PRESIGNED_URL_SUCCESS.getMessage()));
	}
}
