package integration.tests.smoketests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static integration.client.RequestSpecs.get;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

public class YandexSmokeTest {

    @Test
    @Tag("Smoke")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Получение информации о диске")
    @DisplayName("Получить информацию о диске")
    @Story("Диск")
    void shouldAuthorizeSuccessfully(){
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
    @Story("Диск")
    void CreateAndDeleteFolder(){
        String folderPath = "/smoke_test_" + System.currentTimeMillis();
        given()
                .spec(get())
                .queryParam("path", folderPath)
        .when()
                .put("/v1/disk/resources")
        .then()
                .statusCode(201);

        // Удаление
        given()
                .spec(get())
                .queryParam("path", folderPath)
        .when()
                .delete("/v1/disk/resources")
        .then()
                .statusCode(204);
    }

    @Test
    @Tag("Smoke")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Общий объем диска больше нуля")
    @DisplayName("Общий объем диска больше нуля")
    @Story("Диск")
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