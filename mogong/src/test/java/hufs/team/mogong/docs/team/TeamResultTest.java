package hufs.team.mogong.docs.team;

import static hufs.team.mogong.common.exception.ErrorCodeAndMessages.NOT_COMPLETED_SUBMIT;
import static hufs.team.mogong.common.response.ResponseCodeAndMessages.GENERATE_TEAM_RESULT_SUCCESS;
import static hufs.team.mogong.docs.team.TeamSnippet.TEAM_RESULT_RESPONSE_SNIPPET;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import hufs.team.mogong.docs.InitDocumentationTest;
import hufs.team.mogong.image.Image;
import hufs.team.mogong.image.repository.ImageRepository;
import hufs.team.mogong.team.Team;
import hufs.team.mogong.team.exception.NotFoundTeamIdException;
import hufs.team.mogong.team.repository.TeamRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("[API DOCS] Team 멤버 결과 요청")
class TeamResultTest extends InitDocumentationTest {
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
		team = teamRepository.save(new Team("1c487536-08ef-4332-bc2f-16830f49495f", 5));
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
	@DisplayName("팀 멤버 결과 요청 성공")
	void result_team_success(){
		imageRepository.save(
			new Image(team, "https://mogong.s3.ap-northeast-2.amazonaws.com/image/sample_4.JPG")
		);
		imageRepository.save(
			new Image(team, "https://mogong.s3.ap-northeast-2.amazonaws.com/image/sample_5.JPG")
		);

		//given
		given(this.spec)
			.filter(document(DEFAULT_RESTDOCS_PATH,
				TEAM_RESULT_RESPONSE_SNIPPET))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(CONTENT_TYPE, APPLICATION_JSON)
			.log().all()

			//when
			.when()
			.get("/teams/{teamId}/results", teamId)

			//then
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("code", Matchers.equalTo(GENERATE_TEAM_RESULT_SUCCESS.getCode()))
			.body("message", Matchers.equalTo(GENERATE_TEAM_RESULT_SUCCESS.getMessage()))
			.log().all();
	}

	@Test
	@DisplayName("팀 멤버 결과 요청 실패(제출 수 미달)")
	void result_team_fail(){
		//given
		given(this.spec)
			.filter(document(DEFAULT_RESTDOCS_PATH))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(CONTENT_TYPE, APPLICATION_JSON)
			.log().all()

			//when
			.when()
			.get("/teams/{teamId}/results", teamId)

			//then
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("code", Matchers.equalTo(NOT_COMPLETED_SUBMIT.getCode()))
			.body("message", Matchers.equalTo(NOT_COMPLETED_SUBMIT.getMessage()))
			.body("data", Matchers.equalTo(null))
			.log().all();
	}

}
