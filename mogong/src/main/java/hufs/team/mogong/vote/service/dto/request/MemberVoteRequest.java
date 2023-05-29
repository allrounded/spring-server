package hufs.team.mogong.vote.service.dto.request;

import hufs.team.mogong.team.service.dto.request.TimeRequests;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class MemberVoteRequest {

	private TimeRequests timeRequests;
}
