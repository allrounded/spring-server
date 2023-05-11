package hufs.team.mogong.team.service.dto.response;

import hufs.team.mogong.team.exception.ImageServerError;
import hufs.team.mogong.team.exception.TeamResultBadRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ImageServerResultResponse {

	private Integer code;

	public void checkStatusCode() {

		if (this.code == null || this.code == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
			throw new ImageServerError();
		}

		if (this.code == HttpStatus.BAD_REQUEST.value()) {
			throw new TeamResultBadRequest();
		}
	}
}
