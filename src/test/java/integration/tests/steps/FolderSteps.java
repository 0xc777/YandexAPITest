package integration.tests.steps;

import integration.tests.dto.LinkResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;


import static integration.client.RequestSpecs.get;
import static integration.constants.Endpoints.*;
import static integration.utils.AsyncOperationHelper.waitForOperationComplete;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.*;


public class FolderSteps {
    @Step("Создать папку {name}")
    public void createFolder(String folderPath){
        LinkResponse response =
                given()
                    .spec(get())
                    .queryParam("path", folderPath)
                .when()
                    .put(RESOURCES)
                .then()
                    .statusCode(201)
                    .extract()
                    .as(LinkResponse.class);
        String decodedHref = URLDecoder.decode(response.getHref(), StandardCharsets.UTF_8);

        assertThat(response.getMethod()).isEqualTo("GET");
        assertThat(response.isTemplated()).isFalse();
        assertThat(decodedHref).contains(folderPath);

    }
    @Step("Удалить папку {folderPath}")
    public void deleteFolder(String folderPath) {
        await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .until(() -> {
                    Response response = given()
                            .spec(get())
                            .queryParam("path", folderPath)
                            .queryParam("permanently", true)
                    .when()
                            .delete(RESOURCES)
                    .then()
                            .extract()
                            .response();

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
                        throw new AssertionError("Unexpected status code: " + statusCode);
                    }
                });
    }



    @Step("Проверить, что ресурс {folderPath} НЕ существует")
    public void FolderNotExists(String folderPath) {
        await()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .until(() -> {
                    return given()
                            .spec(get())
                            .queryParam("path", folderPath)
                    .when()
                            .get(RESOURCES)
                    .then()
                            .extract()
                            .statusCode() == 404;
                });
    }
}
