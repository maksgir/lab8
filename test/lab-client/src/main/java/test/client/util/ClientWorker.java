package test.client.util;

import test.common.entities.Route;
import test.common.entities.User;
import test.common.util.Request;
import test.common.util.Response;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

public class ClientWorker {
    private User user;
    private ClientSocketWorker clientSocketWorker;

    public ClientWorker() {
        init();
    }

    private void init() {
        try {
            this.clientSocketWorker = new ClientSocketWorker();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public User getUser(){
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public boolean sendRequest(Request request) {
        if (request != null) {
            try {
                clientSocketWorker.sendRequest(request);
                return true;
            } catch (IOException e) {
                throw new IllegalArgumentException("Возникла ошибка сериализации данных");
            }
        } else {
            return false;
        }
    }

    public Response receiveResponse() {
        try {
            Response response = clientSocketWorker.receiveResponse();
            return response;
        } catch (SocketTimeoutException e) {
            throw new IllegalArgumentException("Время ожидания отклика от сервера превышено, попробуйте позже");
        } catch (IOException e) {
            throw new IllegalArgumentException("Ошибка получения ответа от сервера");

        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Ответ от сервера пришел поврежденный");

        }

    }
}
