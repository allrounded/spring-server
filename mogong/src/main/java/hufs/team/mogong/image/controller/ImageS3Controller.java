package hufs.team.mogong.image.controller;

import static hufs.team.mogong.common.response.ResponseCodeAndMessages.GENERATE_IMAGE_PRESIGNED_URL_SUCCESS;

import hufs.team.mogong.common.response.BaseResponse;
import hufs.team.mogong.image.service.ImageUploadService;
import hufs.team.mogong.image.service.dto.PreSignedUrlRequest;
import hufs.team.mogong.image.service.dto.PreSignedUrlResponse;
import java.io.IOException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
public class ImageS3Controller {

	private final ImageUploadService imageUploadService;

	public ImageS3Controller(ImageUploadService imageUploadService) {
		this.imageUploadService = imageUploadService;
	}

	@PostMapping
	public BaseResponse<PreSignedUrlResponse> upload(final @RequestBody PreSignedUrlRequest request){
		PreSignedUrlResponse response = imageUploadService.generate(request);
		return new BaseResponse<>(GENERATE_IMAGE_PRESIGNED_URL_SUCCESS, response);
	}
}
