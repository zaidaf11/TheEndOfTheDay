package com.mycompany.theendoftheday;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Random;

public class TheEndOfTheDay {

    static Random rand = new Random();
    static Player player = new Player();
    static int turn = 1;
    static int CDFW = 0;
    static Mission mission = new Mission();

    // Komponen GUI
    static JFrame frame;
    static JTextPane textPane; 
    static JTextField inputField;
    static volatile String currentInput = null; 

    public static void main(String[] args) {
        setupGUI();

        new Thread(() -> {
            Intro();
            gameloop();
        }).start();
    }

    static void setupGUI() {
        frame = new JFrame("The End Of The Day - Survival");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setBackground(Color.BLACK);
        textPane.setForeground(new Color(50, 255, 50));
        textPane.setFont(new Font("Monospaced", Font.BOLD, 14));
        
        // Setting teks di tengah
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        JScrollPane scroll = new JScrollPane(textPane);
        scroll.setBorder(null);
        frame.add(scroll, BorderLayout.CENTER);

        inputField = new JTextField();
        inputField.setBackground(new Color(30, 30, 30));
        inputField.setForeground(Color.WHITE);
        inputField.setFont(new Font("Monospaced", Font.BOLD, 18));
        inputField.setHorizontalAlignment(JTextField.CENTER);
        frame.add(inputField, BorderLayout.SOUTH);

        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        doc.insertString(doc.getLength(), String.valueOf((char) b), null);
                        textPane.setCaretPosition(doc.getLength());
                    } catch (BadLocationException e) {}
                });
            }
        }));

        inputField.addActionListener(e -> {
            currentInput = inputField.getText();
            inputField.setText("");
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static int getNextInt() {
        currentInput = null;
        while (currentInput == null) {
            try { Thread.sleep(50); } catch (InterruptedException e) {}
        }
        try {
            return Integer.parseInt(currentInput.trim());
        } catch (NumberFormatException e) {
            System.out.println("\n> INPUT HARUS ANGKA! <");
            return getNextInt();
        }
    }

    static void clearScreen() {
        SwingUtilities.invokeLater(() -> textPane.setText(""));
    }

    // --- Sisanya sama aja ---
    public static void Intro() {
        clearScreen();
        System.out.println("\n\n\n====================");
        System.out.println("The End Of The Day");
        System.out.println("====================");
        System.out.println("\nKamu terbangun di jalanan yang sunyi...");
        delay();
        System.out.println("Darah di tanganmu. Suara erangan di kejauhan.");
        System.out.println("\n[ Ketik angka sembarang untuk Lanjut ]");
        getNextInt(); 
    }

    static void gameloop() {
        while (true) {
            clearScreen(); 
            System.out.println("\n--- TURN " + turn + " ---");
            mission.showMissionStatus();
            showStatus();
            
            actionMenu();
            randomEvent();
            hungerSystem();
            
            turn++;
            CDFW--;

            if (player.getHP() <= 0) {
                clearScreen();
                endingMati();
                break;
            }
            if (player.GetInfected()) {
                System.out.println("\n[!] VIRUS ZOMBIE MENYEBAR [!]");
                player.setHP(player.getHP() - 10);
                if (rand.nextInt(100) < 15) {
                    clearScreen();
                    endingZombie();
                    break;
                }
            }
            delay();
        }
    }

    static void showStatus() {
        System.out.println("\n------------------------------------------------");
        System.out.println("HP: " + player.getHP() + "/100 | STAMINA: " + player.getStamina() + "/100");
        System.out.println("LAPAR: " + player.getHunger() + " | SENJATA: " + player.getWeapon());
        System.out.println("------------------------------------------------\n");
    }

    static void actionMenu() {
        System.out.println("1. CARI SENJATA");
        System.out.println("2. SERANG ZOMBIE");
        System.out.println("3. SEMBUNYI");
        System.out.println("4. LARI");
        System.out.println("5. ISTIRAHAT");
        System.out.print("\nPILIH TINDAKAN (1-5): ");
        
        int pilihan = getNextInt();

        switch(pilihan) {
            case 1:
                if (CDFW <= 0) findweapon();
                else System.out.println("CD Senjata: " + CDFW + " Turn.");
                break;
            case 2: battle(); break;
            case 3: hide(); break;
            case 4: runAway(); break;
            case 5: rest(); break;
        }
    }

    static void rest() {
        System.out.println("Beristirahat...");
        delay();
        player.setStamina(Math.min(100, player.getStamina() + 15));
        player.setHP(Math.min(100, player.getHP() + 10));
        System.out.println("HP dan Stamina kamu pulih.");
    }

    static void findweapon() {
        CDFW = 3;
        System.out.println("Mencari senjata...");
        delay();
        int s = rand.nextInt(5);
        if(s==0) { player.setWeapon("Pisau"); player.setDamage(15); }
        else if (s==1) { player.setWeapon("Bedog"); player.setDamage(20); }
        else if (s==2) { player.setWeapon("Pistol"); player.setDamage(35); }
        else { System.out.println("Hanya menemukan rongsokan."); }
        System.out.println("Senjata: " + player.getWeapon());
    }

    static void battle() {
        Zombie z = spawnZombie();
        System.out.println("ZOMBIE " + z.getType().toUpperCase() + " MENDEKAT!");
        while (z.HP > 0 && player.getHP() > 0) {
            System.out.println("\n1. SERANG | 2. KABUR");
            int c = getNextInt();
            if (c == 1) {
                z.HP -= player.getDamage();
                System.out.println("Serang! Zombie HP: " + z.HP);
                if (z.HP > 0) z.attack(player);
                else System.out.println("Zombie hancur!");
            } else break;
        }
    }

    static void hide() {
        if (rand.nextBoolean()) System.out.println("Kamu tidak terlihat.");
        else {
            System.out.println("Kamu ketahuan!");
            player.setHP(player.getHP() - 10);
        }
    }

    static void runAway() {
        if (player.getStamina() >= 10) {
            System.out.println("Kamu lari sekuat tenaga.");
            player.setStamina(player.getStamina() - 15);
        } else System.out.println("Kamu terlalu lelah untuk lari!");
    }

    static Zombie spawnZombie() {
        int r = rand.nextInt(3);
        if (r == 0) return new Walker();
        if (r == 1) return new Runner();
        return new Tank();
    }

    static void randomEvent() {
        int r = rand.nextInt(20);
        if (r == 0) {
            System.out.println("\n[EVENT] Menemukan kotak P3K!");
            player.setHP(Math.min(100, player.getHP() + 20));
        }
    }

    static void hungerSystem() {
        player.setHunger(player.getHunger() + 5);
        if (player.getHunger() >= 50) {
            System.out.println("> Kamu Kelaparan! (HP -5)");
            player.setHP(player.getHP() - 5);
        }
    }

    static void delay() { try { Thread.sleep(1000); } catch (Exception e) {} }
    static void endingMati() { System.out.println("\n=== GAME OVER ===\nKota ini menjadi kuburanmu."); }
    static void endingZombie() { System.out.println("\n=== ENDING: INFECTED ===\nKamu sekarang bagian dari mereka."); }
}