package org.school.server.response;

public class ErrorResponse extends Response {
    public ErrorResponse(String data) {
        super("ERROR", data);
    }
}
