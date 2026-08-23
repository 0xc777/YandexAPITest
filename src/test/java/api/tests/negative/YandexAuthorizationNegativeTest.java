package api.tests.negative;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static api.constants.Endpoints.DISK;
import static api.utils.Config.*;
import static io.restassured.RestAssured.given;

public class YandexAuthorizationNegativeTest {

    @Test
    @Tag("Negative")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Запрос без токена 401")
    @DisplayName("Запрос без токена")
    @Story("Авторизация")
    void return401WhenNoToken (){
        given()
                .baseUri(getBaseUrl())
                .contentType("application/json")
        .when()
                .get(DISK)
        .then()
                .statusCode(401);

    }
    @Test
    @Tag("Negative")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Запрос с невалидным токеном 401")
    @DisplayName("Запрос с невалидным токеном")
    @Story("Авторизация")
    void return401WhenInvalidToken(){
        given()
                .baseUri(getBaseUrl())
                .header("Authorization", "OAuth invalid_token")
                .contentType("application/json")
        .when()
                .get(DISK)
        .then()
                .statusCode(401);
    }

}
