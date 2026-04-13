/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.theendoftheday;

/**
 *
 * @author Zaid 
 */
import java.util.Scanner;
import java.util.Random;
public class TheEndOfTheDay {
    static Scanner input = new Scanner(System.in);
    static Random rand = new Random();
    static Player player = new Player();
    static int turn = 1;
    
    public static void main(String[] args) {
        Intro();
        gameloop();
    }
        public static void Intro() {
        System.out.println("Kamu terbangun di tengah-tengah kerumunan para Zombie!");
        System.out.println("Hari mulai mencekam, apa yang akan kamu lakukan?");
        }
        static void gameloop() {
            System.out.println("\n=== TURN " + turn + " ===");
            showStatus();
        
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
        System.out.println("rasya");
    }
}
