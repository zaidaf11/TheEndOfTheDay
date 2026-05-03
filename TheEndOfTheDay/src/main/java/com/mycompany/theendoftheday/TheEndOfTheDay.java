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
<<<<<<< HEAD
    static Mission mission = new Mission();

    // Komponen GUI
    static JFrame frame;
    static JTextPane textPane; 
    static JTextField inputField;
    static volatile String currentInput = null; 

=======
    
   static Mission mission = new Mission();
   static Location currentLocation = null;
   
   static int foodStock = 0;
    
>>>>>>> 41ae11cd3028d0fe414ac293297d191b9a548fb4
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
            
<<<<<<< HEAD
            actionMenu();
            randomEvent();
            hungerSystem();
            
            turn++;
            CDFW--;

            if (player.getHP() <= 0) {
                clearScreen();
                endingMati();
=======
             if (mission.isAllMissionComplete()) {
                endingMenang();
>>>>>>> 41ae11cd3028d0fe414ac293297d191b9a548fb4
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
<<<<<<< HEAD
        System.out.println("\n------------------------------------------------");
        System.out.println("HP: " + player.getHP() + "/100 | STAMINA: " + player.getStamina() + "/100");
        System.out.println("LAPAR: " + player.getHunger() + " | SENJATA: " + player.getWeapon());
        System.out.println("------------------------------------------------\n");
=======
        System.out.println("HP; " + player.getHP());
        System.out.println("Stamina; " + player.getStamina());
        System.out.println("Hunger; " + player.getHunger());
        System.out.println("Weapon; " + player.getWeapon());
        
        
        
>>>>>>> 41ae11cd3028d0fe414ac293297d191b9a548fb4
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
<<<<<<< HEAD

    static void rest() {
        System.out.println("Beristirahat...");
        delay();
        player.setStamina(Math.min(100, player.getStamina() + 15));
        player.setHP(Math.min(100, player.getHP() + 10));
        System.out.println("HP dan Stamina kamu pulih.");
    }

    static void findweapon() {
=======
    
    static void makan() {
        if (foodStock <= 0) {
            System.out.println("> Kamu tidak punya makanan. Cari dulu di Minimarket atau dari random event.");
            return;
        }
        foodStock--;
        int pemulihanHunger = 30;
        player.setHunger(Math.max(0, player.getHunger() - pemulihanHunger));
        System.out.println("> Kamu membuka kaleng dan memakannya cepat-cepat.");
        System.out.println("> Lapar berkurang " + pemulihanHunger + ". Sisa makanan: " + foodStock + " kaleng.");
    }
    
    static void findweapon () {
>>>>>>> 41ae11cd3028d0fe414ac293297d191b9a548fb4
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
<<<<<<< HEAD
            System.out.println("Kamu ketahuan!");
            player.setHP(player.getHP() - 10);
=======
            player.setWeapon("Bedog");
            player.setDamage(20);   
                }
        
        System.out.println("Kamu Mendapatkan: " + player.getWeapon());
        System.out.println("Damage kamu meninggkat: " + player.getDamage());
        
        
        }
   
    }
    
     static void battle() {
    Zombie z = spawnZombie(); 
    System.out.println("> Zombie muncul: " + z.getType());

    while (z.HP > 0 && player.getHP() > 0) {
        System.out.println("\n1. Serang");
        System.out.println("2. Kabur");
        System.out.print("Pilihanmu: ");
        int c = input.nextInt();

        if (c == 1) { // JIKA MEMILIH SERANG
            if (z instanceof Dynamite) {
                z.attack(player); 
                System.out.println("> Sekarang kamu bisa menyerang Dynamite Zombie!");
            }
            
            z.HP -= player.getDamage();
            System.out.println("> Kamu menyerang! Damage: " + player.getDamage());

            if (z.HP <= 0) {
                System.out.println("> Zombie mati!");
            } else {
                
                z.attack(player);
                
                
                if (rand.nextInt(10) < 2) {
                    System.out.println("> Kamu tergigit dalam perkelahian!");
                    player.setInfected(true);
                }
            }
        } 
        else if (c == 2) { // JIKA MEMILIH KABUR
            if (rand.nextBoolean()) { 
                System.out.println("> Berhasil kabur dari pertarungan!");
                return; // Keluar dari method battle dan lanjut ke turn berikutnya
            } else {
                System.out.println("> Gagal kabur! Zombie menyerangmu!");
                z.attack(player);
            }
>>>>>>> 41ae11cd3028d0fe414ac293297d191b9a548fb4
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
<<<<<<< HEAD
        int r = rand.nextInt(20);
        if (r == 0) {
            System.out.println("\n[EVENT] Menemukan kotak P3K!");
            player.setHP(Math.min(100, player.getHP() + 20));
        }
    }

=======
        int r = rand.nextInt(15);

        if (r == 0) {
            System.out.println("\n> Kamu mencium bau makanan dari balik reruntuhan...");
            delay();
            System.out.println("> Sekaleng sardine! +1 makanan.");
            foodStock++;
        }

        if (r == 1) {
            System.out.println("\n> Seseorang memanggil dari balik pintu berkarat...");
            delay();
            System.out.println("> \"Hei! Kamu masih hidup? Aku punya obat!\"");
            System.out.println("1. Tolong dia  2. Abaikan");
            int c = Integer.parseInt(input.nextLine().trim());
            if (c == 1) {
                System.out.println("> Seorang pria tua dengan kotak P3K. 'Terima kasih percaya padaku.' HP +20");
                player.setHP(player.getHP() + 20);
            } else {
                System.out.println("> Kamu berjalan menjauh. Suara pintu itu terus terngiang.");
            }
        }

        if (r == 2) {
            System.out.println("\n> Kamu menemukan jarum suntik di lantai...");
            delay();
            System.out.println("1. Suntikkan  2. Buang");
            int c = Integer.parseInt(input.nextLine().trim());
            if (c == 1) {
                if (rand.nextBoolean()) {
                    System.out.println("> Kamu merasa lebih baik! HP +15");
                    player.setHP(player.getHP() + 15);
                    if (player.GetInfected()) { player.setInfected(false); System.out.println("> Infeksi mereda!"); }
                } else {
                    System.out.println("> Jarum bekas! Terinfeksi!");
                    player.setInfected(true);
                }
            } else {
                System.out.println("> Kamu membuangnya.");
            }
        }

        if (r == 3) {
            System.out.println("\n> Kamu menemukan ransel kecil di pinggir jalan.");
            System.out.println("> Di dalamnya: 2 kaleng makanan! +2 makanan.");
            foodStock += 2;
        }

        if (r == 4) {
            System.out.println("\n> Hujan lebat turun tiba-tiba...");
            delay();
            System.out.println("> Zombies melambat. Kamu bisa beristirahat sejenak. HP +5, Stamina +10");
            player.setHP(player.getHP() + 5);
            player.setStamina(player.getStamina() + 10);
        }

        if (r == 5) {
            System.out.println("\n> Kamu mendengar siaran radio redup...");
            delay();
            System.out.println("> '...tolong siapapun yang mendengar... bunker di koordinat...' [terputus]");
            System.out.println("> Itu cukup untuk meyakinkanmu. Terus bergerak.");
        }

        if (r == 6) {
            System.out.println("\n> Zombie datang tiba-tiba dari gang sempit!");
            delay();
            battle();
        }
    }
    
>>>>>>> 41ae11cd3028d0fe414ac293297d191b9a548fb4
    static void hungerSystem() {
        player.setHunger(player.getHunger() + 5);
        if (player.getHunger() >= 50) {
            System.out.println("> Kamu Kelaparan! (HP -5)");
            player.setHP(player.getHP() - 5);
        }
    }

<<<<<<< HEAD
    static void delay() { try { Thread.sleep(1000); } catch (Exception e) {} }
    static void endingMati() { System.out.println("\n=== GAME OVER ===\nKota ini menjadi kuburanmu."); }
    static void endingZombie() { System.out.println("\n=== ENDING: INFECTED ===\nKamu sekarang bagian dari mereka."); }
=======
       
   static void endingMati() {
        System.out.println("\n==========================================");
        delay();
        System.out.println("> HP kamu habis...");
        delay();
        System.out.println("> Pandanganmu gelap. Lutut menyentuh tanah.");
        delay();
        System.out.println("> Di turn ke-" + turn + ", perjalananmu berakhir.");
        System.out.println("\n=============================");
        System.out.println("         GAME OVER            ");
        System.out.println("================================");
   }
   
   
    static void endingMenang() {
        System.out.println("\n==========================================");
        delay();
        System.out.println("> Kamu memasang baterai terakhir ke radio darurat...");
        delay();
        System.out.println("> Lampu merah berkedip. Lalu hijau.");
        delay();
        System.out.println("> 'SIARAN DARURAT AKTIF. KOORDINAT TERKIRIM.'");
        delay();
        System.out.println("> Kamu duduk di lantai bunker yang dingin.");
        delay();
        System.out.println("> Untuk pertama kali sejak semua ini dimulai... kamu tersenyum.");
        System.out.println("\n=====================================");
        System.out.println("         KAMU BERHASIL!               ");
        System.out.println("   Bantuan sedang dalam perjalanan.   ");
        System.out.println("=======================================");
        System.out.println("\nSelesai dalam " + turn + " turn.");
    }

    static void endingZombie() { System.out.println("> Kamu berubah jadi zombie...\nENDING: INFECTED"); }

    

   
    static void hide (){
        System.out.println("Kamu memilih untuk bersembunyi dari Zombie");
        delay();
        if (rand.nextBoolean()){
            System.out.println("Kamu berhasil bersembunyi dari para Zombie");
        
        }
        else{
            System.out.println("Kamu gagal bersembunyi!! zombie menemukanmu!!");
        }
        
    }
    
    static void runAway(){
        System.out.println("Kamu mencoba kabur...");
        delay();
        if (rand.nextBoolean()){
            System.out.println("Kamu berhasil kabur");
            delay();
            System.out.println("Stamina kamu berkurang 10..");
            player.setStamina(player.getStamina() - 10);
        
        }
        else{
            System.out.println("Kamu gagal kabur!!");
        }
        
    }
    
    static void rest(){
            System.out.println("Kamu memilih untuk beristirahat");
            delay();
            System.out.println("Stamina kamu bertambah 10..");
            System.out.println("HP kamu bertambah 5..");
    
        player.setStamina(player.getStamina() + 10);
        player.setHP(player.getHP() + 5);
    
    }
    
       
    
>>>>>>> 41ae11cd3028d0fe414ac293297d191b9a548fb4
}