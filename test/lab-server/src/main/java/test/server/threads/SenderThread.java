package test.server.threads;

import test.common.util.Response;
import test.server.socket.ServerSocketWorker;

import java.io.IOException;

public class SenderThread implements Runnable {

    private Response response;
    private ServerSocketWorker serverSocketWorker;

    public SenderThread(Response response, ServerSocketWorker serverSocketWorker) {
        this.response = response;
        this.serverSocketWorker = serverSocketWorker;
    }

    @Override
    public void run() {
        try {
            serverSocketWorker.sendResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
