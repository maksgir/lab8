package test.common.util;

import java.io.Serializable;

public class Response implements Serializable {

    private String messageToResponse;
    private boolean successful;


    public Response(String messageToResponse, boolean isSuccessful) {
        this.messageToResponse = messageToResponse;
        this.successful = isSuccessful;
    }

    public String getMessageToResponse() {
        return messageToResponse;
    }

    public boolean isSuccessful() {
        return successful;
    }
}
