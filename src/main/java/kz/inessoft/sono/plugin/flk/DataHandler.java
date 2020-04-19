package kz.inessoft.sono.plugin.flk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHandler {

    public static Map<String, PageInfo> fields = new HashMap<>();
    public static Map<String, List<String>> pages = new HashMap<>();

    public static class PageInfo {
        String xmlPageName;
        String xmlFieldName;
        String pageField;
        String variable;
        boolean isLocalVariable;
        String localVariableType;
        boolean isFieldList;
    }
}
