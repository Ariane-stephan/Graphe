public class Paire implements Comparable<Paire> {

    private Cluster a;
    private Cluster b;
    private double incrementationModulaire;
    private boolean utillisable;

    public Paire(Cluster a, Cluster b, double modularite) {
        this.a = a;
        this.b = b;
        this.incrementationModulaire = modularite;
        this.utillisable = true;
    }

    public Cluster getA() {
        return a;
    }

    public void setA(Cluster a) {
        this.a = a;
    }

    public Cluster getB() {
        return b;
    }

    public void setB(Cluster b) {
        this.b = b;
    }

    public double getModularite() {
        return incrementationModulaire;
    }

    public void setModularite(double modularite) {
        this.incrementationModulaire = modularite;
    }

    public boolean isUtillisable() {
        return utillisable;
    }

    public void setUtillisable(boolean utillisable) {
        this.utillisable = utillisable;
    }

    @Override
    public int compareTo(Paire paire) {
        if (this.incrementationModulaire < paire.incrementationModulaire)
            return -1;
        else if (this.incrementationModulaire > paire.incrementationModulaire)
            return 1;
        else return 0;
    }
}
