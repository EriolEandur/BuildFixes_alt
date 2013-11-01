package me.dags.BuildFixes.Commands.StencilListBuilder;

import me.dags.BuildFixes.BuildFixes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author dags_ <dags@dags.me>
 */

public class StencilListBuilder {

    private String name;
    private List<String> stencils;

    public StencilListBuilder() {
        stencils = new ArrayList<String>();
    }

    public void setName(String s) {
        name = s;
    }

    public void addStencil(String s) {
        stencils.add(s);
    }

    public String getName() {
        return name;
    }

    public List<String> getStencils() {
        return stencils;
    }

    public boolean writeToFile() {


        try {
            File newList = new File(getVoxelDir() + "/stencilLists/", name + ".txt");

            if (!newList.exists()) {
                newList.createNewFile();

                FileWriter fw = new FileWriter(newList, true);
                PrintWriter pw = new PrintWriter(fw);

                Collections.sort(this.stencils);
                for (String s : stencils) {
                    pw.println(s);
                }

                pw.flush();
                pw.close();

                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String getVoxelDir() {
        return BuildFixes.inst().getDataFolder().getParent() + "/VoxelSniper";
    }
}
