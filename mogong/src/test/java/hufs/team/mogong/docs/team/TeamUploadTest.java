package hufs.team.mogong.docs.team;

import static hufs.team.mogong.common.response.ResponseCodeAndMessages.UPLOAD_ALL_MEMBER_IMAGE_SUCCESS;
import static hufs.team.mogong.common.response.ResponseCodeAndMessages.UPLOAD_SINGLE_MEMBER_IMAGE_SUCCESS;
import static hufs.team.mogong.docs.team.TeamSnippet.UPLOAD_TEAM_REQUEST_BODY_SNIPPET;
import static hufs.team.mogong.docs.team.TeamSnippet.UPLOAD_TEAM_RESPONSE_SNIPPET;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import hufs.team.mogong.docs.InitDocumentationTest;
import hufs.team.mogong.image.Image;
import hufs.team.mogong.image.repository.ImageRepository;
import hufs.team.mogong.team.Team;
import hufs.team.mogong.team.repository.TeamRepository;
import hufs.team.mogong.team.service.dto.request.UploadTeamRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("[API DOCS] Team 멤버 이미지 등록")
class TeamUploadTest extends InitDocumentationTest {

	private static final String SAMPLE_IMAGE_URL = "https://mogong.s3.ap-northeast-2.amazonaws.com/image/sample_4.JPG";

	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private ImageRepository imageRepository;

	private Long teamId;
	private Team team;

	@BeforeEach
	void init() {
		imageRepository.deleteAllInBatch();
		teamRepository.deleteAllInBatch();
		team = teamRepository.save(new Team("1c487536-08ef-4332-bc2f-16830f49495f", 5, "1234"));
		teamId = team.getTeamId();
		imageRepository.save(
			new Image(team, "https://mogong.s3.ap-northeast-2.amazonaws.com/image/sample_1.JPG")
		);
		imageRepository.save(
			new Image(team, "https://mogong.s3.ap-northeast-2.amazonaws.com/image/sample_2.JPG")
		);
		imageRepository.save(
			new Image(team, "https://mogong.s3.ap-northeast-2.amazonaws.com/image/sample_3.JPG")
		);
	}

	@Test
	@DisplayName("팀 멤버 이미지 업로드 성공(제출 수 미충족)")
	void upload_team_success(){
		UploadTeamRequest request = new UploadTeamRequest(SAMPLE_IMAGE_URL);

		//given
		given(this.spec)
			.filter(document(DEFAULT_RESTDOCS_PATH,
				UPLOAD_TEAM_REQUEST_BODY_SNIPPET,
				UPLOAD_TEAM_RESPONSE_SNIPPET))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(CONTENT_TYPE, APPLICATION_JSON)
			.body(request)
			.log().all()

			//when
			.when()
			.post("/teams/{teamId}", teamId)

			//then
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("code", Matchers.equalTo(UPLOAD_SINGLE_MEMBER_IMAGE_SUCCESS.getCode()))
			.body("message", Matchers.equalTo(UPLOAD_SINGLE_MEMBER_IMAGE_SUCCESS.getMessage()))
			.log().all();
	}

	@Test
	@DisplayName("팀 멤버 이미지 업로드 성공(제출 수 충족)")
	void completed_upload_team_success(){
		imageRepository.save(
			new Image(team, "https://mogong.s3.ap-northeast-2.amazonaws.com/image/sample_5.JPG")
		);
		UploadTeamRequest request = new UploadTeamRequest(SAMPLE_IMAGE_URL);

		//given
		given(this.spec)
			.filter(document(DEFAULT_RESTDOCS_PATH,
				UPLOAD_TEAM_REQUEST_BODY_SNIPPET,
				UPLOAD_TEAM_RESPONSE_SNIPPET))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(CONTENT_TYPE, APPLICATION_JSON)
			.body(request)
			.log().all()

			//when
			.when()
			.post("/teams/{teamId}", teamId)

			//then
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("code", Matchers.equalTo(UPLOAD_ALL_MEMBER_IMAGE_SUCCESS.getCode()))
			.body("message", Matchers.equalTo(UPLOAD_ALL_MEMBER_IMAGE_SUCCESS.getMessage()))
			.log().all();
	}
}
