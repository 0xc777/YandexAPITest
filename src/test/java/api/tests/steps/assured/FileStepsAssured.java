package api.tests.steps.assured;

import api.tests.dto.*;
import api.tests.steps.interfaces.FileSteps;
import io.qameta.allure.Step;
import io.restassured.response.Response;


import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static api.client.RequestSpecs.get;
import static api.constants.Endpoints.*;
import static api.utils.awaitility.WaitHelper.waitUntil;
import static api.utils.awaitility.assured.AsyncOperationHelper.waitForOperationComplete;
import static io.restassured.RestAssured.given;


public class FileStepsAssured implements FileSteps {


    @Step("Получить ссылку для загрузки файла {filePath}")
    public String getLink(String filePath) {
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
    public Response uploadFileByLink(String uploadUrl, byte[] content) {
        return given()
                .body(content)
        .when()
                .put(uploadUrl)
        .then()
                .extract()
                .response();
    }

    @Step("Загрузить файл {filePath} с содержимым")
    public Response uploadFile(String filePath, byte[] content) {
        String uploadUrl = getLink(filePath);
        return uploadFileByLink(uploadUrl, content);
    }



    @Step("Поиск файла{filePath}")
    public Response searchFile(String filePath) {
        AtomicReference<Response> responseRef = new AtomicReference<>();
        waitUntil(() -> {
                    Response response = given()
                            .spec(get())
                            .queryParam("path", filePath)
                    .when()
                            .get(RESOURCES)
                    .then()
                            .extract()
                            .response();

                    if (response.statusCode() == 200) {
                        responseRef.set(response);
                        return true;
                    }
                    return false;
                });

        return responseRef.get();
    }


    @Step("Проверить, что файл {filePath} НЕ существует")
    public void checkFileNotExists(String filePath) {
        waitUntil(() ->
                given()
                        .spec(get())
                        .queryParam("path", filePath)
                .when()
                        .get(RESOURCES)
                .then()
                        .extract()
                        .statusCode() == 404
        );
    }

    @Step("Загрузить файл из URL {fileUrl} по пути {filePath} (с ожиданием)")
    public Response uploadFileFromUrl(String filePath, String fileUrl) {
        Response response = sendUploadFromUrl(filePath, fileUrl);
        if (response.getStatusCode() == 202) {
            String operationId = response.jsonPath().getString("operation_id");
            if (operationId != null) {
                waitForOperationComplete(operationId);
            }
        }
        return response;
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
                .extract()
                .as(ResourceMetadataResponse.class);
        return response;
    }

    @Step("Отправить запрос на загрузку файла из URL {fileUrl} по пути {filePath}")
    public Response sendUploadFromUrl(String filePath, String fileUrl) {
        return given()
                .spec(get())
                .header("User-Agent", "Mozilla/5.0")
                .queryParam("path", filePath)
                .queryParam("url", fileUrl)
        .when()
                .post(UPLOAD)
        .then()
                .extract()
                .response();
    }

    @Step("Передача значения overwrite")
    public Response sendOverwrite(String overwrite, String path){
        return given()
                .spec(get())
                .queryParam("path", path)
                .queryParam("overwrite", overwrite)
        .when()
                .get(UPLOAD)
        .then()
                .extract()
                .response();
    }

}