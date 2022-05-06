package modele;

public class Case {
    private int valeur;

    private boolean fusionnable;

    public Case(int _valeur) {
        valeur = _valeur;
        fusionnable = true;
    }

    public int getValeur() {
        return valeur;
    }
    public void setValeur(int valeur) {this.valeur = valeur;}
    public boolean isFusionable() {return fusionnable;}
    public void setFusionnable(boolean fusionnable) {this.fusionnable = fusionnable;}

    public void Fuse() {
        valeur *= 2;
    }

}
