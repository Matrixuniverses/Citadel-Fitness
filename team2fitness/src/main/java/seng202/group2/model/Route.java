package seng202.group2.model;

import javafx.collections.ObservableList;

/**
 * Model class used for the Mapping features, creates a JSON array using given a list of points
 */
public class Route {
    private ObservableList<DataPoint> path;

    /**
     * Creates a new route with the given points
     * @param points Datapoints to generate route from
     */
    public Route(ObservableList<DataPoint> points) {
        path = points;
    }

    /**
     * Creates a new JSON array that is parsed by the Map features in JavaScript
     * @return String containing the array
     */
    public String toJSONArray() {
        StringBuilder stringBuild = new StringBuilder();
        stringBuild.append("[");
        path.forEach(point -> stringBuild.append(
                String.format("{lat: %f, lng: %f}, ", point.getLatitude(), point.getLongitude())));
        stringBuild.append("]");
        return stringBuild.toString();
    }
}
