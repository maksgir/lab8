package test.server.util;

import test.common.entities.Route;
import test.common.exceptions.CollectionIsEmptyException;
import test.common.exceptions.GroupNotFoundException;
import test.common.exceptions.GroupNotMinException;
import test.common.exceptions.IDNotFoundException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class RoutesCollection {
    private static long idCounter = 4;
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

    public void updateById(Long id, Route route) throws IDNotFoundException {
        if (routes.removeIf(r -> Objects.equals(r.getId(), id))) {
            route.setId(id);
            routes.add(route);
        } else {
            throw new IDNotFoundException("There is no group with this ID");
        }
    }

    public void addRoute(Route route) {
        route.setId(idCounter++);
        routes.add(route);
    }

    public void addIfMin(Route route) throws GroupNotMinException {
        for (Route r : routes) {
            if (r.compareTo(route) <= 0) {
                throw new GroupNotMinException("The entered route is not minimal");
            }
        }
        addRoute(route);
    }

    public void clearCollection() {
        routes.clear();
    }

    public void removeRouteById(Long id) throws IDNotFoundException {
        if (!routes.removeIf(r -> Objects.equals(r.getId(), id))) {
            throw new IDNotFoundException("There is no route with this ID");
        }
    }

    public Route removeHead() {
        return routes.pollFirst();
    }

    public List<Route> removeIfLower(Route route) throws CollectionIsEmptyException {
        ArrayDeque<Route> copy = new ArrayDeque<>(routes);
        if (!routes.isEmpty()) {
            routes.removeIf(mb -> mb.compareTo(route) < 0);
            copy.removeAll(routes);
        } else {
            throw new CollectionIsEmptyException("Collection is empty");
        }
        return new ArrayList<>(copy);
    }

    public Route removeAnyByDistance(Long distance) throws GroupNotFoundException, CollectionIsEmptyException {
        if (!routes.isEmpty()) {
            List<Route> matchRoutes = routes.stream().filter(mb -> Objects.equals(mb.getDistance(), distance)).collect(Collectors.toList());
            if (matchRoutes.isEmpty()) {
                throw new GroupNotFoundException("There is no route with such a distance");
            } else {
                routes.remove(matchRoutes.get(0));
                return matchRoutes.get(0);
            }
        } else {
            throw new CollectionIsEmptyException("Collection is empty");
        }
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
