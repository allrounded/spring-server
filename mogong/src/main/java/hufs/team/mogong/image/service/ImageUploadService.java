package hufs.team.mogong.image.service;

import static com.amazonaws.HttpMethod.PUT;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import hufs.team.mogong.image.service.dto.PreSignedUrlRequest;
import hufs.team.mogong.image.service.dto.PreSignedUrlResponse;
import java.util.Date;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ImageUploadService {

	private static final String IMAGE_DIR = "image/";
	private static final long SET_TIME = 100000L * 60 * 60;

	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public ImageUploadService(AmazonS3 amazonS3) {
		this.amazonS3 = amazonS3;
	}

	public PreSignedUrlResponse generate(PreSignedUrlRequest request) {
		String extension = request.getExtension();
		log.debug("IMAGE FILE EXTENSION = {}", extension);
		String fileName = IMAGE_DIR + UUID.randomUUID() + extension;
		log.debug("IMAGE FILE NAME = {}", fileName.substring(IMAGE_DIR.length()));
		return new PreSignedUrlResponse(
			getPreSignedUrl(fileName),
			fileName.substring(IMAGE_DIR.length()));
	}

	public PreSignedUrlResponse generate(PreSignedUrlRequest request, String imagePath) {
		String extension = request.getExtension();
		log.debug("IMAGE FILE EXTENSION = {}", extension);
		String fileName = imagePath + UUID.randomUUID() + extension;
		log.debug("IMAGE FILE NAME = {}", fileName.substring(imagePath.length()));
		return new PreSignedUrlResponse(
			getPreSignedUrl(fileName),
			fileName.substring(imagePath.length()));
	}

	public PreSignedUrlResponse generateTeamUrl(PreSignedUrlRequest request, String imagePath) {
		String extension = request.getExtension();
		log.debug("IMAGE FILE EXTENSION = {}", extension);
		String fileName = imagePath + extension;
		log.debug("IMAGE FILE NAME = {}", fileName.substring(imagePath.length()));
		return new PreSignedUrlResponse(
			getPreSignedUrl(fileName),
			fileName.substring(imagePath.length()));
	}

	private String getPreSignedUrl(String fileName) {
		Date exp = new Date();
		exp.setTime(exp.getTime() + SET_TIME);
		log.debug("Pre-signed URL 만료 시간 = {}", exp);
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
			bucket, fileName)
			.withMethod(PUT)
			.withExpiration(exp);
		generatePresignedUrlRequest.addRequestParameter(
			Headers.S3_CANNED_ACL,
			CannedAccessControlList.PublicRead.toString());

		return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
	}
}
