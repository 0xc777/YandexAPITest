package integration.tests.negative;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static integration.constants.Endpoints.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

import static integration.utils.Config.BASE_URL;

import static io.restassured.RestAssured.given;
import static integration.client.RequestSpecs.get;


public class YandexNegativeTest {

    @Test
    @Tag("Negative")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Запрос без токена 401")
    @DisplayName("Запрос без токена")
    @Story("Авторизация")
    void Return401WhenNoToken (){
        given()
                .baseUri(BASE_URL)
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
    void Return401WhenInvalidToken(){
        given()
                .baseUri(BASE_URL)
                .header("Authorization", "OAuth invalid_token")
                .contentType("application/json")
        .when()
                .get(DISK)
        .then()
                .statusCode(401);
    }

    @Test
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @Description("Запрос информации о несуществующей папке 404")
    @DisplayName("Несуществующая папка")
    @Story("Ресурсы")
    void Return404WhenFolderNotFound(){
        given()
                .spec(get())
                .queryParam("path", "/non_existent_folder_" + UUID.randomUUID())
        .when()
                .get(RESOURCES)
        .then()
                .statusCode(404);
    }

    @Test
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @Description("Создание папки с существующим именем 409")
    @DisplayName("Создание уже существующей папки")
    @Story("Ресурсы")
    void Return409WhenFolderAlreadyExists(){
        String folderPath = "/negative_test_" + UUID.randomUUID();
        given()
                .spec(get())
                .queryParam("path", folderPath)
        .when()
                .put(RESOURCES)
        .then()
                .statusCode(201);
        given()
                .spec(get())
                .queryParam("path", folderPath)
        .when()
                .put(RESOURCES)
        .then()
                .statusCode(409);
        given()
                .spec(get())
                .queryParam("path", folderPath)
                .queryParam("permanently", true)
        .when()
                .delete(RESOURCES)
        .then()
                .statusCode(204);

    }
    @Test
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @Description("Удаление несуществующей папки 404")
    @DisplayName("Удаление несуществующей папки")
    @Story("Ресурсы")
    void Return404WhenDeletingNonExistentFolder(){
        given()
                .spec(get())
                .queryParam("path", "/non_existent_folder_" + UUID.randomUUID())
                .queryParam("permanently", true)
        .when()
                .delete(RESOURCES)
        .then()
                .statusCode(404);
    }
    @Test
    @Tag("negative")
    @Severity(SeverityLevel.MINOR)
    @Description("Пустой путь при создании папки 400")
    @DisplayName("Пустой путь при создании папки")
    @Story("Ресурсы")
    void Return400WhenEmptyPath(){
        given()
                .spec(get())
                .queryParam("path", "")
        .when()
                .put(RESOURCES)
        .then()
                .statusCode(400);
    }
    @Test
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @Description("Копирование в несуществующую папку 409")
    @DisplayName("Копирование в несуществующую папку")
    @Story("Ресурсы")
    void FailToCopyWhenTargetFolderNotExists() throws IOException {
        String sourcePath = "/source_file_" + UUID.randomUUID() + ".txt";
        String targetPath = "/non_existent_folder_" + UUID.randomUUID() + "/copy_file.txt";
        try {
            String uploadUrl = given()
                    .spec(get())
                    .queryParam("path", sourcePath)
            .when()
                    .get(UPLOAD)
            .then()
                    .statusCode(200)
                    .extract()
                    .path("href");
            given()
                    .body("test content".getBytes())
            .when()
                    .put(uploadUrl)
            .then()
                    .statusCode(201);
            given()
                    .spec(get())
                    .queryParam("from", sourcePath)
                    .queryParam("path", targetPath)
            .when()
                    .post(COPY)
            .then()
                    .statusCode(409);
        }
        finally {
            given()
                    .spec(get())
                    .queryParam("path", sourcePath)
                    .queryParam("permanently", true)
            .when()
                    .delete(RESOURCES)
            .then()
                    .statusCode(anyOf(is(204), is(404)));
            given()
                    .spec(get())
                    .queryParam("path", sourcePath)
            .when()
                    .get(RESOURCES)
            .then()
                    .statusCode(404);
        }
    }

    @Test
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @Description("Восстановление несуществующего файла 404")
    @DisplayName("Восстановление несуществующего файла ")
    @Story("Корзина")
    void Return404RestoreNonExistentFile() {
        String path = "/non_existent_" + UUID.randomUUID() + ".txt";

        given()
                .spec(get())
                .queryParam("path", path)
        .when()
                .put(TRASH_RESTORE)
        .then()
                .statusCode(404);
    }

    @Test
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @Description("Публикация несуществующего ресурса 404")
    @DisplayName("Публикация несуществующего ресурса")
    @Story("Публикация")
    void Return404PublishNonExistentResource() {
        String path = "/non_existent_" + UUID.randomUUID();

        given()
                .spec(get())
                .queryParam("path", path)
        .when()
                .put(PUBLISH)
        .then()
                .statusCode(404);
    }
    @Test
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @Description("Удаление публикации несуществующего ресурса  405")
    @DisplayName("Удаление публикации несуществующего ресурса")
    @Story("Публикация")
    void Return404NonPublishedResource() {
        String path = "/non_existent_" +  UUID.randomUUID();

        given()
                .spec(get())
                .queryParam("path", path)
        .when()
                .delete(PUBLISH)
        .then()
                .statusCode(405);
    }
    @Test
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @Description("Копирование поверх существующего файла без overwrite 409")
    @DisplayName("Копирование поверх существующего файла без overwrite")
    @Story("Ресурсы")
    void shouldReturn409WhenCopyOverExistingFileWithoutOverwrite()  throws IOException {
        String sourcePath = "/source_" + UUID.randomUUID() + ".txt";
        String targetPath = "/target_" + UUID.randomUUID() + ".txt";

        try {

            String uploadUrl = given()
                    .spec(get())
                    .queryParam("path", sourcePath)
            .when()
                    .get(UPLOAD)
            .then()
                    .statusCode(200)
                    .extract()
                    .path("href");

            given()
                    .body("source content".getBytes())
            .when()
                    .put(uploadUrl)
            .then()
                    .statusCode(201);


            String targetUploadUrl = given()
                    .spec(get())
                    .queryParam("path", targetPath)
            .when()
                    .get(UPLOAD)
            .then()
                    .statusCode(200)
                    .extract()
                    .path("href");

            given()
                    .body("target content".getBytes())
                    .when()
                    .put(targetUploadUrl)
                    .then()
                    .statusCode(201);


            given()
                    .spec(get())
                    .queryParam("from", sourcePath)
                    .queryParam("path", targetPath)
            .when()
                    .post(COPY)
            .then()
                    .statusCode(409);

        } finally {

            for (String path : Arrays.asList(sourcePath, targetPath)) {
                given()
                        .spec(get())
                        .queryParam("path", path)
                        .queryParam("permanently", true)
                .when()
                        .delete(RESOURCES)
                .then()
                        .statusCode(anyOf(is(204), is(404)));
            }
        }
    }
}
