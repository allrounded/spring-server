package hufs.team.mogong.service;

import static com.amazonaws.HttpMethod.PUT;

import com.amazonaws.services.s3.AmazonS3;
import hufs.team.mogong.service.dto.PreSignedUrlResponse;
import java.util.Date;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ImageUploadService {

	private static final String IMAGE_DIR = "image/";
	private static final int SET_TIME = 1000 * 60 * 10;

	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public ImageUploadService(AmazonS3 amazonS3) {
		this.amazonS3 = amazonS3;
	}

	public PreSignedUrlResponse generate(String extension) {
		log.debug("IMAGE FILE EXTENSION = {}", extension);
		String fileName = IMAGE_DIR + UUID.randomUUID() + extension;
		log.debug("IMAGE FILE NAME = {}", fileName.substring(IMAGE_DIR.length()));
		return new PreSignedUrlResponse(
			getPreSignedUrl(fileName),
			fileName.substring(IMAGE_DIR.length())
		);
	}

	private String getPreSignedUrl(String fileName) {
		Date exp = new Date();
		exp.setTime(exp.getTime() + SET_TIME);
		log.debug("Pre-signed URL 만료 시간 = {}", exp);
		return amazonS3.generatePresignedUrl(bucket, fileName, exp, PUT).toString();
	}
}
