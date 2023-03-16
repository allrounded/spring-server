package hufs.team.mogong.docs;

import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseBody;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
class SimpleRestDocsTest {

	private static final String BASE_URL = "http://localhost";

	@LocalServerPort
	private int port;

	protected RequestSpecification spec;

	@BeforeEach
	void setUpRestDocs(RestDocumentationContextProvider restDocumentation) {
		this.spec = new RequestSpecBuilder()
			.addFilter(
				documentationConfiguration(restDocumentation)
					.snippets()
					.withDefaults(
						httpRequest(),
						httpResponse(),
						requestBody(),
						responseBody()
					)
			)
			.build();
	}

	@Test
	void simpleRead(){
	    //given
		RestDocumentationFilter restDocumentationFilter = document(
			"simple-read"
		);

		RequestSpecification given = RestAssured.given(this.spec)
			.baseUri(BASE_URL)
			.port(port)
			.filter(restDocumentationFilter);

		//when
		Response actual = given.when()
			.get("/simple");

		//then
		actual.then()
			.statusCode(HttpStatus.OK.value())
			.log().all();
	}
}
