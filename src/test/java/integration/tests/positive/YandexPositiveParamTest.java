package integration.tests.positive;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static integration.constants.Endpoints.*;
import static integration.client.RequestSpecs.get;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.Is.is;

public class YandexPositiveParamTest {
    @ParameterizedTest(name = "[{index}] Загрузка файла с расширением .{0}")
    @ValueSource(strings = {"txt", "jpg", "png", "pdf", "bin"})
    @Tag("positive")
    @Severity(SeverityLevel.NORMAL)
    @Description("Загрузка файлов разных типов")
    @DisplayName("Загрузка файлов разных расширений → 201")
    @Story("Ресурсы")
    void LoadingFilesOfDifferentTypes(String extension) throws IOException {
        String fileName = "upload_test_" + UUID.randomUUID() + "." + extension;
        String filePath = "/" + fileName;
        String content = "Test content for " + extension + " file";

        File tempFile = File.createTempFile("upload", "." + extension);
        Files.write(tempFile.toPath(), content.getBytes());

        try {
            String uploadUrl = given()
                    .spec(get())
                    .queryParam("path", filePath)
            .when()
                    .get(UPLOAD)
            .then()
                    .statusCode(200)
                    .extract()
                    .path("href");

            given()
                    .body(tempFile)
            .when()
                    .put(uploadUrl)
            .then()
                    .statusCode(201);

            given()
                    .spec(get())
                    .queryParam("path", filePath)
            .when()
                    .get(RESOURCES)
            .then()
                    .statusCode(200)
                    .body("name", equalTo(fileName))
                    .body("type", equalTo("file"));

        } finally {
            given()
                    .spec(get())
                    .queryParam("path", filePath)
                    .queryParam("permanently", true)
            .when()
                    .delete(RESOURCES)
            .then()
                    .statusCode(anyOf(is(204), is(404)));

            Files.deleteIfExists(tempFile.toPath());
        }
    }

    @ParameterizedTest(name = "[{index}] имя папки=''{0}'' → 201")
    @ValueSource(strings = {"Папка", "Моя папка", "Фото 2026","PapkaFolder","12312453АА"})
    @Tag("positive")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание папки с разными допустимыми именами и пробелами 201")
    @Story("Ресурсы")
    void CreateFolderWithCyrillicAndSpaces(String folderName) {
        String path = "/" + folderName + "_" + System.currentTimeMillis();

        given()
                .spec(get())
                .queryParam("path", path)
        .when()
                .put(RESOURCES)
        .then()
                .statusCode(201);

        // Очистка
        given()
                .spec(get())
                .queryParam("path", path)
                .queryParam("permanently", true)
        .when()
                .delete(RESOURCES)
        .then()
                .statusCode(anyOf(is(204), is(404)));
    }
    @ParameterizedTest(name = "[{index}] Имя файла=''{0}'' → 201")
    @ValueSource(strings = {
            "файл",
            "мой файл",
            "file_with_underscore",
            "file-with-dash",
            "file.with.dot",
            "file+with+plus",
            "file=with=equals",
            "123213ф"
    })
    @Tag("positive")
    @Severity(SeverityLevel.NORMAL)
    @Description("Создание файлов с разными допустимыми именами 201")
    @Story("Ресурсы")
    void shouldCreateFilesWithDifferentNames(String fileName) throws IOException {
        String filePath = "/" + fileName + "_" + System.currentTimeMillis() + ".txt";
        String content = "Test content";

        File tempFile = File.createTempFile("upload", ".txt");
        Files.write(tempFile.toPath(), content.getBytes());

        try {
            String uploadUrl = given()
                    .spec(get())
                    .queryParam("path", filePath)
                    .when()
                    .get(UPLOAD)
                    .then()
                    .statusCode(200)
                    .extract()
                    .path("href");

            given()
                    .body(tempFile)
                    .when()
                    .put(uploadUrl)
                    .then()
                    .statusCode(201);

            given()
                    .spec(get())
                    .queryParam("path", filePath)
                    .when()
                    .get(RESOURCES)
                    .then()
                    .statusCode(200)
                    .body("name", equalTo(filePath.substring(filePath.lastIndexOf("/") + 1)));

        } finally {
            given()
                    .spec(get())
                    .queryParam("path", filePath)
                    .queryParam("permanently", true)
                    .when()
                    .delete(RESOURCES)
                    .then()
                    .statusCode(anyOf(is(204), is(404)));

            try {
                Files.deleteIfExists(tempFile.toPath());
            } catch (IOException ignored) {}
        }
    }
}
