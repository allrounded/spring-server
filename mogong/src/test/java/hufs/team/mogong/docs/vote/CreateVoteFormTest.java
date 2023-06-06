package hufs.team.mogong.docs.vote;

import static hufs.team.mogong.common.exception.ErrorCodeAndMessages.ALREADY_EXISTS_TEAM_VOTE;
import static hufs.team.mogong.common.response.ResponseCodeAndMessages.CREATE_VOTE_FORM_SUCCESS;
import static hufs.team.mogong.docs.vote.VoteSnippet.CREATE_VOTE_FORM_FAIL_RESPONSE_SNIPPET;
import static hufs.team.mogong.docs.vote.VoteSnippet.CREATE_VOTE_FORM_REQUEST_BODY_SNIPPET;
import static hufs.team.mogong.docs.vote.VoteSnippet.CREATE_VOTE_FORM_SUCCESS_RESPONSE_SNIPPET;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import hufs.team.mogong.vote.TeamVoteForm;
import hufs.team.mogong.vote.service.dto.request.VoteFormRequest;
import hufs.team.mogong.vote.service.dto.response.VoteFormResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("[API DOCS] 팀 투표 폼 생성")
class CreateVoteFormTest extends InitVoteTest {


	@Test
	@DisplayName("팀 투표 폼 생성 성공")
	void create_team_vote_success() {
		saveTeam();
		VoteFormRequest request = new VoteFormRequest(30, false);
		VoteFormResponse response = new VoteFormResponse(teamId, 30, false);

		//given
		given(this.spec)
			.filter(document(DEFAULT_RESTDOCS_PATH, CREATE_VOTE_FORM_REQUEST_BODY_SNIPPET,
				CREATE_VOTE_FORM_SUCCESS_RESPONSE_SNIPPET))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(CONTENT_TYPE, APPLICATION_JSON)
			.body(request)
			.log().all()

			//when
			.when()
			.post("/votes/teams/{teamId}/forms", teamId)

			//then
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("code", Matchers.equalTo(CREATE_VOTE_FORM_SUCCESS.getCode()))
			.body("message", Matchers.equalTo(CREATE_VOTE_FORM_SUCCESS.getMessage()))
			.body("data.divisorMinutes", Matchers.equalTo(response.getDivisorMinutes()))
			.log().all();
	}

	@Test
	@DisplayName("팀 투표 폼 생성 실패(이미 팀 투표 폼이 생성된 경우)")
	void create_team_vote_fail() {
		saveTeam();
		VoteFormRequest request = new VoteFormRequest(30, false);
		teamVoteFormRepository.save(new TeamVoteForm(team, 30, false));

		//given
		given(this.spec)
			.filter(document(DEFAULT_RESTDOCS_PATH, CREATE_VOTE_FORM_REQUEST_BODY_SNIPPET,
				CREATE_VOTE_FORM_FAIL_RESPONSE_SNIPPET))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(CONTENT_TYPE, APPLICATION_JSON)
			.body(request)
			.log().all()

			//when
			.when()
			.post("/votes/teams/{teamId}/forms", teamId)

			//then
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("code", Matchers.equalTo(ALREADY_EXISTS_TEAM_VOTE.getCode()))
			.body("message", Matchers.equalTo(ALREADY_EXISTS_TEAM_VOTE.getMessage()))
			.log().all();
	}
}
