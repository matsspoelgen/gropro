package logic;

import ioHandling.logger.ConstantsLogging;
import ioHandling.logger.Logger;
import model.Bahnhof;
import model.Zugverbindung;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Streckennetz {

    private ArrayList<Zugverbindung> connections;
    private HashMap<String, Bahnhof> stations;
    private Logger logger;

    public Streckennetz(ArrayList<Zugverbindung> verbindungen) {
        this.logger = Logger.getInstance();
        this.connections = verbindungen;
        this.stations = getStations();
    }

    // TODO auslagern in einlesen
    private HashMap<String, Bahnhof> getStations() {
        HashMap<String, Bahnhof> stations = new HashMap<>();
        for (Zugverbindung connection : this.connections) {
            for (String station : connection.getStations()) {
                if (!stations.containsKey(station)) {
                    stations.put(station, new Bahnhof(station));
                }
            }
        }
        return stations;
    }

    public HashSet<String> getMinStations() {
        reduceConnections();

        // ordne allen Stationen die reduzierten Zugverbindungen zu
        for (int i = 0; i < this.connections.size(); i++) {
            for (String station : this.connections.get(i).getStations()) {
                this.stations.get(station).addConnection(i);
            }
        }

        reduceStations();


        logger.start(ConstantsLogging.MIN_STATIONS);

        HashSet<String> res = new HashSet<>();
        List<Zugverbindung> current = new ArrayList<>(connections);  //copy

        while (current.size() != 0) {
            Map<String, Long> bahnhofCount = getCount(current);  // wie oft kommt jede Station in allen Verbindungen vor
            String max = Collections.max(bahnhofCount.entrySet(), Map.Entry.comparingByValue()).getKey(); // meiste Verbindungen
            res.add(max); // max als erste Servicestation

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

    public HashSet<String> getMinStationsTS() {
        reduceConnections();

        // ordne allen Stationen die reduzierten Zugverbindungen zu
        for (int i = 0; i < this.connections.size(); i++) {
            for (String station : this.connections.get(i).getStations()) {
                this.stations.get(station).addConnection(i);
            }
        }

        reduceStations();

        System.out.println(this.stations.size());
        System.out.println(this.connections.size());

        System.out.println(this.connections);
        System.out.println(this.stations);

        HashSet<String> serviceStations = new HashSet<>();
        this.remainingStations = new HashSet<>(this.stations.keySet());
        this.remainingConnections = new HashSet<>(IntStream.range(0, this.connections.size()).boxed().toList());

        getMinStationsRek(new HashSet<>(), serviceStations);

        return serviceStations;
    }

    private HashSet<Bahnhof> removeConnection(int connection) {
        HashSet<Bahnhof> ret = new HashSet<>();
//        for (String station :
//                ) {
//
//        }
        return ret;
    }

    private HashSet<Integer> remainingConnections = new HashSet<>();
    private HashSet<String> remainingStations = new HashSet<>();

    public void getMinStationsRek(HashSet<String> current, HashSet<String> shortest) {

        if (shortest.size() != 0 && current.size() >= shortest.size()) {                    // schlechter, als bisher beste Loesung
            return;
        }

        if (this.remainingConnections.isEmpty()) {                                          // alle Verbindungen mit weniger Stationen abgedeckt
            shortest.clear();
            shortest.addAll(current);
            System.out.println("solution " + shortest);
            return;
        }

        for (String station : remainingStations) {

            HashSet<Integer> originalConnections = new HashSet<>(remainingConnections);

            remainingConnections.removeAll(this.stations.get(station).getConnections());

            HashSet<String> originalStations = new HashSet<>(remainingStations);

            remainingStations = new HashSet<>();
            for (int cIndex : remainingConnections) {
                remainingStations.addAll(this.connections.get(cIndex).getStations());
            }

            current.add(station);
            getMinStationsRek(current, shortest);
            remainingStations = originalStations;
            remainingConnections = originalConnections;
            current.remove(station);


        }
    }


    private Map<String, Long> getCount(List<Zugverbindung> verbindungen) {
        List<String> occurrences = new ArrayList<>();
        verbindungen.forEach(verbindung -> occurrences.addAll(verbindung.getStations()));
        return occurrences.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
    }

    private void reduceStations() {
        HashSet<String> reducedStations = new HashSet<>();

        logger.start(ConstantsLogging.REDUCE_STATIONS);

        for (String keyA : this.stations.keySet()) {
            for (String keyB : this.stations.keySet()) {
                if (keyA.equals(keyB) || reducedStations.contains(keyA) || reducedStations.contains(keyB)) {
                    continue;
                }

                HashSet<Integer> connectionsA = this.stations.get(keyA).getConnections();
                HashSet<Integer> connectionsB = this.stations.get(keyB).getConnections();

                if (connectionsB.containsAll(connectionsA)) {
                    reducedStations.add(keyA);
                    for (Zugverbindung connection : this.connections) {
                        connection.removeStation(keyA);
                    }
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
