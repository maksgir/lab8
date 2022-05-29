package test.client.util;

import test.common.entities.Route;

import java.util.ArrayList;
import java.util.List;

public class RouteFilter {
    private List<Route> allRoutes;
    private String name;
    private Long distance;
    private String sign;
    private String from;
    private String to;

    public RouteFilter(List<Route> allRoutes, String name, Long distance, String sign, String from, String to) {
        this.allRoutes = allRoutes;
        this.name = name;
        this.distance = distance;
        this.sign = sign;
        this.from = from;
        this.to = to;
    }

    public List<Route> filterRoutes(){
        List<Route> filtered = new ArrayList<>();
        for (Route route: allRoutes){
            if (checkRoute(route)){
                filtered.add(route);
            }
        }
        return filtered;
    }

    private boolean checkRoute(Route route){
        return checkName(route)&&checkDistance(route)&&checkFrom(route)&&checkTo(route);
    }

    private boolean checkName(Route route){
        return (name == null || route.getName().equals(name));
    }

    private boolean checkDistance(Route route){
        long routeDistance = route.getDistance();
        if (sign == null){
            return true;
        } else if (sign.equals(">")){
            return routeDistance > distance;
        } else if (sign.equals("<")){
            return routeDistance < distance;
        } else if (sign.equals("=")){
            return routeDistance == distance;
        } else if (sign.equals(">=")){
            return routeDistance >= distance;
        } else if (sign.equals("<=")){
            return routeDistance <= distance;
        } else return false;
    }

    private boolean checkFrom(Route route){
        return (from == null || route.getFrom().getName().equals(from));
    }

    private boolean checkTo(Route route){
        return (to == null || route.getTo().getName().equals(to));
    }


}
