/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dags.BuildFixes.randomiser;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Material;

/**
 *
 * @author Eriol_Eandur
 */
public class RandomiserConfig {
    
    int radius = 100;

    private int minValue = 4;
    
    private int maxValue = 7;
    
    private int[] props = evenProps();
    
    private final Set<Material> materials = new HashSet();
    
    public RandomiserConfig() {
        materials.add(Material.CROPS);
    }
    
    public void setDataValueRange(int min, int max) {
        minValue = min;
        maxValue = max;
        props = evenProps();
    }
    
    private int[] evenProps() {
        int[] newProps = new int[maxValue-minValue+1];
        for(int i = 0; i<newProps.length;i++) {
            newProps[i] = 100/newProps.length;
        }
        return newProps;
    }
    
    public boolean setMaterials(String[] names) {
        boolean allAllowed = true;
        materials.clear();
        for(String name : names) {
            Material mat = Material.matchMaterial(name.toUpperCase());
            if(mat!=null && Randomiser.isAllowed(mat)) {
                materials.add(mat);
            }
            else {
                allAllowed = false;
            }
        }
        return allAllowed;
    }

    public void setProbs(int[] newProbs) {
        props = newProbs;
    }
    
    public void setRadius(int radius) {
        if(radius > 0 && radius <=150) {
            this.radius = radius;
        }
    }
    
    public int getRadius() {
        return radius;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public String getProbs() {
        String str = "";
        for(int prob : props) {
            str = str+prob+" ";
        }
        return str;
    }

    public String getMaterials() {
        String str = "";
        for(Material mat : materials) {
            str = str+mat.name()+" ";
        }
        return str;
    }
    
    public boolean isIn(Material type) {
        for(Material search: materials) {
            if(search.equals(type)) {
                return true;
            }
        }
        return false;
    }
    
    public int countDataValues() {
        return props.length;
    }
   
    public byte randomDataValue() {
        int rand = (int) Math.round(Math.floor(100*Math.random()));
        byte dataValue;
        int sum = 0;
        for(int i = 0; i<props.length ; i++) {
            sum = sum + props[i];
            if(rand<sum) {
                return new Integer(minValue+i).byteValue();
            }
        }
        return new Integer(maxValue).byteValue();
    }
}
