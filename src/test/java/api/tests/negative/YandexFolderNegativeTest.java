package api.tests.negative;

import api.tests.assertions.ErrorConditions;
import api.tests.assertions.LinkResponseAssertions;
import api.tests.dto.ErrorResponse;
import api.tests.dto.LinkResponse;
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

import java.io.IOException;
import java.util.UUID;

import static api.tests.assertions.ErrorConditions.diskNotFound;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class YandexFolderNegativeTest {

    private final FolderSteps folderSteps = StepsFactory.getFolderStepsAssured();
    private final ResourceSteps resourceSteps = StepsFactory.getResourceStepsAssured();

    @Test
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @Description("Запрос информации о несуществующей папке 404")
    @DisplayName("Несуществующая папка")
    @Story("Папки")
    void return404WithErrorFolderNotFound(){
        String nonExistentPath = "/non_existent_folder_" + UUID.randomUUID();

        Response response = resourceSteps.sendResource(nonExistentPath);
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(errorResponse).is(diskNotFound());

    }

    @Test
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @Description("Создание папки с существующим именем 409")
    @DisplayName("Создание уже существующей папки")
    @Story("Папки")
    void return409WhenFolderAlreadyExists() throws IOException {

        String folderPath = "/negative_test_" + UUID.randomUUID();

        try {
            Response createResponse = folderSteps.createFolder(folderPath);
            assertThat(createResponse.getStatusCode()).isEqualTo(201);
            LinkResponse linkResponse = createResponse.as(LinkResponse.class);
            LinkResponseAssertions.assertLinkResponse(linkResponse, folderPath);

            Response createResponse2 = folderSteps.createFolder(folderPath);
            assertThat(createResponse2.getStatusCode()).isEqualTo(409);
            ErrorResponse errorResponse = createResponse2.as(ErrorResponse.class);
            assertThat(errorResponse).is(ErrorConditions.DiskPathPointsToExistentDirectoryError());
        }
        finally {
            resourceSteps.deleteResource(folderPath);
            resourceSteps.resourceNotExists(folderPath);
        }


    }

    @Test
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @Description("Удаление несуществующей папки 404")
    @DisplayName("Удаление несуществующей папки")
    @Story("Папки")
    void return404WhenDeletingNonExistentFolder(){
        String nonExistentPath = "/non_existent_folder_" + UUID.randomUUID();


        Response response = resourceSteps.sendDeleteResource(nonExistentPath);
        assertThat(response.getStatusCode()).isEqualTo(404);
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(errorResponse).is(ErrorConditions.diskNotFound());

    }

    @Test
    @Tag("negative")
    @Severity(SeverityLevel.MINOR)
    @Description("Пустой путь при создании папки 400")
    @DisplayName("Пустой путь при создании папки")
    @Story("Папки")
    void return400WhenEmptyPath(){

        Response response = folderSteps.createFolder("");
        assertThat(response.getStatusCode()).isEqualTo(400);
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(errorResponse).is(ErrorConditions.FieldValidationError());

    }
}
