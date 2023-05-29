package hufs.team.mogong.vote.controller;

import static hufs.team.mogong.common.response.ResponseCodeAndMessages.CREATE_VOTE_FORM_SUCCESS;
import static hufs.team.mogong.common.response.ResponseCodeAndMessages.FIND_VOTE_FORM_SUCCESS;

import hufs.team.mogong.common.response.BaseResponse;
import hufs.team.mogong.vote.service.VoteService;
import hufs.team.mogong.vote.service.dto.request.MemberVoteRequest;
import hufs.team.mogong.vote.service.dto.request.VoteFormRequest;
import hufs.team.mogong.vote.service.dto.response.VoteFormResponse;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/votes/teams/{teamId}")
public class VoteController {

	private final VoteService voteService;

	public VoteController(VoteService voteService) {
		this.voteService = voteService;
	}

	/**
	 * 0-1. 투표 폼 생성 (v)
	 * 0-2. 투표 폼 조회 (v)
	 * 1. 투표 결과 조회
	 * 2. 투표 결과 생성 (각 멤버별 요청)
	 * 3. 투표 결과 수정 (각 멤버별 요청)
	 */

	@PostMapping("/forms")
	public BaseResponse<VoteFormResponse> createVoteForm(final @PathVariable Long teamId,
		final @Valid @RequestBody VoteFormRequest request) {
		VoteFormResponse response = voteService.createVoteForm(teamId, request);
		return new BaseResponse<>(CREATE_VOTE_FORM_SUCCESS, response);
	}

	@GetMapping("/forms")
	public BaseResponse<VoteFormResponse> readVoteForm(final @PathVariable Long teamId) {
		VoteFormResponse response = voteService.findVoteForm(teamId);
		return new BaseResponse<>(FIND_VOTE_FORM_SUCCESS, response);
	}

	@GetMapping
	public BaseResponse<Void> readVotes(final @PathVariable Long teamId) {
		return new BaseResponse<>(null, null);
	}

	@PostMapping("/members/{memberId}")
	public BaseResponse<Void> createVote(final @PathVariable Long teamId,
		final @PathVariable Long memberId, final @RequestBody MemberVoteRequest request) {
//		voteService.create(teamId, memberId, request);
		return new BaseResponse<>(null, null);
	}

	@PutMapping("/members/{memberId}")
	public BaseResponse<Void> updateVote(final @PathVariable Long teamId,
		final @PathVariable Long memberId) {
		return new BaseResponse<>(null, null);
	}

}
