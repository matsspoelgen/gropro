package logic;

import ioHandling.logger.Logger;
import model.Zugverbindung;

import java.util.ArrayList;

public class VerbindungsReduktion implements Datenreduktionstechnik {

    private Streckennetz netz;
    private Logger logger;

    public VerbindungsReduktion(Streckennetz netz) {
//        this.logger = Logger.getInstance();
//        this.netz = netz;
    }

    @Override
    public void reduce() {
//        ArrayList<Zugverbindung> verbindungen = netz.getVerbindungen();
//
//        for (int i = 0; i < verbindungen.size(); i++) {
//            for (int j = 0; j < verbindungen.size(); j++) {
//                if(i == j){
//                    continue;
//                }
//
//                if(verbindungen.get(i).getStationen().containsAll(verbindungen.get(j).getStationen())){
//                    logger.log(String.format("Entferne Verbindung %s", verbindungen.get(i)));
//                    verbindungen.remove(i);
//                    --i;
//                    break;
//                }
//            }
//        }
//
//        netz.setVerbindungen(verbindungen);
    }

}