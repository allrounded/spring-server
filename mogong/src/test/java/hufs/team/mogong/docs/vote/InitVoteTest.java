package hufs.team.mogong.docs.vote;

import static hufs.team.mogong.team.DayOfWeek.*;
import static hufs.team.mogong.team.DayOfWeek.MON;

import hufs.team.mogong.docs.InitDocumentationTest;
import hufs.team.mogong.image.MemberImage;
import hufs.team.mogong.image.TeamImage;
import hufs.team.mogong.image.repository.MemberImageRepository;
import hufs.team.mogong.image.repository.TeamImageRepository;
import hufs.team.mogong.member.Member;
import hufs.team.mogong.member.repository.MemberRepository;
import hufs.team.mogong.team.DayOfWeek;
import hufs.team.mogong.team.Team;
import hufs.team.mogong.team.repository.TeamRepository;
import hufs.team.mogong.team.service.dto.request.TimeRequests;
import hufs.team.mogong.team.service.dto.request.TimeTableRequest;
import hufs.team.mogong.timetable.repository.MemberTimeTableV2Repository;
import hufs.team.mogong.tool.RestTemplateMocks;
import hufs.team.mogong.vote.repository.TeamVoteFormRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.TestPropertySource;

@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
	"image-server.url=http://localhost:${wiremock.server.port}/teams/{teamId}/members/{memberId}",
	"image-server.result=http://localhost:${wiremock.server.port}/teams/{teamId}/results"
})
public abstract class InitVoteTest extends InitDocumentationTest {

	@Autowired
	protected TeamRepository teamRepository;

	@Autowired
	protected TeamImageRepository teamImageRepository;

	@Autowired
	protected MemberImageRepository memberImageRepository;

	@Autowired
	protected MemberRepository memberRepository;

	@Autowired
	protected TeamVoteFormRepository teamVoteFormRepository;

	@Autowired
	protected MemberTimeTableV2Repository memberTimeTableV2Repository;

	protected static final Integer NUMBER_OF_TEAM = 5;
	protected static final String AUTH_CODE = "1234";
	protected static final Integer DIVISOR_MINUTES = 30;

	protected Long teamId;
	protected Team team;
	protected Member member1;
	protected Member member2;
	protected List<Long> times = new ArrayList<>();;
	protected TimeRequests timeRequests;

	@BeforeEach
	protected void init() throws IOException {
		teamVoteFormRepository.deleteAllInBatch();
		memberTimeTableV2Repository.deleteAllInBatch();
		memberImageRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
		teamImageRepository.deleteAllInBatch();
		teamRepository.deleteAllInBatch();

		times.add(33333L);
		times.add(11111L);
		times.add(22222L);
		times.add(555555L);
		times.add(33333L);
		times.add(0L);
		times.add(0L);
		RestTemplateMocks.setUpResponses();
	}

	protected void saveTeam() {
		team = teamRepository.save(new Team("1c487536-08ef-4332-bc2f-16830f49495f",
			2, 1, "1234"));
		teamImageRepository.save(new TeamImage(team,
			"https://mogong.s3.ap-northeast-2.amazonaws.com/image/teams/results/cf1fd929-4896-4a03-9382-33b70445c87a.jpeg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20230510T185520Z&X-Amz-SignedHeaders=host&X-Amz-Expires=599&X-Amz-Credential=AKIATAIREWDPDOXL3X74%2F20230510%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Signature=46aee1b91e37155e5681300e2226479cb349f46acd29a42e6abbfabf27dd318e"));
		teamId = team.getTeamId();
	}

	protected void saveMember() {
		member1 = memberRepository.save(new Member("dummy_nickname-1", team, true));
		member2 = memberRepository.save(new Member("dummy_nickname-2", team, false));
		memberImageRepository.save(new MemberImage(member1,
			"https://mogong.s3.ap-northeast-2.amazonaws.com/image/sample_2.JPG"));
		memberImageRepository.save(new MemberImage(member2,
			"https://mogong.s3.ap-northeast-2.amazonaws.com/image/sample_3.JPG"));
	}

	protected void saveTimeRequest() {
		TimeTableRequest[] timeTableRequests = {
			new TimeTableRequest(MON,
				new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1,
					1, 0, 1, 0, 1}),
			new TimeTableRequest(TUE,
				new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1,
					1, 0, 1, 0, 1}),
			new TimeTableRequest(WED,
				new int[]{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0}),
			new TimeTableRequest(THU,
				new int[]{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0}),
			new TimeTableRequest(FRI,
				new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1,
					1, 0, 1, 0, 1}),
			new TimeTableRequest(SAT,
				new int[]{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0}),
			new TimeTableRequest(SUN,
				new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1,
					1, 0, 1, 0, 1}),

		};

		timeRequests = new TimeRequests(DIVISOR_MINUTES, timeTableRequests);
	}
}
