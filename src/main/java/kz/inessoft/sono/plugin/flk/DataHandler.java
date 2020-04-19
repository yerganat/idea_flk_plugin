package kz.inessoft.sono.plugin.flk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHandler {

    public static Map<String, FieldInfo> fields = new HashMap<>();
    public static Map<String, List<String>> pages = new HashMap<>();

    public static class FieldInfo {
        String xmlPageName; //page_100_00_06
        String xmlFieldName; //field_100_00_050
        String fieldProperty; //field10006016
        String fieldType; //String, Boolean, Long
        String pageVariable; //page1000001
        boolean isLocalPageVariable;
        String localPageVariableType; //Page1000001
        boolean isVariablePageList; //page1000601List
    }
}
