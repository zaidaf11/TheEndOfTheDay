/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.theendoftheday;

/**
 *
 * @author Zaid Akmal
 */

public class Location {
    private String name;
    private String description;
    private String enterDialog;

   
    private String itemName;
    private int itemRequired;
    private int itemCollected;

   
    private String clueStoredName;       
    private String clueStoredDesc;       
    private boolean clueFound;           

   
    private String clueNeededName;       
    private String clueNeededHint;       
    
    private String bossName;
    private boolean bossDefeated;
    private boolean bossTriggered;       

    public Location(String name, String description, String enterDialog,
                    String itemName, int itemRequired,
                    String clueStoredName, String clueStoredDesc,
                    String clueNeededName, String clueNeededHint,
                    String bossName) {
        this.name = name;
        this.description = description;
        this.enterDialog = enterDialog;
        this.itemName = itemName;
        this.itemRequired = itemRequired;
        this.itemCollected = 0;
        this.clueStoredName = clueStoredName;
        this.clueStoredDesc = clueStoredDesc;
        this.clueFound = false;
        this.clueNeededName = clueNeededName;
        this.clueNeededHint = clueNeededHint;
        this.bossName = bossName;
        this.bossDefeated = false;
        this.bossTriggered = false;
    }

  
    public boolean collectItem() {
        if (itemCollected < itemRequired) {
            itemCollected++;
            return true;
        }
        return false;
    }

    public boolean isItemComplete() { return itemCollected >= itemRequired; }

   
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getEnterDialog() { return enterDialog; }
    public String getItemName() { return itemName; }
    public int getItemRequired() { return itemRequired; }
    public int getItemCollected() { return itemCollected; }
    public String getItemStatus() { return itemName + " (" + itemCollected + "/" + itemRequired + ")"; }

    public String getClueStoredName() { return clueStoredName; }
    public String getClueStoredDesc() { return clueStoredDesc; }
    public boolean isClueFound() { return clueFound; }
    public void setClueFound(boolean b) { this.clueFound = b; }
    public boolean hasClueStored() { return clueStoredName != null; }

    public String getClueNeededName() { return clueNeededName; }
    public String getClueNeededHint() { return clueNeededHint; }
    public boolean needsClue() { return clueNeededName != null; }

    public String getBossName() { return bossName; }
    public boolean isBossDefeated() { return bossDefeated; }
    public void setBossDefeated(boolean b) { this.bossDefeated = b; }
    public boolean isBossTriggered() { return bossTriggered; }
    public void setBossTriggered(boolean b) { this.bossTriggered = b; }
}