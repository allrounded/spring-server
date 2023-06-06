package hufs.team.mogong.vote.service.dto.response;

import hufs.team.mogong.team.service.dto.response.TimeResponses;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class MemberVoteResponse {

	/**
	 * 1. 팀 시간표 현황 2. 팀 투표 현황
	 */

	private TeamTotalVotesResponse teamTotalVotesResponse;
	private Long memberId;
	private final TimeResponses memberVote;
}
