package br.com.castgroup;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.castgroup.controllers.LeftRightController;
import br.com.castgroup.models.Left;
import br.com.castgroup.models.Right;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LeftRightResourcesTest {

	@Mock
	protected LeftRightController leftRightController;

	@Value("${local.server.port}")
	protected int port;

	@Before
	public void setUp() {
		RestAssured.port = port;
	}

	@Test
	public void a() {
		given().get("/v1/diff/").then().statusCode(HttpStatus.SC_OK).body("Error",
				containsString("Nenhum documento left e right encontrado"));
	}

	@Test
	public void b() {
		Right right = new Right();
		right.setBase64("RGllZ28gQWFzaXMgQ2FydmFsaG8gcGVyZWlyYQ==");
		given().contentType(ContentType.JSON).pathParam("id", 12345).body(right).post("/v1/diff/{id}/right").then()
				.statusCode(HttpStatus.SC_OK);
		given().get("/v1/diff/").then().statusCode(HttpStatus.SC_OK).body("Error",
				containsString("Nenhum documento left encontrado"));
	}

	@Test
	public void c() {
		Left left = new Left();
		left.setBase64("RGllZ28gQWFzaXMgQ2FydmFsaG8gcGVyZWlyYQ==");
		given().contentType(ContentType.JSON).pathParam("id", 12345).body(left).post("/v1/diff/{id}/left").then()
				.statusCode(HttpStatus.SC_OK);
		given().get("/v1/diff/").then().statusCode(HttpStatus.SC_OK).body("Error",
				containsString("Nenhum documento right encontrado"));
	}

	@Test
	public void d() {
		Left left = new Left();
		left.setBase64("RGllZ28gQXNzaXMgQ2FydmFsaG8gUGVyZWlyYQ==");
		given().contentType(ContentType.JSON).pathParam("id", 12345).body(left).post("/v1/diff/{id}/left").then()
				.statusCode(HttpStatus.SC_OK);
		Right right = new Right();
		right.setBase64("RGllZ28gQXNzaXMgQ2FydmFsaG8gUGVyZWly");
		given().contentType(ContentType.JSON).pathParam("id", 12345).body(right).post("/v1/diff/{id}/right").then()
				.statusCode(HttpStatus.SC_OK);
		given().get("/v1/diff/").then().statusCode(HttpStatus.SC_OK).body("Error",
				containsString("Documentos 12345 e 12345 com tamanhos diferentes"));
	}

	@Test
	public void e() {
		Left left = new Left();
		left.setBase64("RGllZ28gQXNzaXMgQ2FydmFsaG8gUGVyZWlyYQ==");
		given().contentType(ContentType.JSON).pathParam("id", 12345).body(left).post("/v1/diff/{id}/left").then()
				.statusCode(HttpStatus.SC_OK);
		Right right = new Right();
		right.setBase64("RGllZ28gQXNzaXMgQ2FydmFsaG8gUGVyZWlyYQ==");
		given().contentType(ContentType.JSON).pathParam("id", 12345).body(right).post("/v1/diff/{id}/right").then()
				.statusCode(HttpStatus.SC_OK);
		given().get("/v1/diff/").then().statusCode(HttpStatus.SC_OK).body("Success",
				containsString("Documentos 12345 e 12345 idênticos"));
	}

}
