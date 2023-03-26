package hufs.team.mogong.docs;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import hufs.team.mogong.config.AwsMockConfig;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.internal.mapping.Jackson2Mapper;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.UriModifyingOperationPreprocessor;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = AwsMockConfig.class)
@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
public class InitDocumentationTest {

	protected static final String DEFAULT_RESTDOCS_PATH = "{class_name}/{method_name}/";
	protected static final String DEFAULT_RESTDOCS_SCHEMA = "http";
	protected static final String DEFAULT_RESTDOCS_HOST = "localhost";
	protected static final String CONTENT_TYPE = "Content-type";
	protected static final String APPLICATION_JSON = "application/json";

	@LocalServerPort
	private int port;

	@Autowired
	protected ObjectMapper objectMapper;

	protected RequestSpecification spec;

	{
		setUpRestAssured();
	}

	private void setUpRestAssured() {
		RestAssured.config = RestAssuredConfig.config()
			.objectMapperConfig(
				new ObjectMapperConfig(
					new Jackson2Mapper((type, charset) -> objectMapper)));
	}

	@BeforeEach
	void setUpRestDocs(RestDocumentationContextProvider restDocumentation) {
		RestAssured.port = port;
		UriModifyingOperationPreprocessor requestOperationProcessor = modifyUris()
			.scheme(DEFAULT_RESTDOCS_SCHEMA)
			.host(DEFAULT_RESTDOCS_HOST)
			.removePort();

		this.spec = new RequestSpecBuilder()
			.addFilter(
				documentationConfiguration(restDocumentation)
					.operationPreprocessors()
					.withRequestDefaults(requestOperationProcessor, prettyPrint())
					.withResponseDefaults(prettyPrint())
			)
			.build();
	}

}
