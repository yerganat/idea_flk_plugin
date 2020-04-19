package kz.inessoft.sono.plugin.flk.utils;

import java.util.ArrayList;
import java.util.List;

public enum Logic {
    OR("ИЛИ"),
    AND("И");
    String logic;
    Logic(String logic){
        this.logic = logic;
    }

    public static String[] getValues(boolean excludeNotEquality) {
        List<String> valueList = new ArrayList<String>();

        for (Logic o: values() ) {
            valueList.add(o.logic);
        }

        return valueList.toArray(new String[0]);
    }
}
