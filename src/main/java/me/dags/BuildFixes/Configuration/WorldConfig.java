package me.dags.BuildFixes.Configuration;

import static me.dags.BuildFixes.BuildFixes.multiWorlds;
import static me.dags.BuildFixes.BuildFixes.worlds;

import java.util.HashSet;

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
    private boolean stencilGen = false;
    private boolean bfinfo = false;

    private boolean decay = false;
    private boolean form = false;
    private boolean weather = false;
    private boolean fire = false;
    private boolean animal = false;
    private boolean monster = false;

    private HashSet<Integer> noPhysList = new HashSet<Integer>();

    public WorldConfig(World w) {

        JavaPlugin instance = (JavaPlugin) BuildFixes.inst();
        Configuration cfg;
        String output;

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
            stencilGen = cfg.getBoolean("Modules.Utilities.StencListGenerator");
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

        for (Object o : cfg.getList("NoPhysicsList")) {
            if (isInt(o.toString())) {
                noPhysList.add(Integer.valueOf(o.toString()));
            } else {
                BuildFixes.log("(NoPhysicsList) " + w.getName()
                        + ": " + o.toString() + " is not an integer!");
            }
        }

        w.setSpawnFlags(!monster, !animal);

        String value = String.valueOf(!fire);
        w.setGameRuleValue("doFireTick", value);

        BuildFixes.log("Using " + output
                + " settings for world: " + w.getName());
    }

    private static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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

    public boolean stenGen() {
        return stencilGen;
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

    public HashSet<Integer> noPhysList() {
        return noPhysList;
    }
}
