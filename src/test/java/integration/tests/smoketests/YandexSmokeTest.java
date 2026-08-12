package integration.tests.smoketests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.awt.image.ImagingOpException;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static integration.client.RequestSpecs.get;
import static org.hamcrest.Matchers.greaterThan;

public class YandexSmokeTest {

    @Test
    @Tag("Smoke")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Получение информации о диске")
    @DisplayName("Получить информацию о диске")
    @Story("Авторизация")
    void AuthorizeSuccessfully(){
        given()
                .spec(get())
        .when()
                .get("/v1/disk")
        .then()
                .statusCode(200);

    }
    @Test
    @Tag("Smoke")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Создание папки на диске")
    @DisplayName("Создание папки на диске")
    @Story("Ресурсы")
    void CreateAndDeleteFolder()throws ImagingOpException {
        String folderPath = "/smoke_test_" + UUID.randomUUID();
        try {
        given()
                .spec(get())
                .queryParam("path", folderPath)
        .when()
                .put("/v1/disk/resources")
        .then()
                .statusCode(201);

        }
        finally {
            given()
                    .spec(get())
                    .queryParam("path", folderPath)
            .when()
                    .delete("/v1/disk/resources")
            .then()
                    .statusCode(204);
        }

    }

    @Test
    @Tag("Smoke")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Общий объем диска больше нуля")
    @DisplayName("Общий объем диска больше нуля")
    @Story("Ресурсы")
    void HaveAvailableSpace(){
        given()
                .spec(get())
        .when()
                .get("/v1/disk")
        .then()
                .statusCode(200)
                .body("total_space", greaterThan(0L));
    }
}