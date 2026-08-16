package integration.tests.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static integration.client.RequestSpecs.get;
import static integration.constants.Endpoints.TRASH_RESTORE;
import static io.restassured.RestAssured.given;

public class TrashSteps {

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
}

