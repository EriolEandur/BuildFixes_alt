/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.dags.BuildFixes.bannerEditor;

/**
 *
 * @author Eriol_Eandur
 */
public enum BannerEditorMode {
    LIST    ("l"),
    PATTERN ("p"),
    COLOR   ("c"),
    ADD     ("a"),
    REMOVE  ("r");
    
    private final String name;

    private BannerEditorMode(String name) {
        this.name = name;
    }
    
    public static BannerEditorMode getEditorMode(String name) {
        for(BannerEditorMode type: BannerEditorMode.values()) {
            if(type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
    
}
