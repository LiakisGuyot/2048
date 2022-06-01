package modele;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Observable;
import java.util.Random;

public class Jeu extends Observable {

    private Case[][] tabCases;
//Pour suppresion case faire un tabCases = null et removede la HashMap
    private static Random rnd = new Random(3918);
    private HashMap<Case, Point> map = new HashMap<Case, Point>();

    public Jeu(int size) {
        tabCases = new Case[size][size];
        map = new HashMap<Case, Point>();
        rnd();
    }
    //region Initialisation

    //Créer un tableau de jeu
    public void rnd() {
        new Thread() { // permet de libérer le processus graphique ou de la console
           public void run() {
               int r;

               for (int i = 0; i < tabCases.length; i++) {
                   for (int j = 0; j < tabCases.length; j++) {
                       r = rnd.nextInt(3);

                       switch (r) {
                           case 0:
                               tabCases[i][j] = null;
                               break;
                           case 1:
                               tabCases[i][j] = new Case(2);
                               map.put(tabCases[i][j], new Point(i, j));

                               break;
                           case 2:
                               tabCases[i][j] = new Case(4);
                               map.put(tabCases[i][j], new Point(i, j));
                               break;
                       }
                   }
               }
           }

        }.start();


        setChanged();
        notifyObservers();


    }

    //endregion

    //region Differents tests
    public boolean CanMove(Direction D, Case kase) {
        if(kase == null){return false;}
        Point p = map.get(kase);
            switch (D) {
                case haut:
                    if (p.x <= 0) {
                        return false;
                    }
                    break;
                case bas:
                    if (p.x >= this.getSize()-1) {
                        return false;
                    }
                    break;
                case gauche:
                    if (p.y <= 0) {
                        return false;
                    }
                    break;
                case droite:
                    if (p.y >= this.getSize()-1) {
                        return false;
                    }
                    break;
            }
            Case nextCase = getCaseInDirection(D, kase);
            if(nextCase == null){return true;}
            if(nextCase.isFusionable() == false || nextCase.getValeur()!=kase.getValeur() || kase.isFusionable() == false ){
                return false;
            }
            return true;
    }
    public void Move(Direction D, Case kase) {
        Point p = map.get(kase);
        map.remove(kase);
        tabCases[p.x][p.y]=null;
        switch(D) {
            case haut:
                p.x -= 1;
                break;
            case bas:
                p.x += 1;
                break;
            case gauche:
                p.y -= 1;
                break;
            case droite:
                p.y += 1;
                break;
        }
        tabCases[p.x][p.y]=kase;
        map.put(kase, p);
    }

    public void MoveCase(Direction D, Case kase) {
        System.out.println("    //Je regarde si je peux bouger");
        if(CanMove(D,kase)==false){
            System.out.println("   //Je ne peux pas bouger");
        }else{
            while (CanMove(D, kase)==true ) {
                    System.out.println("        //Je peux bouger, je regarde quel est la case à coté de moi");
                Case nextCase = getCaseInDirection(D, kase);
                                    if(nextCase != null) {
                                        System.out.println("        //La case à coté de moi à pour valeur" + nextCase.getValeur());
                                    }
                if (nextCase == null) {
                    Move(D, kase);
                }
                else if (kase.isFusionable() && kase.canIFuseWith(nextCase)) {
                    //Delete previous case ?
                    Point pnextcase = map.get(nextCase);
                    Point pcurrentcase = map.get(kase);
                    tabCases[pcurrentcase.x][pcurrentcase.y] = null;
                    tabCases[pnextcase.x][pnextcase.y] = kase;
                    pcurrentcase = pnextcase;
                    map.put(kase,pcurrentcase);
                    map.remove(nextCase);
                    nextCase = null;

                }
            }
        }
    }

    //endregion

    //region Procédures de jeu
    public void mouvement(Direction direction) {
        new Thread() {// permet de libérer le processus graphique ou de la console

            public void run() {
                switch (direction) {
                    case gauche:
                        for (int i = 0; i < tabCases.length; i++) {
                            for (int j = 0; j < tabCases.length; j++) {
                                tabCases[i][j] = new Case(1);
                            }
                        }
                        break;

                    case droite:
                        for (int i = 0; i < tabCases.length; i++) {
                            for (int j = 0; j < tabCases.length; j++) {
                                tabCases[i][j] = new Case(2);
                            }
                        }
                        break;

                    case haut:
                        for (int i = 0; i < tabCases.length; i++) {
                            for (int j = 0; j < tabCases.length; j++) {
                                tabCases[i][j] = new Case(3);
                            }
                        }
                        break;

                    case bas:
                        for (int i = 0; i < tabCases.length; i++) {
                            for (int j = 0; j < tabCases.length; j++) {
                                tabCases[i][j] = new Case(4);
                            }
                        }
                        break;


                }
                setChanged();
                notifyObservers();
            }
        }.start();

    }

    public void jouerVers(Direction direction){
        new Thread() {

         public void run() {
             //On regarde le sens dans lequel on déplace le jeu
             switch (direction) {
                 case gauche:
                     //On parcour les lignes de haut en bas
                     for(int x = 0; x < tabCases.length; x++){
                        //On parcour la ligne y de gauche à droite
                         for (int y = 0; y < tabCases.length; y++) {
                             System.out.println("Je me situe à la case ["+x+"]["+y+"]");
                             if(tabCases[x][y]!=null) {
                                 MoveCase(direction, getCase(x, y));
                             }
                         }
                         for(int y = 0; y< tabCases.length; y++){
                             if(tabCases[x][y]== null){
                                 System.out.print(" 0 | ");
                             }else{
                                 System.out.print(tabCases[x][y].getValeur() + " | "  );}
                             if(tabCases[x][y]!=null){tabCases[x][y].setFusionnable(true);}
                         }
                         System.out.println("");
                     }
                     break;


                 case droite:
                     //On  parcour les y lignes
                     for(int x = 0; x < tabCases.length; x++){
                         //On parcour la ligne y de droite à gauche
                         for(int y = tabCases.length -1; y >= 0; y--){
                             System.out.println("Je me situe à la case ["+x+"]["+y+"]");
                             if(tabCases[x][y]!=null) {
                                 MoveCase(direction, getCase(x, y));
                             }
                         }
                         for(int y = tabCases.length -1; y >= 0; y--){
                             if(tabCases[x][y]== null){
                                 System.out.print(" 0 | ");
                             }else{
                             System.out.print(tabCases[x][y].getValeur() + " | "  );}
                             if(tabCases[x][y]!=null){tabCases[x][y].setFusionnable(true);}
                         }
                         System.out.println("");
                     }

                     break;
                 case haut:
                     //On parcour les x colones
                     for(int y = 0; y < tabCases.length; y++){
                         //On parcour la x colone de haut en bas
                         for(int x = 0 ; x <=tabCases.length -1; x++){
                             System.out.println("Je me situe à la case ["+x+"]["+y+"]");
                             if(tabCases[x][y]!=null) {
                                 MoveCase(direction, getCase(x, y));
                             }
                         }
                         for(int x = 0 ; x <=tabCases.length -1; x++){
                             if(tabCases[x][y]== null){
                                 System.out.println("0 | ");
                             }else{
                                 System.out.println(tabCases[x][y].getValeur() + " | "  );}
                             if(tabCases[x][y]!=null){tabCases[x][y].setFusionnable(true);}
                         }
                     }

                     break;
                 case bas:
                     //On parcour les x colones
                     for(int y = 0; y < tabCases.length; y++){
                         //On parcour la colone x de bas en haut
                         for(int x = tabCases.length -1; x >= 0; x--){
                             System.out.println("Je me situe à la case ["+x+"]["+y+"]");
                             if(tabCases[x][y]!=null) {
                                 MoveCase(direction, getCase(x, y));
                             }
                         }
                         for(int x = tabCases.length -1; x >= 0; x--){
                             if(tabCases[x][y]== null){
                                 System.out.println("0 | ");
                             }else{
                                 System.out.println(tabCases[x][y].getValeur() + " | "  );}
                             if(tabCases[x][y]!=null){tabCases[x][y].setFusionnable(true);}
                         }
                     }

                     break;

             }


             //Mise à jour côté graphique
             setChanged();
             notifyObservers();
         }
        }.start();

    }
    //endregion


    //region Get/Set
    public int getSize() {
        return tabCases.length;
    }

    public Case getCase(int i, int j) {
        return tabCases[i][j];
    }

    public Case getCaseInDirection(Direction D, Case kase) {
        boolean outofrange = false;
        Point p = new Point(map.get(kase));


        System.out.print("            //Je suis la case ["+p.x+"]["+p.y+"] et ma case voisin est la case ");
        if(p == null){return null;}
        switch(D) {
            case haut:
                p.x -= 1;
                System.out.print("["+p.x+"]["+p.y+"]\n");
                 if(p.x<0){outofrange=true;}
                break;
            case bas:
                p.x += 1;
                System.out.print("["+p.x+"]["+p.y+"]\n");
                if(p.x>3){outofrange=true;}
                break;
            case gauche:
                p.y -= 1;
                System.out.print("["+p.x+"]["+p.y+"]\n");
                if(p.y<0){outofrange=true;}
                break;
            case droite:
                p.y += 1;
                System.out.print("["+p.x+"]["+p.y+"]\n");
                if(p.y>3){outofrange=true;}
                break;
        }

        return tabCases[p.x][p.y];
    }

    //endregion





}
