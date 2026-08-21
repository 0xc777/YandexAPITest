package api.tests.positive;

import api.tests.steps.interfaces.FolderSteps;
import api.tests.steps.interfaces.PublishSteps;
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

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class YandexPublishPositiveTest {

    private final PublishSteps publishSteps = StepsFactory.getPublishSteps();
    private final FolderSteps folderSteps = StepsFactory.getFolderSteps();
    private final ResourceSteps resourceSteps = StepsFactory.getResourceSteps();

    @Test
    @Tag("positive")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Публикация папки и проверка, что она стала публичной 200")
    @DisplayName("Публикация папки, проверка публичного статуса")
    @Story("Публикации")
    void publishFolderAndCheckPublicStatus() throws IOException {


        String folderPath = "/publish_folder_" + UUID.randomUUID();

        try {

            Response createFolderResponse = folderSteps.createFolder(folderPath);
            assertThat(createFolderResponse.statusCode()).isEqualTo(201);
            Response publishFolder = publishSteps.publishResource(folderPath);
            assertThat(publishFolder.getStatusCode()).isEqualTo(200);
            publishFolder = publishSteps.getPublicLink(folderPath);
            String publicURL = publishSteps.extractPublicUrl(publishFolder);
            assertThat(publicURL).isNotNull();
            Response publicResourceResponse = publishSteps.searchResourceIsPublic(publicURL);
            assertThat(publicResourceResponse.getStatusCode()).isEqualTo(200);

        } finally {

            resourceSteps.deleteResource(folderPath);
            resourceSteps.resourceNotExists(folderPath);

        }
    }
}
