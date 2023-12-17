package com.x_tornado10.craftattack.utils.mgrs;

import com.x_tornado10.craftattack.area.Area;
import com.x_tornado10.craftattack.craftattack;
import com.x_tornado10.craftattack.utils.CustomBlock;
import com.x_tornado10.craftattack.utils.id.Id;
import com.x_tornado10.craftattack.utils.statics.Paths;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class SaveMgr {
    private final Logger logger;
    public void createSaveFiles() {
        for (String s : Paths.getSaveFiles()) {
            File file = new File(s);
            if (!file.exists()) {
                try {
                    if (file.createNewFile()) {
                        logger.info(file.getName() + " was successfully created!");
                    }
                } catch (IOException e) {
                    logger.severe("There was an error trying to create " + file.getName() + "!");
                }
            }
        }
    }
    public SaveMgr() {
        craftattack plugin = craftattack.getInstance();
        logger = plugin.getLogger();
    }
    public void save() {
        logger.info("Saving data...");
        try {
            saveAreas();
        } catch (IOException | InvalidConfigurationException e) {
            logger.severe("There was an error while saving the data! Data loss is to be expected!");
            return;
        }
        logger.info("Data successfully saved!");
    }

    private void saveAreas() throws IOException, InvalidConfigurationException {
        File file = new File(Paths.areaSaveFile);
        YamlConfiguration save = new YamlConfiguration();
        save.load(file);
        for (Area a : craftattack.areaList) {
            String name = a.getName();
            if (save.contains(name)) save.set(name, "");
            HashMap<Id, CustomBlock> blocks = a.getBlocks();
            int i = 0;
            for (Map.Entry<Id, CustomBlock> entry : blocks.entrySet()) {
                HashMap<String, Object> map0 = (HashMap<String, Object>) entry.getKey().serialize();
                HashMap<String, Object> map1 = (HashMap<String, Object>) entry.getValue().serialize();
                for (Map.Entry<String, Object> entry1 : map0.entrySet()) {
                    save.set(name + ".blocks." + i + ".Id." + entry1.getKey(), entry1.getValue());
                }
                for (Map.Entry<String, Object> entry1 : map1.entrySet()) {
                    save.set(name + ".blocks." + i + ".CustomBlock." + entry1.getKey(), entry1.getValue());
                }
                i++;
            }
        }
        save.save(file);
    }

    public void load() {
        logger.info("Loading data...");
        try {
            loadAreas();
        } catch (IOException | InvalidConfigurationException e) {
            logger.severe("There was an error while loading the data! Data loss is to be expected!");
            return;
        }
        logger.info("Data was successfully loaded!");
    }

    private void loadAreas() throws IOException, InvalidConfigurationException {
        File file = new File(Paths.areaSaveFile);
        YamlConfiguration save = new YamlConfiguration();
        save.load(file);
        List<Area> areaList = new ArrayList<>();
        for (String key : save.getKeys(false)) {
            HashMap<Id, CustomBlock> blocks = new HashMap<>();
            ConfigurationSection cs = save.getConfigurationSection(key);
            if (cs == null) break;
            for (String key0 : cs.getKeys(false)) {
                if (key0.equals("blocks")) {
                    ConfigurationSection cs0 = save.getConfigurationSection(key + "." + key0);
                    if (cs0 == null) break;
                    for (String key1 : cs0.getKeys(false)) {
                        int x = Integer.parseInt(Objects.requireNonNull(cs0.get(key1 + ".Id.x")).toString());
                        int y = Integer.parseInt(Objects.requireNonNull(cs0.get(key1 + ".Id.y")).toString());
                        int z = Integer.parseInt(Objects.requireNonNull(cs0.get(key1 + ".Id.z")).toString());

                        Id id = new Id(x,y,z);

                        BlockData bdata = Bukkit.createBlockData((String) Objects.requireNonNull(cs0.get(key1 + ".CustomBlock.data")));
                        ConfigurationSection cs1 = save.getConfigurationSection(key + "." + key0 + "." + key1 + ".CustomBlock.location");
                        if (cs1 == null) break;

                        Location loc = new Location(Bukkit.getWorld(Objects.requireNonNull(cs1.get("world")).toString()), Double.parseDouble(Objects.requireNonNull(cs1.get("x")).toString()), Double.parseDouble(Objects.requireNonNull(cs1.get("y")).toString()), Double.parseDouble(Objects.requireNonNull(cs1.get("z")).toString()));
                        CustomBlock cb = new CustomBlock(bdata, loc);
                        blocks.put(id, cb);
                    }
                }
            }
            areaList.add(new Area(key, blocks));
        }
        craftattack.areaList.addAll(areaList);
    }
}
