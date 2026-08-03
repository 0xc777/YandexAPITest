package integration.client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import integration.utils.Config;

public class RequestSpecs
{
    private static final RequestSpecification REQUEST_SPEC = new RequestSpecBuilder()
            .setBaseUri(Config.BASE_URL)
            .setBaseUri(Config.BASE_URL)
            .addHeader("Authorization", "OAuth " + Config.TOKEN)
            .setContentType("application/json")
            .build();

    public static RequestSpecification get() {
        return REQUEST_SPEC;
    }
}
