import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;


public class Partition implements Cloneable {

    private Map<Integer, Cluster> hashMapCluster;
    private Map<Integer, Cluster> hashMapClusterTrivial;

    private double[][] m;
    private PriorityQueue<Paire> tasMax;
    private double bestModularite;
    private Paire[][] matricePaire;


    public Partition() {
        hashMapCluster = new HashMap<Integer, Cluster>();
        hashMapClusterTrivial = new HashMap<Integer, Cluster>();

        tasMax = new PriorityQueue<Paire>(new Comparator<Paire>(){
            public int compare(Paire p1, Paire p2){
                return p2.compareTo(p1);
            }
        });



    }

    public Map<Integer, Cluster> getHashMapClusterTrivial() {
        return hashMapClusterTrivial;
    }

    public Paire[][] getMatricePaire() {
        return matricePaire;
    }

    public void setMatricePaire(Paire[][] matricePaire) {
        this.matricePaire = matricePaire;
    }

    public double getBestModularite() {
        return bestModularite;
    }

    public void setBestModularite(double bestModularite) {
        this.bestModularite = bestModularite;
    }

    public PriorityQueue<Paire> getTasMax() {
        return tasMax;
    }

    public Map<Integer, Cluster> getHashMapCluster() {
        return hashMapCluster;
    }

    public void setHashMapCluster(Map<Integer, Cluster> hashMap) {
        this.hashMapCluster = hashMap;
    }

    public double[][] getM() {
        return m;
    }

    public void setM(double[][] m) {
        this.m = m;
    }



    /*******copie un HashMap de Cluster*********/

   public Map<Integer, Cluster> copieHashMapCluster(){
        Map<Integer, Cluster> hashMapRes = new HashMap<>();
        Map<Integer, Sommet> hashMapSom = new HashMap<>();

        for (Map.Entry mapentry : hashMapCluster.entrySet()) {
            Cluster cluster = (Cluster) (mapentry.getValue());
            hashMapSom = new HashMap<>();
            for (Map.Entry mapentry2 : cluster.getHashMap().entrySet()) {
                Sommet sommet = (Sommet) (mapentry2.getValue());
                Sommet sommetNew = new Sommet(sommet.getId());
                hashMapSom.put(sommet.getId(), sommetNew);
            }
            Cluster clusterRes = new Cluster(cluster.getId());
            clusterRes.setHashMap(hashMapSom);
            hashMapRes.put(cluster.getId(), clusterRes);
        }
        return hashMapRes;
    }


}
