package integration.tests.steps.assured;

import integration.tests.steps.interfaces.FolderSteps;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static integration.client.RequestSpecs.get;
import static integration.constants.Endpoints.*;

import static io.restassured.RestAssured.given;




public class FolderStepsAssured implements FolderSteps {
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