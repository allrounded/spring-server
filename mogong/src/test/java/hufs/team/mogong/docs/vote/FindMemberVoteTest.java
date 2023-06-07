package hufs.team.mogong.docs.vote;

import static hufs.team.mogong.common.response.ResponseCodeAndMessages.FIND_MEMBER_VOTE_SUCCESS;
import static hufs.team.mogong.docs.vote.VoteSnippet.FIND_MEMBER_VOTE_SUCCESS_RESPONSE_SNIPPET;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import hufs.team.mogong.timetable.TeamTimeTable;
import hufs.team.mogong.vote.MemberVote;
import hufs.team.mogong.vote.TeamVote;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("[API DOCS] 멤버 투표 조회")
class FindMemberVoteTest extends InitVoteTest {

	@Test
	@DisplayName("멤버 투표 조회 성공")
	void find_member_vote_success() {
		saveTeam();
		saveMember();
		teamTimeTableRepository.saveAndFlush(
			new TeamTimeTable(team, new long[]{10, 20, 30, 40, 50, 60, 70}));

		saveTimeRequest();
		teamVoteRepository.saveAndFlush(new TeamVote(team, initTeamVoteForUpdate()));
		memberVoteRepository.saveAndFlush(new MemberVote(member1, times));

		//given
		given(this.spec)
			.filter(document(DEFAULT_RESTDOCS_PATH,
				FIND_MEMBER_VOTE_SUCCESS_RESPONSE_SNIPPET))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(CONTENT_TYPE, APPLICATION_JSON)

			//when
			.when()
			.get("/votes/teams/{teamId}/members/{memberId}", teamId, member1.getMemberId())

			//then
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("code", Matchers.equalTo(FIND_MEMBER_VOTE_SUCCESS.getCode()))
			.body("message", Matchers.equalTo(FIND_MEMBER_VOTE_SUCCESS.getMessage()))
			.log().all();
	}

}
