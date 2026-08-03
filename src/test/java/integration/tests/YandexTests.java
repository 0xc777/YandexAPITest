package integration.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static integration.client.RequestSpecs.get;
import static org.hamcrest.Matchers.notNullValue;

public class YandexTests {

    @Test
    @Tag("Smoke")
    @Tag("positive")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Получение информации о диске (проверка статуса и наличия поля trash_size)")
    @DisplayName("Получить информацию о диске")
    @Story("Диск")
    void shouldGetDiskInfo() {
        given()
                .spec(get())
                .when()
                .get("/v1/disk")
                .then()
                .statusCode(200)
                .body("trash_size", notNullValue());
    }



}