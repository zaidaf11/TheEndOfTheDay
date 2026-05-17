/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.theendoftheday;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author DELL
 */
public class Gameconsole {

    private static final int CONSOLE_WIDTH  = 620;
    private static final int CONSOLE_HEIGHT = 780;
    private static final int IMAGE_WIDTH    = 460;

    private static final Color BG_COLOR     = new Color(10, 10, 12);
    private static final Color TEXT_COLOR   = new Color(200, 220, 200);
    private static final Color INPUT_COLOR  = new Color(100, 220, 100);
    private static final Color SYSTEM_COLOR = new Color(150, 150, 170);
    private static final Color TITLE_COLOR  = new Color(220,  80,  60);
    private static final Color WARN_COLOR   = new Color(220, 180,  50);
    private static final Color CLUE_COLOR   = new Color(100, 180, 255);
    private static final Color STORY_COLOR  = new Color(210, 200, 175);
    private static final Color MENU_COLOR   = new Color(160, 200, 160);
    private static final Color BORDER_COLOR = new Color( 40,  60,  40);
    private static final Color IMG_BG_COLOR = new Color( 12,  15,  12);

    private static final Font F_CONSOLE = new Font("Courier New", Font.PLAIN, 13);
    private static final Font F_INPUT   = new Font("Courier New", Font.BOLD,  13);
    private static final Font F_CAPTION = new Font("Courier New", Font.BOLD,  12);
    private static final Font F_TAG     = new Font("Courier New", Font.PLAIN,  9);

    private JFrame         frame;
    private JTextPane      outputPane;
    private JTextField     inputField;
    private JLabel         imageLabel;
    private JLabel         captionLabel;
    private JLabel         sceneTagLabel;
    private StyledDocument doc;

    private PipedOutputStream pipeOut;
    private PipedInputStream  pipeIn;
    private PrintStream       consolePrintStream;

    // FIX 1: init "" bukan "intro" supaya loadSceneImage("intro") tidak diblok guard
    private volatile String currentScene    = "";
    private volatile String currentLocation = "jalanan";

    private static Gameconsole instance;
    private static Runnable    gameRunnable;

    private static final Map<String, String> SCENE_IMAGES = new HashMap<>();
    private static final Map<String, String> SCENE_LABELS = new HashMap<>();
    private static final Map<String, Color>  SCENE_COLORS = new HashMap<>();

    static {
        String[] all = {
            "intro",
            "jalanan","rumah_sakit","sekolah","minimarket","kantor_polisi","bunker",
            "jelajah_jalanan","jelajah_rumah_sakit","jelajah_sekolah",
            "jelajah_minimarket","jelajah_kantor_polisi","jelajah_bunker","cari_senjata","pindah_lokasi",
            "sembunyi","kabur","istirahat","makan",
            "battle","battle_walker","battle_runner","battle_tank","battle_dynamite",
            "boss_dokter","boss_guru","boss_kasir","boss_polisi","boss_komandan","boss_phase2",
            "event_makanan","event_orang","event_jarum","event_ransel",
            "event_hujan","event_radio","event_zombie_gang",
            "item_antivirus","item_peta","item_kaleng","item_amunisi","item_baterai",
            "clue_kartu_akses","clue_daftar_belanja","clue_kunci_loker","clue_kertas_kode","game_over","menang","terinfeksi"
        };
        for (String k : all) SCENE_IMAGES.put(k, "/images/" + k + ".png");

        SCENE_LABELS.put("intro",             "The End Of The Day"); //done
        SCENE_LABELS.put("jalanan",           "Jalanan Kota"); //done
        SCENE_LABELS.put("rumah_sakit",       "Rumah Sakit");//done
        SCENE_LABELS.put("sekolah",           "Sekolah"); //done
        SCENE_LABELS.put("minimarket",        "Minimarket"); //done
        SCENE_LABELS.put("kantor_polisi_1",     "Kantor Polisi"); //done
        SCENE_LABELS.put("bunker",            "Bunker"); //done
        SCENE_LABELS.put("jelajah_jalanan",       "Menjelajahi Jalanan"); //done
        SCENE_LABELS.put("jelajah_rumah_sakit",   "Menjelajahi Rumah Sakit"); //done
        SCENE_LABELS.put("jelajah_sekolah",       "Menjelajahi Sekolah"); //done
        SCENE_LABELS.put("jelajah_minimarket",    "Menjelajahi Minimarket"); //done
        SCENE_LABELS.put("jelajah_kantor_polisi", "Menjelajahi Kantor Polisi"); //done
        SCENE_LABELS.put("jelajah_bunker",        "Menjelajahi Bunker"); //done
        SCENE_LABELS.put("cari_senjata",      "Mencari Senjata");
        SCENE_LABELS.put("pindah_lokasi",     "Pindah Lokasi");
        SCENE_LABELS.put("sembunyi",          "Bersembunyi");
        SCENE_LABELS.put("istirahat",         "Beristirahat");
        SCENE_LABELS.put("makan",             "Makan");
        SCENE_LABELS.put("battle",            "!! PERTARUNGAN !!");
        SCENE_LABELS.put("battle_walker",     "!! Walker Zombie !!");
        SCENE_LABELS.put("battle_runner",     "!! Runner Zombie !!");
        SCENE_LABELS.put("battle_tank",       "!! Tank Zombie !!");
        SCENE_LABELS.put("battle_dynamite",   "!! Dynamite Zombie !!");
        SCENE_LABELS.put("boss_dokter",       "BOSS: Zombie Dokter"); //done
        SCENE_LABELS.put("boss_guru",         "BOSS: Zombie Guru"); //done
        SCENE_LABELS.put("boss_kasir",        "BOSS: Zombie Kasir"); 
        SCENE_LABELS.put("boss_polisi",       "BOSS: Zombie Polisi");
        SCENE_LABELS.put("boss_komandan",     "BOSS FINAL: Komandan!");
        SCENE_LABELS.put("boss_phase2",       "FASE 2 - MENGAMUK!!");
        SCENE_LABELS.put("event_makanan",     "Menemukan Makanan");
        SCENE_LABELS.put("event_orang",       "Orang Asing...");
        SCENE_LABELS.put("event_jarum",       "Jarum Suntik"); 
        SCENE_LABELS.put("event_ransel",      "Menemukan Ransel");
        SCENE_LABELS.put("event_hujan",       "Hujan Lebat");
        SCENE_LABELS.put("event_radio",       "Siaran Radio Redup");
        SCENE_LABELS.put("event_zombie_gang", "Zombie dari Gang!");
        SCENE_LABELS.put("item_antivirus",    "[+] Obat Antivirus!"); //done
        SCENE_LABELS.put("item_peta",         "[+] Peta Kota!"); //done
        SCENE_LABELS.put("item_kaleng",       "[+] Kaleng Makanan!"); //done
        SCENE_LABELS.put("item_amunisi",      "[+] Amunisi!"); //done
        SCENE_LABELS.put("item_baterai",      "[+] Baterai Radio!"); //done
        SCENE_LABELS.put("clue_kartu_akses",  "[!] Kartu Akses ditemukan!");  //done
        SCENE_LABELS.put("clue_daftar_belanja","[!] Daftar Belanja ditemukan!"); //done
        SCENE_LABELS.put("clue_kunci_loker",  "[!] Kunci Loker L-09 ditemukan!"); //done
        SCENE_LABELS.put("clue_kertas_kode",  "[!] Kertas Kode 7887 ditemukan!"); //done
        SCENE_LABELS.put("game_over",         "=== GAME OVER ==="); //done
        SCENE_LABELS.put("menang",            "=== KAMU BERHASIL! ==="); 
        SCENE_LABELS.put("terinfeksi",        "[!] TERINFEKSI VIRUS");

        Color cLoc  = new Color(140, 200, 140);
        Color cAct  = new Color(120, 170, 200);
        Color cBat  = new Color(220, 150,  50);
        Color cBoss = new Color(220,  60,  60);
        Color cEvt  = new Color(160, 200, 255);
        Color cItem = new Color( 80, 220, 100);
        
        //--Dapuy Rodoxs--
        
        for (String k : new String[]{"intro","jalanan","rumah_sakit","sekolah","minimarket","kantor_polisi","bunker"})
            SCENE_COLORS.put(k, cLoc);
        // Di loop cAct, HAPUS "jelajah" dari array, GANTI DENGAN:
        for (String k : new String[]{
            "jelajah_jalanan","jelajah_rumah_sakit","jelajah_sekolah",
            "jelajah_minimarket","jelajah_kantor_polisi","jelajah_bunker",
            "cari_senjata","pindah_lokasi","sembunyi","kabur","istirahat","makan"})
            SCENE_COLORS.put(k, cAct);
        for (String k : new String[]{"battle","battle_walker","battle_runner","battle_tank","battle_dynamite","event_zombie_gang"})
            SCENE_COLORS.put(k, cBat);
        for (String k : new String[]{"boss_dokter","boss_guru","boss_kasir","boss_polisi","boss_komandan","boss_phase2"})
            SCENE_COLORS.put(k, cBoss);
        for (String k : new String[]{"event_makanan","event_orang","event_jarum","event_ransel","event_hujan","event_radio"})
            SCENE_COLORS.put(k, cEvt);
        for (String k : new String[]{"item_antivirus","item_peta","item_kaleng","item_amunisi","item_baterai","clue_kartu_akses","clue_daftar_belanja","clue_kunci_loker","clue_kertas_kode","menang"})
            SCENE_COLORS.put(k, cItem);
        SCENE_COLORS.put("game_over",  new Color(200, 200, 200));
        SCENE_COLORS.put("terinfeksi", WARN_COLOR);
    }

    public static void main(String[] args) {
        launch(() -> { TheEndOfTheDay.Intro(); TheEndOfTheDay.gameloop(); });
    }

    public static void launch(Runnable game) {
        gameRunnable = game;
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> {
            instance = new Gameconsole();
            instance.buildUI();
            instance.redirectStreams();
            instance.startGame();
        });
    }

    private void buildUI() {
        frame = new JFrame("The End Of The Day");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JPanel consolePanel = new JPanel(new BorderLayout(0, 0));
        consolePanel.setBackground(BG_COLOR);
        consolePanel.setPreferredSize(new Dimension(CONSOLE_WIDTH, CONSOLE_HEIGHT));
        consolePanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 4));

        JLabel titleBar = new JLabel("  > THE END OF THE DAY  //  TERMINAL", SwingConstants.LEFT);
        titleBar.setFont(new Font("Courier New", Font.BOLD, 11));
        titleBar.setForeground(TITLE_COLOR);
        titleBar.setBackground(new Color(25, 10, 10));
        titleBar.setOpaque(true);
        titleBar.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));

        outputPane = new JTextPane();
        outputPane.setEditable(false);
        outputPane.setBackground(BG_COLOR);
        outputPane.setForeground(TEXT_COLOR);
        outputPane.setFont(F_CONSOLE);
        outputPane.setCaretColor(INPUT_COLOR);
        outputPane.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        doc = outputPane.getStyledDocument();

        JScrollPane scrollPane = new JScrollPane(outputPane);
        scrollPane.setBackground(BG_COLOR);
        scrollPane.getViewport().setBackground(BG_COLOR);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));

        JPanel inputPanel = new JPanel(new BorderLayout(0, 0));
        inputPanel.setBackground(new Color(5, 20, 5));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(4, 0, 4, 0)));
        JLabel prompt = new JLabel("  >> ");
        prompt.setFont(F_INPUT);
        prompt.setForeground(INPUT_COLOR);

        inputField = new JTextField();
        inputField.setFont(F_INPUT);
        inputField.setForeground(INPUT_COLOR);
        inputField.setBackground(new Color(5, 20, 5));
        inputField.setCaretColor(INPUT_COLOR);
        inputField.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        inputField.addActionListener(e -> handleInput());
        inputField.setEnabled(false);

        inputPanel.add(prompt, BorderLayout.WEST);
        inputPanel.add(inputField, BorderLayout.CENTER);

        consolePanel.add(titleBar,   BorderLayout.NORTH);
        consolePanel.add(scrollPane, BorderLayout.CENTER);
        consolePanel.add(inputPanel, BorderLayout.SOUTH);

        JPanel imagePanel = new JPanel(new BorderLayout(0, 0));
        imagePanel.setBackground(IMG_BG_COLOR);
        imagePanel.setPreferredSize(new Dimension(IMAGE_WIDTH, CONSOLE_HEIGHT));
        imagePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 0, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        sceneTagLabel = new JLabel("[ INTRO ]", SwingConstants.CENTER);
        sceneTagLabel.setFont(F_TAG);
        sceneTagLabel.setForeground(new Color(55, 75, 55));
        sceneTagLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setBackground(IMG_BG_COLOR);
        imageLabel.setOpaque(true);

        captionLabel = new JLabel("Memulai perjalanan...", SwingConstants.CENTER);
        captionLabel.setFont(F_CAPTION);
        captionLabel.setForeground(new Color(140, 200, 140));
        captionLabel.setBorder(BorderFactory.createEmptyBorder(6, 4, 0, 4));

        JPanel captionPanel = new JPanel(new BorderLayout());
        captionPanel.setBackground(IMG_BG_COLOR);
        captionPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));
        captionPanel.add(captionLabel, BorderLayout.CENTER);

        imagePanel.add(sceneTagLabel, BorderLayout.NORTH);
        imagePanel.add(imageLabel,    BorderLayout.CENTER);
        imagePanel.add(captionPanel,  BorderLayout.SOUTH);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG_COLOR);
        root.add(consolePanel, BorderLayout.CENTER);
        root.add(imagePanel,   BorderLayout.EAST);

        frame.setContentPane(root);
        frame.pack();
        frame.setLocationRelativeTo(null);

        // FIX 1: currentScene masih "" jadi load intro tidak diblok
        loadSceneImage("intro");
        currentScene = "intro";

        frame.setVisible(true);
        inputField.requestFocusInWindow();
    }

    private void redirectStreams() {
        OutputStream outStream = new OutputStream() {
            private StringBuilder buffer = new StringBuilder();
            @Override public void write(int b) {
                char c = (char) b;
                if (c == '\f') { SwingUtilities.invokeLater(() -> clearConsole()); return; }
                buffer.append(c);
                if (c == '\n') {
                    final String line = buffer.toString();
                    buffer = new StringBuilder();
                    SwingUtilities.invokeLater(() -> {
                        appendToConsole(line);
                        detectAndUpdateScene(line);
                        scrollToBottom();
                    });
                }
            }
            @Override public void write(byte[] b, int off, int len) {
                for (char c : new String(b, off, len).toCharArray()) write(c);
            }
        };
        consolePrintStream = new PrintStream(outStream, true);
        System.setOut(consolePrintStream);
        System.setErr(consolePrintStream);
        try {
            pipeOut = new PipedOutputStream();
            pipeIn  = new PipedInputStream(pipeOut);
            System.setIn(pipeIn);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void handleInput() {
        String text = inputField.getText().trim();
        inputField.setText("");
        if (text.isEmpty()) text = "";
        appendColored(">> " + text + "\n", INPUT_COLOR, false);
        try { pipeOut.write((text + "\n").getBytes()); pipeOut.flush(); }
        catch (IOException e) { appendColored("[ERROR: Input gagal]\n", TITLE_COLOR, false); }
    }

    private void appendToConsole(String text) {
        String t = text.trim();
        Color c = TEXT_COLOR; boolean bold = false;
        if (t.startsWith("===") || t.startsWith(">>>") || t.contains("GAME OVER") || t.contains("KAMU BERHASIL") || t.contains("BOSS BATTLE"))
            { c = TITLE_COLOR; bold = true; }
        else if (t.startsWith(">> "))
            { c = INPUT_COLOR; }
        else if (t.contains("MENGAMUK") || t.contains("FASE 2") || t.startsWith("> PERINGATAN") || t.startsWith("  !! STATUS"))
            { c = WARN_COLOR; bold = true; }
        else if (t.contains("CLUE DITEMUKAN") || t.startsWith("[CLUE"))
            { c = CLUE_COLOR; bold = true; }
        else if (t.startsWith("> ") || t.startsWith(">\""))
            { c = STORY_COLOR; }
        else if (t.startsWith("[ HP") || t.startsWith("  Lokasi"))
            { c = SYSTEM_COLOR; }
        else if (t.matches("^[1-8]\\..+"))
            { c = MENU_COLOR; }
        else if (t.startsWith("Pilihanmu") || t.startsWith("Pilih ("))
            { c = INPUT_COLOR; }
        appendColored(text, c, bold);
    }

    private void appendColored(String text, Color color, boolean bold) {
        try {
            SimpleAttributeSet a = new SimpleAttributeSet();
            StyleConstants.setForeground(a, color);
            StyleConstants.setBold(a, bold);
            doc.insertString(doc.getLength(), text, a);
        } catch (BadLocationException ignored) {}
    }

    private void clearConsole() {
        try { doc.remove(0, doc.getLength()); } catch (BadLocationException ignored) {}
    }

    private void scrollToBottom() { outputPane.setCaretPosition(doc.getLength()); }

    private void detectAndUpdateScene(String raw) {
        String L = raw.toLowerCase().trim();

        // Ending
        if (L.contains("game over") || L.contains("hp kamu habis") || L.contains("pandanganmu gelap"))
            { changeScene("game_over"); return; }
        if (L.contains("kamu berhasil") || L.contains("bantuan sedang dalam perjalanan") || L.contains("siaran darurat aktif"))
            { changeScene("menang"); return; }

        // Infeksi
        if (L.contains("virus zombie menyebar") || L.contains("!! status: terinfeksi"))
            { changeScene("terinfeksi"); return; }

        // Boss fase 2
        if (L.contains("fase 2") || L.contains("boss mengamuk") || L.contains("serangan semakin brutal"))
            { changeScene("boss_phase2"); return; }

        // Boss — deteksi dari header "BOSS BATTLE: ..."
        if (L.contains("boss battle:")) {
            if      (L.contains("dokter"))   { changeScene("boss_dokter");   return; }
            else if (L.contains("guru"))     { changeScene("boss_guru");     return; }
            else if (L.contains("kasir"))    { changeScene("boss_kasir");    return; }
            else if (L.contains("polisi"))   { changeScene("boss_polisi");   return; }
            else if (L.contains("komandan")) { changeScene("boss_komandan"); return; }
        }
        // Dialog intro boss sebagai fallback
        if (L.contains("pasien") && L.contains("kabur"))      { changeScene("boss_dokter");   return; }
        if (L.contains("kelas") && L.contains("selesai"))     { changeScene("boss_guru");     return; }
        if (L.contains("harga") && L.contains("naik"))        { changeScene("boss_kasir");    return; }
        if (L.contains("freeze") && L.contains("tangkapan"))  { changeScene("boss_polisi");   return; }
        if (L.contains("mengaktifkan radio itu"))              { changeScene("boss_komandan"); return; }

        // Battle per tipe
        if (L.contains("zombie muncul: walker"))                              { changeScene("battle_walker");   return; }
        if (L.contains("zombie muncul: runner"))                              { changeScene("battle_runner");   return; }
        if (L.contains("zombie muncul: tank"))                                { changeScene("battle_tank");     return; }
        if (L.contains("zombie muncul: dynamite") || L.contains("dynamite zombie")) { changeScene("battle_dynamite"); return; }
        if (L.contains("zombie muncul:") || L.contains("zombie menghadangmu") || L.contains("zombie menjaga"))
            { changeScene("battle"); return; }

        // FIX 3: Battle selesai → langsung balik ke lokasi, tidak tunggu turn baru
        if (L.contains("zombie mati!") || L.contains("roboh!") || L.contains("berhasil kabur") || L.contains("kini aman"))
            { changeScene(currentLocation); return; }

        // Item
        if (L.contains("dua dosis antivirus"))                          { changeScene("item_antivirus"); return; }
        if (L.contains("tanda x di beberapa titik"))                    { changeScene("item_peta");      return; }
        if (L.contains("jangan menyerah") || L.contains("+3 kaleng"))  { changeScene("item_kaleng");    return; }
        if (L.contains("kotak amunisi masih tersegel"))                 { changeScene("item_amunisi");   return; }
        if (L.contains("ini dia. baterai radio"))                       { changeScene("item_baterai");   return; }

        // Clue
        if (L.contains("kamu menyimpan kartu akses") || L.contains("clue ditemukan: kartu akses"))
            { changeScene("clue_kartu_akses");   return; }
        if (L.contains("kamu menyimpan daftar belanja") || L.contains("clue ditemukan: daftar belanja"))
            { changeScene("clue_daftar_belanja"); return; }
        if (L.contains("kamu menyimpan kunci loker") || L.contains("clue ditemukan: kunci loker"))
            { changeScene("clue_kunci_loker");   return; }
        if (L.contains("kamu menyimpan kertas kode") || L.contains("clue ditemukan: kertas kode"))
            { changeScene("clue_kertas_kode");   return; }

        // Random event
        if (L.contains("sekaleng sardine") || L.contains("bau makanan dari balik reruntuhan")) { changeScene("event_makanan");     return; }
        if (L.contains("seseorang memanggil") || L.contains("hei! kamu masih hidup"))           { changeScene("event_orang");       return; }
        if (L.contains("jarum suntik di lantai"))                                                { changeScene("event_jarum");       return; }
        if (L.contains("ransel kecil di pinggir"))                                               { changeScene("event_ransel");      return; }
        if (L.contains("hujan lebat turun"))                                                     { changeScene("event_hujan");       return; }
        if (L.contains("siaran radio redup") || L.contains("tolong siapapun yang mendengar"))   { changeScene("event_radio");       return; }
        if (L.contains("zombie datang tiba-tiba dari gang sempit"))                              { changeScene("event_zombie_gang"); return; }

        // Aksi
        if (L.contains("mencari senjata") || L.contains("kamu mendapatkan:"))  { changeScene("cari_senjata");  return; }
        if (L.contains("kamu menjelajahi") || L.contains("menggeledah area")) {
        switch (currentLocation) {
        case "rumah_sakit":   changeScene("jelajah_rumah_sakit");   break;
        case "sekolah":       changeScene("jelajah_sekolah");       break;
        case "minimarket":    changeScene("jelajah_minimarket");    break;
        case "kantor_polisi": changeScene("jelajah_kantor_polisi"); break;
        case "bunker":        changeScene("jelajah_bunker");        break;
        default:              changeScene("jelajah_jalanan");       break;
    }
    return;
}
        if (L.contains("=== pilih lokasi ===") || L.contains("kamu bergerak menuju")) { changeScene("pindah_lokasi"); return; }
        if (L.contains("bersembunyi dari zombie"))                               { changeScene("sembunyi");      return; }
        if (L.contains("mencoba kabur"))                                         { changeScene("kabur");         return; }
        if (L.contains("memilih untuk beristirahat"))                            { changeScene("istirahat");     return; }
        if (L.contains("membuka kaleng dan memakannya"))                         { changeScene("makan");         return; }

        // FIX 2: Deteksi lokasi dari baris status "  Lokasi: ..."
        // Lebih andal karena muncul setiap turn, bukan hanya dari dialog enter
        if (L.contains("lokasi:")) {
            if      (L.contains("rumah sakit"))   { currentLocation = "rumah_sakit";   changeScene("rumah_sakit");   return; }
            else if (L.contains("sekolah"))       { currentLocation = "sekolah";       changeScene("sekolah");       return; }
            else if (L.contains("minimarket"))    { currentLocation = "minimarket";    changeScene("minimarket");    return; }
            else if (L.contains("kantor polisi")) { currentLocation = "kantor_polisi"; changeScene("kantor_polisi"); return; }
            else if (L.contains("bunker"))        { currentLocation = "bunker";        changeScene("bunker");        return; }
            else if (L.contains("jalanan"))       { currentLocation = "jalanan";       changeScene("jalanan");       return; }
        }

        // Turn baru → balik ke lokasi
        if (L.contains("=== turn ")) {
            changeScene(currentLocation);
        }
    }

    public void changeScene(String sceneName) {
        if (sceneName.equals(currentScene)) return;
        currentScene = sceneName;
        SwingUtilities.invokeLater(() -> loadSceneImage(sceneName));
    }

    private void loadSceneImage(String sceneName) {
        String path = SCENE_IMAGES.getOrDefault(sceneName, null);
        if (path == null) { setPlaceholderImage(sceneName); return; }
        try {
            InputStream is = getClass().getResourceAsStream(path);
            if (is == null) {
                java.io.File f = new java.io.File("src/main/resources" + path);
                if (f.exists()) is = new FileInputStream(f);
            }
            if (is != null) {
                BufferedImage img = ImageIO.read(is);
                is.close();
                if (img != null) {
                    int maxW = IMAGE_WIDTH - 20;
                    int maxH = CONSOLE_HEIGHT - 70;
                    double r = Math.min((double) maxW / img.getWidth(), (double) maxH / img.getHeight());
                    int w = (int)(img.getWidth() * r);
                    int h = (int)(img.getHeight() * r);
                    imageLabel.setIcon(new ImageIcon(img.getScaledInstance(w, h, Image.SCALE_SMOOTH)));
                    imageLabel.setText(null);
                    updateCaption(sceneName);
                    return;
                }
            }
        } catch (IOException ignored) {}
        setPlaceholderImage(sceneName);
    }

    private void setPlaceholderImage(String sceneName) {
        int w = IMAGE_WIDTH - 20;
        int h = CONSOLE_HEIGHT - 70;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(IMG_BG_COLOR); g.fillRect(0, 0, w, h);
        g.setColor(new Color(20, 28, 20));
        for (int x = 0; x < w; x += 20) g.drawLine(x, 0, x, h);
        for (int y = 0; y < h; y += 20) g.drawLine(0, y, w, y);
        g.setColor(BORDER_COLOR); g.drawRect(3, 3, w-6, h-6);
        g.setColor(new Color(25, 40, 25)); g.drawRect(8, 8, w-16, h-16);

        String icon  = getSceneIcon(sceneName);
        String label = SCENE_LABELS.getOrDefault(sceneName, sceneName);
        Color  col   = SCENE_COLORS.getOrDefault(sceneName, SYSTEM_COLOR);

        g.setFont(new Font("Courier New", Font.BOLD, 55));
        FontMetrics fm = g.getFontMetrics();
        g.setColor(col);
        g.drawString(icon, (w - fm.stringWidth(icon)) / 2, h/2 - 5);
        g.setFont(new Font("Courier New", Font.BOLD, 12));
        fm = g.getFontMetrics();
        g.setColor(col);
        g.drawString(label, (w - fm.stringWidth(label)) / 2, h/2 + 30);
        String fn = "images/" + sceneName + ".png";
        g.setFont(new Font("Courier New", Font.PLAIN, 9));
        fm = g.getFontMetrics();
        g.setColor(new Color(45, 65, 45));
        g.drawString(fn, (w - fm.stringWidth(fn)) / 2, h - 14);
        g.dispose();

        imageLabel.setIcon(new ImageIcon(img));
        imageLabel.setText(null);
        updateCaption(sceneName);
    }

    private void updateCaption(String scene) {
        captionLabel.setText(SCENE_LABELS.getOrDefault(scene, scene));
        captionLabel.setForeground(SCENE_COLORS.getOrDefault(scene, SYSTEM_COLOR));
        sceneTagLabel.setText("[ " + scene.toUpperCase().replace('_', ' ') + " ]");
    }

    private String getSceneIcon(String s) {
        if (s.startsWith("boss"))          return "X";
        if (s.startsWith("battle"))        return "!";
        if (s.equals("game_over"))         return ".";
        if (s.equals("menang"))            return "V";
        if (s.equals("terinfeksi"))        return "~";
        if (s.startsWith("item"))          return "+";
        if (s.startsWith("clue_"))         return "?";
        if (s.startsWith("event_makanan")) return "%";
        if (s.startsWith("event"))         return "*";
        if (s.equals("rumah_sakit"))       return "+";
        if (s.equals("sekolah"))           return "S";
        if (s.equals("minimarket"))        return "$";
        if (s.equals("kantor_polisi"))     return "P";
        if (s.equals("bunker"))            return "B";
        if (s.startsWith("jelajah")) return "~";
        return ">";
    }

    private void startGame() {
        inputField.setEnabled(true);
        inputField.requestFocusInWindow();
        Thread t = new Thread(() -> {
            try { gameRunnable.run(); }
            catch (Exception e) { System.out.println("\n[GAME ENDED: " + e.getMessage() + "]"); }
        }, "GameThread");
        t.setDaemon(true);
        t.start();
    }

    public static void clearScreen() { System.out.print("\f"); }

    public static void setScene(String sceneName) {
        if (instance != null) instance.changeScene(sceneName);
    }
}