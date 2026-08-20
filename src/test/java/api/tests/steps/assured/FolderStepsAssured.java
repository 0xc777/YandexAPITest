package api.tests.steps.assured;

import api.tests.steps.interfaces.FolderSteps;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static api.client.RequestSpecs.get;
import static api.constants.Endpoints.*;

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