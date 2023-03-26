package hufs.team.mogong.team.service.dto.request;

import hufs.team.mogong.image.Image;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ImageUrlRequest {

	private String url;

	public static ImageUrlRequest from(Image image) {
		return new ImageUrlRequest(image.getUrl());
	}
}
