package integration.tests.positive;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;

import static integration.constants.Endpoints.*;
import static io.restassured.RestAssured.given;
import static integration.client.RequestSpecs.get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;


public class YandexPositiveTest {

    @Test
    @Tag("positive")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Публикация папки и проверка, что она стала публичной 200")
    @DisplayName("Публикация папки, проверка публичного статуса")
    @Story("Публикация")
    void PublishFolderAndCheckPublicStatus() throws IOException {
        String folderPath = "/publish_folder_" + UUID.randomUUID();

        try{
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
                    .put(PUBLISH)
            .then()
                    .statusCode(200);


            given()
                    .spec(get())
            .when()
                    .get(RESOURCES + "/public")
            .then()
                    .statusCode(200);
        }

        finally {
            given()
                    .spec(get())
                    .queryParam("path", folderPath)
            .when()
                    .delete(PUBLISH)
            .then()
                    .statusCode(anyOf(is(204), is(404), is(405)));


            given()
                    .spec(get())
                    .queryParam("path", folderPath)
                    .queryParam("permanently", true)
            .when()
                    .delete(RESOURCES)
            .then()
                    .statusCode(anyOf(is(204), is(404)));
        }


    }
    @Test
    @Tag("positive")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Копирование файла в другую папку 201")
    @DisplayName("Копирование файла, проверка наличия копии")
    @Story("Ресурсы")
    void CopyFileToAnotherFolder() throws IOException {

        String sourceFolder = "/source_folder_" + UUID.randomUUID();
        String targetFolder = "/target_folder_" + UUID.randomUUID();
        String fileName = "test_file.txt";
        String sourcePath = sourceFolder + "/" + fileName;
        String targetPath = targetFolder + "/" + fileName;

        try {

            given()
                    .spec(get())
                    .queryParam("path", sourceFolder)
            .when()
                    .put(RESOURCES)
            .then()
                    .statusCode(201);

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
                    .body("Hello, World!".getBytes())
            .when()
                    .put(uploadUrl)
            .then()
                    .statusCode(201);

            given()
                    .spec(get())
                    .queryParam("path", targetFolder)
            .when()
                    .put(RESOURCES)
            .then()
                    .statusCode(201);

            given()
                    .spec(get())
                    .queryParam("from", sourcePath)
                    .queryParam("path", targetPath)
            .when()
                    .post(COPY)
            .then()
                    .statusCode(201); //*****

            given()
                    .spec(get())
                    .queryParam("path", targetPath)
            .when()
                    .get(RESOURCES)
            .then()
                    .statusCode(200)
                    .body("name", equalTo(fileName))
                    .body("type", equalTo("file"));

        } finally {
            for (String path : Arrays.asList(sourceFolder, targetFolder)) {
                given()
                        .spec(get())
                        .queryParam("path", path)
                        .queryParam("permanently", true)
                .when()
                        .delete(RESOURCES)
                .then()
                        .statusCode(anyOf(is(204), is(202), is(404)));
            }
        }
    }

    @Test
    @Tag("positive")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Перемещение файла в другую папку 201")
    @DisplayName("Перемещение файла, проверка наличия на новом месте и отсутствия на старом")
    @Story("Ресурсы")
    public void MoveFileToAnotherFolder() throws IOException {
        String sourceFolder = "/source_folder_" + UUID.randomUUID();
        String targetFolder = "/target_folder_" + UUID.randomUUID();
        String fileName = "test_file.txt";
        String sourcePath = sourceFolder + "/" + fileName;
        String targetPath = targetFolder + "/" + fileName;

        try {
            given()
                    .spec(get())
                    .queryParam("path", sourceFolder)
            .when()
                    .put(RESOURCES)
            .then()
                    .statusCode(201);

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
                    .body("Hello, World!".getBytes())
            .when()
                    .put(uploadUrl)
            .then()
                    .statusCode(201);

            given()
                    .spec(get())
                    .queryParam("path", targetFolder)
            .when()
                    .put(RESOURCES)
            .then()
                    .statusCode(201);

            given()
                    .spec(get())
                    .queryParam("from", sourcePath)
                    .queryParam("path", targetPath)
            .when()
                    .post(MOVE)
            .then()
                    .statusCode(201); //*******

            given()
                    .spec(get())
                    .queryParam("path", targetPath)
            .when()
                    .get(RESOURCES)
            .then()
                    .statusCode(200)
                    .body("name", equalTo(fileName));

            given()
                    .spec(get())
                    .queryParam("path", sourcePath)
            .when()
                    .get(RESOURCES)
            .then()
                    .statusCode(404);

        } finally {
            for (String path : Arrays.asList(sourceFolder, targetFolder)) {
                given()
                        .spec(get())
                        .queryParam("path", path)
                        .queryParam("permanently", true)
                .when()
                        .delete(RESOURCES)
                .then()
                        .statusCode(anyOf(is(204), is(202), is(404)));
            }
        }
    }
    @Test
    @Tag("positive")
    @Severity(SeverityLevel.NORMAL)
    @Description("Загрузка файла на диск по публичной ссылке с GitHub 202")
    @DisplayName("Загрузка файла по URL, ожидание появления файла")
    @Story("Ресурсы")
    void UploadFileFromUrl() {
        String path = "/uploaded_from_url_" + UUID.randomUUID() + ".txt";
        String fileUrl = "https://raw.githubusercontent.com/0xc777/YandexAPITest/main/README.md";
        String fileName = path.substring(path.lastIndexOf("/") + 1);

        try {
            given()
                    .header("User-Agent", "Mozilla/5.0")
                    .spec(get())
                    .queryParam("path", path)
                    .queryParam("url", fileUrl)
            .when()
                    .post(UPLOAD)
            .then()
                    .statusCode(202); // или 200

            Awaitility
                    .await()
                    .atMost(Duration.ofSeconds(30))
                    .pollInterval(Duration.ofSeconds(2))
                    .ignoreExceptions()
                    .untilAsserted(() -> {
                        given()
                                .spec(get())
                                .queryParam("path", path)
                        .when()
                                .get(RESOURCES)
                        .then()
                                .statusCode(200)
                                .body("name", equalTo(fileName));
                    });

        } finally {
            given()
                    .spec(get())
                    .queryParam("path", path)
                    .queryParam("permanently", true)
            .when()
                    .delete(RESOURCES)
            .then()
                    .statusCode(anyOf(is(204), is(202), is(404)));
        }
    }
    @Test
    @Tag("positive")
    @Severity(SeverityLevel.NORMAL)
    @Description("Обновление пользовательских метаданных у файла")
    @DisplayName("Обновление метаданных файла → 200, свойства обновлены")
    @Story("Метаданные")
    void shouldUpdateFileMetadata() {
        String filePath = "/metadata_test_" + UUID.randomUUID() + ".txt";

        try {
            // 1. Создаём файл
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
                    .body("test content".getBytes())
                    .when()
                    .put(uploadUrl)
                    .then()
                    .statusCode(201);

            // 2. Обновляем метаданные (добавляем пользовательские свойства)
            Map<String, String> customProps = new HashMap<>();
            customProps.put("tag", "important");
            customProps.put("color", "red");

            given()
                    .spec(get())
                    .queryParam("path", filePath)
                    .body(Map.of("custom_properties", customProps))
                    .when()
                    .patch(RESOURCES)
                    .then()
                    .statusCode(200)
                    .body("custom_properties.tag", equalTo("important"))
                    .body("custom_properties.color", equalTo("red"));

            // 3. Проверяем, что свойства сохранились (опционально, можно сделать отдельный GET)
            given()
                    .spec(get())
                    .queryParam("path", filePath)
                    .when()
                    .get(RESOURCES)
                    .then()
                    .statusCode(200)
                    .body("custom_properties.tag", equalTo("important"))
                    .body("custom_properties.color", equalTo("red"));

        } finally {
            // 4. Очистка
            given()
                    .spec(get())
                    .queryParam("path", filePath)
                    .queryParam("permanently", true)
                    .when()
                    .delete(RESOURCES)
                    .then()
                    .statusCode(anyOf(is(204), is(404)));
        }
    }
}
