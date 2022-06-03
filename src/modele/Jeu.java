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
    private boolean canGenerate = false;

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
    public boolean CanMove(Direction D, Case kase) { //Vérifie que la case ne soit pas au bord du plateau
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
        if (!nextCase.isFusionable() || nextCase.getValeur() != kase.getValeur() || !kase.isFusionable()) {
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
        while (CanMove(D, kase)) {
            Case nextCase = getCaseInDirection(D, kase);
            if (nextCase == null) { //Move si next case est vide
                Move(D, kase);
                setCanGenerate(true); //Une case a bougé : canGenerate = true
            } else if (kase.isFusionable() && kase.canIFuseWith(nextCase)) { //Move si fusion avec next case
                Point pnextcase = map.get(nextCase);
                Point pcurrentcase = map.get(kase);
                tabCases[pcurrentcase.x][pcurrentcase.y] = null;
                tabCases[pnextcase.x][pnextcase.y] = kase;
                pcurrentcase = pnextcase;
                map.put(kase, pcurrentcase);
                map.remove(nextCase);
                setCanGenerate(true); //Une case a bougé : canGenerate = true
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
                boolean test = CheckIfGameOver();

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

    public void setCanGenerate(boolean canGenerate) {
        this.canGenerate = canGenerate;
    }

    //endregion

    public void AddRandomCase() {
        if (canGenerate) {
            ArrayList<Point> listEmpty = new ArrayList<>();

            //Parcourir le tableau et recup une liste des cases vides :
            for (int i = 0; i < tabCases.length; i++) {
                for (int j = 0; j < tabCases.length; j++) {
                    if (tabCases[i][j] == null) {
                        listEmpty.add(new Point(i, j));
                    }
                }
            }

            //Assignation d'une case de valeur 2 ou 4 dans la case selectionnée :
            int randomCase;
            int rand;

            randomCase = rnd.nextInt(listEmpty.size());
            rand = rnd.nextInt(10);

            Case caseToAdd;
            if (rand > 0) {
                caseToAdd = new Case(2);
            } else {
                caseToAdd = new Case(4);
            }
            tabCases[listEmpty.get(randomCase).x][listEmpty.get(randomCase).y] = caseToAdd;
            map.put(caseToAdd, new Point(listEmpty.get(randomCase).x, listEmpty.get(randomCase).y));

            setCanGenerate(false); //La case est générée, canGenerate = false;
        }
    }

    public boolean CheckIfGameOver() { //Si une case peut bouger ou fusionner, on passe le gameOver à false
        boolean gameOver = true;

        //case gauche:
        for (int x = 0; x < tabCases.length; x++) { //On parcour les lignes de haut en bas
            for (int y = 0; y < tabCases.length; y++) { //On parcour la ligne y de gauche à droite
                if (tabCases[x][y] != null) {
                    if (CanMove(Direction.gauche, getCase(x, y))) {
                        Case nextCase = getCaseInDirection(Direction.gauche, getCase(x, y));
                        if (nextCase == null) { //Move si next case est vide
                            gameOver = false;
                        }
                        else if (getCase(x, y).isFusionable() && getCase(x, y).testCanIFuseWith(nextCase)) { //Move si fusion avec next case
                            gameOver = false;
                        }
                    }
                }
            }
        }

        //case droite:
        for (int x = 0; x < tabCases.length; x++) { //On  parcour les y lignes
            for (int y = tabCases.length - 1; y >= 0; y--) { //On parcour la ligne y de droite à gauche
                if (tabCases[x][y] != null) {
                    if (CanMove(Direction.droite, getCase(x, y))) {
                        Case nextCase = getCaseInDirection(Direction.droite, getCase(x, y));
                        if (nextCase == null) { //Move si next case est vide
                            gameOver = false;
                        }
                        else if (getCase(x, y).isFusionable() && getCase(x, y).testCanIFuseWith(nextCase)) { //Move si fusion avec next case
                            gameOver = false;
                        }
                    }
                }
            }
        }

        //case haut:
        for (int y = 0; y < tabCases.length; y++) { //On parcour les x colones
            for (int x = 0; x <= tabCases.length - 1; x++) { //On parcour la x colone de haut en bas
                if (tabCases[x][y] != null) {
                    if (CanMove(Direction.haut, getCase(x, y))) {
                        Case nextCase = getCaseInDirection(Direction.haut, getCase(x, y));
                        if (nextCase == null) { //Move si next case est vide
                            gameOver = false;
                        }
                        else if (getCase(x, y).isFusionable() && getCase(x, y).testCanIFuseWith(nextCase)) { //Move si fusion avec next case
                            gameOver = false;
                        }
                    }
                }
            }
        }

        //case bas:
        for (int y = 0; y < tabCases.length; y++) { //On parcour les x colones
            for (int x = tabCases.length - 1; x >= 0; x--) { //On parcour la colone x de bas en haut
                if (tabCases[x][y] != null) {
                    if (CanMove(Direction.bas, getCase(x, y))) {
                        Case nextCase = getCaseInDirection(Direction.bas, getCase(x, y));
                        if (nextCase == null) { //Move si next case est vide
                            gameOver = false;
                        }
                        else if (getCase(x, y).isFusionable() && getCase(x, y).testCanIFuseWith(nextCase)) { //Move si fusion avec next case
                            gameOver = false;
                        }
                    }
                }
            }
        }

        System.out.println("RESULT TEST : " + gameOver);
        return gameOver;
    }

    //NOTES : Pour le score, ajouter la valeur de chaque case qui vient de fusionner et voala

}
