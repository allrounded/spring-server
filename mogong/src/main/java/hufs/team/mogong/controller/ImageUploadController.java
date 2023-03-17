package hufs.team.mogong.controller;

import static hufs.team.mogong.common.response.ResponseCodeAndMessages.GENERATE_IMAGE_PRESIGNED_URL_SUCCESS;

import hufs.team.mogong.common.response.BaseResponse;
import hufs.team.mogong.service.ImageUploadService;
import hufs.team.mogong.service.dto.PreSignedUrlResponse;
import java.io.IOException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
public class ImageUploadController {

	private final ImageUploadService imageUploadService;

	public ImageUploadController(ImageUploadService imageUploadService) {
		this.imageUploadService = imageUploadService;
	}

	@GetMapping
	public BaseResponse<PreSignedUrlResponse> upload(
		final @RequestParam("extension") String extension) throws IOException {
		PreSignedUrlResponse response = imageUploadService.generate(extension);
		return new BaseResponse<>(GENERATE_IMAGE_PRESIGNED_URL_SUCCESS, response);
	}
}
