/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.theendoftheday;

import com.mycompany.theendoftheday.Player;

/**
 *
 * @author Zaid Akmal
 */
public class Zombie {
    protected int HP;
    protected int Damage;
    
    public void attack (Player P)
    {P.setHP(P.getHP()-Damage);
    System.out.println("Zombie menyerang!!"+Damage+HP);
    }
    
    public String getType(){
        return "Zombie";
    }
    
}