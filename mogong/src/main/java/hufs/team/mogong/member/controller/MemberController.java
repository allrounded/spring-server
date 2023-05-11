package hufs.team.mogong.member.controller;

import static hufs.team.mogong.common.response.ResponseCodeAndMessages.CREATE_MEMBER_SUCCESS;
import static hufs.team.mogong.common.response.ResponseCodeAndMessages.FIND_MEMBER_SUCCESS;
import static hufs.team.mogong.common.response.ResponseCodeAndMessages.UPLOAD_MEMBER_IMAGE_SUCCESS;

import hufs.team.mogong.common.response.BaseResponse;
import hufs.team.mogong.member.service.MemberService;
import hufs.team.mogong.member.service.dto.request.CreateMemberRequest;
import hufs.team.mogong.member.service.dto.response.MemberImageUploadResponse;
import hufs.team.mogong.member.service.dto.response.MemberResponse;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("teams/{teamId}/members")
public class MemberController {

	private final MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@PostMapping
	public BaseResponse<MemberResponse> create(final @PathVariable Long teamId,
		final @Valid @RequestBody CreateMemberRequest request) {
		MemberResponse response = memberService.create(teamId, request);
		return new BaseResponse<>(CREATE_MEMBER_SUCCESS, response);
	}

	@GetMapping("/{memberId}")
	public BaseResponse<MemberResponse> findById(final @PathVariable Long memberId) {
		MemberResponse response = memberService.findById(memberId);
		return new BaseResponse<>(FIND_MEMBER_SUCCESS, response);
	}

	@PutMapping("/{memberId}/images/v1")
	public BaseResponse<MemberImageUploadResponse> uploadV1(final @PathVariable Long teamId,
		final @PathVariable Long memberId) {

		MemberImageUploadResponse response = memberService.upsertV1(teamId, memberId);
		return new BaseResponse<>(UPLOAD_MEMBER_IMAGE_SUCCESS, response);
	}

	@PutMapping("/{memberId}/images/v2")
	public BaseResponse<MemberImageUploadResponse> uploadV2(final @PathVariable Long teamId,
		final @PathVariable Long memberId) {

		MemberImageUploadResponse response = memberService.upsertV2(teamId, memberId);
		return new BaseResponse<>(UPLOAD_MEMBER_IMAGE_SUCCESS, response);
	}

}
