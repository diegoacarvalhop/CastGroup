package br.com.castgroup;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.castgroup.models.Left;
import br.com.castgroup.models.Right;
import br.com.castgroup.resources.LeftRightResources;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LeftRightResourcesTest {

	@Mock
	protected LeftRightResources leftRightResources;

	@Value("${local.server.port}")
	protected int port;

	@Before
	public void setUp() {
		RestAssured.port = port;
	}

	@Test
	public void a() {
		Left left = new Left();
		left.setBase64("RGllZ28gQWFzaXMgQ2FydmFsaG8gcGVyZWlyYQ==");
		given().contentType(ContentType.JSON).pathParam("id", 12345L).body(left).post("/v1/diff/{id}/left").then()
				.statusCode(HttpStatus.SC_OK);
	}

	@Test
	public void b() {
		Right right = new Right();
		right.setBase64("RGllZ28gQWFzaXMgQ2FydmFsaG8gcGVyZWlyYQ==");
		given().contentType(ContentType.JSON).pathParam("id", 12345L).body(right).post("/v1/diff/{id}/right").then()
				.statusCode(HttpStatus.SC_OK);
	}

	@Test
	public void cSucess() {
		given().get("/v1/diff/").then().statusCode(HttpStatus.SC_OK).body("Success",
				containsString("Documentos <12345> e <12345> idênticos"));
	}

	@Ignore
	@Test
	public void cError() {
		given().get("/v1/diff/").then().statusCode(HttpStatus.SC_OK).body("Error",
				containsString("Documentos <12345> e <12345> com tamanhos diferentes"));
	}

	@Ignore
	@Test
	public void cErrorDiff() {
		given().get("/v1/diff/").then().statusCode(HttpStatus.SC_OK).body("12345",
				containsString("Diego 'Aasis' Carvalho 'pereira'"));
	}

}
