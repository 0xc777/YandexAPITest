package api.tests.steps.assured;

import api.tests.steps.interfaces.TrashSteps;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static api.client.RequestSpecs.get;
import static api.constants.Endpoints.*;
import static api.utils.awaitility.assured.AsyncOperationHelper.waitForOperationComplete;
import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;

public class TrashStepsAssured implements TrashSteps {

    @Step("Восстановление файла")
    public Response restoreFile( String path){
        return given()
                .spec(get())
                .queryParam("path", path)
        .when()
                .put(TRASH_RESTORE)
        .then()
                .extract()
                .response();
    }

    @Step("Отправить запрос на удаление ресурса {resourcePath} в корзину")
    public Response sendDeleteResourceOnTrash(String path) {
        return given()
                .spec(get())
                .queryParam("path", path)
        .when()
                .delete(RESOURCES)
        .then()
                .extract()
                .response();
    }

    @Step("Удаление ресурса в корзину")
    public void deleteFileOnTrash(String resourcePath){
        await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .until(() -> {
                    Response response = sendDeleteResourceOnTrash(resourcePath);
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
    @Step("Получить спискок ресурсов в корзине")
    public Response resourceListOnTrash(){
        return given()
                .spec(get())
        .when()
                .get(TRASH_RESOURCES)
        .then()
                .extract()
                .response();
    }
    @Step("Найти ресурс в корзине")
    public String searchFileOnTrash(Response response, String resourcePath) {
        String fileName = resourcePath.substring(resourcePath.lastIndexOf('/') + 1);
        List<Map<String, Object>> items = response.jsonPath().getList("_embedded.items");

        for (Map<String, Object> item : items) {
            String name = (String) item.get("name");
            if (fileName.equals(name)) {
                return (String) item.get("path");
            }
        }
        throw new AssertionError("Файл не найден в корзине: " + resourcePath);
    }
}

