package integration.constants;

public final class Endpoints {
    public static final String DISK = "/v1/disk";
    public static final String RESOURCES = DISK + "/resources";
    public static final String UPLOAD = RESOURCES + "/upload";
    public static final String COPY = RESOURCES + "/copy";
    public static final String MOVE = RESOURCES + "/move";

    private Endpoints() {}
}