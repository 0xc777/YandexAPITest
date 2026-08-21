package api.tests.steps.interfaces;

import api.tests.dto.ResourceMetadataResponse;
import io.restassured.response.Response;

public interface ResourceSteps {

    ResourceMetadataResponse getResourceMetadata (String filePath);
    Response sendDeleteResource(String path);
    void deleteResource(String resourcePath);
    void resourceNotExists(String resourcePath);
    Response  sendResource(String resourcePath);
    Response sendMove(String fromPath, String toPath);
    Response moveResource(String fromPath, String toPath);
    Response sendCopyRequest(String fromPath, String toPath);
    Response copyResource(String fromPath, String toPath);
    Response sendEndpoint(String endpoint, String method);
}
