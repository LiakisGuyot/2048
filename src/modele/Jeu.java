package modele;

import java.util.HashMap;
import java.util.Observable;
import java.util.Random;

public class Jeu extends Observable {

    private Case[][] tabCases;
//Pour suppresion case faire un tabCases = null et removede la HashMap
    private static Random rnd = new Random(4);
    private HashMap<Case, Point> map = new HashMap<Case, Point>();

    public Jeu(int size) {
        tabCases = new Case[size][size];
        rnd();
        InitHashMap();
    }

    public void InitHashMap() {
        for(int y = 0; y < tabCases.length; y++){
            //On parcour la ligne y de gauche à droite
            for (int x = 0; x < tabCases.length; x++) {
                this.map.put(tabCases[x][y], new Point(x, y));
            }
        }
    }

    public boolean CanMove(Direction D, Case kase) {
        Point p = map.get(kase);
        switch(D) {
            case haut:
                if(p.y <= 0) {
                    return false;
                }
                break;
            case bas:
                if(p.y >= this.getSize()) {
                    return false;
                }
                break;
            case gauche:
                if(p.x <= 0) {
                    return false;
                }
                break;
            case droite:
                if(p.x >= this.getSize()) {
                    return false;
                }
                break;
        }
        return true;
    }

    public int getSize() {
        return tabCases.length;
    }

    public Case getCase(int i, int j) {
        return tabCases[i][j];
    }

    public Case getCaseInDirection(Direction D, Case kase) {
        Point p = map.get(kase);
        switch(D) {
            case haut:
                p.y += 1;
                break;
            case bas:
                p.y -= 1;
                break;
            case gauche:
                p.x += 1;
                break;
            case droite:
                p.x -= 1;
                break;
        }
        return tabCases[p.x][p.y];
    }


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
                                break;
                            case 2:
                                tabCases[i][j] = new Case(4);
                                break;
                        }
                    }
                }
            }

        }.start();


        setChanged();
        notifyObservers();


    }

    public void Move(Direction D, Case kase) {
        Point p = map.get(kase);
        switch(D) {
            case haut:
                p.y += 1;
                break;
            case bas:
                p.y -= 1;
                break;
            case gauche:
                p.x += 1;
                break;
            case droite:
                p.x -= 1;
                break;
        }
        map.replace(kase, p);
    }

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
                     for(int y = 0; y < tabCases.length; y++){
                        //On parcour la ligne y de gauche à droite
                         for (int x = 0; x < tabCases.length; x++) {
                             //INSTRUCTIONS
                         }
                     }
                     break;


                 case droite:
                     //On  parcour les y lignes
                     for(int y = 0; y < tabCases.length; y++){
                         //On parcour la ligne y de droite à gauche
                         for(int x = tabCases.length -1; x >= 0; x--){
                             //INSTRUCTIONS
                         }
                     }

                     break;
                 case haut:
                     //On parcour les x colones
                     for(int x = 0; x < tabCases.length; x++){
                         //On parcour la x colone de haut en bas
                         for(int y = 0; y < tabCases.length; y++){
                             //INSTRUCTION
                         }
                     }

                     break;
                 case bas:
                     //On parcour les x colones
                     for(int x = 0; x < tabCases.length; x++){
                         //On parcour la colone x de bas en haut
                         for(int y = tabCases.length -1; y >= 0; y--){
                             //INSTRUCTIONS
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


    public void MoveCase(Direction D, Case kase) {
        while (CanMove(D, kase)) {
            Case nextCase = getCaseInDirection(D, kase);
            if (nextCase == null) {
                Move(D, kase);
            }
            else if (kase.canIFuseWith(nextCase)) {
                //Delete previous case ?
                Move(D, kase);
            }
        }
    }





}
