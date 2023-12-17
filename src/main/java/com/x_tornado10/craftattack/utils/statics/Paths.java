package com.x_tornado10.craftattack.utils.statics;

import com.x_tornado10.craftattack.craftattack;

import java.util.ArrayList;
import java.util.List;

public class Paths {

    private static final String plpath = craftattack.getInstance().getDataFolder().getPath();
    public static final String areaSaveFile = plpath + "/areas.yml";
    public static List<String> getSaveFiles() {
        List<String> temp = new ArrayList<>();
        temp.add(areaSaveFile);
        return temp;
    }
}
