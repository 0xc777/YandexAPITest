package api.tests.smoketests;

import api.tests.steps.interfaces.FolderSteps;
import api.tests.steps.interfaces.ResourceSteps;
import api.utils.StepsFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.awt.image.ImagingOpException;
import java.util.UUID;
import static api.constants.Endpoints.*;
import static io.restassured.RestAssured.given;
import static api.client.RequestSpecs.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class YandexSmokeTest {

    private final FolderSteps folderSteps = StepsFactory.getFolderSteps();
    private final ResourceSteps resourceSteps = StepsFactory.getResourceSteps();

    @Test
    @Tag("Smoke")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Получение информации о диске")
    @DisplayName("Получить информацию о диске")
    @Story("Авторизация")
    void authorizeSuccessfully(){
        given()
                .spec(get())
        .when()
                .get(DISK)
        .then()
                .statusCode(200);

    }
    @Test
    @Tag("Smoke")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Создание папки на диске")
    @DisplayName("Создание папки на диске")
    @Story("Папки")
    void createAndDeleteFolder()throws ImagingOpException {
        String folderPath = "/smoke_test_" + UUID.randomUUID();


        try {
            Response response = folderSteps.createFolder(folderPath);
            assertThat(response.statusCode()).isEqualTo(201);
        } finally {
            resourceSteps.deleteResource(folderPath);
            resourceSteps.resourceNotExists(folderPath);
        }
    }
    @Test
    @Tag("Smoke")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Общий объем диска больше нуля")
    @DisplayName("Общий объем диска больше нуля")
    @Story("Ресурсы")
    void haveAvailableSpace(){
        given()
                .spec(get())
        .when()
                .get(DISK)
        .then()
                .statusCode(200)
                .body("total_space", greaterThan(0L));
    }
}