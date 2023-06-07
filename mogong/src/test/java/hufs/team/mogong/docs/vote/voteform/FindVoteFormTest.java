package hufs.team.mogong.docs.vote.voteform;

import static hufs.team.mogong.common.response.ResponseCodeAndMessages.FIND_VOTE_FORM_SUCCESS;
import static hufs.team.mogong.docs.vote.VoteSnippet.CREATE_VOTE_FORM_SUCCESS_RESPONSE_SNIPPET;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import hufs.team.mogong.docs.vote.InitVoteTest;
import hufs.team.mogong.vote.TeamVoteForm;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("[API DOCS] 팀 투표 폼 조회")
class FindVoteFormTest extends InitVoteTest {

	@Test
	@DisplayName("팀 투표 폼 조회 성공")
	void find_team_vote_success() {
		saveTeam();
		teamVoteFormRepository.save(new TeamVoteForm(team, 30, false));

		//given
		given(this.spec)
			.filter(document(DEFAULT_RESTDOCS_PATH, CREATE_VOTE_FORM_SUCCESS_RESPONSE_SNIPPET))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(CONTENT_TYPE, APPLICATION_JSON)
			.log().all()

			//when
			.when()
			.get("/votes/teams/{teamId}/forms", teamId)

			//then
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("code", Matchers.equalTo(FIND_VOTE_FORM_SUCCESS.getCode()))
			.body("message", Matchers.equalTo(FIND_VOTE_FORM_SUCCESS.getMessage()))
			.log().all();
	}

}
