package hufs.team.mogong.docs.vote;

import static hufs.team.mogong.common.response.ResponseCodeAndMessages.CREATE_MEMBER_VOTE_SUCCESS;
import static hufs.team.mogong.docs.vote.VoteSnippet.CREATE_MEMBER_VOTE_REQUEST_BODY_SNIPPET;
import static hufs.team.mogong.docs.vote.VoteSnippet.CREATE_MEMBER_VOTE_SUCCESS_RESPONSE_SNIPPET;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import hufs.team.mogong.timetable.TeamTimeTable;
import hufs.team.mogong.timetable.repository.TeamTimeTableRepository;
import hufs.team.mogong.vote.TeamVote;
import hufs.team.mogong.vote.repository.TeamVoteRepository;
import hufs.team.mogong.vote.service.dto.request.MemberVoteRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("[API DOCS] 멤버 투표 생성")
class CreateMemberVoteTest extends InitVoteTest {

	@Test
	@DisplayName("멤버 투표 생성 성공")
	void create_member_vote_success() {
		saveTeam();
		saveMember();
		teamVoteRepository.saveAndFlush(new TeamVote(team, times));
		teamTimeTableRepository.saveAndFlush(
			new TeamTimeTable(team, new long[]{10, 20, 30, 40, 50, 60, 70}));

		saveTimeRequest();
		MemberVoteRequest request = new MemberVoteRequest(timeRequests);

		//given
		given(this.spec)
			.filter(document(DEFAULT_RESTDOCS_PATH, CREATE_MEMBER_VOTE_REQUEST_BODY_SNIPPET,
				CREATE_MEMBER_VOTE_SUCCESS_RESPONSE_SNIPPET))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(CONTENT_TYPE, APPLICATION_JSON)
			.body(request)

			//when
			.when()
			.post("/votes/teams/{teamId}/members/{memberId}", teamId, member1.getMemberId())

			//then
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("code", Matchers.equalTo(CREATE_MEMBER_VOTE_SUCCESS.getCode()))
			.body("message", Matchers.equalTo(CREATE_MEMBER_VOTE_SUCCESS.getMessage()))
			.log().all();
	}
}
