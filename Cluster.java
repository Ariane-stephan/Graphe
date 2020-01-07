import java.util.HashMap;
import java.util.Map;

public class Cluster implements Cloneable{

    private int id;
    private Map<Integer, Sommet> hashMap;

    public Cluster(int id) {
        this.id = id;
        hashMap = new HashMap<Integer, Sommet>();
    }

    public int getId() {
        return id;
    }

    public Map<Integer, Sommet> getHashMap() {
        return hashMap;
    }

    public void setHashMap(Map<Integer, Sommet> hashMap) {
        this.hashMap = hashMap;

    }

    public double degre() {
        double res = 0.0;
        for (Map.Entry mapentry : hashMap.entrySet()) {
            Sommet sommet = (Sommet) (mapentry.getValue());
            res += sommet.getDegres();
        }
        return res;
    }



}
