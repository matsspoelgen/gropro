package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Zugverbindung {
    private HashSet<String> stationen;
    public Zugverbindung(String[] stationen) {
        this.stationen = new HashSet<>(Arrays.asList(stationen));
    }

    public HashSet<String> getStationen() {
        return this.stationen;
    }

    public void removeStations(ArrayList<String> stations) {
        stations.forEach(this.stationen::remove);
    }


    public String toString() {
        return stationen.toString();
    }
}
