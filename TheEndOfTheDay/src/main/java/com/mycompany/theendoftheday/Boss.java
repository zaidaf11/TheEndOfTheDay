/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.theendoftheday;

/**
 *
 * @author DELL
 */

public class Boss extends Zombie {

    private String bossName;
    private String[] dialogLines;
    private int phase;
    private int maxHP;

    public Boss(String bossName, int hp, int damage, String[] dialogLines) {
        this.bossName = bossName;
        this.HP = hp;
        this.maxHP = hp;
        this.Damage = damage;
        this.dialogLines = dialogLines;
        this.phase = 1;
    }

    @Override
    public void attack(Player player) {
        if (HP <= (maxHP / 2) && phase == 1) {
            phase = 2;
            System.out.println("\n>>> " + bossName + " MENGAMUK! Serangan semakin brutal! <<<");
            if (dialogLines != null && dialogLines.length > 1) {
                System.out.println("> \"" + dialogLines[1] + "\"");
            }
            Damage = (int)(Damage * 1.5);
        }

        System.out.println("> " + bossName + " menyerang! Damage: " + Damage);
        player.setHP(player.getHP() - Damage);
    }

    public void printIntroDialog() {
        System.out.println("\n> " + bossName + " berbalik menghadapmu...");
        if (dialogLines != null && dialogLines.length > 0) {
            System.out.println("> \"" + dialogLines[0] + "\"");
        }
    }

    public String getBossName() { return bossName; }
    public int getPhase() { return phase; }
    public int getMaxHP() { return maxHP; }
}