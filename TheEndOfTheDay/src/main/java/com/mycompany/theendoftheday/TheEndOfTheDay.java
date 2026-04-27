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
    
    public static void main(String[] args) {
        Intro();
        gameloop();
    }
        public static void Intro() {
        System.out.println("Kamu terbangun di tengah-tengah kerumunan para Zombie!");
        delay();
        System.out.println("Hari mulai mencekam, apa yang akan kamu lakukan?");
        delay();
        }
        static void gameloop() {
    boolean gameRunning = true;

    while (gameRunning) {
        System.out.println("==================");
        System.out.println("\n=== TURN " + turn + " ===");
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
            
            // Peluang kecil berubah langsung jadi zombie tiap turn
            if (rand.nextInt(100) < 15) { 
                endingZombie();
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
                // Zombie menyerang balik jika belum mati
                z.attack(player);
                
                // Peluang terinfeksi saat kena serang
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
        int r = rand.nextInt(5);
        if (r == 0) return new Walker();
        else if (r == 1) return new Runner();
        else if (r == 2) return new Tank();
        else return new Dynamite();
    }

    static void randomEvent() {
        int r = rand.nextInt(20);
        if (r == 0) { System.out.println("> Kamu menemukan makanan!"); player.setHunger(player.getHunger() - 10); }
        if (r == 1) {
            System.out.println("> Kamu bertemu survivor...");
            System.out.println("1. Tolong\n2. Abaikan");
            int c = input.nextInt();
            if (c == 1) { System.out.println("> Dia membantu kamu!"); player.setHP(player.getHP() + 10); }
        }
        
        if (r == 2) {
        System.out.println("> Kamu menemukan persediaan medis bekas...");
        System.out.println("1. Ambil\n2. Biarkan");
        int c = input.nextInt();
        if (c == 1) {
            System.out.println("> Jarum suntik bekas melukaimu! Kamu terinfeksi virus.");
            player.setInfected(true);
        }
    }
        else;
}
    static void hungerSystem() {
        player.setHunger(player.getHunger() + 5);
        if (player.getHunger() >= 50) { System.out.println("> Kamu kelaparan!"); player.setHP(player.getHP() - 5); }
    }

       
    static void endingMati() { System.out.println("> Kamu mati...\nGAME OVER"); }
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