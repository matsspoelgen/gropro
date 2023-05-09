package logic;

import ioHandling.logger.ConstantsLogging;
import ioHandling.logger.Logger;
import model.Zugverbindung;

import java.util.*;
import java.util.stream.Collectors;

public class Streckennetz {

    private ArrayList<Zugverbindung> connections;
    private Set<String> stations;
    private Logger logger;

    public Streckennetz(ArrayList<Zugverbindung> verbindungen) {
        this.logger = Logger.getInstance();
        this.connections = verbindungen;
        this.stations = getStations();
    }

    private HashSet<String> getStations() {
        HashSet<String> stations = new HashSet<>();
        for (Zugverbindung connection :
                this.connections) {
            stations.addAll(connection.getStations());
        }
        return stations;
    }

    public HashSet<String> getMinStations() {
        reduceConnections();
        reduceStations();

        logger.start(ConstantsLogging.MIN_STATIONS);

        HashSet<String> res = new HashSet<>();
        List<Zugverbindung> current = new ArrayList<>(connections);

        while (current.size() != 0) {
            Map<String, Long> bahnhofCount = getCount(current);
            String max = Collections.max(bahnhofCount.entrySet(), Map.Entry.comparingByValue()).getKey();
            res.add(max);

            List<Zugverbindung> faulty = testConnectAndReturnFaulty(current, res);
            if (faulty.size() != 0) {
                current = faulty;
            } else {
                break;
            }
        }

        logger.log(String.format("Minimale Servicestationen ermittelt: %s", res));
        logger.stop(ConstantsLogging.MIN_STATIONS);
        return res;
    }

    private Map<String, Long> getCount(List<Zugverbindung> verbindungen) {
        List<String> occurrences = new ArrayList<>();
        verbindungen.forEach(verbindung -> occurrences.addAll(verbindung.getStations()));
        return occurrences.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
    }

    private void reduceStations() {
        ArrayList<String> reducedStations = new ArrayList<>();
        logger.start(ConstantsLogging.REDUCE_STATIONS);

        for (String currentStation : this.stations) {
            ArrayList<Zugverbindung> stationConnections = new ArrayList<>();

            // alle Verbindungen des aktuellen Bahnhofs merken
            for (Zugverbindung v : this.connections) {
                if (v.getStations().contains(currentStation)) {
                    stationConnections.add(v);
                }
            }

            HashMap<String, Integer> commonStations = new HashMap<>();
            // ermitteln, ob diese Verbindungen auch eine weitere Station gemeinsam haben
            boolean doRemove = false;
            int maxCommonStations = 0;
            for (int i = 0; i < stationConnections.size(); i++) {
                for (String verbindungsStation : stationConnections.get(i).getStations()) {
                    if (verbindungsStation.equals(currentStation)) {
                        continue;
                    }
                    int newCount = 1;
                    if (commonStations.containsKey(verbindungsStation)) {
                        newCount = commonStations.get(verbindungsStation) + 1;
                    }
                    if (newCount == stationConnections.size()) { // station kommt genau so haeufig vor
                        doRemove = true;
                        break;
                    } else if (newCount > maxCommonStations) {
                        maxCommonStations = newCount;
                    }
                    commonStations.put(verbindungsStation, newCount);
                }
                if (maxCommonStations < i + 1) { // in einer Verbindung gab es keine gemeinsamen Stationen
                    break;
                };
            }

            if (doRemove) {
                reducedStations.add(currentStation);
                for (Zugverbindung verbindung : stationConnections) {
                    verbindung.removeStation(currentStation);
                }
            }
        }

        for (String station : reducedStations) {
            this.stations.remove(station);
        }
        logger.log(String.format("Bahnhoefe entfernt: (%d) %s", reducedStations.size(), reducedStations.size() > 0 ? reducedStations : ""));
        logger.stop(ConstantsLogging.REDUCE_STATIONS);
    }


    protected void reduceConnections() {
        List<Zugverbindung> reducedConnections = new ArrayList<>();
        logger.start(ConstantsLogging.REDUCE_CONNECTIONS);

        for (Zugverbindung v1 : connections) {
            for (Zugverbindung v2 : connections) {
                if (v1 == v2 || reducedConnections.contains(v1)) continue;

                if (v1.getStations().containsAll(v2.getStations())) {
                    reducedConnections.add(v1.getStations().size() > v2.getStations().size() ? v1 : v2);
                }
            }
        }

        connections.removeAll(reducedConnections);
        logger.log(String.format("Zugverbindungen entfernt: (%d) %s", reducedConnections.size(), reducedConnections.size() > 0 ? reducedConnections : ""));
        logger.stop(ConstantsLogging.REDUCE_CONNECTIONS);
    }

    private List<Zugverbindung> testConnectAndReturnFaulty(List<Zugverbindung> vList, Set<String> stations) {
        List<Zugverbindung> res = new ArrayList<>();
        for (Zugverbindung v : vList) {
            if (v.getStations().stream().noneMatch(stations::contains)) {
                res.add(v);
            }
        }
        return res;
    }

}
