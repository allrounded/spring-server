package hufs.team.mogong.tool;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static java.nio.charset.Charset.defaultCharset;
import static org.springframework.util.StreamUtils.copyToString;

import com.amazonaws.services.s3.Headers;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


public class RestTemplateMocks {

	public static void setUpResponses() throws IOException {
		setUpMockTeamResultResponse();
		setUpMockMemberImageUploadResponse();
	}

	private static void setUpMockTeamResultResponse() throws IOException {
		stubFor(post(urlPathMatching("/teams/[0-9]+/results"))
			.willReturn(aResponse()
				.withStatus(HttpStatus.OK.value())
				.withHeader(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.withBody(getMockResponseBodyByPath("payload/post-team-result-response.json"))
			));
	}

	private static void setUpMockMemberImageUploadResponse() throws IOException {
		stubFor(post(urlPathMatching("/teams/[0-9]+/members/[0-9]+"))
			.willReturn(aResponse()
				.withStatus(HttpStatus.OK.value())
				.withHeader(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.withBody(getMockResponseBodyByPath("payload/post-member-image-upload-response.json"))
			));
	}

	private static String getMockResponseBodyByPath(String path) throws IOException {
		return copyToString(getMockResourceStream(path), defaultCharset());
	}

	private static InputStream getMockResourceStream(String path) {
		return RestTemplateMocks.class.getClassLoader().getResourceAsStream(path);
	}
}
