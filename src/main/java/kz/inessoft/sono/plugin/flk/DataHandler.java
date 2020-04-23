package kz.inessoft.sono.plugin.flk;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DataHandler {

    public static Map<String, FieldInfo> fields = new TreeMap<>();
    public static Map<String, List<String>> pages = new TreeMap<>();
    public static Map<String, List<FieldInfo>> rows = new TreeMap<>();

    public static boolean formIdx = false;
    public static void reset() {
        fields = new TreeMap<>();
        pages = new TreeMap<>();
        rows = new TreeMap<>();
        formIdx = false;
    }

    public static class FieldInfo {
        String xmlPageName; //page_100_00_06
        String xmlFieldName; //field_100_00_050
        String fieldProperty; //field10006016
        String fieldType; //String, Boolean, Long
        String pageVariable; //page1000001
        boolean isLocalPageVariable;
        String localPageVariableType; //Page1000001
        boolean isVariablePageList; //page1000601List
        boolean isRowInfo; //page1000601List
    }
}
