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
public class BannerEditorConfig {
    
    private int patternId = 0;
    
    private BannerEditorMode editorMode = BannerEditorMode.LIST;

    public BannerEditorMode getEditorMode() {
        return editorMode;
    }

    public void setEditorMode(BannerEditorMode editorMode) {
        this.editorMode = editorMode;
    }

    public int getPatternId() {
        return patternId;
    }

    public void setPatternId(int patternId) {
        this.patternId = patternId;
    }
    
}
