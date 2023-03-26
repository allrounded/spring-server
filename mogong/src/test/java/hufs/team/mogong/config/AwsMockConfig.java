package hufs.team.mogong.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import hufs.team.mogong.tool.ProcessUtils;
import io.findify.s3mock.S3Mock;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.util.UriComponentsBuilder;


@Slf4j
@TestConfiguration
public class AwsMockConfig {


	@Value("${cloud.aws.s3.bucket}")
	public String bucket;

	@Value("${cloud.aws.region.static}")
	private String region;

	@Value("${cloud.aws.s3.mock.port}")
	private int port;

	@Bean
	public S3Mock s3Mock() throws IOException {
		port = ProcessUtils.isRunningPort(port) ? ProcessUtils.findAvailableRandomPort() : port;
		log.debug("IN MEMORY S3 MOCK SERVER START! PORT = {}", port);
		return new S3Mock.Builder()
			.withPort(port)
			.withInMemoryBackend()
			.build();
	}

	@Bean
	@Primary
	public AmazonS3 amazonS3Mock(S3Mock s3Mock) {
		s3Mock.start();
		AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(
			getUri(), region);

		AmazonS3 client = AmazonS3ClientBuilder
			.standard()
			.withPathStyleAccessEnabled(true)
			.withEndpointConfiguration(endpoint)
			.withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
			.build();

		client.createBucket(bucket);
		return client;
	}

	private String getUri() {
		return UriComponentsBuilder.newInstance()
			.scheme("http")
			.host("localhost")
			.port(port)
			.build()
			.toUriString();
	}
}
