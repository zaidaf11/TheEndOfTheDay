/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.theendoftheday;

/**
 *
 * @author Zaid Akmal
 */
import java.util.*;

public class Player {
    private int HP;
    private int Stamina;
    private int Hunger;
    private String Weapon;
    private int Damage;
    private boolean Infected;
    
    public Player (){
        HP = 100;
        Stamina = 90;
        Hunger = 0;
        Weapon = "Tangan Kosong";
        Damage = 10;
        Infected = false;
    }
    public int getHP (){return HP;}
    public void setHP (int HP){ this.HP = Math.max(HP,0);}
    
    public int getStamina (){return Stamina;}
    public void setStamina (int Stamina){ this.Stamina = Math.max(Stamina,0);}
    
    public int getHunger (){return Hunger;}
    public void setHunger (int Hunger){ this.Hunger = Math.max(Hunger,0);}
    
    public String getWeapon (){return Weapon;}
    public void setWeapon (String Weapon){ this.Weapon = Weapon;}
    
    public int getDamage (){return Damage;}
    public void setDamage (int Damage){ this.Damage = Damage;}
    
    public boolean GetInfected (){return Infected;}
    public void setInfected (boolean Infected){ this.Infected = Infected;}
    
}
