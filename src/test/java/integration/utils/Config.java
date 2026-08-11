package integration.utils;

import io.restassured.RestAssured;
import io.restassured.config.LogConfig;

import java.util.List;

public class Config {
    public static final String BASE_URL = "https://cloud-api.yandex.net";
    public static final String TOKEN =
            System.getenv("YANDEX_TOKEN");


    static {

        if (TOKEN == null || TOKEN.isBlank()) {

            throw new RuntimeException(
                    "YANDEX_TOKEN is missing"
            );
        }
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().blacklistHeaders(List.of("Authorization")));
    }
}