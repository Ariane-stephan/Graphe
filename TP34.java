import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.System.exit;

public class TP34 {


    public static void main(String[] args) {




        if (args.length < 1) {
            System.out.println("Erreur de syntaxe, vous devez renseigner le nom d'un fichier au format txt");
        } else {
            if (args[0].equals("modu")) {

                /*****construction du graphe******/

                Graphe g = new Graphe();
                construction_graphe(args[1], g);

                /*****construction de la partition à partir du fichier clu********/

                constructionPartition(args[2], g);

                /*****affichage de la modularite*******/

                System.out.println(g.partitionQ());

            }
            else if(args[0].equals("louvain")) {

                Graphe g = new Graphe();
                construction_graphe(args[1], g);

                g.partitionQ();
                g.louvain();
                g.write(args[2]);

                constructionPartition(args[2], g);
                System.out.println(g.partitionQ());


            }
            else if(args[0].equals("paire")) {
                Graphe g = new Graphe();
                construction_graphe(args[1], g);

                if(constructionPartition(args[2], g)) {


                    g.constructionTas();

                    Paire paire = g.getPartition().getTasMax().poll();
                    Cluster A = paire.getA();
                    Cluster B = paire.getB();

                    for (Map.Entry mapentry : A.getHashMap().entrySet()) {
                        Sommet tmp = (Sommet) (mapentry.getValue());
                        System.out.print(tmp.getId() + " ");
                    }

                    System.out.println();


                    for (Map.Entry mapentry : B.getHashMap().entrySet()) {
                        Sommet tmp = (Sommet) (mapentry.getValue());
                        System.out.print(tmp.getId() + " ");
                    }

                    System.out.println();
                    System.out.println("incrément de modularité " + g.incrementModularite(A, B));
                }

            }
            else
                System.out.println("commande inexistante");

        }

        System.out.println("Mémoire allouée : " +
                (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + " octets");
    }


    public static void construction_graphe(String fichier, Graphe graphe) { //fichier argument commande
        int nb1 = 0, nb2 = 0, compteurBoucle = 0, compteurDoublon = 0, compteurArete = 0;
        String ligne;
        BufferedReader br;
        int numSommetActuel = -1;
        int compteurClusterHashMap = 0;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fichier))));
            ligne = br.readLine();

            String tmp = ligne.substring(0, 1);
            while (tmp.equals("#")) { //tant qu'on est sur les lignes commençant par #
                ligne = br.readLine(); //passe à la ligne suivante
                tmp = ligne.substring(0, 1);

            }

            /***************************************/

            Sommet s1 = null, s2;
            while (ligne != null) { //tant qu'on a des lignes a lire dans le fichier

                List<Integer> sommets = Arrays.stream(ligne.split("\\s+")).map(Integer::parseInt).collect(Collectors.toList());

                nb1 = sommets.get(0); //on recupere le premier nombre
                nb2 = sommets.get(1); //on recupere le deuxieme nombre


                //on verifie si on commence une nouvelle serie (si le premier nombre est different du premier nombre de la ligne precedente)
                //pour ne pas rechercher le sommet 1 si on est toujours sur le meme

                if (numSommetActuel != nb1 && nb1 != nb2) {
                    if (graphe.getHashMap().containsKey(nb1)) {
                        s1 = graphe.getHashMap().get(nb1);
                    } else {
                        s1 = graphe.nouveauSommet(nb1);
                        Cluster c = new Cluster(compteurClusterHashMap);
                        c.getHashMap().put(nb1,s1);
                        graphe.getPartition().getHashMapCluster().put(compteurClusterHashMap,c);
                        graphe.getHashMap().get(nb1).setIdCLuster(compteurClusterHashMap);

                        compteurClusterHashMap++;

                    }
                }

                if (nb1 != nb2) { //on verifie si on est pas sur une boucle

                    if (graphe.getHashMap().containsKey(nb2)) {
                        s2 = graphe.getHashMap().get(nb2);
                    } else {
                        s2 = graphe.nouveauSommet(nb2);
                        Cluster c = new Cluster(compteurClusterHashMap);
                        c.getHashMap().put(nb2,s2);
                        graphe.getPartition().getHashMapCluster().put(compteurClusterHashMap,c);
                        graphe.getHashMap().get(nb2).setIdCLuster(compteurClusterHashMap);

                        compteurClusterHashMap++;
                    }

                    if (!(s1.getSommetAdj2().containsKey(s2.getId()))) { //on verifie si on est pas sur un doublon
                        s1.getSommetAdj2().put(s2.getId(), s2);
                        s1.setDegres(s1.getDegres() + 1);
                        s2.getSommetAdj2().put(s1.getId(), s1);
                        s2.setDegres(s2.getDegres() + 1);

                        compteurArete++;
                    } else
                        compteurDoublon++;

                } else
                    compteurBoucle++;


                ligne = br.readLine();
            }

            //   Sommet sommetDegreMax = graphe.sommetDegreMax();


            int[][] tableau = new int[graphe.getSommetMax()][graphe.getSommetMax()];
            graphe.setMatriceAdj(tableau);


            for (Map.Entry mapentry : graphe.getHashMap().entrySet()) {
                Sommet sommet = (Sommet) (mapentry.getValue());
                for (Map.Entry mapentry2 : sommet.getSommetAdj2().entrySet()) {
                    Sommet sommet2 = (Sommet) (mapentry2.getValue());
                    graphe.getMatriceAdj()[sommet.getId()][sommet2.getId()] = 1;
                    graphe.getMatriceAdj()[sommet2.getId()][sommet.getId()] = 1;

                }

            }
            graphe.getBestPartition().setHashMapCluster(graphe.getPartition().copieHashMapCluster());
            double[][] m = new double[compteurClusterHashMap][compteurClusterHashMap];
            graphe.getPartition().setM(m);
        //    graphe.setPartition(graphe.getPartition());

            graphe.constructionM();
            graphe.setNbrArete(compteurArete);
           // System.out.println("Voici nbr de sommets "+graphe.getNombreSommets());
        //    System.out.println("Voici nbr de double "+compteurDoublon);

            br.close();
        } catch (FileNotFoundException e) {
            //   e.printStackTrace();
            System.out.println("Erreur entree/sortie sur " + fichier);

        } catch (IOException e) {
            //  e.printStackTrace();
        }



    }

    /*****construction de la partition à partir du fichier clu**********/

    public static boolean constructionPartition(String fichier, Graphe graphe) {
        String ligne;
        BufferedReader br;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fichier))));
            ligne = br.readLine();
            int compteurSommet = 0;
            String[] tableauNombre;
            String tmp = ligne.substring(0, 1);



            int compteur = 0;
            while (ligne != null) { //tant qu'on a des lignes a lire dans le fichier
                tableauNombre = ligne.split("\\s+");

                int a = 0;
                Cluster cluster = new Cluster(compteur);
                for (int i = 0; i < tableauNombre.length; i++) {
                    int b = Integer.parseInt(tableauNombre[i]);
                    compteurSommet++;
                    if (graphe.getHashMap().containsKey(b)) {
                        cluster.getHashMap().put(b, graphe.getHashMap().get(b));
                        graphe.getHashMap().get(b).setIdCLuster(cluster.getId());
                    }
                    else {
                        System.out.println("erreur");
                        return false;
                    }
                }

                graphe.getPartition().getHashMapCluster().put(compteur, cluster);
                ligne = br.readLine();

                compteur++;

            }
            if(compteurSommet != graphe.getHashMap().size()){
                System.out.println("Erreur nombre sommets");
                return false;
            }
            double[][] m = new double[compteur][compteur];
            graphe.getPartition().setM(m);
        //    graphe.setPartition(graphe.getPartition());

            graphe.constructionM();
        } catch (FileNotFoundException e) {
            //   e.printStackTrace();
            System.out.println("Erreur entree/sortie sur " + fichier);
            return false;

        } catch (IOException e) {
            //  e.printStackTrace();
            return false;
        }

     try {
         graphe.getBestPartition().setHashMapCluster(graphe.getPartition().copieHashMapCluster());
     }catch (Exception e){

     }

        return true;

    }


}