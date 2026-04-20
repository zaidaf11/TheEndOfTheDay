/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.theendoftheday;

/**
 *
 * @author DELL
 * @author Zaid A F
 */
import com.mycompany.theendoftheday.Player;
import com.mycompany.theendoftheday.Zombie;
import java.util.Scanner;
import java.util.Random;
public class Dynamite extends Zombie {
    private int BombCode;
    
    
    public Dynamite(){
        HP = 40;
        Damage = 15;
        BombCode = new Random().nextInt(5)+1;
    }
    
    @Override
    public void attack(Player P){
        Scanner sc = new Scanner(System.in);
        System.out.println("Dynamite zombie membawa bomb!!");
        System.out.println("Masukkan kode untuk menjinakkan bom!!");
        int input= sc.nextInt();
        if (input == BombCode){
            System.out.println("Bomb berhasil dijinakkan! sekarang kamu bisa menyerang");
        }else {
            System.out.println("Kode salah!! bomb meledak kamu terkena damage");
            P.setHP(P.getHP() - Damage);
        }
    }
    
    @Override
    public String getType(){
        return "Dynamite zombie";
    }
    //dynamite
    
}
