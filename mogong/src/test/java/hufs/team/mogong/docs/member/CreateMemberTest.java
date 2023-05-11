package hufs.team.mogong.docs.member;


import static hufs.team.mogong.common.response.ResponseCodeAndMessages.CREATE_MEMBER_SUCCESS;
import static hufs.team.mogong.docs.member.MemberSnippet.CREATE_MEMBER_REQUEST_BODY_SNIPPET;
import static hufs.team.mogong.docs.member.MemberSnippet.MEMBER_RESPONSE_SNIPPET;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import hufs.team.mogong.docs.InitDocumentationTest;
import hufs.team.mogong.image.TeamImage;
import hufs.team.mogong.image.repository.TeamImageRepository;
import hufs.team.mogong.member.service.dto.request.CreateMemberRequest;
import hufs.team.mogong.team.Team;
import hufs.team.mogong.team.repository.TeamRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("[API DOCS] 팀원 생성")
public class CreateMemberTest extends InitDocumentationTest {

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private TeamImageRepository teamImageRepository;

	private Long teamId;
	private Team team;

	@BeforeEach
	void init() {
		teamRepository.deleteAllInBatch();
		team = teamRepository.save(new Team("1c487536-08ef-4332-bc2f-16830f49495f",
			5, 3, "1234"));
		teamImageRepository.save(new TeamImage(team, "https://aws.s3.dummy-img.jpeg"));
		teamId = team.getTeamId();
	}

	@Test
	@DisplayName("팀원 생성 성공")
	void create_member_success(){
		CreateMemberRequest request = new CreateMemberRequest("dummy_nickname");

		//given
		given(this.spec)
			.filter(document(
				DEFAULT_RESTDOCS_PATH,
				CREATE_MEMBER_REQUEST_BODY_SNIPPET,
				MEMBER_RESPONSE_SNIPPET))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header(CONTENT_TYPE, APPLICATION_JSON)
			.body(request)

		//when
			.when()
			.post("/teams/{teamId}/members", teamId)

		//then
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("code", Matchers.equalTo(CREATE_MEMBER_SUCCESS.getCode()))
			.body("message", Matchers.equalTo(CREATE_MEMBER_SUCCESS.getMessage()));
	}
}
