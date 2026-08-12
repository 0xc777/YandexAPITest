package integration.tests.negative;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;
import java.util.stream.Stream;


import static integration.client.RequestSpecs.get;
import static integration.constants.Endpoints.*;
import static io.restassured.RestAssured.given;


public class YandexNegativeParamTest {

    @ParameterizedTest(name = "[{index}] overwrite=''{0}'' → 400")
    @ValueSource(strings = {"yes", "no", "truee", "falsee", "-1", "+0","1-","0+","+1","-0"," "," true"," false"})
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Невалидные значения overwrite возвращают 400")
    @Story("Ресурсы")
    void Return400ForInvalidOverwrite(String overwrite) {
        String path = "/test_file_" + UUID.randomUUID() + ".txt";

        given()
                .spec(get())
                .queryParam("path", path)
                .queryParam("overwrite", overwrite)
        .when()
                .get(UPLOAD)
        .then()
                .statusCode(400);
    }

    @ParameterizedTest(name = "[{index}] {0} {1} → 405")
    @MethodSource("dataFor405Test")
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Неподдерживаемый метод  405")
    @Story("Ресурсы")
    void MethodsFor405(String endpoint, String method) {
        given()
                .spec(get())
        .when()
                .request(method, endpoint)
        .then()
                .statusCode(405);
    }

    static Stream<Arguments> dataFor405Test() {
        return Stream.of(
                Arguments.of(RESOURCES, "POST"),
                Arguments.of(DISK, "PUT"),
                Arguments.of(DISK, "DELETE"),
                Arguments.of(UPLOAD, "DELETE")
        );
    }



}
