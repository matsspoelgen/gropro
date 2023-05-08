package logic;

import ioHandling.logger.Logger;
import model.Zugverbindung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Streckennetz {

    private ArrayList<Zugverbindung> verbindungen;
    private HashSet<String> stationen;
    private Datenreduktionstechnik[] drt;
    private Logger logger;

    public Streckennetz(ArrayList<Zugverbindung> verbindungen) {
        this.logger = Logger.getInstance();
        this.verbindungen = verbindungen;
        this.stationen = new HashSet<>();
        verbindungen.forEach(v -> stationen.addAll(v.getStationen()));
        this.drt = new Datenreduktionstechnik[]{new StationsReduktion(this), new VerbindungsReduktion(this)};
    }

    public void reduziereVerbindungen() {
        for (int i = 0; i < this.verbindungen.size(); i++) {
            for (int j = 0; j < this.verbindungen.size(); j++) {
                if(i == j){
                    continue;
                }

                if(this.verbindungen.get(i).getStationen().containsAll(this.verbindungen.get(j).getStationen())){
                    logger.log(String.format("Entferne Verbindung %s", verbindungen.get(i)));
                    verbindungen.remove(i);
                    --i;
                    break;
                }
            }
        }
    }

    private void reduziereStationen() {
        Map<String, ArrayList<Integer>> stationsVerbindungen = new HashMap<>();
        ArrayList<String> removed = new ArrayList<>();

        for(int i = 0; i < this.verbindungen.size(); i++) {
            HashSet<String> stationen = this.verbindungen.get(i).getStationen();
            for (String station: stationen) {
                ArrayList<Integer> tempVerbindungen = stationsVerbindungen.get(station);
                if(tempVerbindungen == null) {
                    tempVerbindungen = new ArrayList<>();
                }
                tempVerbindungen.add(i);
            }
        }

        for (String key1: stationsVerbindungen.keySet()) {
            for (String key2: stationsVerbindungen.keySet()) {
                if(key1.equals(key2)) {
                    continue;
                }

                if (stationsVerbindungen.get(key1).containsAll(stationsVerbindungen.get(key2))){
                    stationsVerbindungen.remove(key1);
                    removed.add(key1);
                    logger.log(String.format("Bahnhof %s entfernt.", key1));
                }
            }
        }

        for (Zugverbindung verbindung : this.verbindungen) {
            verbindung.removeStations(removed);
        }
    }

    public ArrayList<String> getMinStationen() {
        this.reduziereVerbindungen();
        this.reduziereStationen();

        ArrayList<String> serviceStationen = new ArrayList<>();

        return serviceStationen;
    }
}
