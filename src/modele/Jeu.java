package modele;

import java.util.HashMap;
import java.util.Observable;
import java.util.Random;

public class Jeu extends Observable {

    private Case[][] tabCases;
    private Point[][] tabPoint; //
    private static Random rnd = new Random(4);
    private HashMap<Case, Point> map = new HashMap<Case, Point>();; //

    public Jeu(int size) {
        tabCases = new Case[size][size];
        rnd();
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
            }
        }.start();
        setChanged();
        notifyObservers();
    }


}
