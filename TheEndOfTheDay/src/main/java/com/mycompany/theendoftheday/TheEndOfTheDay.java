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
        System.out.print("\f"); 
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
        System.out.println("[ HP: " + player.getHP()
                + " | Stamina: " + player.getStamina()
                + " | Lapar: " + player.getHunger() + "/100"
                + " | Senjata: " + player.getWeapon()
                + " | Makanan: " + foodStock + " kaleng ]");
        if (player.GetInfected()) System.out.println("  !! STATUS: TERINFEKSI VIRUS !!");
        if (currentLocation != null)
            System.out.println("  Lokasi: " + currentLocation.getName());
        else
            System.out.println("  Lokasi: Jalanan");
    }
    
    
    
    
    static void actionMenu() {
        System.out.println("\n--- APA YANG KAMU LAKUKAN? ---");
        System.out.println("1. Pindah Lokasi");
        System.out.println("2. Jelajahi Area");
        System.out.println("3. Cari Senjata");
        System.out.println("4. Makan (Stok: " + foodStock + " kaleng)");
        System.out.println("5. Sembunyi");
        System.out.println("6. Lari");
        System.out.println("7. Istirahat");
        System.out.println("8. Cek Status Misi");
        System.out.print("Pilihanmu (1-8): ");

        int pilihan;
        try { pilihan = readInt(); }
        catch (NumberFormatException e) { pilihan = -1; }
        switch (pilihan) {
            case 1: pindahLokasi(); break;
            case 2: jelajahiArea(); break;
            case 3:
                if (CDFW <= 0) findweapon();
                else System.out.println("> Tunggu " + CDFW + " turn lagi untuk cari senjata.");
                break;
            case 4: makan(); break;
            case 5: hide(); break;
            case 6: runAway(); break;
            case 7: rest(); break;
            case 8: mission.showMissionStatus(); break;
            default: System.out.println("> Pilihan tidak valid.");
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
    
     static void pindahLokasi() {
        Location[] locations = mission.getLocations();

        System.out.println("\n=== PILIH LOKASI ===");
        for (int i = 0; i < locations.length; i++) {
            Location loc = locations[i];
            String tag;
            if (loc.isItemComplete() && loc.isBossDefeated()) tag = "[SELESAI]";
            else if (loc.isItemComplete() && !loc.isBossDefeated()) tag = "[BOSS MENUNGGU!]";
            else tag = "[BELUM SELESAI]";
            System.out.println((i + 1) + ". " + loc.getName() + " " + tag);
            System.out.println("   " + loc.getDescription());
        }
        System.out.println("0. Batal");
        System.out.print("Pilih (0-" + locations.length + "): ");

        int pilihan;
        try { pilihan = readInt(); }
        catch (NumberFormatException e) { pilihan = -1; }
        if (pilihan == 0) { System.out.println("> Kamu tidak jadi pindah."); return; }
        if (pilihan < 1 || pilihan > locations.length) { System.out.println("> Tidak valid."); return; }

        Location tujuan = locations[pilihan - 1];
        currentLocation = tujuan;

        System.out.println("\n> Kamu bergerak menuju " + tujuan.getName() + "...");
        delay();

        // Zombie acak bisa muncul di jalanan saat pindah lokasi
        if (rand.nextInt(3) == 0) {
            System.out.println("> Di perjalanan, zombie menghadangmu!");
            delay();
            battle();
            if (player.getHP() <= 0) return;
        }

        System.out.println("\n" + tujuan.getEnterDialog());
        delay();
        player.setStamina(player.getStamina() - 5);
        System.out.println("> Stamina -5 karena perjalanan.");
    }
    
    static void jelajahiArea() {
        if (currentLocation == null) {
            System.out.println("> Kamu berada di jalanan. Pilih lokasi dulu untuk dijelajahi.");
            return;
        }

        Location loc = currentLocation;
        System.out.println("\n> Kamu menjelajahi " + loc.getName() + "...");
        delay();

        int zombieChance = 10 + (turn / 5) * 5; // Makin lama makin sering (max ~80%)
        if (rand.nextInt(100) < zombieChance) {
            System.out.println("> Kamu mendengar suara langkah berat...");
            delay();
            System.out.println("> Zombie muncul dari kegelapan!");
            battle();
            if (player.getHP() <= 0) return;
        }
    
    if (loc.hasClueStored() && !loc.isClueFound()) {
            if (rand.nextInt(2) == 0) { // 50% chance ketemu clue tiap jelajah
                System.out.println("\n> Di antara reruntuhan, kamu menemukan sesuatu...");
                delay();
                System.out.println("> [CLUE DITEMUKAN: " + loc.getClueStoredName() + "]");
                System.out.println("> " + loc.getClueStoredDesc());
                System.out.print("> Ambil? (1=Ya / 2=Tidak): ");
                int c = readInt();
                if (c == 1) {
                    mission.addClue(loc.getClueStoredName());
                    loc.setClueFound(true);
                    System.out.println("> Kamu menyimpan " + loc.getClueStoredName() + ".");
                } else {
                    System.out.println("> Kamu membiarkannya. Mungkin berguna nanti.");
                }
            } else {
                System.out.println("> Kamu menggeledah area ini tapi tidak menemukan apa-apa yang menarik.");
            }
        }
    
    if (!loc.isItemComplete()) {
            System.out.println("\n> Kamu mencari item utama di " + loc.getName() + "...");
            delay();

            if (loc.needsClue() && !loc.getClueNeededName().equals("Semua Item")
                    && !mission.hasClue(loc.getClueNeededName())) {
                System.out.println("> " + loc.getClueNeededHint());
                System.out.println("> [Butuh clue: " + loc.getClueNeededName() + "]");
                return;
            }

            if (loc.needsClue() && loc.getClueNeededName().equals("Semua Item")
                    && !mission.isAllItemsExceptBunker()) {
                System.out.println("> " + loc.getClueNeededHint());
                System.out.println("> [Kumpulkan semua item dari 4 lokasi lainnya terlebih dahulu!]");
                return;
            }            // Punya clue → bisa akses item, tapi masih ada chance random zombie
            System.out.println("> Kamu menuju tempat penyimpanan...");
            delay();
            if (rand.nextInt(100) < 40) {
                System.out.println("> Zombie menjaga item ini!");
                battle();
                if (player.getHP() <= 0) return;
            }

            printItemAccessDialog(loc);
            boolean berhasil = loc.collectItem();
            if (berhasil) {
                System.out.println("> [" + loc.getItemName() + " didapat! "
                        + loc.getItemCollected() + "/" + loc.getItemRequired() + "]");

                if (loc.isItemComplete()) {
                mission.addItem(loc.getItemName());
                 printItemCollectedDialog(loc.getItemName());
                 System.out.println("\n>>> Semua " + loc.getItemName()
                 + " di " + loc.getName() + " terkumpul!");

    // ↓ INI BOSS TRIGGERNYA ↓
                 System.out.println("\n>>> GEMURUH DARI DALAM "
                + loc.getName().toUpperCase() + "! <<<");
                delay();
                System.out.println("> " + loc.getBossName() + " muncul menghalangimu!");
                 delay();
                 loc.setBossTriggered(true);
                bossBattle(loc); // ← langsung panggil boss battle
}
            }
        } else if (!loc.isBossDefeated()) {
            System.out.println("> Semua item sudah diambil. " + loc.getBossName() + " masih mengintai...");
        } else {
            System.out.println("> " + loc.getName() + " sudah bersih. Tidak ada yang tersisa di sini.");
            if (rand.nextInt(5) == 0) {
                System.out.println("> Kamu menemukan kaleng makanan tersisa! +1 makanan.");
                foodStock++;
            }
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
    
    static void bossBattle(Location loc) {
        Boss boss = spawnBoss(loc.getBossName());

        System.out.println("\n==========================");
        System.out.println("  BOSS BATTLE: " + loc.getBossName());
        System.out.println("=============================");
        boss.printIntroDialog();
        delay();

        while (boss.HP > 0 && player.getHP() > 0) {
            System.out.println("\n[ Boss HP: " + boss.HP + "/" + boss.getMaxHP()
                    + " | Player HP: " + player.getHP() + " ]");
            if (boss.getPhase() == 2) System.out.println("  !! FASE 2 - BOSS MENGAMUK !!");
            System.out.println("1. Serang");
            System.out.println("2. Kabur dari lokasi");
            System.out.print("Pilihanmu: ");
            int c = readInt();

            if (c == 1) {
                boss.HP -= player.getDamage();
                System.out.println("> Kamu menyerang! Damage: " + player.getDamage());

                if (boss.HP <= 0) {
                    System.out.println("\n> " + boss.getBossName() + " ROBOH!");
                    System.out.println("> \"" + getBossDeathDialog(loc.getBossName()) + "\"");
                    loc.setBossDefeated(true);
                    loc.setBossTriggered(false);
                    System.out.println("> " + loc.getName() + " kini aman.");
                } else {
                    boss.attack(player);
                    if (rand.nextInt(10) < 3) {
                        System.out.println("> Kamu tergigit boss! Terinfeksi!");
                        player.setInfected(true);
                    }
                }
                
                if (loc.getBossName().equals("Zombie Komandan")) {
                        System.out.println("\n> Di balik tubuh Komandan yang roboh, kamu melihatnya...");
                        delay();
                        System.out.println("> RADIO DARURAT. Masih menyala. Baterai penuh.");
                        delay();
                        System.out.println("> Tanganmu gemetar saat menekan tombol siaran.");
                        endingMenang();
                        
                       
            } else if (c == 2) {
                if (rand.nextBoolean()) {
                    System.out.println("> Kamu berhasil kabur! Boss tetap mengintai di sini.");
                    currentLocation = null;
                    return;
                } else {
                    System.out.println("> Kabur gagal! Boss menghadangmu!");
                    boss.attack(player);
                }
            }

            if (player.getHP() <= 0) return;
        }
        }
    }
    
     static void battle() {
    Zombie z = spawnZombie(); 
    System.out.println("> Zombie muncul: " + z.getType());

    while (z.HP > 0 && player.getHP() > 0) {
        System.out.println("\n1. Serang");
        System.out.println("2. Kabur");
        System.out.print("Pilihanmu: ");
        int c = readInt();

        if (c == 1) { // JIKA MEMILIH SERANG
            if (z instanceof Dynamite) {
                z.attack(player); 
                System.out.println("> Sekarang kamu bisa menyerang Dynamite Zombie!");
            }
            
            z.HP -= player.getDamage();
            System.out.println("> Kamu menyerang! Damage: " + player.getDamage());

             if (z.HP <= 0) {
                    System.out.println("> Zombie mati!");
           
                    if (rand.nextInt(6) == 0) {
                        System.out.println("> Zombie menjatuhkan kaleng makanan! +1 makanan.");
                        foodStock++;
                    }
                
                z.attack(player);
                
                
                if (rand.nextInt(10) < 2) {
                    System.out.println("> Kamu tergigit dalam perkelahian!");
                    player.setInfected(true);
                }
            }
        } 
        else if (c == 2) {
            if (rand.nextBoolean()) { 
                System.out.println("> Berhasil kabur dari pertarungan!");
                return; 
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
            default: 
                return new Boss("Zombie Komandan", 200, 30,
                    new String[]{"Kamu pikir bisa mengaktifkan radio itu?! TIDAK!",
                                 "SEMUANYA AKAN BERAKHIR... SEKARANG!"});
        }
    }
    
    static void printItemAccessDialog(Location loc) {
        delay();
        switch (loc.getItemName()) {
            case "Obat Antivirus":
                System.out.println("> Kamu menuju ruang penyimpanan obat dengan kartu akses itu...");
                System.out.println("> Pintu berbunyi 'klik'. Di dalam, rak berisi suntikan antivirus.");
                break;
            case "Peta Kota":
                System.out.println("> Kamu mengetik kode 7887 di lemari besi ruang guru...");
                System.out.println("> Lemari terbuka. Di dalam ada peta kota yang sudah dilipat rapi.");
                break;
            case "Kaleng Makanan":
                System.out.println("> Kamu menerobos ke rak paling belakang, menyingkirkan kardus merah...");
                System.out.println("> Di baliknya, tumpukan kaleng makanan yang belum terjamah!");
                break;
            case "Amunisi":
                System.out.println("> Kunci L-09 masuk dengan sempurna...");
                System.out.println("> Di dalam loker, kotak amunisi masih tersegel rapat.");
                break;
            case "Baterai Radio":
                System.out.println("> Kamu masuk ke dalam bunker. Radio darurat ada di sana.");
                System.out.println("> Tinggal satu langkah lagi...");
                break;
        }
        delay();
    }
    
    static String getBossDeathDialog(String bossName) {
        switch (bossName) {
            case "Zombie Dokter":  return "Diagnosis terakhir... kamu selamat...";
            case "Zombie Guru":    return "Kelas... di... misikan...";
            case "Zombie Kasir":   return "Struk belanjaan... sudah dicetak...";
            case "Zombie Polisi":  return "Kamu... bebas... pergi...";
            default:               return "TIDAK MUNGKIN... PASUKANKU... GAGAL...";
        }
    }
    
    static void printItemCollectedDialog(String itemName) {
        delay();
        switch (itemName) {
            case "Peta Kota":
                System.out.println("> Kamu membuka peta. Ada tanda X di beberapa titik kota.");
                System.out.println("> Salah satunya: Bunker bawah tanah. Ini yang kamu cari.");
                break;
            case "Obat Antivirus":
                System.out.println("> Dua dosis antivirus. Cukup untuk memperlambat infeksi.");
                System.out.println("> Kamu menyimpannya hati-hati. Ini mungkin satu-satunya harapan.");
                break;
            case "Kaleng Makanan":
                System.out.println("> Tiga kaleng. Cukup untuk beberapa hari ke depan.");
                System.out.println("> Di salah satunya ada tulisan tangan: 'Jangan menyerah'.");
                foodStock += 3; // Kaleng makanan langsung masuk ke food stock!
                System.out.println("> +3 kaleng makanan ditambahkan ke inventory!");
                break;
            case "Amunisi":
                System.out.println("> Kamu mengisi senjata. Untuk pertama kali, kamu merasa lebih siap.");
                System.out.println("> Satu langkah lagi menuju bunker.");
                break;
            case "Baterai Radio":
                System.out.println("> INI DIA. Baterai Radio terakhir.");
                System.out.println("> Kamu merasakannya — harapan nyata untuk pertama kalinya.");
                break;
        }
        delay();
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
            int c = readInt();
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
            int c = readInt();
            if (c == 1) {
                    System.out.println("> Kamu merasa lebih baik! HP +15");
                    player.setHP(player.getHP() + 15);
                    if (player.GetInfected()) { player.setInfected(false); System.out.println("> Infeksi mereda!"); }
                
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
        player.setHP(player.getHP() + 10);
    
    }
    
    static int readInt() {
    while (true) {
        try {
            String line = input.nextLine().trim();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.print("> Input tidak valid, masukkan angka: ");
        }
    }
}
    
       
    
}