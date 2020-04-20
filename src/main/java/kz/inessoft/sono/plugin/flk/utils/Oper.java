package kz.inessoft.sono.plugin.flk.utils;

import java.util.ArrayList;
import java.util.List;

public enum Oper {
    ADD("+"),
    SUBSTRACT("-"),
    MULTIPLAY("*"),
    DIVIDE("/"),
    PERSENT("%"),
    LBRACKET("("),
    RBRACKET(")");
    //IF_NOT_EQUAL("!=");
    String operator;
    Oper(String operator){
        this.operator = operator;
    }

    public static String[] getValues(boolean excludeNotEquality) {
        List<String> valueList = new ArrayList<String>();

        for (Oper o: values() ) {
//            if(excludeNotEquality && IF_NOT_EQUAL.equals(o))
//                continue;
            valueList.add(o.operator);
        }

        return valueList.toArray(new String[0]);
    }
}
