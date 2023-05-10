import java.util.*;

public class Requette {

        public class Pair<K, V> {
                private K key;
                private V value;

                public Pair(K key, V value) {
                        this.key = key;
                        this.value = value;
                }

                public K getKey() {
                        return key;
                }

                public V getValue() {
                        return value;
                }

                public V setValue(V value) {
                        V old = this.value;
                        this.value = value;
                        return old;
                }
        }

        private List< Pair<Var, Boolean> > estimers;
        private List< Pair<Var, Boolean> > sachants;    

        public Requette() {
                estimers = new ArrayList< Pair<Var, Boolean> >();
                sachants = new ArrayList< Pair<Var, Boolean> >();
        }

        public void estimer(Var var, Boolean val) {
                estimers.add(new Pair<Var, Boolean>(var, val));
        }

        public void sachant(Var var, Boolean val) {
                sachants.add(new Pair<Var, Boolean>(var, val));
        }

        public List< Pair<Var, Boolean> > getEstimers() {
                return estimers;
        }

        public List< Pair<Var, Boolean> > getSachants() {
                return sachants;
        }
}
