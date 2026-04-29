/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.theendoftheday;

/**
 *
 * @author Zaid 
 */
// dikulum dikunyah
import java.util.Scanner;
import java.util.Random;
public class TheEndOfTheDay {
    static Scanner input = new Scanner(System.in);
    static Random rand = new Random();
    static Player player = new Player();
    static int turn = 1;
   
    static int CDFW = 0;
    
   static Mission mission = new Mission();
   static Location currentLocation = null;
   
   static int foodStock = 0;
    
    public static void main(String[] args) {
        Intro();
        gameloop();
        
    }
        public static void Intro() {
            
        System.out.println("====================");
        System.out.println("The End Of The Day");
        System.out.println("====================");
        
        System.out.println("\nKamu terbangun di jalanan yang sunyi...");
        delay();
        System.out.println("Darah di tanganmu. Suara erangan di kejauhan.");
        delay();
        System.out.println("Kamu tidak ingat apa-apa. Tapi satu hal jelas:");
        delay();
        System.out.println("> Kamu harus keluar dari kota ini.");
        delay();
        System.out.println("\nAda radio darurat di bunker bawah kota.");
        System.out.println("Aktifkan radio itu — bantuan akan datang.");
        delay();
        System.out.println("> Tapi jalan menuju sana tidak mudah...");
        delay();
        mission.showMissionStatus();
        }
        static void gameloop() {
    boolean gameRunning = true;

    while (gameRunning) {
        System.out.println("==================");
        System.out.println("\n=== TURN " + turn + " ===");
        mission.showMissionStatus();
        showStatus();
        
        actionMenu();
        randomEvent();
        hungerSystem();
        turn++;
        CDFW--;
        
        
        if (player.getHP()<=0){
            endingMati ();
            break;
        }
        
        if (player.GetInfected()) {
            System.out.println("> PERINGATAN: Virus zombie menyebar di tubuhmu! HP berkurang.");
            player.setHP(player.getHP() - 10); // Penalti HP karena infeksi
            
             if (mission.isAllMissionComplete()) {
                endingMenang();
                break;
            }

        }
    }
}
    
    static void delay() {
    try {
        Thread.sleep(1000); // Jeda selama 1 detik (1000 milidetik)
    }
    catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    } 
    }

    static void showStatus() {
        System.out.println("HP; " + player.getHP());
        System.out.println("Stamina; " + player.getStamina());
        System.out.println("Hunger; " + player.getHunger());
        System.out.println("Weapon; " + player.getWeapon());
        
        
        
    }
    static void actionMenu () {
        System.out.println("1. Cari Senjata");
        System.out.println("2. Serang Zombie");
        System.out.println("3. Sembunyi");
        System.out.println("4. Lari");
        System.out.println("5. Istirahat");
        System.out.print("Apa pilihan kamu (1-5)? ");
        int pilihan = input.nextInt();
        
        
        switch(pilihan){
            case 1 : 
                if (CDFW <= 0){
                    findweapon();
                    
                }else{
                    
                    System.out.println("Ti1dak bisa Mengambil lagi Senjata tunggu "+CDFW+ " Turn lagi");
                } 
                break;
            case 2 : battle(); break;
            case 3 : hide(); break;
            case 4 : runAway(); break;
            case 5 : rest(); break;
           
        }
    }
    
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
        CDFW = 3;
        System.out.println("Mencari senjata...");
        delay();
        int s = rand.nextInt(11);{
        if(s==0){
            player.setWeapon("Pisau");
            player.setDamage(15);
        }
        else if (s==1){
            player.setWeapon("Bedog");
            player.setDamage(20);
    }   else if (s==2){
            player.setWeapon("Tangan Kosong");
            player.setDamage(10);
    }
        else if (s==3){
            player.setWeapon("Pistol");
            player.setDamage(30);   
        }
        
        else if (s==4) {
            player.setWeapon("Tangan Kosong");
            player.setDamage(10);   
                }
        
        else if (s==5) {
            player.setWeapon("Tangan Kosong");
            player.setDamage(10);   
                }
        
        else if (s==6) {
            player.setWeapon("Tangan Kosong");
            player.setDamage(10);   
                }
        
        else if (s==7) {
            player.setWeapon("Pisau");
            player.setDamage(15);   
                }
        
        else if (s==8) {
            player.setWeapon("Pisau");
            player.setDamage(15);   
                }
        
        else {
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
        }
    }
}
     
     static Zombie spawnZombie() {
        int r = rand.nextInt(4);
        if (r == 0) return new Walker();
        else if (r == 1) return new Runner();
        else if (r == 2) return new Tank();
        else return new Dynamite();
    }
 
    static Boss spawnBoss(String bossName) {
        switch (bossName) {
            case "Zombie Dokter":
                return new Boss("Zombie Dokter", 100, 20,
                    new String[]{"Pasien... tidak boleh... kabur...",
                                 "OPERASI DARURAT... TANPA BIUS!"});
            case "Zombie Guru":
                return new Boss("Zombie Guru", 80, 15,
                    new String[]{"Kelas... belum... selesai...",
                                 "NILAI KAMU... NOL BESAR!"});
            case "Zombie Kasir":
                return new Boss("Zombie Kasir", 70, 18,
                    new String[]{"Harga... naik... terus...",
                                 "DISKON HARI INI... NYAWAMU!"});
            case "Zombie Polisi":
                return new Boss("Zombie Polisi", 120, 22,
                    new String[]{"FREEZE... di bawah... tangkapan...",
                                 "TIDAK ADA YANG LOLOS... DARI HUKUM!"});
            default: // Zombie Komandan
                return new Boss("Zombie Komandan", 200, 30,
                    new String[]{"Kamu pikir bisa mengaktifkan radio itu?! TIDAK!",
                                 "SEMUANYA AKAN BERAKHIR... SEKARANG!"});
        }
    }
    
    
    

    static void randomEvent() {
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
    
    static void hungerSystem() {
        player.setHunger(player.getHunger() + 5);
        if (player.getHunger() >= 50) { System.out.println("> Kamu kelaparan!"); player.setHP(player.getHP() - 5); }
    }

       
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
    
       
    
}