package api.utils.awaitility.assured;

import io.qameta.allure.Step;

import static api.client.RequestSpecs.get;
import static api.utils.awaitility.WaitHelper.waitUntil;
import static io.restassured.RestAssured.given;


public class AsyncOperationHelper {

    @Step("Дождаться завершения операции {operationId}")
    public static void waitForOperationComplete(String operationId) {
        waitUntil(() -> {
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