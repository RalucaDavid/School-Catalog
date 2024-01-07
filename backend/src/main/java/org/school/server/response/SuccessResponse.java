package org.school.server.response;

public class SuccessResponse extends Response {
    public SuccessResponse(Object data) {
        super("SUCCESS", data);
    }
}

