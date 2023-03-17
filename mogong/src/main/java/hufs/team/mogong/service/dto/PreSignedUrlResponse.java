package hufs.team.mogong.service.dto;

import lombok.Getter;

@Getter
public class PreSignedUrlResponse {

	private final String preSignedUrl;
	private final String fileName;

	public PreSignedUrlResponse(String preSignedUrl, String fileName) {
		this.preSignedUrl = preSignedUrl;
		this.fileName = fileName;
	}
}
