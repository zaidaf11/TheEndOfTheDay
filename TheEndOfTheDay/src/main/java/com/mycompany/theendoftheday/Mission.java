/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.theendoftheday;

/**
 *
 * @author Zaid Akmal
 */


import java.util.ArrayList;
import java.util.List;

public class Mission{

    private List<String> collectedItems = new ArrayList<>();   // item utama
    private List<String> collectedClues = new ArrayList<>();   // item pendukung

    private Location[] locations;

    public Mission() {
       
        locations = new Location[]{
            new Location(
                "Rumah Sakit",
                "Koridor putih bernoda merah. Bau antiseptik bercampur sesuatu yang lebih busuk.",
                "Pintu UGD terbuka perlahan...\n> Lampu darurat berkedip. Erangan samar dari balik tirai.",
                "Obat Antivirus", 2,
                // Clue woi ini cluei
                "Kartu Akses",
                "Sebuah kartu akses berlogo kepolisian. Mungkin terjatuh dari seseorang...\n> Di baliknya tertulis: 'Loker L-09, kantor polisi sektor 4'",
                // Clue buat item utama
                null, null,
                "Zombie Dokter"// nama boss
            ),
            new Location(
                "Sekolah",
                "Gedung sekolah yang sunyi. Meja-meja berserakan, papan tulis penuh coretan panik.",
                "Kamu mendorong pintu sekolah yang berderit...\n> Udara pengap menyambutmu. Di sudut lorong, sesuatu bergerak.",
                "Peta Kota", 1,
                
                "Daftar Belanja Lama",
                "Selembar kertas lusuh dengan daftar belanja. Di bawahnya ada catatan:\n> 'Stok darurat di Minimarket Jaya - rak paling belakang, di balik kardus merah'",
                
                "Kertas Kode 7887",
                "Ada lemari besi terkunci di ruang guru. Sepertinya butuh kode angka untuk membukanya.",
                "Zombie Guru"
            ),
            new Location(
                "Minimarket",
                "Rak-rak terbalik, lantai penuh pecahan kaca dan kemasan makanan.",
                "Kamu masuk lewat jendela yang sudah pecah...\n> Bau busuk memenuhi udara. Tapi di suatu sudut, pasti masih ada yang tersisa.",
                "Kaleng Makanan", 3,
                
                "Kunci Loker L-09",
                "Sebuah kunci kecil dengan tag 'L-09'. Kamu ingat pernah melihat deretan loker di kantor polisi.",
                
                "Daftar Belanja Lama",
                "Kamu menggeledah rak-rak yang berantakan, tapi tidak tahu harus cari di mana.\n> Sepertinya kamu butuh petunjuk lokasi stok tersembunyi di sini.",
                "Zombie Kasir"
            ),
            new Location(
                "Kantor Polisi",
                "Sel-sel terbuka, senjata berserakan tanpa amunisi. Seseorang sudah lebih dulu ke sini.",
                "Kamu melewati palang besi yang sudah dibengkokkan...\n> Di meja resepsionis, bayangan bergerak sangat lambat.",
                "Amunisi", 2,
                
                "Kertas Kode 7887",
                "Secarik kertas yang tertempel di papan pengumuman:\n> 'Kode brankas darurat: 7887' — tulisan tangan, tampak terburu-buru.",
                
                "Kunci Loker L-09",
                "Ada deretan loker di ruang belakang. Salah satunya pasti berisi amunisi.\n> Tapi semua loker terkunci. Kamu butuh kunci yang sesuai.",
                "Zombie Polisi"
            ),
            new Location(
                "Bunker",
                "Pintu baja tebal tersembunyi di bawah tanah. Ini tempat terakhir.",
                "Kamu menemukan pintu bunker...\n> Berat. Gelap. Tapi di dalamnya ada harapan terakhir.",
                "Baterai Radio", 1,
                
                null, null,
                
                "Semua Item",
                "Pintu bunker terkunci dari dalam. Kamu butuh semua perlengkapan sebelum bisa masuk.",
                "Zombie Komandan"
            )
        };
    }


    public void addClue(String clueName) {
        if (!collectedClues.contains(clueName)) {
            collectedClues.add(clueName);
        }
    }

    public boolean hasClue(String clueName) {
        return collectedClues.contains(clueName);
    }

    public boolean canCollectItem(Location loc) {
        String needed = loc.getClueNeededName();
        if (needed == null) return true;
        if (needed.equals("Semua Item")) return isAllItemsExceptBunker();
        return collectedClues.contains(needed);
    }

    public boolean isAllItemsExceptBunker() {
        for (int i = 0; i < locations.length - 1; i++) {
            if (!locations[i].isItemComplete()) return false;
        }
        return true;
    }



    public void addItem(String itemName) {
        if (!collectedItems.contains(itemName)) {
            collectedItems.add(itemName);
        }
    }

    public boolean hasItem(String itemName) {
        return collectedItems.contains(itemName);
    }

    public boolean isAllMissionComplete() {
        for (Location loc : locations) {
            if (!loc.isItemComplete()) return false;
        }
        return true;
    }

    

    public void showMissionStatus() {
        System.out.println("\n========== STATUS MISI ==========");
        System.out.println(">> ITEM UTAMA:");
        for (Location loc : locations) {
            String status = loc.isItemComplete() ? "[v]" : "[ ]";
            System.out.println("  " + status + " " + loc.getName() + " -> " + loc.getItemStatus());
        }
        System.out.println("\n>> CLUE TERKUMPUL:");
        if (collectedClues.isEmpty()) {
            System.out.println("  (belum ada)");
        } else {
            for (String clue : collectedClues) {
                System.out.println("  - " + clue);
            }
        }
        System.out.println("==================================");
    }

    public Location[] getLocations() { return locations; }
    public Location getLocation(int index) { return locations[index]; }
    public List<String> getCollectedItems() { return collectedItems; }
    public List<String> getCollectedClues() { return collectedClues; }
}