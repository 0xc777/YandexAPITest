package integration.tests.steps.assured;

import integration.tests.dto.ResourceMetadataResponse;
import integration.tests.steps.interfaces.ResourceSteps;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static integration.client.RequestSpecs.get;
import static integration.constants.Endpoints.*;
import static integration.utils.AsyncOperationHelper.waitForOperationComplete;
import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;

public class ResourceStepsAssured implements ResourceSteps {

    @Step("Получить метаданные ресурса {resourcePath}")
    public ResourceMetadataResponse getResourceMetadata (String filePath) {
        ResourceMetadataResponse response = given()
                .spec(get())
                .queryParam("path", filePath)
        .when()
                .get(RESOURCES)
        .then()
                .extract()
                .as(ResourceMetadataResponse.class);
        return response;
    }

    @Step("Отправить запрос на удаление ресурса {resourcePath}")
    public Response sendDeleteResource(String path) {
        return given()
                .spec(get())
                .queryParam("path", path)
                .queryParam("permanently", true)
        .when()
                .delete(RESOURCES)
        .then()
                .extract()
                .response();
    }

    @Step("Удалить ресурс {resourcePath} (с ожиданием)")
    public void deleteResource(String resourcePath) {
        await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .until(() -> {
                    Response response = sendDeleteResource(resourcePath);
                    int statusCode = response.statusCode();

                    if (statusCode == 204 || statusCode == 404) {
                        return true;
                    } else if (statusCode == 202) {
                        String operationId = response.jsonPath().getString("operation_id");
                        if (operationId != null) {
                            waitForOperationComplete(operationId);
                        }
                        return true;
                    } else if (statusCode == 423) {
                        return false;
                    } else {
                        throw new AssertionError("status code: " + statusCode);
                    }
                });
    }


    @Step("Проверить, что ресурс {resourcePath} НЕ существует")
    public void resourceNotExists(String resourcePath) {
        await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .until(() -> {
                    return given()
                            .spec(get())
                            .queryParam("path", resourcePath)
                    .when()
                            .get(RESOURCES)
                    .then()
                            .extract()
                            .statusCode() == 404;
                });

    }

    @Step("Отправить запрос на получение информации о ресурсе {resourcePath}")
    public Response  sendResource(String resourcePath) {
        return given()
                .spec(get())
                .queryParam("path", resourcePath)
        .when()
                .get(RESOURCES)
        .then()
                .extract()
                .response();
    }

    @Step("Отправить запрос на перемещение из {fromPath} в {toPath}")
    public Response sendMove(String fromPath, String toPath) {
        return given()
                .spec(get())
                .queryParam("from", fromPath)
                .queryParam("path", toPath)
        .when()
                .post(MOVE)
        .then()
                .extract()
                .response();
    }

    @Step("Переместить ресурс из {fromPath} в {toPath} (с ожиданием)")
    public Response moveResource(String fromPath, String toPath) {
        Response response = sendMove(fromPath, toPath);
        if (response.getStatusCode() == 202) {
            String operationId = response.jsonPath().getString("operation_id");
            if (operationId != null) {
                waitForOperationComplete(operationId);
            }
        }
        return response;
    }

    @Step("Отправить запрос на копирование из {fromPath} в {toPath}")
    public Response sendCopyRequest(String fromPath, String toPath) {
        return given()
                .spec(get())
                .queryParam("from", fromPath)
                .queryParam("path", toPath)
        .when()
                .post(COPY)
        .then()
                .extract()
                .response();
    }

    @Step("Скопировать ресурс из {fromPath} в {toPath} (с ожиданием при асинхронном копировании)")
    public Response copyResource(String fromPath, String toPath) {
        Response response = sendCopyRequest(fromPath, toPath);
        if (response.getStatusCode() == 202) {
            String operationId = response.jsonPath().getString("operation_id");
            if (operationId != null) {
                waitForOperationComplete(operationId);
            }
        }
        return response;
    }


}
