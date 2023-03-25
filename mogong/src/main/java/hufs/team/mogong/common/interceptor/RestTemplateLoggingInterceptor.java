package hufs.team.mogong.common.interceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

@Slf4j
public class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		log.debug("Request Body: {}", new String(body, StandardCharsets.UTF_8));
		ClientHttpResponse response = execution.execute(request, body);

		String responseBody = new BufferedReader(
			new InputStreamReader(
				response.getBody(),
				StandardCharsets.UTF_8))
			.lines()
			.collect(Collectors.joining("\n"));

		log.debug("Response Body: {}", responseBody);
		return response;
	}
}
