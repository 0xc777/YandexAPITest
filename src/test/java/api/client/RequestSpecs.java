package api.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.specification.RequestSpecification;
import api.utils.Config;

import java.util.List;

public class RequestSpecs
{
    private static final RequestSpecification REQUEST_SPEC = new RequestSpecBuilder()
            .setBaseUri(Config.BASE_URL)
            .addHeader("Authorization", "OAuth " + Config.TOKEN)
            .setContentType("application/json")
            .setConfig(RestAssured.config()
                    .logConfig(LogConfig
                            .logConfig()
                            .blacklistHeaders(List.of("Authorization"))))
            .addFilter(new AllureRestAssured())
            .build();

    public static RequestSpecification get() {
        return REQUEST_SPEC;
    }
}
