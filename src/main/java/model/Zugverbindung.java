package model;

import java.util.Arrays;
import java.util.HashSet;

public class Zugverbindung {
    private final HashSet<String> stationen;
    public Zugverbindung(String[] stationen) {
        this.stationen = new HashSet<>(Arrays.asList(stationen));
    }

    public HashSet<String> getStations() {
        return this.stationen;
    }

    public void removeStation(String station) {
        this.stationen.remove(station);
    }

    public String toString() {
        return stationen.toString();
    }
}
