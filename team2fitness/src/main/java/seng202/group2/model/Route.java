package seng202.group2.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng202.group2.model.DataPoint;

import java.util.ArrayList;

public class Route {
    private ObservableList<DataPoint> path = FXCollections.observableArrayList();

    public Route(ObservableList<DataPoint> points) {
        path = points;
    }

    public String toJSONArray() {
        StringBuilder stringBuild = new StringBuilder();
        stringBuild.append("[");
        path.forEach(point -> stringBuild.append(
                String.format("{lat: %f, lng: %f}, ", point.getLatitude(), point.getLongitude())));
        stringBuild.append("]");
        return stringBuild.toString();
    }
}
