package hufs.team.mogong.vote.service.dto.response;

import hufs.team.mogong.team.service.dto.response.TimeResponses;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TeamTotalVotesResponse {

	/**
	 * 1. 팀 시간표 현황
	 * 2. 팀 투표 현황
	 */

	private Long teamId;
	private final TimeResponses teamTimeTable;
	private final TimeResponses teamVote;
}
