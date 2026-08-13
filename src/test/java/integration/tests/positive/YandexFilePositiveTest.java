package integration.tests.positive;

import integration.tests.dto.ResourceMetadataResponse;
import integration.tests.steps.FileSteps;
import integration.tests.steps.FolderSteps;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class YandexFilePositiveTest {

    @Test
    @Tag("positive")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Копирование файла в другую папку 201")
    @DisplayName("Копирование файла, проверка наличия копии")
    @Story("Файлы")
    void copyFileToAnotherFolder() throws IOException {

        String pathFolder = "/path_folder_" + UUID.randomUUID();
        String targetFolder = "/target_folder_" + UUID.randomUUID();
        String fileName = "test_file.txt";
        String filePath = pathFolder + "/" + fileName;
        String targetPath = targetFolder + "/" + fileName;

        FolderSteps folderSteps = new FolderSteps();
        FileSteps fileSteps = new FileSteps();
        try {

            folderSteps.createFolder(pathFolder);
            folderSteps.createFolder(targetFolder);
            fileSteps.uploadFile(filePath, "Hello, World!".getBytes());
            fileSteps.copyResource(pathFolder,targetPath);
            fileSteps.assertFileExists(filePath,fileName);

        } finally {
            folderSteps.deleteFolder(pathFolder);
            folderSteps.deleteFolder(targetFolder);
            folderSteps.FolderNotExists(pathFolder);
            folderSteps.FolderNotExists(targetFolder);
        }
    }
    @Test
    @Tag("positive")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Перемещение файла в другую папку 201")
    @DisplayName("Перемещение файла, проверка наличия на новом месте и отсутствия на старом")
    @Story("Файлы")
    public void moveFileToAnotherFolder() throws IOException {
        String pathFolder = "/source_folder_" + UUID.randomUUID();
        String targetFolder = "/target_folder_" + UUID.randomUUID();
        String fileName = "test_file.txt";
        String filePath = pathFolder + "/" + fileName;
        String targetPath = targetFolder + "/" + fileName;

        FolderSteps folderSteps = new FolderSteps();
        FileSteps fileSteps = new FileSteps();

        try {

            folderSteps.createFolder(pathFolder);
            folderSteps.createFolder(targetFolder);
            fileSteps.uploadFile(filePath, "Hello, World!".getBytes());
            fileSteps.moveResource(filePath, targetPath);
            fileSteps.assertFileExists(targetPath, fileName);
            fileSteps.assertFileNotExists(filePath);

        } finally {
            folderSteps.deleteFolder(pathFolder);
            folderSteps.deleteFolder(targetFolder);
        }
    }
    @Test
    @Tag("positive")
    @Severity(SeverityLevel.NORMAL)
    @Description("Загрузка файла на диск по публичной ссылке с GitHub 202")
    @DisplayName("Загрузка файла по URL, ожидание появления файла")
    @Story("Файлы")
    void uploadFileFromUrl() {
        String path = "/uploaded_from_url_" + UUID.randomUUID() + ".txt";
        String fileUrl = "https://raw.githubusercontent.com/0xc777/YandexAPITest/main/README.md";
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        FolderSteps folderSteps = new FolderSteps();
        FileSteps fileSteps = new FileSteps();

        try {
            fileSteps.uploadFileFromUrl(path, fileUrl);
            fileSteps.assertFileExists(path, fileName);
        } finally {
            folderSteps.deleteFolder(path);  // удаляем загруженный файл
        }
    }

    @Test
    @Tag("positive")
    @Severity(SeverityLevel.NORMAL)
    @Description("Обновление пользовательских метаданных у файла")
    @DisplayName("Обновление метаданных файла → 200, свойства обновлены")
    @Story("Метаданные")
    void updateFileMetadata() {
        String filePath = "/metadata_test_" + UUID.randomUUID() + ".txt";
        byte[] content = "test content".getBytes();
        FolderSteps folderSteps = new FolderSteps();
        FileSteps fileSteps = new FileSteps();
        Map<String, String> customProps = new HashMap<>();
        customProps.put("tag", "important");
        customProps.put("color", "red");
        try {
            fileSteps.uploadFile(filePath, content);
            ResourceMetadataResponse patchResponse = fileSteps.updateFileMetadata(filePath, customProps);
            assertThat(patchResponse.getCustomProperties())
                    .containsEntry("tag", "important")
                    .containsEntry("color", "red");

            ResourceMetadataResponse getResponse = fileSteps.getResourceMetadata(filePath);
            assertThat(getResponse.getCustomProperties())
                    .containsEntry("tag", "important")
                    .containsEntry("color", "red");
        } finally {
            folderSteps.deleteFolder(filePath);
        }
    }
}
