/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.theendoftheday;

/**
 *
 * @author Zaid Akmal
 */
public class Tank extends Zombie{
     public Tank (){
        HP = 50;
        Damage = 5;
      
    }
@Override
    public String getType (){
        return"Tank";
    }
    
}
