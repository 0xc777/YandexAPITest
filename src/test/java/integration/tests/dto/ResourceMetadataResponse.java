package integration.tests.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceMetadataResponse {
    private String path;
    private String name;
    private String type;
    private long size;
    private String created;
    private String modified;
    private String publicUrl;
    private String method;
    private String href;
    private String publicKey;
    private boolean templated;
    private Map<String, String> customProperties;

    public ResourceMetadataResponse() {}


    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }

    public String getCreated() { return created; }
    public void setCreated(String created) { this.created = created; }

    public String getModified() { return modified; }
    public void setModified(String modified) { this.modified = modified; }

    @JsonProperty("public_url")    // маппинг на поле в JSON
    public String getPublicUrl() { return publicUrl; }
    public void setPublicUrl(String publicUrl) { this.publicUrl = publicUrl; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getHref() { return href; }
    public void setHref(String href) { this.href = href; }

    public boolean isTemplated() { return templated; }
    public void setTemplated(boolean templated) { this.templated = templated; }

    @JsonProperty("public_key")
    public String getPublicKey() { return publicKey; }
    public void setPublicKey(String publicKey) { this.publicKey = publicKey; }

    @JsonProperty("custom_properties")
    public Map<String, String> getCustomProperties() { return customProperties; }
    public void setCustomProperties(Map<String, String> customProperties) { this.customProperties = customProperties; }
}

