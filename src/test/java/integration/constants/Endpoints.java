package integration.constants;

public final class Endpoints {
    public static final String DISK = "/v1/disk";
    public static final String RESOURCES = DISK + "/resources";
    public static final String UPLOAD = RESOURCES + "/upload";
    public static final String COPY = RESOURCES + "/copy";
    public static final String MOVE = RESOURCES + "/move";
    public static final String TRASH = "/v1/disk/trash";
    public static final String TRASH_RESOURCES = TRASH + "/resources";
    public static final String TRASH_RESTORE = TRASH_RESOURCES + "/restore";
    public static final String PUBLISH = RESOURCES + "/publish";
}