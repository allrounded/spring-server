package hufs.team.mogong.docs.team;

import static hufs.team.mogong.common.exception.ErrorCodeAndMessages.NOT_COMPLETED_SUBMIT;
import static hufs.team.mogong.common.response.ResponseCodeAndMessages.FIND_TEAM_SUCCESS;
import static hufs.team.mogong.docs.team.TeamSnippet.TEAM_RESULT_FAIL_RESPONSE_SNIPPET;
import static hufs.team.mogong.docs.team.TeamSnippet.TEAM_RESULT_RESPONSE_SNIPPET;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import hufs.team.mogong.docs.InitDocumentationTest;
import hufs.team.mogong.image.MemberImage;
import hufs.team.mogong.image.TeamImage;
import hufs.team.mogong.image.repository.MemberImageRepository;
import hufs.team.mogong.image.repository.TeamImageRepository;
import hufs.team.mogong.member.Member;
import hufs.team.mogong.member.repository.MemberRepository;
import hufs.team.mogong.member.service.MemberService;
import hufs.team.mogong.team.Team;
import hufs.team.mogong.team.repository.TeamRepository;
import hufs.team.mogong.timetable.TimeTableV2;
import hufs.team.mogong.timetable.repository.TimeTableV2Repository;
import hufs.team.mogong.tool.RestTemplateMocks;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
	"image-server.url=http://localhost:${wiremock.server.port}/teams/{teamId}/members/{memberId}",
	"image-server.result=http://localhost:${wiremock.server.port}/teams/{teamId}/results"
})
@DisplayName("[API DOCS] Team 현황 조회 요청")
class FindTeamTest extends InitDocumentationTest {

	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private TeamImageRepository teamImageRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MemberImageRepository memberImageRepository;

	@Autowired
	private TimeTableV2Repository timeTableV2Repository;

	private Long teamId;
	private Team team;
	private Member member1;
	private Member member2;
	private Member member3;
	private List<Long> times = new ArrayList<>();;

	@BeforeEach
	void init() throws IOException {
		timeTableV2Repository.deleteAllInBatch();
		memberImageRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
		teamImageRepository.deleteAllInBatch();
		teamRepository.deleteAllInBatch();


		team = teamRepository.save(new Team("1c487536-08ef-4332-bc2f-16830f49495f",
			3, 3, "1234"));
		teamImageRepository.save(new TeamImage(team,
			"https://mogong.s3.ap-northeast-2.amazonaws.com/image/teams/results/cf1fd929-4896-4a03-9382-33b70445c87a.jpeg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20230510T185520Z&X-Amz-SignedHeaders=host&X-Amz-Expires=599&X-Amz-Credential=AKIATAIREWDPDOXL3X74%2F20230510%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Signature=46aee1b91e37155e5681300e2226479cb349f46acd29a42e6abbfabf27dd318e"));
		teamId = team.getTeamId();


		member1 = memberRepository.save(new Member("dummy_nickname-1", team));
		member2 = memberRepository.save(new Member("dummy_nickname-2", team));
		member3 = memberRepository.save(new Member("dummy_nickname-3", team));
		memberImageRepository.save(new MemberImage(member1,
			"https://mogong.s3.ap-northeast-2.amazonaws.com/image/sample_2.JPG"));
		memberImageRepository.save(new MemberImage(member2,
			"https://mogong.s3.ap-northeast-2.amazonaws.com/image/sample_3.JPG"));
		memberImageRepository.save(new MemberImage(member3,
			"https://mogong.s3.ap-northeast-2.amazonaws.com/image/sample_4.JPG"));

		times.add(33333L);
		times.add(11111L);
		times.add(22222L);
		times.add(555555L);
		times.add(33333L);
		times.add(0L);
		times.add(0L);
		RestTemplateMocks.setUpResponses();
	}

	/**
	 * 상황 1. 팀원 제출이 마무리 되었음
	 * 상황 2. 팀원 제출이 마무리 되지 않았음 (authCode 없음)
	 * 상황 3. 팀원 제출이 마무리 되지 않았음 (authCode 있음)
	 */

	@Test
	@DisplayName("팀 멤버 결과 요청 성공(authCode 무관)")
	void result_team_success() {
		timeTableV2Repository.save(new TimeTableV2(member1, times));
		timeTableV2Repository.save(new TimeTableV2(member2, times));
		timeTableV2Repository.save(new TimeTableV2(member3, times));


		//given
		given(this.spec)
			.filter(document(DEFAULT_RESTDOCS_PATH, TEAM_RESULT_RESPONSE_SNIPPET))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(CONTENT_TYPE, APPLICATION_JSON)
			.log().all()

			//when
			.when()
			.get("/teams/{teamId}/v2", teamId)

			//then
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("code", Matchers.equalTo(FIND_TEAM_SUCCESS.getCode()))
			.body("message", Matchers.equalTo(FIND_TEAM_SUCCESS.getMessage()))
			.log().all();
	}

	@Test
	@DisplayName("팀 멤버 결과 요청 실패(제출 수 미달)")
	void result_team_fail() {
		//given
		given(this.spec)
			.filter(document(DEFAULT_RESTDOCS_PATH, TEAM_RESULT_FAIL_RESPONSE_SNIPPET))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(CONTENT_TYPE, APPLICATION_JSON)
			.log().all()

			//when
			.when()
			.get("/teams/{teamId}/v2", teamId)

			//then
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("code", Matchers.equalTo(NOT_COMPLETED_SUBMIT.getCode()))
			.body("message", Matchers.equalTo(NOT_COMPLETED_SUBMIT.getMessage()))
			.log().all();
	}

	@Test
	@DisplayName("팀 멤버 결과 요청시, submit이 미충족 되더라도 알맞은 authCode가 있는 경우")
	void result_team_with_auth_code() {
		timeTableV2Repository.save(new TimeTableV2(member1, times));
		timeTableV2Repository.save(new TimeTableV2(member2, times));

		//given
		given(this.spec)
			.filter(document(DEFAULT_RESTDOCS_PATH, TEAM_RESULT_RESPONSE_SNIPPET))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(CONTENT_TYPE, APPLICATION_JSON)
			.log().all()

			//when
			.when()
			.get("/teams/{teamId}/v2?auth_code={authCode}", teamId, team.getAuthCode())

			//then
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("code", Matchers.equalTo(FIND_TEAM_SUCCESS.getCode()))
			.body("message", Matchers.equalTo(FIND_TEAM_SUCCESS.getMessage()))
			.log().all();
	}

	@Test
	@DisplayName("팀 멤버 결과 요청시, submit이 미충족 되었지만, 알맞지 않은 auth code가 있는 경우")
	void result_team_with_wrong_auth_code() {
		timeTableV2Repository.save(new TimeTableV2(member1, times));
		timeTableV2Repository.save(new TimeTableV2(member2, times));

		//given
		given(this.spec)
			.filter(document(DEFAULT_RESTDOCS_PATH, TEAM_RESULT_FAIL_RESPONSE_SNIPPET))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(CONTENT_TYPE, APPLICATION_JSON)
			.log().all()

			//when
			.when()
			.get("/teams/{teamId}/v2?auth_code={authCode}", teamId, "0000")

			//then
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("code", Matchers.equalTo(NOT_COMPLETED_SUBMIT.getCode()))
			.body("message", Matchers.equalTo(NOT_COMPLETED_SUBMIT.getMessage()))
			.log().all();
}

}
