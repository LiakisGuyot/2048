package modele;

import java.time.LocalTime;
import java.util.ArrayList;
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
        if (kase == null) {
            return false;
        }
        Point p = map.get(kase);
        switch (D) {
            case haut:
                if (p.x <= 0) {
                    return false;
                }
                break;
            case bas:
                if (p.x >= this.getSize() - 1) {
                    return false;
                }
                break;
            case gauche:
                if (p.y <= 0) {
                    return false;
                }
                break;
            case droite:
                if (p.y >= this.getSize() - 1) {
                    return false;
                }
                break;
        }
        Case nextCase = getCaseInDirection(D, kase);
        if (nextCase == null) {
            return true;
        }
        if (nextCase.isFusionable() == false || nextCase.getValeur() != kase.getValeur() || kase.isFusionable() == false) {
            return false;
        }
        return true;
    }

    public void Move(Direction D, Case kase) {
        Point p = map.get(kase);
        map.remove(kase);
        tabCases[p.x][p.y] = null;
        switch (D) {
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
        tabCases[p.x][p.y] = kase;
        map.put(kase, p);
    }

    public void MoveCase(Direction D, Case kase) {
        while (CanMove(D, kase) == true) {
            Case nextCase = getCaseInDirection(D, kase);
            if (nextCase == null) {
                Move(D, kase);
            } else if (kase.isFusionable() && kase.canIFuseWith(nextCase)) {
                //Delete previous case ?
                Point pnextcase = map.get(nextCase);
                Point pcurrentcase = map.get(kase);
                tabCases[pcurrentcase.x][pcurrentcase.y] = null;
                tabCases[pnextcase.x][pnextcase.y] = kase;
                pcurrentcase = pnextcase;
                map.put(kase, pcurrentcase);
                map.remove(nextCase);
                nextCase = null;

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

    public void jouerVers(Direction direction) {
        new Thread() {

            public void run() {
                //On regarde le sens dans lequel on déplace le jeu
                switch (direction) {
                    case gauche:
                        //On parcour les lignes de haut en bas
                        for (int x = 0; x < tabCases.length; x++) {
                            //On parcour la ligne y de gauche à droite
                            for (int y = 0; y < tabCases.length; y++) {
                                if (tabCases[x][y] != null) {
                                    MoveCase(direction, getCase(x, y));
                                }
                            }
                            for (int y = 0; y < tabCases.length; y++) {
                                if (tabCases[x][y] != null) {
                                    tabCases[x][y].setFusionnable(true);
                                }
                            }
                        }
                        break;


                    case droite:
                        //On  parcour les y lignes
                        for (int x = 0; x < tabCases.length; x++) {
                            //On parcour la ligne y de droite à gauche
                            for (int y = tabCases.length - 1; y >= 0; y--) {
                                if (tabCases[x][y] != null) {
                                    MoveCase(direction, getCase(x, y));
                                }
                            }
                            for (int y = tabCases.length - 1; y >= 0; y--) {
                                if (tabCases[x][y] != null) {
                                    tabCases[x][y].setFusionnable(true);
                                }
                            }
                        }

                        break;
                    case haut:
                        //On parcour les x colones
                        for (int y = 0; y < tabCases.length; y++) {
                            //On parcour la x colone de haut en bas
                            for (int x = 0; x <= tabCases.length - 1; x++) {
                                if (tabCases[x][y] != null) {
                                    MoveCase(direction, getCase(x, y));
                                }
                            }
                            for (int x = 0; x <= tabCases.length - 1; x++) {
                                if (tabCases[x][y] != null) {
                                    tabCases[x][y].setFusionnable(true);
                                }
                            }
                        }

                        break;
                    case bas:
                        //On parcour les x colones
                        for (int y = 0; y < tabCases.length; y++) {
                            //On parcour la colone x de bas en haut
                            for (int x = tabCases.length - 1; x >= 0; x--) {
                                if (tabCases[x][y] != null) {
                                    MoveCase(direction, getCase(x, y));
                                }
                            }
                            for (int x = tabCases.length - 1; x >= 0; x--) {
                                if (tabCases[x][y] != null) {
                                    tabCases[x][y].setFusionnable(true);
                                }
                            }
                        }
                        break;
                }

                AddRandomCase();

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


        if (p == null) {
            return null;
        }
        switch (D) {
            case haut:
                p.x -= 1;
                if (p.x < 0) {
                    outofrange = true;
                }
                break;
            case bas:
                p.x += 1;
                if (p.x > 3) {
                    outofrange = true;
                }
                break;
            case gauche:
                p.y -= 1;
                if (p.y < 0) {
                    outofrange = true;
                }
                break;
            case droite:
                p.y += 1;
                if (p.y > 3) {
                    outofrange = true;
                }
                break;
        }

        return tabCases[p.x][p.y];
    }

    //endregion

    public void AddRandomCase() {
        Case[][] tabEmptyCases = new Case[tabCases.length][tabCases.length];
        HashMap<Case, Point> mapOfEmpty = new HashMap<Case, Point>();
        ArrayList<Point> listEmpty = new ArrayList<>();

        //Parcourir le tableau et recup une liste des cases vides :
        System.out.println("Liste des coordonnées des cases vides :\n");
        for (int i = 0; i < tabCases.length; i++) {
            for (int j = 0; j < tabCases.length; j++) {
                if (tabCases[i][j] == null) {
                    mapOfEmpty.put(tabCases[i][j], new Point(i, j));
                    listEmpty.add(new Point(i, j));
                    System.out.println("[" + i + ", " + j + "]");
                }
            }
        }


        //Assignation d'une case de valeur 2 ou 4 dans la case selectionnée :
        int randomCase;
        int rand;

        randomCase = rnd.nextInt(listEmpty.size());
        rand = rnd.nextInt(10);

        System.out.println("SIZE OF LIST EMPTY CASES : " + listEmpty.size());

        System.out.println("INT OF CASE SELECTED IN LIST : " + randomCase);

        Case caseToAdd;
        if (rand > 0) {
            caseToAdd = new Case(1024);
        }
        else {
            caseToAdd = new Case(4096);
        }
        System.out.println("RANDOM 1-10 = " + rand + "     CASE TO ADD = " + caseToAdd.getValeur()); //Probleme : x et y mal gérés par rapport à i et j

        System.out.println("COORDS OF CASE TO ADD " + listEmpty.get(randomCase).x + ", " + listEmpty.get(randomCase).y);

        tabCases[listEmpty.get(randomCase).x][listEmpty.get(randomCase).y] = caseToAdd;
        map.put(caseToAdd, new Point(listEmpty.get(randomCase).x, listEmpty.get(randomCase).y));
        //tabCases[listEmpty.get(randomCase).x][listEmpty.get(randomCase).y]
    }

    //NOTES : Pour le score, ajouter la valeur de chaque case qui vient de fusionner et voala

}
