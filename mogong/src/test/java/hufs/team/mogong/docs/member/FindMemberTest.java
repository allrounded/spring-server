package hufs.team.mogong.docs.member;

import static hufs.team.mogong.common.response.ResponseCodeAndMessages.FIND_MEMBER_SUCCESS;
import static hufs.team.mogong.docs.member.MemberSnippet.MEMBER_RESPONSE_SNIPPET;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import hufs.team.mogong.docs.InitDocumentationTest;
import hufs.team.mogong.image.MemberImage;
import hufs.team.mogong.image.TeamImage;
import hufs.team.mogong.image.repository.MemberImageRepository;
import hufs.team.mogong.image.repository.TeamImageRepository;
import hufs.team.mogong.member.Member;
import hufs.team.mogong.member.repository.MemberRepository;
import hufs.team.mogong.team.Team;
import hufs.team.mogong.team.repository.TeamRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("[API DOCS] 팀원 조회")
class FindMemberTest extends InitDocumentationTest {

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MemberImageRepository memberImageRepository;

	@Autowired
	private TeamImageRepository teamImageRepository;

	private Long teamId;
	private Team team;
	private Long memberId;
	private Member member;

	@BeforeEach
	void init() {
		memberImageRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
		teamImageRepository.deleteAllInBatch();
		teamRepository.deleteAllInBatch();


		team = teamRepository.save(new Team("1c487536-08ef-4332-bc2f-16830f49495f",
			5, 3, "1234"));
		teamImageRepository.save(new TeamImage(team, "https://aws.s3.dummy-img.jpeg"));
		teamId = team.getTeamId();


		member = memberRepository.save(new Member("dummy_nickname", team));
		memberId = member.getMemberId();

		memberImageRepository.save(new MemberImage(member, "https://aws.s3.dummy-img/teams/1/dummy_img.jpeg"));
	}

	@Test
	@DisplayName("팀원 조회 성공")
	void find_member_success(){
	    //given
		given(this.spec)
			.filter(document(DEFAULT_RESTDOCS_PATH, MEMBER_RESPONSE_SNIPPET))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(CONTENT_TYPE, APPLICATION_JSON)
			.pathParam("teamId", teamId)
			.pathParam("memberId", memberId)
			.log().all()

	    //when
			.when()
			.get("/teams/{teamId}/members/{memberId}", teamId, memberId)

	    //then
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("code", Matchers.equalTo(FIND_MEMBER_SUCCESS.getCode()))
			.body("message", Matchers.equalTo(FIND_MEMBER_SUCCESS.getMessage()))
			.log().all();
	}

}
