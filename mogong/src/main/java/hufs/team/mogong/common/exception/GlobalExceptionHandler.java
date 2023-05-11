package hufs.team.mogong.common.exception;

import hufs.team.mogong.common.response.BaseResponse;
import hufs.team.mogong.team.exception.NotCompletedSubmit;
import hufs.team.mogong.team.service.dto.response.SubmitResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BaseException.class)
	public BaseResponse<Void> handleConversionFailed(BaseException e) {
		log.warn("Error Code={}, Error Message={}", e.getCode(), e.getMessage());
		return BaseResponse.error(e.getCode(), e.getMessage());
	}

	@ExceptionHandler(NotCompletedSubmit.class)
	public BaseResponse<SubmitResponse> handleNotCompletedSubmit(NotCompletedSubmit e) {
		log.debug("[NOT COMPLETED SUBMIT] 현재 이미지 요청 수 = {}", e.getNumberOfSubmit());
		return new BaseResponse<>(
			e.getCode(),
			e.getMessage(),
			new SubmitResponse(e.getNumberOfSubmit(), e.getNumberOfMember()));
	}
}
