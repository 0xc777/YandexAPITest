package integration.tests.steps;

import integration.tests.dto.ResourceMetadataResponse;
import integration.tests.dto.LinkResponse;
import io.qameta.allure.Step;

import static integration.client.RequestSpecs.get;
import static integration.constants.Endpoints.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class PublishSteps {

    @Step("Опубликовать ресурс {path}")
    public String publishResource(String  path) {
        LinkResponse response = given()
                .spec(get())
                .queryParam("path", path)
        .when()
                .put(PUBLISH)
        .then()
                .statusCode(200)
                .extract()
                .as(LinkResponse.class);

        return response.getHref();
    }
    @Step("Получить публичную ссылку на ресурс {path}")
    public String getPublicLink(String path) {
        ResourceMetadataResponse response = given()
                .spec(get())
                .queryParam("path", path)
        .when()
                .get(RESOURCES)   // эндпоинт /resources
        .then()
                .statusCode(200)
                .extract()
                .as(ResourceMetadataResponse.class);   // ✅ правильный DTO

        String publicUrl = response.getPublicUrl(); // теперь поле есть
        assertThat(publicUrl).isNotNull();
        return publicUrl;
    }
    @Step("Проверить, что ресурс {path} опубликован и доступен по ссылке")
    public void assertResourceIsPublic(String path) {
        String publicUrl = getPublicLink(path);

        given()
        .when()
                .get(publicUrl)
        .then()
                .statusCode(200);
    }

}