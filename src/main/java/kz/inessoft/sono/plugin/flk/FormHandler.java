package kz.inessoft.sono.plugin.flk;

import java.util.*;

public class FormHandler {
    public String mainXmlField = "";
    public boolean isMainOnlyRequired = false;
    public Set<String> dependOnXmlFieldList = new HashSet<>();
    public Set<String> excludeXmlFieldList = new HashSet<>();

    public boolean isHasCalculation = false;
    public Map<String, String> calcXmlFieldMap = new HashMap<>();
}
