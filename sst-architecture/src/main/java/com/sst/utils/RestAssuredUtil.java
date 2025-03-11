package com.sst.utils;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.lang.Integer.parseInt;
import static org.springframework.http.HttpStatus.OK;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * A Util to request a HealthRib endpoints using a {@link RestAssured}
 * 
 * 
 * @author Lucas Ribeiro
 */
public class RestAssuredUtil {

	@Autowired
	private static Environment environment;

	private static Integer getPort() {
		if (environment == null) {
			return 8080;
		}
		return parseInt(environment.getProperty("server.port", "8080"));
	}

	/**
	 * Executes a post request to a API itself by URL and object of return of it
	 * 
	 * @author Lucas Ribeiro
	 * @param url       of request
	 * @param extractAs object extracted by body
	 */
	public static final <T> T post(String url, Class<T> extractAs) {
		return given().basePath(url).port(getPort()).contentType(JSON).when().post().then().statusCode(OK.value())
				.extract().body().as(extractAs);
	}

	/**
	 * Executes a post request to a API itself by URL, body and object of return of
	 * it
	 * 
	 * @author Lucas Ribeiro
	 * @param url       of request
	 * @param body      of request
	 * @param extractAs object extracted by body
	 */
	public static final <T, Body extends Object> T post(String url, Body body, Class<T> extractAs) {
		return given().basePath(url).port(getPort()).body(body).contentType(JSON).when().post().then()
				.statusCode(OK.value()).extract().body().as(extractAs);
	}

	/**
	 * Executes a POST request to a API itself by URL, port and object of return of
	 * it
	 * 
	 * @author Lucas Ribeiro
	 * @param url       of request
	 * @param port      of request
	 * @param extractAs object extracted by body
	 */
	public static final <T> T post(String url, Integer port, Class<T> extractAs) {
		return given().basePath(url).port(port).contentType(JSON).when().post().then().statusCode(OK.value()).extract()
				.body().as(extractAs);
	}

	/**
	 * Executes a POST request to a API itself by URL, port, body and object of
	 * return of it
	 * 
	 * @author Lucas Ribeiro
	 * @param url       of request
	 * @param port      of request
	 * @param extractAs object extracted by body
	 */
	public static final <T, Body extends Object> T post(String url, Integer port, Body body, Class<T> extractAs) {
		return given().basePath(url).port(port).body(body).contentType(JSON).when().post().then().statusCode(OK.value())
				.extract().body().as(extractAs);
	}

	/**
	 * Executes POST request to a API by URL and port returning
	 * {@link RequestSpecification}.
	 * 
	 * @author Lucas Ribeiro
	 * @param url  of request
	 * @param port of request
	 * @return
	 */
	public static final RequestSpecification post(String url, Integer port) {
		return given().basePath(url).port(port).contentType(JSON);
	}

	/**
	 * Executes POST request to a API by URL returning {@link RequestSpecification}.
	 * 
	 * @author Lucas Ribeiro
	 * @param url of request
	 * @return
	 */
	public static final RequestSpecification post(String url) {
		return given().basePath(url).port(getPort()).contentType(JSON);
	}

	/**
	 * Executes a POST request to a API by URL and Body returning
	 * {@link RequestSpecification}.
	 * 
	 * @author Lucas Ribeiro
	 * @param url  of request
	 * @param body of request
	 * @return
	 */
	public static final RequestSpecification post(String url, Object body) {
		return given().basePath(url).port(getPort()).contentType(JSON).body(body);
	}

	/**
	 * Executes a GET request to an API by URL and returns the response as an
	 * object.
	 *
	 * @param url       The request URL.
	 * @param extractAs The class type to extract the response body.
	 * @return The extracted response body as an object of type T.
	 */
	public static final <T> T get(String url, Class<T> extractAs) {
		return given().basePath(url).port(getPort()).contentType(JSON).when().get().then().statusCode(OK.value())
				.extract().body().as(extractAs);
	}

	/**
	 * Executes a GET request to an API by URL and returns the response as an
	 * object.
	 *
	 * @param url       The request URL.
	 * @param extractAs The class type to extract the response body.
	 * @param headers   The request headers
	 * @return The extracted response body as an object of type T.
	 */
	public static final <T> T get(String url, Class<T> extractAs, Map<String, String> headers) {
		return given().basePath(url).port(getPort()).contentType(JSON).headers(headers).when().get().then()
				.statusCode(OK.value()).extract().body().as(extractAs);
	}

	/**
	 * Executes a GET request to an API by URL with query parameters and returns the
	 * response as an object.
	 *
	 * @param url       The request URL.
	 * @param params    The query parameters.
	 * @param extractAs The class type to extract the response body.
	 * @return The extracted response body as an object of type T.
	 */
	public static final <T> T get(String url, Map<String, ?> params, Class<T> extractAs) {
		return given().basePath(url).port(getPort()).queryParams(params).contentType(JSON).when().get().then()
				.statusCode(OK.value()).extract().body().as(extractAs);
	}

	/**
	 * Executes a GET request to an API by URL and port, returning the response as
	 * an object.
	 *
	 * @param url       The request URL.
	 * @param port      The request port.
	 * @param extractAs The class type to extract the response body.
	 * @return The extracted response body as an object of type T.
	 */
	public static final <T> T get(String url, Integer port, Class<T> extractAs) {
		return given().basePath(url).port(port).contentType(JSON).when().get().then().statusCode(OK.value()).extract()
				.body().as(extractAs);
	}

	/**
	 * Executes a GET request to an API by URL, port, and query parameters,
	 * returning the response as an object.
	 *
	 * @param url       The request URL.
	 * @param port      The request port.
	 * @param params    The query parameters.
	 * @param extractAs The class type to extract the response body.
	 * @return The extracted response body as an object of type T.
	 */
	public static final <T> T get(String url, Integer port, Map<String, ?> params, Class<T> extractAs) {
		return given().basePath(url).port(port).queryParams(params).contentType(JSON).when().get().then()
				.statusCode(OK.value()).extract().body().as(extractAs);
	}

	/**
	 * Executes a DELETE request to an API by URL and returns the response as an
	 * object.
	 * 
	 * @param url       URL of the request
	 * @param extractAs Object extracted from the response body
	 * @param <T>       Type of the expected response
	 * @return Extracted object from response body
	 */
	public static final <T> T delete(String url, Class<T> extractAs) {
		return given().basePath(url).port(getPort()).contentType(JSON).when().delete().then().statusCode(OK.value())
				.extract().body().as(extractAs);
	}

	/**
	 * Executes a DELETE request to an API by URL and returns {@link Response}.
	 * 
	 * @param url       URL of the request
	 * @param extractAs Object extracted from the response body
	 * @param <T>       Type of the expected response
	 * @return
	 * @return Extracted object from response body
	 */
	public static final Response delete(String url) {
		return given().basePath(url).port(getPort()).contentType(JSON).when().delete();
	}

	/**
	 * Executes a DELETE request to an API by URL and returns {@link Response}.
	 * 
	 * @param url     URL of the request
	 * @param headers The URL headers
	 * @return
	 * @return Extracted object from response body
	 */
	public static final Response delete(String url, Map<String, String> headers) {
		return given().basePath(url).port(getPort()).contentType(JSON).headers(headers).when().delete();
	}

	/**
	 * Executes a DELETE request to an API by URL with query parameters and returns
	 * the response as an object.
	 * 
	 * @param url       URL of the request
	 * @param params    Query parameters
	 * @param extractAs Object extracted from the response body
	 * @param <T>       Type of the expected response
	 * @return Extracted object from response body
	 */
	public static final <T> T delete(String url, Map<String, ?> params, Class<T> extractAs) {
		return given().basePath(url).port(getPort()).queryParams(params).contentType(JSON).when().delete().then()
				.statusCode(OK.value()).extract().body().as(extractAs);
	}

	/**
	 * Executes a DELETE request to an API by URL and port, returning the response
	 * as an object.
	 * 
	 * @param url       URL of the request
	 * @param port      Port of the request
	 * @param extractAs Object extracted from the response body
	 * @param <T>       Type of the expected response
	 * @return Extracted object from response body
	 */
	public static final <T> T delete(String url, Integer port, Class<T> extractAs) {
		return given().basePath(url).port(port).contentType(JSON).when().delete().then().statusCode(OK.value())
				.extract().body().as(extractAs);
	}

	/**
	 * Executes a DELETE request to an API by URL, port, and query parameters,
	 * returning the response as an object.
	 * 
	 * @param url       URL of the request
	 * @param port      Port of the request
	 * @param params    Query parameters
	 * @param extractAs Object extracted from the response body
	 * @param <T>       Type of the expected response
	 * @return Extracted object from response body
	 */
	public static final <T> T delete(String url, Integer port, Map<String, ?> params, Class<T> extractAs) {
		return given().basePath(url).port(port).queryParams(params).contentType(JSON).when().delete().then()
				.statusCode(OK.value()).extract().body().as(extractAs);
	}

}
