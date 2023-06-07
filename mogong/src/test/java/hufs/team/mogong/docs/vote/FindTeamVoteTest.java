package hufs.team.mogong.docs.vote;

import static hufs.team.mogong.common.response.ResponseCodeAndMessages.FIND_TEAM_TOTAL_VOTES_SUCCESS;
import static hufs.team.mogong.docs.vote.VoteSnippet.FIND_TEAM_TOTAL_VOTES_SUCCESS_RESPONSE_SNIPPET;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import hufs.team.mogong.timetable.TeamTimeTable;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("[API DOCS] 팀 투표 조회")
class FindTeamVoteTest extends InitVoteTest{

	@Test
	@DisplayName("팀 투표 조회 성공")
	void find_team_vote_success() {
		saveTeam();
		saveMember();
		teamTimeTableRepository.saveAndFlush(new TeamTimeTable(team, new long[]{10, 20, 30, 40, 50, 60, 70}));

		//given
		given(this.spec)
			.filter(document(DEFAULT_RESTDOCS_PATH, FIND_TEAM_TOTAL_VOTES_SUCCESS_RESPONSE_SNIPPET))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(CONTENT_TYPE, APPLICATION_JSON)

			//when
			.when()
			.get("/votes/teams/{teamId}", teamId)

			//then
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("code", Matchers.equalTo(FIND_TEAM_TOTAL_VOTES_SUCCESS.getCode()))
			.body("message", Matchers.equalTo(FIND_TEAM_TOTAL_VOTES_SUCCESS.getMessage()))
			.log().all();
	}
}
