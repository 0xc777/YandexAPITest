package integration.tests.smoketests;

import integration.tests.steps.FolderSteps;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.awt.image.ImagingOpException;
import java.util.UUID;
import static integration.constants.Endpoints.*;
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
        FolderSteps Folder = new FolderSteps();
        try {
            Folder.createFolder(folderPath);
        } finally {
            Folder.deleteFolder(folderPath);
            Folder.FolderNotExists(folderPath);
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