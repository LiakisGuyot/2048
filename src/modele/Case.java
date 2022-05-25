package modele;

public class Case {
    private int valeur;

    private boolean fusionnable;

    public Case(int _valeur) {
        this.valeur = _valeur;
        this.fusionnable = true;
    }

    public int getValeur() {
        return this.valeur;
    }
    public void setValeur(int valeur) {this.valeur = valeur;}
    public boolean isFusionable() {return this.fusionnable;}
    public void setFusionnable(boolean fusionnable) {this.fusionnable = fusionnable;}

    public void Fuse() {
        this.valeur *= 2;
        this.fusionnable = false;
    }

    public boolean canIFuseWith(Case kase) {
        if (this.valeur == kase.getValeur() && kase.isFusionable() == true) {
            Fuse();

            return true;
        }
        return false;
    }
}
