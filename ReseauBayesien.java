import java.util.*;

public class ReseauBayesien {
    private List<Var> variables;

    public ReseauBayesien() {
        variables = new ArrayList<Var>();
    }

    public Var addVarBool(double p) {
        Var var = new Var(new ArrayList<Var>());
        var.setP(p);
        variables.add(var);
        return var;
    }

    public Var addVarBool(Var... args) {
        List<Var> parent = new ArrayList<Var>();
        for (Var arg : args) {
            parent.add(arg);
        }
        Var var = new Var(parent);
        variables.add(var);
        return var;
    }

    public double inferenceApproche(Requette req, int nbIter) {
        int count = 0;
        int evidenceCount = 0;
        Random rand = new Random();

        for (int i = 0; i < nbIter; i++) {
            boolean[] state = new boolean[variables.size()];

            for (Var var : variables) {
                int index = variables.indexOf(var);
                Boolean[] parentState = new Boolean[var.getParents().size()];
                for (int j = 0; j < parentState.length; j++) {
                    parentState[j] = state[variables.indexOf(var.getParents().get(j))];
                }
                double proba = var.P(parentState);
                if (rand.nextDouble() <= proba) {
                    state[index] = true;
                } else {
                    state[index] = false;
                }
            }

            boolean evidenceMatch = true;
            for (Requette.Pair<Var, Boolean> pair : req.getSachants()) {
                if (state[variables.indexOf(pair.getKey())] != pair.getValue()) {
                    evidenceMatch = false;
                    break;
                }
            }

            if (evidenceMatch) {
                evidenceCount++;
                boolean queryMatch = true;
                for (Requette.Pair<Var, Boolean> pair : req.getEstimers()) {
                    if (state[variables.indexOf(pair.getKey())] != pair.getValue()) {
                        queryMatch = false;
                        break;
                    }
                }
                if (queryMatch) {
                    count++;
                }
            }
        }

        return (double) count / evidenceCount;
    }

    public double inferenceGibbs(Requette req, int nbIter) {
        Map<Var, Boolean> values = new HashMap<>();
        Random random = new Random();

        for (Var var : variables) {
            values.put(var, random.nextBoolean());
        }

        for (Requette.Pair<Var, Boolean> evidence : req.getSachants()) {
            values.put(evidence.getKey(), evidence.getValue());
        }

        int count = 0;

        for (int i = 0; i < nbIter; i++) {
            Var selectedVar;
            do {
                selectedVar = variables.get(random.nextInt(variables.size()));
            } while (isEvidence(req, selectedVar));

            List<Boolean> parentValues = new ArrayList<>();
            for (Var parent : selectedVar.getParents()) {
                parentValues.add(values.get(parent));
            }
            double prob = selectedVar.P(parentValues.toArray(new Boolean[0]));

            values.put(selectedVar, random.nextDouble() < prob);

            boolean match = true;
            for (Requette.Pair<Var, Boolean> estimer : req.getEstimers()) {
                if (!values.get(estimer.getKey()).equals(estimer.getValue())) {
                    match = false;
                    break;
                }
            }

            if (match) {
                count++;
            }
        }

        return (double) count / nbIter;
    }

    private boolean isEvidence(Requette req, Var var) {
        for (Requette.Pair<Var, Boolean> evidence : req.getSachants()) {
            if (evidence.getKey().equals(var)) {
                return true;
            }
        }
        return false;
    }

    public double inferenceExacte(Requette req) {
        for (Requette.Pair<Var, Boolean> evidence : req.getSachants()) {
            evidence.getKey().val = evidence.getValue();
        }

        Requette.Pair<Var, Boolean> query = req.getEstimers().get(0);
        query.getKey().val = query.getValue();

        return enumerateAll(variables, req) / (enumerateAll(variables, req) + enumerateAll(variables, req, query.getKey()));
    }

    private double enumerateAll(List<Var> vars, Requette req) {
        if (vars.isEmpty()) {
            return getFullProba();
        }

        Var firstVar = vars.get(0);
        List<Var> remainingVars = vars.subList(1, vars.size());

        if (isInSachants(firstVar, req)) {
            return enumerateAll(remainingVars, req);
        } else {
            firstVar.val = true;
            double trueProb = enumerateAll(remainingVars, req);

            firstVar.val = false;
            double falseProb = enumerateAll(remainingVars, req);

            return trueProb + falseProb;
        }
    }

    private double enumerateAll(List<Var> vars, Requette req, Var excludedVar) {
        if (vars.isEmpty()) {
            return getFullProba();
        }

        Var firstVar = vars.get(0);
        List<Var> remainingVars = vars.subList(1, vars.size());

        if (firstVar == excludedVar || isInSachants(firstVar, req)) {
            return enumerateAll(remainingVars, req, excludedVar);
        } else {
            firstVar.val = true;
            double trueProb = enumerateAll(remainingVars, req, excludedVar);

            firstVar.val = false;
            double falseProb = enumerateAll(remainingVars, req, excludedVar);

            return trueProb + falseProb;
        }
    }

    private boolean isInSachants(Var var, Requette req) {
        for (Requette.Pair<Var, Boolean> sachant : req.getSachants()) {
            if (sachant.getKey() == var) {
                return true;
            }
        }
        return false;
    }

    private double getFullProba() {
        double prob = 1.0;
        for (Var var : variables) {
            prob *= var.likelihood_given_parents();
        }
        return prob;
    }

}