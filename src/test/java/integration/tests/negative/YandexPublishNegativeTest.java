package integration.tests.negative;

import integration.tests.assertions.ErrorConditions;
import integration.tests.dto.ErrorResponse;
import integration.tests.steps.FileSteps;
import integration.tests.steps.FolderSteps;
import integration.tests.steps.PublishSteps;
import integration.tests.steps.ResourceSteps;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class YandexPublishNegativeTest {

    public static PublishSteps publishSteps = new PublishSteps();

    @Test
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @Description("Публикация несуществующего ресурса 404")
    @DisplayName("Публикация несуществующего ресурса")
    @Story("Публикация")
    void return404PublishNonExistentResource() {

        String nonExistentPath = "/non_existent_" + UUID.randomUUID();

        Response publishResponse = publishSteps.publishResource(nonExistentPath);
        assertThat(publishResponse.getStatusCode()).isEqualTo(404);
        ErrorResponse errorResponse = publishResponse.as(ErrorResponse.class);
        assertThat(errorResponse).is(ErrorConditions.diskNotFound());

    }

    @Test
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @Description("Удаление публикации несуществующего ресурса  405")
    @DisplayName("Удаление публикации несуществующего ресурса")
    @Story("Публикация")
    void return404NonPublishedResource() {

        String nonExistentPath = "/non_existent_" + UUID.randomUUID();

        Response deleteResponse = publishSteps.deletePublish(nonExistentPath);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(405);

        ErrorResponse errorResponse = deleteResponse.as(ErrorResponse.class);
        assertThat(errorResponse).is(ErrorConditions.MethodNotAllowedError());

    }
}
