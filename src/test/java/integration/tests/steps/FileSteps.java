package integration.tests.steps;

import integration.tests.dto.LinkResponse;
import integration.tests.dto.ResourceMetadataResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static integration.client.RequestSpecs.get;
import static integration.constants.Endpoints.*;
import static integration.utils.AsyncOperationHelper.waitForOperationComplete;
import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class FileSteps {

    @Step("Получить ссылку для загрузки файла {filePath}")
    public String getUploadLink(String filePath) {
    LinkResponse response =
         given()
                .spec(get())
                .queryParam("path", filePath)
         .when()
                .get(UPLOAD)
         .then()
                .statusCode(200)
                .extract()
                .as(LinkResponse.class);
        return response.getHref();

    }

    @Step("Загрузить файл по ссылке")
    public void uploadFileByLink(String uploadUrl, byte[] content) {
        given()
                .body(content)
        .when()
                .put(uploadUrl)
        .then()
                .statusCode(201);
    }

    @Step("Загрузить файл {filePath} с содержимым")
    public void uploadFile(String filePath, byte[] content) {
        String uploadUrl = getUploadLink(filePath);
        uploadFileByLink(uploadUrl, content);
    }

    @Step("Скопировать ресурс из {fromPath} в {toPath}")
    public void copyResource(String fromPath, String toPath) {
        Response response = given()
                .spec(get())
                .queryParam("from", fromPath)
                .queryParam("path", toPath)
        .when()
                .post(COPY)
        .then()
                .statusCode(anyOf(is(201), is(202)))
                .extract()
                .response();

        if (response.statusCode() == 202) {
            String operationId = response.jsonPath().getString("operation_id");
            if (operationId != null) {
                waitForOperationComplete(operationId);
            }
        }
    }

    @Step("Проверить, что файл {filePath} существует и имеет имя {fileName} и тип file")
    public void assertFileExists(String filePath, String fileName) {
        await()
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .until(() -> {
                    Response response = given()
                            .spec(get())
                            .queryParam("path", filePath)
                    .when()
                            .get(RESOURCES)
                    .then()
                            .extract()
                            .response();

                    return response.statusCode() == 200 &&
                            response.jsonPath().getString("name").equals(fileName) &&
                            response.jsonPath().getString("type").equals("file");
                });
    }

    @Step("Переместить ресурс из {fromPath} в {toPath}")
    public void moveResource(String fromPath, String toPath) {
        Response response = given()
                .spec(get())
                .queryParam("from", fromPath)
                .queryParam("path", toPath)
        .when()
                .post(MOVE)
        .then()
                .statusCode(anyOf(is(201), is(202)))
                .extract()
                .response();

        if (response.statusCode() == 202) {
            String operationId = response.jsonPath().getString("operation_id");
            if (operationId != null) {
                waitForOperationComplete(operationId);
            }
        }
    }

    @Step("Проверить, что файл {filePath} НЕ существует")
    public void assertFileNotExists(String filePath) {
        await()
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .until(() -> {
                    return given()
                            .spec(get())
                            .queryParam("path", filePath)
                    .when()
                            .get(RESOURCES)
                    .then()
                            .extract()
                            .statusCode() == 404;
                });
    }

    @Step("Загрузить файл из URL {fileUrl} по пути {filePath}")
    public void uploadFileFromUrl(String filePath, String fileUrl) {
        Response response = given()
                .spec(get())
                .header("User-Agent", "Mozilla/5.0")
                .queryParam("path", filePath)
                .queryParam("url", fileUrl)
        .when()
                .post(UPLOAD)
        .then()
                .statusCode(anyOf(is(202), is(200)))
                .extract()
                .response();


        if (response.statusCode() == 202) {
            String operationId = response.jsonPath().getString("operation_id");
            if (operationId != null) {
                waitForOperationComplete(operationId);
            }
        }
    }

    @Step("Обновить пользовательские метаданные файла {filePath}")
    public ResourceMetadataResponse updateFileMetadata(String filePath, Map<String, String> customProps) {
        ResourceMetadataResponse response = given()
                .spec(get())
                .queryParam("path", filePath)
                .body(Map.of("custom_properties", customProps))
        .when()
                .patch(RESOURCES)
        .then()
                .statusCode(200)
                .extract()
                .as(ResourceMetadataResponse.class);
        return response;   // ← возвращаем DTO
    }

    @Step("Получить метаданные ресурса {filePath}")
    public ResourceMetadataResponse getResourceMetadata(String filePath) {
        ResourceMetadataResponse response = given()
                .spec(get())
                .queryParam("path", filePath)
        .when()
                .get(RESOURCES)
        .then()
                .statusCode(200)
                .extract()
                .as(ResourceMetadataResponse.class);
        return response;
    }
}