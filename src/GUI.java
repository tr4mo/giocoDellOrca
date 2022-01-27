import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.security.DigestException;
import java.util.LinkedList;

public class GUI {

    private static boolean isWaiting = true;
    private static int numCaselle = 25;

    private static JFrame gameFrame;

    private static JMenuBar menuBar;
    private static JMenu menu;
    private static JMenuItem menuItem1;

    private static JPanel gamePanel, leftPanel;

    private static JLabel punti;

    private static JButton skip, info;

    private static final Icon normal_Background = new ImageIcon("img/normal_Background.jpg");
    private static final Icon normal_PlayerOne = new ImageIcon("img/normal_PlayerOne.jpg");
    private static final Icon normal_PlayerTwo = new ImageIcon("img/normal_PlayerTwo.jpg");
    private static final Icon normal_bothPlayers = new ImageIcon("img/normal_bothPlayers.jpg");
    private static final Icon portal_Background = new ImageIcon("img/portal_Background.jpg");
    private static final Icon portal_PlayerOne = new ImageIcon("img/portal_PlayerOne.jpg");
    private static final Icon portal_PlayerTwo = new ImageIcon("img/portal_PlayerTwo.jpg");
    private static final Icon portal_bothPlayers = new ImageIcon("img/portal_bothPlayers.jpg");
    private static final Icon normal_end = new ImageIcon("img/normal_end.jpg");

    private static final Icon logo = new ImageIcon("img/logo.jpg");

    private static final Icon nextRound = new ImageIcon("img/nextRound.jpg");
    private static final Icon wait = new ImageIcon("img/wait.jpg");

    private static Casella[] caselle = new Casella[25];

    private static Giocatore[] giocatori = new Giocatore[2];

    public static void main(String[] args) {
        caselle[0] = new Casella(1);

        for(int i = 1; i < 25; i++) {
            caselle[i] = new Casella(i + 1);
            caselle[i].setPrev(caselle[i - 1]);
            caselle[i - 1].setNext(caselle[i]);
        }

        caselle[0].entraGiocatore(2);
        caselle[0].entraGiocatore(1);

        giocatori[0] = new Giocatore("Giocatore 1", 0, 0);
        giocatori[1] = new Giocatore("Giocatore 2", 0, 0);

        generaImprevisti();




//GUI

    //FRAME INFO
        gameFrame = new JFrame("Giuoco dell'Orca");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);
        gameFrame.setSize(1200, 800);
        gameFrame.setBackground(Color.red);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setLayout(new BorderLayout());

    //MENU
        menuBar = new JMenuBar();

        menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_M);
        menu.getAccessibleContext().setAccessibleDescription("Qui non c'è nulla :)");
        menuBar.add(menu);

        menuItem1 = new JMenuItem("Crediti",new ImageIcon("img/star.png"));
        menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
        menuItem1.getAccessibleContext().setAccessibleDescription("Questo non fa proprio nulla");
        menu.add(menuItem1);

        menuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null,"Giuoco dell'orca in Java\n\nRealizzato da:\nAlessandro Bonetti\nDenis Scavello\nMatteo Tramontina\nArnelo Ucaj");
            }
        });

        gameFrame.add(menuBar, BorderLayout.PAGE_START);

    //BUTTON GRID
        gamePanel = new JPanel(new GridLayout(5,5,5,5));
        JButton[] buttons = new JButton[25];

        for(int i = 0; i < 25; i++) {
            buttons[i] = new JButton("" + (i+1));

            if(i == 5 || i == 10 || i == 15 || i == 20){
                buttons[i].setIcon(portal_Background);
            }else{
                buttons[i].setIcon(normal_Background);
            }

            if(caselle[i].getMovimento()>0){
                buttons[i].setBorder(new LineBorder(Color.GREEN));
            }else if(caselle[i].getMovimento()<0){
                buttons[i].setBorder(new LineBorder(Color.RED));
            }

            buttons[i].setPreferredSize(new Dimension(150,150));

            gamePanel.add(buttons[i]);


            int finalI = i;
            buttons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    System.out.println("Click on box: " + caselle[finalI].getNumCasella());
                    mostraInfoCasella(finalI);
                }
            });
        }

        buttons[numCaselle-1].setIcon(normal_end);



        gameFrame.add(gamePanel, BorderLayout.LINE_START);

    //LEFT PANEL
        leftPanel = new JPanel(new BorderLayout());

        //SKIP BUTTON
        skip = new JButton("PROSSIMO TURNO");
        skip.setPreferredSize(new Dimension(410, 145));
        skip.setBackground(Color.darkGray);

        skip.setIcon(wait);

        skip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                isWaiting = !isWaiting;

                if(isWaiting){
                    skip.setIcon(wait);
                }else{
                    skip.setIcon(nextRound);
                }
            }
        });

        leftPanel.add(skip, BorderLayout.PAGE_END);

        //INFO BUTTON
        info = new JButton("INFO");
        info.setPreferredSize(new Dimension(410, 145));
        info.setBackground(Color.cyan);
        info.setIcon(logo);

        info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null,"Giuoco dell'orca in Java\n\nRealizzato da:\nAlessandro Bonetti\nDenis Scavello\nMatteo Tramontina\nArnelo Ucaj");
            }
        });

        leftPanel.add(info, BorderLayout.PAGE_START);


    //PUNTI
        punti = new JLabel("Punti");

        punti.setText("Giocatore 1: 0     Giocatore2: 0");

        leftPanel.add(punti, BorderLayout.CENTER);


        gameFrame.add(leftPanel, BorderLayout.LINE_END);

        gamePanel.setVisible(true);
        leftPanel.setVisible(true);

        gameFrame.setVisible(true);

//GIOCO
        int turno = 1;
        while(true){

            //Aggiorno le icone delle caselle

            for(int i = 0; i < 25; i++){
                if(caselle[i].giocatoriPresenti() == 0){
                    if(i == 5 || i == 10 || i == 15 || i == 20){
                        buttons[i].setIcon(portal_Background);
                    }else{
                        buttons[i].setIcon(normal_Background);
                    }
                }else if(caselle[i].giocatoriPresenti() == 1){
                    if(i == 5 || i == 10 || i == 15 || i == 20){
                        buttons[i].setIcon(portal_PlayerOne);
                    }else{
                        buttons[i].setIcon(normal_PlayerOne);
                    }
                }else if(caselle[i].giocatoriPresenti() == 2){
                    if(i == 5 || i == 10 || i == 15 || i == 20){
                        buttons[i].setIcon(portal_PlayerTwo);
                    }else{
                        buttons[i].setIcon(normal_PlayerTwo);
                    }
                }else if(caselle[i].giocatoriPresenti() == 3){
                    if(i == 5 || i == 10 || i == 15 || i == 20){
                        buttons[i].setIcon(portal_bothPlayers);
                    }else{
                        buttons[i].setIcon(normal_bothPlayers);
                    }
                }
            }

            punti.setText("Giocatore 1: " + giocatori[0].getPunti() + "          Giocatore 2: " + giocatori[1].getPunti());

            buttons[numCaselle-1].setIcon(normal_end);

            if(turno == 1){
                while(isWaiting != false){
                    System.out.println("");
                }

                Dado d = new Dado();
                int muovi = d.Risultato();

                JOptionPane.showMessageDialog(null, "Giocatore 1 ha fatto: " + muovi);

                if(giocatori[0].getCasella()+muovi>24){
                    muoviGiocatoreAlla(1, 24-(giocatori[0].getCasella()+muovi-24));
                }else{
                    muoviGiocatore(giocatori[0].getCasella(),1,muovi);
                }

                if(caselle[giocatori[0].getCasella()].giocatoriPresenti() == 3){
                    lotta(new Dado());
                }

                faiDomanda(1);

                if(caselle[giocatori[0].getCasella()].getMovimento() > 0){
                    muoviGiocatore(giocatori[0].getCasella(),1,3);
                }else if(caselle[giocatori[0].getCasella()].getMovimento() < 0){
                    muoviGiocatore(giocatori[0].getCasella(),1,-3);
                }

                isWaiting = true;

                turno = 2;

            }else if(turno == 2){

                while(isWaiting != false){
                    System.out.println("");
                }

                Dado d = new Dado();
                int muovi = d.Risultato();

                JOptionPane.showMessageDialog(null, "Giocatore 2 ha fatto: " + muovi);

                if(giocatori[1].getCasella()+muovi>24){
                    muoviGiocatoreAlla(2, 24-(giocatori[1].getCasella()+muovi-24));
                }else{
                    muoviGiocatore(giocatori[1].getCasella(),2,muovi);
                }



                if(caselle[giocatori[0].getCasella()].giocatoriPresenti() == 3){
                    lotta(new Dado());
                }

                faiDomanda(2);

                if(caselle[giocatori[1].getCasella()].getMovimento() > 0){
                    muoviGiocatore(giocatori[1].getCasella(),2,3);
                }else if(caselle[giocatori[1].getCasella()].getMovimento() < 0){
                    muoviGiocatore(giocatori[1].getCasella(),2,-3);
                }

                isWaiting = true;

                turno = 1;
            }

            if(giocatori[0].getCasella()==24){
                if(giocatori[0].getPunti()>0){
                    JOptionPane.showMessageDialog(null, "GIOCATORE 1 HA VINTO!!!");
                }else{
                    muoviGiocatoreAlla(1, 0);
                }
            }else if(giocatori[1].getCasella()==24){
                if(giocatori[1].getPunti()>0){
                    JOptionPane.showMessageDialog(null, "GIOCATORE 2 HA VINTO!!!");
                }else{
                    muoviGiocatoreAlla(2, 0);
                }
            }

        }

    }

    private static void faiDomanda(int g) {
        final int[] rispostaData = {0};
        String str = AssegnazioneDomande.leggiDomanda();

        StringBuffer sb= new StringBuffer(str);

        int giusta = str.charAt(sb.length()-1);

        sb.deleteCharAt(sb.length()-1);

        JFrame f = new JFrame("Domanda!");

        f.setSize(new Dimension(1000,500));
        f.setResizable(false);
        f.setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridLayout(2,1));

        JLabel domanda = new JLabel(str);

        JPanel pRisposte = new JPanel(new GridLayout(2,2,2,2));

        JButton[] risposte = new JButton[4];

        final boolean[] isClicked = {false};

        for (int i = 0; i < 4; i++) {
            risposte[i] = new JButton("" + (i+1));

            int finalI = i;
            risposte[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    rispostaData[0] = finalI;
                    if(finalI == (giusta-49)){
                        risposte[finalI].setBackground(Color.green);
                    }else{
                        risposte[finalI].setBackground(Color.red);
                    }
                    isClicked[0] = true;
                }
            });

            pRisposte.add(risposte[i]);
        }




        p.add(domanda);
        p.add(pRisposte);

        f.add(p);

        f.setVisible(true);

        while(isClicked[0] == false){
            System.out.println("");
        }

        if(rispostaData[0] == (giusta-49)){
            giocatori[g-1].aggiungiPunti(1);
        }
    }

    private static void mostraInfoCasella(int i) {

        int num = caselle[i].getNumCasella();
        int mov = caselle[i].getMovimento();
        int gPres = caselle[i].giocatoriPresenti();
        boolean g1 = false, g2 = false;

        if(gPres == 3 || gPres == 1){
            g1 = true;
        }

        if(gPres == 3 || gPres == 2){
            g2 = true;
        }

        if (num == 1){
            JOptionPane.showMessageDialog(null,"INFO SULLA CASELLA\nCasella numero: 1 (Inizio)" + "\nMovimento di: 0" + "\nGiocatore 1 presente: " + g1 + "\nGiocatore 2 presente: " + g2);
        }else if (num == numCaselle) {
            JOptionPane.showMessageDialog(null,"INFO SULLA CASELLA\nCasella numero: " + num + " (Fine)" + "\nMovimento di: " + mov + "\nGiocatore 1 presente: " + g1 + "\nGiocatore 2 presente: " + g2);
        }else{
            JOptionPane.showMessageDialog(null,"INFO SULLA CASELLA\nCasella numero: " + num + "\nMovimento di: " + mov + "\nGiocatore 1 presente: " + g1 +"\nGiocatore 2 presente: " + g2);
        }

    }

    public static int lotta(Dado d) {
        int n1, n2, vincitore;

        do{
            n1 = d.Risultato();
            n2 = d.Risultato();
        }while (n1==n2);

        if(n1 > n2) {
            vincitore = 1;
            giocatori[0].aggiungiPunti(giocatori[1].getPunti());
            giocatori[1].aggiungiPunti(giocatori[1].getPunti()*-1);

            muoviGiocatore(giocatori[1].getCasella(),2,giocatori[1].getCasella()*-1);
        }else{
            vincitore = 2;
            giocatori[1].aggiungiPunti(giocatori[0].getPunti());
            giocatori[0].aggiungiPunti(giocatori[0].getPunti()*-1);
            muoviGiocatore(giocatori[0].getCasella(),1,giocatori[0].getCasella()*-1);
        }

        JOptionPane.showMessageDialog(null, "LOTTA!\n\nDado giocatore 1: " + n1 + "\nDado giocatore 2: " + n2 + "\n\nVINCE GIOCATORE " + vincitore);

        return vincitore;
    }

    public static int setFine() {
        final int[] valore = {-1};

        JFrame f = new JFrame("Imposta fine (max 25)");

        f.setSize(1050,100);
        f.setLayout(new GridLayout(1,20));
        f.setResizable(false);
        f.setLocationRelativeTo(null);

        JButton[] b = new JButton[21];

        for(int i = 5; i < 26; i++) {
            b[i-5] = new JButton("" + i);

            b[i-5].setPreferredSize(new Dimension(100, 50));

            f.add(b[i-5]);


            int finalI = i;
            b[i-5].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    valore[0] = finalI;
                }
            });

        }

        f.setVisible(true);

        while(valore[0] == -1){}

        return valore[0];
    }

    public static void muoviGiocatore(int c, int g, int mov){
        caselle[c].esceGiocatore(g);
        caselle[c+mov].entraGiocatore(g);

        giocatori[g-1].setCasella(c+mov);
    }

    public static void muoviGiocatoreAlla(int g, int c2) {
        caselle[giocatori[g-1].getCasella()].esceGiocatore(g);
        caselle[c2].entraGiocatore(g);

        giocatori[g-1].setCasella(c2);
    }

    public static void generaImprevisti() {
        int imprevisti[] = new int[5];

        boolean x = true;

        for (int i = 0; i < 5; i++) {
            imprevisti[i] = (int)(Math.random()*14)+3;

            if(x) {
                caselle[imprevisti[i]].setMovimento(+3);
            }else{
                caselle[imprevisti[i]].setMovimento(-3);
            }

            x = !x;

        }


    }
}

