package model;

import java.util.HashSet;

public class Bahnhof {
    private HashSet<Integer> connections;
    private String name;

    public Bahnhof(String name) {
        this.connections = new HashSet<>();
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void addConnection(int connectionIndex) {
        this.connections.add(connectionIndex);
    }

    public HashSet<Integer> getConnections() {
        return this.connections;
    }

    public String toString() {
        return this.connections.toString();
    }
}
