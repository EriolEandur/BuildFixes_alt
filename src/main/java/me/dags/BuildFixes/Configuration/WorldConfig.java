package me.dags.BuildFixes.Configuration;

import static me.dags.BuildFixes.BuildFixes.multiWorlds;
import static me.dags.BuildFixes.BuildFixes.worlds;

import java.util.ArrayList;
import java.util.List;

import me.dags.BuildFixes.BuildFixes;

import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author dags_ <dags@dags.me>
 */

public class WorldConfig {

    private boolean doors = false;
    private boolean eggs = false;
    private boolean nophys = false;
    private boolean logs = false;

    private boolean getCMD = false;
    private boolean fbCMD = false;

    private boolean schem = false;
    private boolean stencil = false;
    private boolean bfinfo = false;

    private boolean decay = false;
    private boolean form = false;
    private boolean weather = false;
    private boolean fire = false;
    private boolean animal = false;
    private boolean monster = false;

    private List<Integer> noPhysList = new ArrayList<Integer>();

    public WorldConfig(World w) {

        JavaPlugin instance = (JavaPlugin) BuildFixes.inst();
        Configuration cfg;
        String output = "null";

        if (BuildFixes.multiWorlds) {
            ConfigUtil cu = new ConfigUtil(instance, w.getName());
            cfg = cu.getWorldConfig();
            output = "MultiWorld";
        } else {
            cfg = instance.getConfig();
            output = "Global";
        }

        if (cfg.getBoolean("Modules.BuildFixes.Enable")) {
            eggs = cfg.getBoolean("Modules.BuildFixes.DragonEggBlocking");
            doors = cfg.getBoolean("Modules.BuildFixes.HalfDoors");
            logs = cfg.getBoolean("Modules.BuildFixes.SpecialLogs");
            nophys = cfg.getBoolean("Modules.BuildFixes.NoPhysics");
        }
        if (cfg.getBoolean("Modules.Commands.Enable")) {
            getCMD = cfg.getBoolean("Modules.Commands.GetItem");
            fbCMD = cfg.getBoolean("Modules.Commands.Fullbright");
        }
        if (cfg.getBoolean("Modules.Utilities.Enable")) {
            schem = cfg.getBoolean("Modules.Utilities.SchematicsBrowser");
            stencil = cfg.getBoolean("Modules.Utilities.StencListBrowser");
            bfinfo = cfg.getBoolean("Modules.Utilities.BuildFixesInfo");
        }
        if (cfg.getBoolean("Modules.Environment.Enable")) {
            decay = cfg.getBoolean("Modules.Environment.DecayBlocking");
            form = cfg.getBoolean("Modules.Environment.FormBlocking");
            weather = cfg.getBoolean("Modules.Environment.WeatherBlocking");
            fire = cfg.getBoolean("Modules.Environment.FireSpreadBlocking");
            animal = cfg.getBoolean("Modules.Environment.AnimalBlocking");
            monster = cfg.getBoolean("Modules.Environment.MonsterBlocking");
        }

        for (Integer i : cfg.getIntegerList("NoPhysicsList")) {
            noPhysList.add(i);
        }

        w.setSpawnFlags(!monster, !animal);

        String value = String.valueOf(!fire);
        w.setGameRuleValue("doFireTick", value);

        System.out.print("[BuildFixes] is using " + output
                + " settings for world: " + w.getName());
    }

    public static WorldConfig getWorldConf(World w) {
        String s = w.getName();
        WorldConfig conf = worlds.get(s);
        WorldConfig newConf;

        if (conf == null) {
            if (multiWorlds) {
                JavaPlugin instance = (JavaPlugin) BuildFixes.inst();
                ConfigUtil cfg = new ConfigUtil(instance, w.getName());

                cfg.getWorldConfig().options().copyDefaults(true);
                cfg.saveWorldConfig();
            }
            newConf = new WorldConfig(w);
            worlds.put(s, newConf);
            conf = worlds.get(s);
        }

        return conf;
    }

    public boolean doors() {
        return doors;
    }

    public boolean eggs() {
        return eggs;
    }

    public boolean noPhys() {
        return nophys;
    }

    public boolean logs() {
        return logs;
    }

    public boolean getCmd() {
        return getCMD;
    }

    public boolean fbCmd() {
        return fbCMD;
    }

    public boolean schList() {
        return schem;
    }

    public boolean stenList() {
        return stencil;
    }

    public boolean bfInfo() {
        return bfinfo;
    }

    public boolean decay() {
        return decay;
    }

    public boolean form() {
        return form;
    }

    public boolean weather() {
        return weather;
    }

    public List<Integer> noPhysList() {
        return noPhysList;
    }


}
