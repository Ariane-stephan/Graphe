import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sommet implements Cloneable{
    private int id;
    private int degres;
    //   private List<Sommet> sommetsAdj;
    private Map<Integer, Sommet> sommetAdj2;
    private int idCLuster;


    public Sommet(int id){
        this.id = id;
        this.degres = 0;
        //     this.sommetsAdj = new ArrayList<Sommet>();
        this.sommetAdj2 = new HashMap<Integer, Sommet>();
    }

    public int getId() {
        return id;
    }

    public int getIdCLuster() {
        return idCLuster;
    }

    public void setIdCLuster(int idCLuster) {
        this.idCLuster = idCLuster;
    }
/*public List<Sommet> getSommetsAdj() {
       return sommetsAdj;
    }*/

    public Map<Integer, Sommet> getSommetAdj2() {
        return sommetAdj2;
    }

    public void setDegres(int degres) {
        this.degres = degres;
    }

    public int getDegres() {
        return degres;
    }

    public void afficherSommetsAdj() {


        for (Map.Entry mapentry : sommetAdj2.entrySet()) {
            Sommet tmp = (Sommet)(mapentry.getValue());
            System.out.print(tmp.getId()+" ");
        }
    }

  /*  @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }*/

}
