package api.tests.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LinkResponse {
    private String method;
    private String href;
    private boolean templated;
    private String public_url;
    private String operation_id;

    public LinkResponse() {}


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public boolean isTemplated() {
        return templated;
    }

    public void setTemplated(boolean templated) {
        this.templated = templated;
    }

    public String getPublicUrl() {
        return public_url;
    }

    public void setPublicUrl(String publicUrl) {
        this.public_url = publicUrl;
    }

    @JsonProperty("operation_id")
    public String getOperationId() { return operation_id; }

    @JsonProperty("operation_id")
    public void setOperationId(String operationId) { this.operation_id = operationId; }
}