package hufs.team.mogong.docs.team;

import static hufs.team.mogong.common.response.ResponseCodeAndMessages.FIND_TEAM_ID_SUCCESS;
import static hufs.team.mogong.docs.team.TeamSnippet.TEAM_NAME_RESPONSE_SNIPPET;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import hufs.team.mogong.docs.InitDocumentationTest;
import hufs.team.mogong.team.Team;
import hufs.team.mogong.team.repository.TeamRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("[API DOCS] Team Name으로 Team Id 조회")
class TeamNameTest extends InitDocumentationTest {

	@Autowired
	private TeamRepository teamRepository;

	private Long teamId;
	private String teamName;
	private Team team;

	@BeforeEach
	void init() {
		teamRepository.deleteAllInBatch();
		team = teamRepository.save(new Team("1c487536-08ef-4332-bc2f-16830f49495f", 5, "1234"));
		teamId = team.getTeamId();
		teamName = team.getTeamName();
	}

	@Test
	void findTeamIdByTeamName(){
	    //given
		given(this.spec)
			.filter(document(DEFAULT_RESTDOCS_PATH, TEAM_NAME_RESPONSE_SNIPPET))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(CONTENT_TYPE, APPLICATION_JSON)
			.pathParam("teamName", teamName)
			.log().all()

			//when
			.when()
			.get("/teams/names/{teamName}", teamName)

			//then
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("code", Matchers.equalTo(FIND_TEAM_ID_SUCCESS.getCode()))
			.body("message", Matchers.equalTo(FIND_TEAM_ID_SUCCESS.getMessage()))
			.log().all();
	}

}
