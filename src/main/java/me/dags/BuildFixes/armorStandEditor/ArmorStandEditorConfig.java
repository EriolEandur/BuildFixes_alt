/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dags.BuildFixes.armorStandEditor;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.ArmorStand;

/**
 *
 * @author Eriol_Eandur
 */
public class ArmorStandEditorConfig {
    
    private ArmorStandPart part = ArmorStandPart.HEAD;
    
    private ArmorStandEditorMode editorMode = ArmorStandEditorMode.HAND;
    
    private int rotationStep = 10;
    
    private MemoryConfiguration copiedEntity;
    
    public ArmorStandEditorConfig() {
        clearCopiedArmorStand();
     }

    public void placeArmorStand(Location loc, boolean exact) {
        Map<String,Object> data = (Map<String,Object>)copiedEntity.get("ArmorStand");
        Location saved = ArmorStandUtil.deserializeLocation((Map<String,Object>) data.get("location"));
        if(!exact) {
            loc.setX(loc.getBlockX()+saved.getX()-saved.getBlockX());
            loc.setZ(loc.getBlockZ()+saved.getZ()-saved.getBlockZ());
            loc.setYaw(saved.getYaw());
        }
        data.put("location",ArmorStandUtil.serializeLocation(loc));
        ArmorStandUtil.deserializeArmorStand((Map<String,Object>)copiedEntity.get("ArmorStand"));
        data.put("location",ArmorStandUtil.serializeLocation(saved));
    }
    
    public final void clearCopiedArmorStand() {
        copiedEntity = new MemoryConfiguration();
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("location", ArmorStandUtil.serializeLocation(new Location(Bukkit.getWorlds().get(0),0.5,0,0.5,0,0)));
        copiedEntity.set("ArmorStand", data);
    }
    
    public void copyArmorStand(ArmorStand armor) {
        copiedEntity = new MemoryConfiguration();
        copiedEntity.set("ArmorStand",ArmorStandUtil.serializeArmorStand(armor));
    }
    
    public ArmorStandEditorMode getEditorMode() {
        return editorMode;
    }

    public void setEditorMode(ArmorStandEditorMode editorMode) {
        this.editorMode = editorMode;
    }

    public ArmorStandPart getPart() {
        return part;
    }

    public void setPart(ArmorStandPart part) {
        this.part = part;
    }
    
    public void setRotationStep(int stepInDegree) {
        rotationStep = stepInDegree;
    }
    
    public int getRotationStep() {
        return rotationStep;
    }
    
}
