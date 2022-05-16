package test.common.util;

import test.common.entities.Route;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Response implements Serializable {

    private String messageToResponse;
    private boolean successful;

    private Route routeToResponse;
    private List<Route> collectionToResponse;



    public Response(String messageToResponse, boolean isSuccessful) {
        this.messageToResponse = messageToResponse;
        this.successful = isSuccessful;
    }

    public Response(String messageToResponse) {
        this.messageToResponse = messageToResponse;
    }

    public Response(String messageToResponse, Route routeToResponse) {
        this.messageToResponse = messageToResponse;
        this.routeToResponse = routeToResponse;
    }

    public Response(String messageToResponse, List<Route> collectionToResponse) {
        this.messageToResponse = messageToResponse;
        this.collectionToResponse = collectionToResponse;
    }

    public void setMessageToResponse(String messageToResponse) {
        this.messageToResponse = messageToResponse;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public Route getRouteToResponse() {
        return routeToResponse;
    }

    public void setRouteToResponse(Route routeToResponse) {
        this.routeToResponse = routeToResponse;
    }

    public List<Route> getCollectionToResponse() {
        return collectionToResponse;
    }

    public void setCollectionToResponse(List<Route> collectionToResponse) {
        this.collectionToResponse = collectionToResponse;
    }




    public String getMessageToResponse() {
        return messageToResponse;
    }

    public boolean isSuccessful() {
        return successful;
    }

    @Override
    public String toString() {
        StringBuilder collection = new StringBuilder();
        if (collectionToResponse != null && !collectionToResponse.isEmpty()) {
            List<Route> sortedRoutes = new ArrayList<>(collectionToResponse);
            sortedRoutes = sortedRoutes.stream().sorted().collect(Collectors.toList());

            for (Route route : sortedRoutes) {
                collection.append(route.toString()).append("\n");
            }
            collection = new StringBuilder(collection.substring(0, collection.length() - 1));
        }
        return (messageToResponse == null ? "" : messageToResponse)
                + (routeToResponse == null ? "" : "\n" + routeToResponse)
                + ((collectionToResponse == null) ? "" : "\n"
                + collection);
    }
}
