package api.tests.dto;

public class ErrorResponse {
    private String message;
    private String error;
    private String description;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
