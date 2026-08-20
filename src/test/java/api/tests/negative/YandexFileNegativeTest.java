package api.tests.negative;

import api.tests.assertions.ErrorConditions;
import api.tests.assertions.LinkResponseAssertions;
import api.tests.dto.ErrorResponse;
import api.tests.dto.LinkResponse;
import api.tests.steps.interfaces.FileSteps;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class YandexFileNegativeTest {

    private final FileSteps fileSteps = StepsFactory.getFileStepsAssured();
    private final FolderSteps folderSteps = StepsFactory.getFolderStepsAssured();
    private final ResourceSteps resourceSteps = StepsFactory.getResourceStepsAssured();


    @Test
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @Description("Копирование в несуществующую папку 409")
    @DisplayName("Копирование в несуществующую папку")
    @Story("Файлы")
    void failToCopyWhenTargetFolderNotExists() throws IOException {

        String folderPath = "/test_folder_" + UUID.randomUUID();
        String fileName = "test_file.txt";
        String filePath = folderPath + "/"+ fileName;
        String targetPath = "/non_existent_folder_" + UUID.randomUUID() + filePath;


        try {
            Response createResponse = folderSteps.createFolder(folderPath);
            assertThat(createResponse.statusCode()).isEqualTo(201);
            LinkResponse linkResponse = createResponse.as(LinkResponse.class);
            LinkResponseAssertions.assertLinkResponse(linkResponse, folderPath);
            fileSteps.uploadFile(filePath,"test content".getBytes());

            Response copyResponse = resourceSteps.sendCopyRequest(filePath, targetPath);
            assertThat(copyResponse.getStatusCode()).isEqualTo(409);
            assertThat(copyResponse.as(ErrorResponse.class)).is(ErrorConditions.DiskPathDoesntExistsError());

        }

        finally {
           resourceSteps.deleteResource(folderPath);
           resourceSteps.resourceNotExists(folderPath);
        }
    }

    @Test
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @Description("Копирование поверх существующего файла без overwrite 409")
    @DisplayName("Копирование поверх существующего файла без overwrite")
    @Story("Файлы")
    void return409WhenCopyOverExistingFileWithoutOverwrite()  throws IOException {
        String sourcePath = "/source_" + UUID.randomUUID() + ".txt";
        String targetPath = "/target_" + UUID.randomUUID() + ".txt";

        try {

            Response uploadSource = fileSteps.uploadFile(sourcePath, "source content".getBytes());
            assertThat(uploadSource.getStatusCode()).isEqualTo(201);

            Response uploadTarget = fileSteps.uploadFile(targetPath, "target content".getBytes());
            assertThat(uploadTarget.getStatusCode()).isEqualTo(201);

            Response copyResponse = resourceSteps.sendCopyRequest(sourcePath, targetPath);
            assertThat(copyResponse.getStatusCode()).isEqualTo(409);

            ErrorResponse errorResponse = copyResponse.as(ErrorResponse.class);
            assertThat(errorResponse).is(ErrorConditions.conflict());

        } finally {

            resourceSteps.deleteResource(sourcePath);
            resourceSteps.deleteResource(targetPath);
            resourceSteps.resourceNotExists(sourcePath);
            resourceSteps.resourceNotExists(targetPath);
        }
    }
}
