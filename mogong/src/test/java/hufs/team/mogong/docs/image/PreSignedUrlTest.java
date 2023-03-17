package hufs.team.mogong.docs.image;

import static hufs.team.mogong.common.response.ResponseCodeAndMessages.GENERATE_IMAGE_PRESIGNED_URL_SUCCESS;
import static hufs.team.mogong.docs.image.S3Snippet.PRESIGNED_URL_GENERATE_REQUEST_PARAM_SNIPPET;
import static hufs.team.mogong.docs.image.S3Snippet.PRESIGNED_URL_GENERATE_RESPONSE_SNIPPET;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import hufs.team.mogong.docs.InitDocumentationTest;
import hufs.team.mogong.service.dto.PreSignedUrlResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("[API DOCS] AWS S3 PreSigned URL 생성")
class PreSignedUrlTest extends InitDocumentationTest {

	private static final String BUCKET_URL = "https://mogong.s3.ap-northeast-2.amazonaws.com/";
	private static final String FILE_NAME = "image/sample_1.JPG";
	@Test
	void generate_PreSignedURL_success(){
		PreSignedUrlResponse response = new PreSignedUrlResponse(
			BUCKET_URL + FILE_NAME,
			FILE_NAME);

		//given
		given(this.spec)
			.filter(document(DEFAULT_RESTDOCS_PATH,
				PRESIGNED_URL_GENERATE_REQUEST_PARAM_SNIPPET,
				PRESIGNED_URL_GENERATE_RESPONSE_SNIPPET))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(CONTENT_TYPE, APPLICATION_JSON)
			.queryParam("extension", ".jpg")

		//when
		.when()
			.get("/images")

		//then
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("code", Matchers.equalTo(GENERATE_IMAGE_PRESIGNED_URL_SUCCESS.getCode()))
			.body("message", Matchers.equalTo(GENERATE_IMAGE_PRESIGNED_URL_SUCCESS.getMessage()));
	}
}
