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
import java.util.Random;
public class Dynamite extends Zombie {
    private int BombCode;
    
    
    public Dynamite(){
        HP = 40;
        Damage = 15;
        BombCode = new Random().nextInt(5)+1;
    }
    
    
}
