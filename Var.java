import java.util.ArrayList;
import java.util.List;

public class Var {
    private List<Var> parents;
    private List<Var> enfants;
    private ArrayList<Double> tabProbabilite; // Déclaration d'un vecteur de probabilités

    boolean val;

    public Var(List<Var> vec) {
        parents = vec;
        enfants = new ArrayList<Var>();
        for (Var e : parents) {
            e.enfants.add(this);
        }
        tabProbabilite = new ArrayList<Double>();
    }

    // Méthode pour définir les probabilités de la table
    public void setP(double proba, Boolean... args) {
        if (args.length != parents.size()) {
            throw new IllegalArgumentException("Nombre d'arguments incorrect");
        }

        int intValue = 0;
        for (int i = 0; i < args.length; i++) {
            intValue += (args[i] ? 1 : 0) << (args.length - i - 1);
        }

        while (tabProbabilite.size() <= intValue)
            tabProbabilite.add(0.0);
        tabProbabilite.set(intValue, proba);
    }

    // Méthode pour accéder au probabilité de la table
    public double P(Boolean... args) {
        int intValue = 0;
        for (int i = 0; i < args.length; i++) {
            intValue += (args[i] ? 1 : 0) << (args.length - i - 1);
        }
        while (tabProbabilite.size() <= intValue)
            tabProbabilite.add(0.0);

        return tabProbabilite.get(intValue);
    }

    public List<Var> getParents() {
        return parents;
    }

    public List<Var> getEnfants() {
        return enfants;
    }

    public double likelihood_given_parents() {
        Boolean[] parentVals = new Boolean[parents.size()];
        for (int i = 0; i < parents.size(); i++) {
            parentVals[i] = parents.get(i).val;
        }
        return val ? P(parentVals) : 1 - P(parentVals);
    }
}
