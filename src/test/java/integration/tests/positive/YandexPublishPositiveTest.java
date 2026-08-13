package integration.tests.positive;

import integration.tests.steps.FolderSteps;
import integration.tests.steps.PublishSteps;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class YandexPublishPositiveTest {

    @Test
    @Tag("positive")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Публикация папки и проверка, что она стала публичной 200")
    @DisplayName("Публикация папки, проверка публичного статуса")
    @Story("Публикация")
    void publishFolderAndCheckPublicStatus() {
        FolderSteps folderSteps = new FolderSteps();
        PublishSteps publishSteps = new PublishSteps();

        String folderPath = "/publish_folder_" + UUID.randomUUID();

        try {
            folderSteps.createFolder(folderPath);
            publishSteps.publishResource(folderPath);
            publishSteps.assertResourceIsPublic(folderPath);

        } finally {
            folderSteps.deleteFolder(folderPath);
        }
    }
}
