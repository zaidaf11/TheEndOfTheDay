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
            actionMenu();
            
        
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
            case 1 : findweapon();
           
        }
    }
    
    static void findweapon () {
        System.out.println("Mencari senjata...");
        int s = rand.nextInt(3);{
        if(s==0){
            player.setWeapon("Pisau");
            player.setDamage(15);
        }
        else if (s==1){
            player.setWeapon("Bedog");
            player.setDamage(20);
    }
        else {
            player.setWeapon("Pistol");
            player.setDamage(30);   
        }
        System.out.println("Kamu Mendapatkan: " + player.getWeapon());
        System.out.println("Damage kamu meninggkat: " + player.getDamage());
        
        
        }
   
    }
   
}
