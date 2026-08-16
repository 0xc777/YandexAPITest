package integration.tests.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static integration.client.RequestSpecs.get;
import static integration.constants.Endpoints.*;

import static io.restassured.RestAssured.given;




public class FolderSteps {
    @Step("Создать папку {name}")
    public Response createFolder (String folderPath) {
        return given()
                .spec(get())
                .queryParam("path", folderPath)
        .when()
                .put(RESOURCES)
        .then()
                .extract()
                .response();
    }








}