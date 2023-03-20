package hufs.team.mogong.common.exception;

import hufs.team.mogong.common.response.BaseResponse;
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

}
