package hufs.team.mogong.docs.team;

import static hufs.team.mogong.common.response.ResponseCodeAndMessages.CREATE_TEAM_SUCCESS;
import static hufs.team.mogong.docs.team.TeamSnippet.CREATE_TEAM_REQUEST_BODY_SNIPPET;
import static hufs.team.mogong.docs.team.TeamSnippet.CREATE_TEAM_RESPONSE_SNIPPET;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import hufs.team.mogong.docs.InitDocumentationTest;
import hufs.team.mogong.image.repository.MemberImageRepository;
import hufs.team.mogong.image.repository.TeamImageRepository;
import hufs.team.mogong.member.repository.MemberRepository;
import hufs.team.mogong.team.repository.TeamRepository;
import hufs.team.mogong.team.service.dto.request.CreateTeamRequest;
import hufs.team.mogong.timetable.repository.MemberTimeTableV2Repository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("[API DOCS] Team 생성")
class TeamCreateTest extends InitDocumentationTest {

	private static final Integer NUMBER_OF_TEAM = 5;
	private static final String AUTH_CODE = "1234";

	@Autowired
	private MemberImageRepository memberImageRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private TeamImageRepository teamImageRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private MemberTimeTableV2Repository memberTimeTableV2Repository;

	@BeforeEach
	void init() {
		memberTimeTableV2Repository.deleteAllInBatch();
		memberImageRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
		teamImageRepository.deleteAllInBatch();
		teamRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("팀 생성 성공")
	void create_team_success(){
		CreateTeamRequest request = new CreateTeamRequest(NUMBER_OF_TEAM, AUTH_CODE);

		//given
		given(this.spec)
			.filter(document(DEFAULT_RESTDOCS_PATH,
				CREATE_TEAM_REQUEST_BODY_SNIPPET,
				CREATE_TEAM_RESPONSE_SNIPPET))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(CONTENT_TYPE, APPLICATION_JSON)
			.body(request)

			//when
			.when()
				.post("/teams")

			//then
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("code", Matchers.equalTo(CREATE_TEAM_SUCCESS.getCode()))
			.body("message", Matchers.equalTo(CREATE_TEAM_SUCCESS.getMessage()));
	}
}
