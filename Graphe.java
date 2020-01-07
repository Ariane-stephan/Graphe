import jdk.management.cmm.SystemResourcePressureMXBean;

import javax.swing.plaf.synth.SynthEditorPaneUI;
import javax.swing.text.html.HTMLDocument;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Graphe {

    private int nombreSommets;

    private Map<Integer, Sommet> hashMap;
    private int sommetMax;
    private int[][] matriceAdj;
    private Partition partition;
    private Partition bestPartition;

    private double nbrArete;


    public Graphe() {
        nombreSommets = 0;
        hashMap = new HashMap<Integer, Sommet>();
        sommetMax = 0;
        partition = new Partition();
        bestPartition = new Partition();
        nbrArete = 0;

    }

    public double getNbrArete() {
        return nbrArete;
    }


    public Partition getBestPartition() {
        return bestPartition;
    }

    public void setBestPartition(Partition bestPartition) throws CloneNotSupportedException {

        this.bestPartition = bestPartition;
    }

    public void setNbrArete(int nbrArete) {
        this.nbrArete = nbrArete;
    }

    public Partition getPartition() {
        return partition;
    }

    public void setPartition(Partition partition) {
        this.partition = partition;
    }

    public Map<Integer, Sommet> getHashMap() {
        return hashMap;
    }

    public int getSommetMax() {
        return sommetMax;
    }

    public void setSommetMax(int sommetMax) {
        this.sommetMax = sommetMax;
    }

    public int getNombreSommets() {
        return nombreSommets;
    }

    public void setNombreSommets(int nombreSommets) {
        this.nombreSommets = nombreSommets;
    }


    public int[][] getMatriceAdj() {
        return matriceAdj;
    }

    public void setMatriceAdj(int[][] matriceAdj) {
        this.matriceAdj = matriceAdj;
    }

    public Sommet sommetDegreMax() {
        Sommet tmp;
        Sommet res = null;
        Map.Entry<Integer, Sommet> entry = hashMap.entrySet().iterator().next();

        int max = entry.getKey();

        for (Map.Entry mapentry : hashMap.entrySet()) {

            tmp = (Sommet) (mapentry.getValue());
            if (max < tmp.getDegres() || (max == tmp.getDegres() && tmp.getId() < res.getId())) {
                res = tmp;
                max = tmp.getDegres();
            }

        }
        return res;
    }

    public int[] distributionDegre() {
        int a = 0;
        System.out.println("Deg max : " + sommetDegreMax().getDegres());
        int[] res = new int[sommetDegreMax().getDegres() + 1];
        for (Map.Entry mapentry : hashMap.entrySet()) {
            Sommet tmp = (Sommet) (mapentry.getValue());
            res[tmp.getDegres()]++;
            a++;
        }
        res[0] = sommetMax - a + 1;

        return res;
    }


    public Sommet nouveauSommet(int nb) {
        if (nb >= sommetMax)
            sommetMax = nb + 1; //sommet 0
        Sommet s1 = new Sommet(nb);
        getHashMap().put(nb, s1);
        setNombreSommets(getNombreSommets() + 1);

        if (getSommetMax() < nb)
            setSommetMax(nb);

        return s1;
    }

    public void affichage() {
        //System.out.println(compteurDoublon + " doublons ont ete supprimes");
        System.out.println("Nombre de sommets : " + sommetMax);

        //  System.out.println("Nombre d’aretes : " + compteurArete);
        //System.out.println("Sommet de degré max (de numéro minimal) : " + sommetDegreMax.getId());

        System.out.println("Sa liste d’adjacence (ligne suivante) :");
        //sommetDegreMax.afficherSommetsAdj();

        System.out.println();
        System.out.println("Ditribution des degrés : ");
        int[] res = distributionDegre();
        for (int i = 0; i < res.length; i++)
            System.out.println(i + " : " + res[i]);
    }

    public int min(int a, int b) {
        if (a <= b)
            return a;
        return b;
    }

    public int max(int a, int b) {
        if (a < b)
            return b;
        return a;
    }

    public double max(double a, double b) {
        if (a < b)
            return b;
        return a;
    }

    public void constructionM() {
        for (int i = 0; i < partition.getM().length; i++) {
            for (int j = 0; j < i; j++) {
                partition.getM()[i][j] = 0;

            }
        }
        for (int i = 0; i < matriceAdj.length; i++) {
            for (int j = 0; j < i; j++) {

                if (matriceAdj[i][j] == 1) {
                    partition.getM()[max(hashMap.get(i).getIdCLuster(), hashMap.get(j).getIdCLuster())][min(hashMap.get(i).getIdCLuster(), hashMap.get(j).getIdCLuster())]++;
                }
            }
        }
    }

    public double partitionQ() {

        double res = 0;
        double a = 0;
        double b = 0;

        int g = 0;
        for (int i = 0; i < partition.getM().length; i++) {

            if (partition.getM()[i][i] >= 0) {
                b = 0;
                a = partition.getM()[i][i] / nbrArete;
                for (Map.Entry mapentry : partition.getHashMapCluster().get(i).getHashMap().entrySet()) {
                    g++;

                    Sommet tmp = (Sommet) (mapentry.getValue());
                    b += tmp.getDegres();
                }
                b = Math.pow(b, 2) / (4 * Math.pow(nbrArete, 2));
                res += a - b;
            }
        }
        return res;
    }


    public double incrementModularite(Cluster a, Cluster b) {


        double res = partition.getM()[max(a.getId(), b.getId())][min(a.getId(), b.getId())] / nbrArete;
        double degreA = a.degre();
        double degreB = b.degre();
        res -= (Math.pow(degreA + degreB, 2)) / (4 * Math.pow(nbrArete, 2));

        res += Math.pow(degreA, 2) / (4 * Math.pow(nbrArete, 2));
        res += Math.pow(degreB, 2) / (4 * Math.pow(nbrArete, 2));
        return res;

    }


    public void constructionTas() {
        Paire[][] tableau = new Paire[partition.getHashMapCluster().size()][partition.getHashMapCluster().size()];
        partition.setMatricePaire(tableau);
        for (int i = 0; i < partition.getM().length; i++) {
            for (int j = 0; j < i; j++) {

                Paire paire = new Paire(partition.getHashMapCluster().get(i), partition.getHashMapCluster().get(j), incrementModularite(partition.getHashMapCluster().get(i), partition.getHashMapCluster().get(j)));
                partition.getTasMax().add(paire);
                partition.getMatricePaire()[max(partition.getHashMapCluster().get(i).getId(), partition.getHashMapCluster().get(j).getId())][min(partition.getHashMapCluster().get(i).getId(), partition.getHashMapCluster().get(j).getId())] = paire;
            }
        }


    }


    /*****fusion de deux clusters*******/

    public Cluster fusion(Paire paire) {

        /*****on recupere les deux clusters de la paire*******/

        int max = max(paire.getA().getId(), paire.getB().getId());

        int min = min(paire.getA().getId(), paire.getB().getId());

        Cluster clusterMax = partition.getHashMapCluster().get(max);
        Cluster clusterMin = partition.getHashMapCluster().get(min);


        /*****on rend inutilisable les clusters au lieu de les remove**********/

        /******on met aussi à jour la matrice M*****/

        for (int j = 0; j <= min; j++) { // ligne min

            partition.getM()[min][j] = partition.getM()[max][j] + partition.getM()[min][j];

            if (partition.getMatricePaire()[min][j] != null)
                partition.getMatricePaire()[min][j].setUtillisable(false);

            partition.getMatricePaire()[min][j] = null;

        }


        for (int j = min; j < partition.getM().length; j++) { // colonne min

            if (partition.getMatricePaire()[j][min] != null) {
                partition.getMatricePaire()[j][min].setUtillisable(false);
                partition.getMatricePaire()[j][min] = null;
            }
            partition.getM()[max(j, min)][min(min, j)] = partition.getM()[j][min] + partition.getM()[max(j, max)][min(j, max)];

        }



        for (int j = 0; j <= max; j++) { //ligne max

            if (partition.getMatricePaire()[max][j] != null) {

                partition.getMatricePaire()[max][j].setUtillisable(false);
                partition.getMatricePaire()[max][j] = null;
            }
            partition.getM()[max][j] = -1;

        }


        partition.getM()[max][min] = -1;
        if (partition.getMatricePaire()[min][max] != null) {
            partition.getMatricePaire()[min][max].setUtillisable(false);
            partition.getMatricePaire()[min][max] = null;
        }
        for (int j = max; j < partition.getM().length; j++) { //colonne max

            if (partition.getMatricePaire()[j][max] != null) {
                partition.getMatricePaire()[j][max].setUtillisable(false);
                partition.getMatricePaire()[j][max] = null;
            }
            partition.getM()[j][max] = -1;

        }


        clusterMin.getHashMap().putAll(clusterMax.getHashMap());


        paire.setUtillisable(false);


        partition.getHashMapCluster().remove(max);


        return clusterMin;

    }


    /*****Mise à jour de notre TAS****/

    public void MiseAJourTas() {

        /****On enleve toutes les paires non utilisable du tas*********/

        while (partition.getTasMax().size() > 0 && !partition.getTasMax().peek().isUtillisable()) { // on enleve tous les non utilisatble en haut du tas
            partition.getTasMax().poll();
        }

        /*****si apres ca notre tas n'est pas vide*********/

        if (partition.getTasMax().size() > 0) {

            /****on fait la fusion de notre paire******/

            int idNewCluster = fusion(partition.getTasMax().peek()).getId();


            if (partition.getTasMax().peek().getModularite() < 0.001) {
            }
            else {

                /****si la modularite augmente avec cette fusion, on enregistre cette partition*****/

                bestPartition.setHashMapCluster(partition.copieHashMapCluster());
            }
            partition.getTasMax().poll();


            for (int j = 0; j < partition.getHashMapCluster().size(); j++) {

                /****on rajoute toutes les nouvelles paires******/

                if (partition.getM()[max(j, idNewCluster)][min(j, idNewCluster)] >= 0 && partition.getM()[min(j, idNewCluster)][max(j, idNewCluster)] >= 0 && j != idNewCluster) {
                    double incrementalite = incrementModularite(partition.getHashMapCluster().get(idNewCluster), partition.getHashMapCluster().get(j));
                    if (incrementalite > 0) {
                        Paire paire = new Paire(partition.getHashMapCluster().get(idNewCluster), partition.getHashMapCluster().get(j), incrementalite);
                        partition.getTasMax().add(paire);
                        partition.getMatricePaire()[max(paire.getA().getId(), paire.getB().getId())][min(paire.getA().getId(), paire.getB().getId())] = paire;
                    }
                }
            }
        }
    }


    public void louvain() {

        partition.setBestModularite(partitionQ());

        ///   int compteur = 0;

        /***construction du TAS*****/

        constructionTas();

        /*****on met à jour le tas tant que le tas n'est pas vide, et tant qu'on a des clusters a fusionner******/

        while (partition.getHashMapCluster().size() > 1 && partition.getTasMax().size() > 0) {


            MiseAJourTas();

            //   System.out.println("Apres " + compteur);
            //      compteur++;

        }
    }


    /****methode qui ecrit dans un fichier les Cluster*********/

    public boolean write(String cheminFichier) {

        FileOutputStream fos = null;

        File fichier = new File(cheminFichier);
        fichier.delete();

        try {
            fos = new FileOutputStream(new File(cheminFichier));


        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();

        }
        int compteur = 0;
        String tmp = "";
        int j = 0;
        try {
            for (Map.Entry mapentry : bestPartition.getHashMapCluster().entrySet()) {
                Cluster cluster = (Cluster) mapentry.getValue();
                for (Map.Entry mapentry2 : cluster.getHashMap().entrySet()) {
                    compteur++;


                    Sommet sommet = (Sommet) (mapentry2.getValue());

                    tmp = sommet.getId() + "";
                    j = 0;
                    while (j < tmp.length()) {
                        fos.write(tmp.charAt(j));
                        j++;
                    }
                    fos.write(' ');
                }
                fos.write('\n');
            }
        } catch (Exception e) {

        }


        return true;
    }


}