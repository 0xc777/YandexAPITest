package integration.utils;

import io.qameta.allure.Step;

import java.util.concurrent.TimeUnit;

import static integration.client.RequestSpecs.get;
import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;

public class AsyncOperationHelper {

    @Step("Дождаться завершения операции {operationId}")
    public static void waitForOperationComplete(String operationId) {
        await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .until(() -> {
                    String status = given()
                            .spec(get())
                            .pathParam("id", operationId)
                    .when()
                            .get("/operations/{id}")
                    .then()
                            .statusCode(200)
                            .extract()
                            .path("status");
                    return "success".equals(status);
                });
    }
}