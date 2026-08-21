package api.tests.steps.interfaces;

import api.tests.dto.ResourceMetadataResponse;
import io.restassured.response.Response;

import java.util.Map;

public interface FileSteps {

    String getLink(String filePath);
    Response uploadFileByLink(String uploadUrl, byte[] content);
    Response uploadFile(String filePath, byte[] content);
    Response searchFile(String filePath);
    void checkFileNotExists(String filePath);
    Response uploadFileFromUrl(String filePath, String fileUrl);
    ResourceMetadataResponse updateFileMetadata(String filePath, Map<String, String> customProps);
    Response sendUploadFromUrl(String filePath, String fileUrl);
    Response sendOverwrite(String overwrite, String path);
}
