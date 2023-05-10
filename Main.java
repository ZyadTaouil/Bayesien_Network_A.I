import java.util.*;

public class Main {
    public static void main(String[] args) {
        ReseauBayesien RB = new ReseauBayesien();

        Var tremblementTerre = RB.addVarBool(0.001);
        Var cambriolage = RB.addVarBool(0.002);
        Var alarme = RB.addVarBool(cambriolage, tremblementTerre);
        alarme.setP(0.95, true, true);
        alarme.setP(0.94, true, false);
        alarme.setP(0.29, false, true);
        alarme.setP(0.001, false, false);

        Var jeanAppelle = RB.addVarBool(alarme);
        jeanAppelle.setP(0.9, true);
        jeanAppelle.setP(0.05, false);

        Var marieAppelle = RB.addVarBool(alarme);
        marieAppelle.setP(0.7, true);
        marieAppelle.setP(0.01, false);

        System.out.println("P(Alarm = true | Cambriolage = true, TremblementTerre = true) = " + alarme.P(true, true));
        System.out.println("P(Alarm = true | Cambriolage = true, TremblementTerre = false) = " + alarme.P(true, false));
        System.out.println("P(Alarm = true | Cambriolage = false, TremblementTerre = false) = " + alarme.P(false, true));
        System.out.println("P(Alarm = true | Cambriolage = false, TremblementTerre = true) = " + alarme.P(false, false));


        Requette reqette = new Requette();
        reqette.estimer(cambriolage, true);
        reqette.sachant(marieAppelle, false);
        reqette.sachant(jeanAppelle, true);
        double resultApproche = RB.inferenceApproche(reqette, 1000);
        System.out.println("P(cambriolage = true | marieAppelle = false, jeanAppelle = true) = " + resultApproche);


        double resultGibbs = RB.inferenceGibbs(reqette, 1000);
        System.out.println("P(cambriolage = true | marieAppelle = false, jeanAppelle = true) = " + resultGibbs);

        double resultExacte = RB.inferenceExacte(reqette);
        System.out.println("P(cambriolage = true | marieAppelle = false, jeanAppelle = true) = " + resultExacte);

        /*
        double valApproche = RB.inferenceApproche(
                new HashMap<Var, Boolean>() {{put(cambriolage, true);}},
                new HashMap<Boolean, Boolean>() {{put(jeanAppelle, true); put(marieAppelle, false);}},
                1000);
        System.out.println("valApproche = " + valApproche);

        double valGibbs = RB.inferenceGibbs(
                new HashMap<Boolean, Boolean>() {{put(cambriolage, true);}},
                new HashMap<Boolean, Boolean>() {{put(jeanAppelle, true); put(marieAppelle, false);}},
                1000);
        System.out.println("valGibbs = " + valGibbs);

        double valExact = RB.inferenceExacte(
                new HashMap<Boolean, Boolean>() {{put(cambriolage, true);}},
                new HashMap<Boolean, Boolean>() {{put(jeanAppelle, true); put(marieAppelle, false);}});
        System.out.println("valExact = " + valExact);
         */
    }
}
