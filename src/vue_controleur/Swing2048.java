package vue_controleur;

import com.sun.tools.javac.Main;
import modele.Case;
import modele.Direction;
import modele.Jeu;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class Swing2048 extends JFrame implements Observer {
    private static final int PIXEL_PER_SQUARE = 120;
    // tableau de cases : i, j -> case graphique
    private JLabel[][] tabC;
    private Jeu jeu;
    private JMenuBar menuBar;
    private JPanel gameOverPanel;
    private JLabel gameOverScore;


    public Swing2048(Jeu _jeu) {
        jeu = _jeu;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(jeu.getSize() * PIXEL_PER_SQUARE, jeu.getSize() * PIXEL_PER_SQUARE);
        tabC = new JLabel[jeu.getSize()][jeu.getSize()];


        JPanel contentPane = new JPanel(new GridLayout(jeu.getSize(), jeu.getSize()));
        contentPane.setOpaque(true);
        contentPane.setBackground(Color.WHITE);

        for (int i = 0; i < jeu.getSize(); i++) {
            for (int j = 0; j < jeu.getSize(); j++) {
                Border border = BorderFactory.createLineBorder(Color.darkGray, 5);
                tabC[i][j] = new JLabel();
                tabC[i][j].setOpaque(true);
                tabC[i][j].setBackground(Colorisation(i, j));
                tabC[i][j].setBorder(border);
                tabC[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                tabC[i][j].setFont(new Font("Arial", Font.BOLD, 30));


                contentPane.add(tabC[i][j]);

            }
        }

        menuBar = new JMenuBar();
        JMenu menu = new JMenu("Score : " + jeu.getScore());
        menuBar.add(menu);
        setJMenuBar(menuBar);

        gameOverPanel = new JPanel(new GridLayout(jeu.getSize(), jeu.getSize()));
        contentPane.setOpaque(true);
        contentPane.setBackground(Color.WHITE);
        JLabel gameOverText = new JLabel("GAME OVER");
        gameOverText.setOpaque(true);
        gameOverText.setHorizontalAlignment(SwingConstants.CENTER);
        gameOverText.setVerticalAlignment(SwingConstants.CENTER);
        gameOverText.setFont(new Font("Arial", Font.BOLD, 30));
        gameOverScore = new JLabel("Score : " + jeu.getScore());
        gameOverScore.setOpaque(true);
        gameOverScore.setHorizontalAlignment(SwingConstants.CENTER);
        gameOverScore.setVerticalAlignment(SwingConstants.CENTER);
        gameOverScore.setFont(new Font("Arial", Font.BOLD, 20));
        gameOverPanel.add(gameOverText);
        gameOverPanel.add(gameOverScore);

        /*
        JButton playButton = new JButton("Click Here");
        //playButton.setBounds(50,100,95,30);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.main(new String[] );
                //setContentPane(contentPane);
                //rafraichir();
            }
        });
        gameOverPanel.add(playButton);*/



        setContentPane(contentPane);
        ajouterEcouteurClavier();
        rafraichir();

    }


    /**
     * Correspond ?? la fonctionnalit?? de Vue : affiche les donn??es du mod??le
     */
    private void rafraichir() {

        SwingUtilities.invokeLater(new Runnable() { // demande au processus graphique de r??aliser le traitement
            @Override
            public void run() {
                for (int i = 0; i < jeu.getSize(); i++) {
                    for (int j = 0; j < jeu.getSize(); j++) {
                        Case c = jeu.getCase(i, j);
                        tabC[i][j].setBackground(Colorisation(i, j));
                        if (c == null) {

                            tabC[i][j].setText("");

                        } else {
                            tabC[i][j].setText(c.getValeur() + "");
                        }


                    }
                }
                menuBar = new JMenuBar();
                JMenu menu = new JMenu("Score : " + jeu.getScore());
                menuBar.add(menu);
                setJMenuBar(menuBar);

                if(jeu.getGameOver()) {
                    gameOverScore.setText("Score : " + jeu.getScore());
                    setContentPane(gameOverPanel);
                    menuBar.remove(menu);
                }
            }
        });
    }

    private Color Colorisation(int i, int j) {
        Color mycolor = new Color(0, 0, 0);
        if (jeu.getCase(i, j) == null) {
            return Color.white;
        }
        int myvaleur = jeu.getCase(i, j).getValeur();

        //2    --> 16   : blanc --> rouge   level 0
        //32   --> 64  : rouge --> jaune    level 1
        //128  --> 512  : jaune --> vert    level 2
        //1024 --> 2048 : vert  --> bleu    level 3
        //2048 --> ++++ : bleu              level 4

        int level = 0;
        int correspond = 0;
        if (myvaleur >= 32) {
            level = 1;
        }
        if (myvaleur >= 128) {
            level = 2;
        }
        if (myvaleur >= 1024) {
            level = 3;
        }
        if (myvaleur >= 2048) {
            level = 4;
        }

        switch (level) {
            case 0:
                correspond = myvaleur * 255 / 64;
                mycolor = new Color(255, 255 - correspond, 255 - correspond); //blanc --> rouge
                break;

            case 1:
                correspond = myvaleur * 255 / 1024;
                mycolor = new Color(255, 0 + correspond, 0); //rouge --> jaune
                break;

            case 2:
                correspond = myvaleur * 255 / 8192;
                mycolor = new Color(255 - correspond, 255, 0); //jaune --> vert
                break;

            case 3:
                correspond = myvaleur * 255 / 8192; //Juste vert en fait
                mycolor = new Color(0, 255 - correspond, 0 + correspond); //jaune --> vert
                break;

            case 4:
                mycolor = new Color(0, 0, 255); //Juste vert en fait
                break;
        }


        return mycolor;
    }

    /**
     * Correspond ?? la fonctionnalit?? de Contr??leur : ??coute les ??v??nements, et d??clenche des traitements sur le mod??le
     */
    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {  // on regarde quelle touche a ??t?? press??e
                    case KeyEvent.VK_LEFT:
                        jeu.jouerVers(Direction.gauche);
                        break;
                    case KeyEvent.VK_RIGHT:
                        jeu.jouerVers(Direction.droite);
                        break;
                    case KeyEvent.VK_DOWN:
                        jeu.jouerVers(Direction.bas);
                        break;
                    case KeyEvent.VK_UP:
                        jeu.jouerVers(Direction.haut);
                        break;
                }
            }
        });
    }


    @Override
    public void update(Observable o, Object arg) {
        rafraichir();
    }
}