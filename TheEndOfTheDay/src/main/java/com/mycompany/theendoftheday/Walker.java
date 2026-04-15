/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.theendoftheday;

import com.mycompany.theendoftheday.Zombie;

/**
 *
 * @author Zaid Akmal
 */
public class Walker extends Zombie{
    public Walker (){
        HP = 30;
        Damage = 5;
      
    }
    @Override
    public String getType (){
        return"Walker";
    }
    
}
