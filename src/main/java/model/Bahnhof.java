package model;

import java.util.HashSet;

public class Bahnhof {
    private final HashSet<Integer> connections;
    private final String name;

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

    public void removeConnections(HashSet<Integer> connections) {
        this.connections.removeAll(connections);
    }

    public void addConnections(HashSet<Integer> connections) {
        this.connections.addAll(connections);
    }

    public Integer getConnectionCount() {
        return this.connections.size();
    }

    public String toString() {
        return this.name + ":" + this.connections.toString();
    }
}
