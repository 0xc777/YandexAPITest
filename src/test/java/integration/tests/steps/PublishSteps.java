package integration.tests.steps;


import io.qameta.allure.Step;
import io.restassured.response.Response;

import static integration.client.RequestSpecs.get;
import static integration.constants.Endpoints.*;
import static io.restassured.RestAssured.given;


public class PublishSteps {

    @Step("Опубликовать ресурс {path}")
    public Response publishResource(String  path) {
        return given()
                .spec(get())
                .queryParam("path", path)
        .when()
                .put(PUBLISH)
        .then()
                .extract()
                .response();

    }

    @Step("Получить публичную ссылку на ресурс {path}")
    public Response getPublicLink(String path) {
       return given()
                .spec(get())
                .queryParam("path", path)
        .when()
                .get(RESOURCES)
        .then()
                .extract()
                .response();
    }

    @Step("Найти ресурс {publicUrl} по ссылке")
    public Response searchResourceIsPublic(String publicUrl) {

        return given()
        .when()
                .get(publicUrl)
        .then()
                .extract()
                .response();
    }

    @Step("Извлечь публичную ссылку из ответа")
    public String extractPublicUrl(Response response) {
        return response.jsonPath().getString("public_url");
    }

    @Step("Удалить публикацию ресурса {path}")
    public Response deletePublish(String path) {
        return given()
                .spec(get())
                .queryParam("path", path)
         .when()
                .delete(PUBLISH)
         .then()
                .extract()
                .response();
    }

}