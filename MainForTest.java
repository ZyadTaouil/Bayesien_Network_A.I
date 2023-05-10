public class MainForTest {
        
        public static void main(String[] args) {
                Boolean ok=true;
                {
                        System.out.println("Test 1");
                        ReseauBayesien RB = new ReseauBayesien();

                        Var we = RB.addVarBool( 0.3 );
                        Var pluie = RB.addVarBool( 0.1 );

                        Var trafic = RB.addVarBool( we, pluie );
                        trafic.setP(0.5, false, false);
                        trafic.setP(0.9, false, true);
                        trafic.setP(0.01, true, false);
                        trafic.setP(0.1, true, true);

                        Var parapluie = RB.addVarBool( pluie );
                        parapluie.setP(0.9, true);
                        parapluie.setP(0.01, false);


                        Requette reqette = new Requette();
                        reqette.estimer(trafic, true);
                        reqette.sachant(parapluie, true);


                        double valApproche = RB.inferenceApproche(reqette, 10000);
                        System.out.println("valApproche = " + valApproche);

                        if(Math.abs(valApproche - 0.632091) > 0.06) {
                                System.out.println("ERREUR inferenceApproche: valeur attendu = " + 0.604545 + " ± 0.06");
                                ok = false;
                        }

                        double valGibbs = RB.inferenceGibbs(reqette, 10000);
                        System.out.println("valGibbs = " + valGibbs);

                        if(Math.abs(valGibbs - 0.632091) > 0.06) {
                                System.out.println("ERREUR inferenceGibbs: valeur attendu = " + 0.604545 + " ± 0.06");
                                ok = false;
                        }

                        double valExact = RB.inferenceExacte(reqette);
                        System.out.println("valExact = " + valExact);

                        if(Math.abs(valExact - 0.632091) > 0.001) {
                                System.out.println("ERREUR inferenceExacte: valeur attendu = " + 0.604545);
                                ok = false;
                        }
                }
        
                {
                        System.out.println("Test 2");
                        ReseauBayesien RB = new ReseauBayesien();

                        Var we = RB.addVarBool( 0.3 );
                        Var pluie = RB.addVarBool( 0.1 );

                        Var trafic = RB.addVarBool( we, pluie );
                        trafic.setP(0.5, false, false);
                        trafic.setP(0.9, false, true);
                        trafic.setP(0.01, true, false);
                        trafic.setP(0.1, true, true);

                        Var parapluie = RB.addVarBool( pluie );
                        parapluie.setP(0.9, true);
                        parapluie.setP(0.01, false);


                        Requette reqette = new Requette();
                        reqette.estimer(trafic, true);
                        reqette.estimer(we, false);
                        reqette.sachant(parapluie, true);

        
                        double valApproche = RB.inferenceApproche(reqette, 10000);
                        System.out.println("valApproche = " + valApproche);

                        if(Math.abs(valApproche - 0.604545) > 0.06) {
                                System.out.println("ERREUR inferenceApproche: valeur attendu = " + 0.604545 + " ± 0.06");
                                ok = false;
                        }

                        double valGibbs = RB.inferenceGibbs(reqette, 10000);
                        System.out.println("valGibbs = " + valGibbs);

                        if(Math.abs(valGibbs - 0.604545) > 0.06) {
                                System.out.println("ERREUR inferenceGibbs: valeur attendu = " + 0.604545 + " ± 0.06");
                                ok = false;
                        }

                        double valExact = RB.inferenceExacte(reqette);
                        System.out.println("valExact = " + valExact);

                        if(Math.abs(valExact - 0.604545) > 0.001) {
                                System.out.println("ERREUR inferenceExacte: valeur attendu = " + 0.604545);
                                ok = false;
                        }
                }
        
                {
                        System.out.println("Test 3");
                        ReseauBayesien RB = new ReseauBayesien();

                        Var tremblementTerre = RB.addVarBool( 0.001 );
                        Var cambriolage = RB.addVarBool( 0.002 );
                        Var alarme = RB.addVarBool( cambriolage, tremblementTerre);
                        alarme.setP(0.95, true, true);
                        alarme.setP(0.94, true, false);
                        alarme.setP(0.29, false, true);
                        alarme.setP(0.001, false, false);

                        Var jeanAppelle = RB.addVarBool( alarme );
                        jeanAppelle.setP(0.9, true);
                        jeanAppelle.setP(0.05, false);

                        Var marieAppelle = RB.addVarBool( alarme );
                        marieAppelle.setP(0.7, true);
                        marieAppelle.setP(0.01, false);


                        Requette reqette = new Requette();
                        reqette.estimer(cambriolage, true);
                        reqette.sachant(jeanAppelle, true);
                        reqette.sachant(marieAppelle, false);

                        double valApproche = RB.inferenceApproche(reqette, 10000);
                        System.out.println("valApproche = " + valApproche);
                        if(Math.abs(valApproche - 0.0102303) > 0.004) {
                                System.out.println("ERREUR inferenceApproche: valeur attendu = " + 0.0102303 + " ± 0.004");
                                ok = false;
                        }

                        double valGibbs = RB.inferenceGibbs(reqette, 100000);
                        System.out.println("valGibbs = " + valGibbs);
                        if(Math.abs(valGibbs - 0.0102303) > 0.01) {
                                System.out.println("ERREUR inferenceGibbs: valeur attendu = " + 0.0102303 + " ± 0.01");
                                ok = false;
                        }


                        double valExact = RB.inferenceExacte(reqette);
                        System.out.println("valExact = " + valExact);

                        if(Math.abs(valExact - 0.0102303) > 0.001) {
                                System.out.println("ERREUR inferenceExacte: valeur attendu = " + 0.0102303);
                                ok = false;
                        }
                }
        
                {
                        System.out.println("Test 4");
                        ReseauBayesien RB = new ReseauBayesien();

                        Var tremblementTerre = RB.addVarBool( 0.001 );
                        Var cambriolage = RB.addVarBool( 0.002 );
                        Var alarme = RB.addVarBool( cambriolage, tremblementTerre);
                        alarme.setP(0.95, true, true);
                        alarme.setP(0.94, true, false);
                        alarme.setP(0.29, false, true);
                        alarme.setP(0.001, false, false);

                        Var jeanAppelle = RB.addVarBool( alarme );
                        jeanAppelle.setP(0.9, true);
                        jeanAppelle.setP(0.05, false);

                        Var marieAppelle = RB.addVarBool( alarme );
                        marieAppelle.setP(0.7, true);
                        marieAppelle.setP(0.01, false);


                        Requette reqette = new Requette();
                        reqette.estimer(cambriolage, false);
                        reqette.estimer(alarme, true);
                        reqette.sachant(jeanAppelle, true);
                        reqette.sachant(marieAppelle, true);

                        double valApproche = RB.inferenceApproche(reqette, 10000);
                        System.out.println("valApproche = " + valApproche);
                        if(Math.abs(valApproche - 0.325053) > 0.01) {
                                System.out.println("ERREUR inferenceApproche: valeur attendu = " + 0.325053 + " ± 0.01");
                                ok = false;
                        }

                        double valGibbs = RB.inferenceGibbs(reqette, 100000);
                        System.out.println("valGibbs = " + valGibbs);
                        if(Math.abs(valGibbs - 0.325053) > 0.01) {
                                System.out.println("ERREUR inferenceGibbs: valeur attendu = " + 0.325053 + " ± 0.01");
                                ok = false;
                        }


                        double valExact = RB.inferenceExacte(reqette);
                        System.out.println("valExact = " + valExact);

                        if(Math.abs(valExact - 0.325053) > 0.001) {
                                System.out.println("ERREUR inferenceExacte: valeur attendu = " + 0.325053);
                                ok = false;
                        }
                }
        
                if(ok) {
                        System.out.println("Vos algorithmes semblent fonctionner :)");
                }
                
        }
}
