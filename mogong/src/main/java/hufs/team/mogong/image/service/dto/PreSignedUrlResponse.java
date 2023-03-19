package hufs.team.mogong.image.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class PreSignedUrlResponse {

	private final String preSignedUrl;
	private final String fileName;
}
