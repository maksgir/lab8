package test.server.util;

import test.common.entities.Route;
import test.common.entities.User;
import test.common.exceptions.CollectionIsEmptyException;
import test.common.exceptions.GroupNotFoundException;
import test.common.exceptions.GroupNotMinException;
import test.common.exceptions.IDNotFoundException;

import java.time.LocalDate;
import java.util.*;


import java.util.stream.Collectors;

public class RoutesCollection {
    private LocalDate dateOfInitialization;
    private ArrayDeque<Route> routes;

    public RoutesCollection() {
        dateOfInitialization = LocalDate.now();
        routes = new ArrayDeque<>();
    }


    public void sort() {
        if (routes.size() != 0) {
            List<Route> routeList = new ArrayList<>(routes);
            routeList = routeList.stream().sorted().collect(Collectors.toList());
            this.routes = new ArrayDeque<>(routeList);
        }

    }

    public void setRoutes(ArrayDeque<Route> routes) {
        this.routes = routes;
    }

    public String returnInfo() {

        return "Коллекция типа: " + routes.getClass().getSimpleName() + ", тип элементов коллекции: "
                + Route.class.getSimpleName() + ", дата инициализации: " + dateOfInitialization
                + ", количество элементов: " + routes.size();
    }

    public List<Route> getListOfRoutes() {
        return new ArrayList<>(routes);
    }

    public String show() {
        if (routes.isEmpty()) {
            return "Collection is empty";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Route route : routes) {
                sb.append(route).append("\n");
            }
            sb = new StringBuilder(sb.substring(0, sb.length() - 2));
            return sb.toString();
        }
    }

    public void updateById(int id, Route route) throws IDNotFoundException {
        routes.removeIf(r -> r.getId() == id);
        route.setId(id);
        routes.add(route);
        System.out.println("Маршрут обновлен");
    }

    public void addRoute(Route route) {
        routes.add(route);
    }

    public boolean checkIfMin(Route route) throws GroupNotMinException {
        for (Route r : routes) {
            if (r.compareTo(route) <= 0) {
                throw new GroupNotMinException("Существует меньший элемент коллекции. Ваш маршрут добавлен не будет");
            }
        }
        return true;
    }

    public void clearCollection(User user) {
        routes.removeIf(r -> r.getOwner().equals(user.getLogin()));

    }

    public void removeRouteById(int id) throws IDNotFoundException {
        routes.removeIf(r -> r.getId() == id);
    }

    public Route removeHead() {
        return routes.pollFirst();
    }

    public Route showHead() {
        return routes.getFirst();
    }

    public List<Route> checkLower(Route route) {
        List<Route> ans = new ArrayList<>();
        for (Route r : routes) {
            if (r.compareTo(route) < 0) {
                ans.add(r);
            }
        }
        return ans;
    }

    public Route AnyEqDistance(Long distance) {

        for (Route r: routes){
            if (r.getDistance()==distance){
                return r;
            }
        }
        return null;

    }



    public List<Route> filterByDistance(Long distance) {
        return routes.stream().filter(x -> x.getDistance() == distance).collect(Collectors.toList());
    }

    public List<Route> filterGreaterByDistance(Long distance) {
        return routes.stream().filter(x -> x.getDistance() > distance).collect(Collectors.toList());
    }


    public ArrayDeque<Route> getCollection() {
        return routes;
    }
}
