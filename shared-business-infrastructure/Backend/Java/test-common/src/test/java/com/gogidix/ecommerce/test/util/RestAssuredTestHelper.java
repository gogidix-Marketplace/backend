package com.gogidix.ecommerce.test.util;

import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.config.RestAssuredConfig.config;

/**
 * Utility class providing helper methods for REST Assured test operations.
 */
public class RestAssuredTestHelper {

    /**
     * Configures RestAssured with default settings for number handling.
     * This prevents issues with BigDecimal and other numeric types in JSON responses.
     */
    public static void configureDefaultRestAssured() {
        RestAssured.config = config()
            .jsonConfig(JsonConfig.jsonConfig()
                .numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL));
    }

    /**
     * Creates a RequestSpecification with JSON content type.
     *
     * @return a configured RequestSpecification
     */
    public static RequestSpecification givenWithJson() {
        return RestAssured.given()
            .contentType("application/json");
    }

    /**
     * Creates a RequestSpecification with JSON content type and body.
     *
     * @param body the request body
     * @return a configured RequestSpecification
     */
    public static RequestSpecification givenWithJson(Object body) {
        return RestAssured.given()
            .contentType("application/json")
            .body(body);
    }

    /**
     * Creates a RequestSpecification with JSON content type and headers.
     *
     * @param headers the headers to include
     * @return a configured RequestSpecification
     */
    public static RequestSpecification givenWithJsonAndHeaders(Map<String, String> headers) {
        return RestAssured.given()
            .contentType("application/json")
            .headers(headers);
    }

    /**
     * Creates a RequestSpecification with JSON content type, body, and headers.
     *
     * @param body the request body
     * @param headers the headers to include
     * @return a configured RequestSpecification
     */
    public static RequestSpecification givenWithJson(Object body, Map<String, String> headers) {
        return RestAssured.given()
            .contentType("application/json")
            .body(body)
            .headers(headers);
    }

    /**
     * Creates a RequestSpecification with Bearer token authentication.
     *
     * @param token the JWT bearer token
     * @return a configured RequestSpecification
     */
    public static RequestSpecification givenWithBearerToken(String token) {
        return RestAssured.given()
            .contentType("application/json")
            .auth().preemptive().oauth2(token);
    }

    /**
     * Creates a RequestSpecification with Bearer token and request body.
     *
     * @param token the JWT bearer token
     * @param body the request body
     * @return a configured RequestSpecification
     */
    public static RequestSpecification givenWithBearerToken(String token, Object body) {
        return RestAssured.given()
            .contentType("application/json")
            .auth().preemptive().oauth2(token)
            .body(body);
    }

    /**
     * Extracts a field value from a JSON response using JsonPath.
     *
     * @param response the REST Assured response
     * @param path the JsonPath expression
     * @param <T> the expected return type
     * @return the extracted value
     */
    public static <T> T extractFromResponse(Response response, String path, Class<T> type) {
        return response.jsonPath().getObject(path, type);
    }

    /**
     * Extracts a field value from a JSON response as a String.
     *
     * @param response the REST Assured response
     * @param path the JsonPath expression
     * @return the extracted value as a String
     */
    public static String extractString(Response response, String path) {
        return response.jsonPath().getString(path);
    }

    /**
     * Extracts a field value from a JSON response as an Integer.
     *
     * @param response the REST Assured response
     * @param path the JsonPath expression
     * @return the extracted value as an Integer
     */
    public static Integer extractInt(Response response, String path) {
        return response.jsonPath().getInt(path);
    }

    /**
     * Extracts a field value from a JSON response as a Long.
     *
     * @param response the REST Assured response
     * @param path the JsonPath expression
     * @return the extracted value as a Long
     */
    public static Long extractLong(Response response, String path) {
        return response.jsonPath().getLong(path);
    }

    private RestAssuredTestHelper() {
        // Utility class - prevent instantiation
    }
}
