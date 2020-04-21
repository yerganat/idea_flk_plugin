package kz.inessoft.sono.plugin.flk;

import com.intellij.openapi.editor.Document;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import kz.inessoft.sono.plugin.flk.utils.PsiDocumentUtils;
import org.apache.commons.lang.WordUtils;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class CodeGenerator {
    public static void generateCode(PsiElement psiElement, FormHandler formHandler) {
        PsiDocumentManager psiDocumentManager = PsiDocumentManager
                .getInstance(psiElement.getProject());
        PsiFile containingFile = psiElement.getContainingFile();
        Document document = psiDocumentManager.getDocument(containingFile);

        PsiClass containingClasee = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);


        List<String> dependOnXmlFieldList = new ArrayList<>();

        Map<String, List<String>> pageFilledFieldsMap = new HashMap<>();
        List<String> pageFieldList = new ArrayList<>();

        if (!formHandler.isMainOnlyRequired) {
            dependOnXmlFieldList.addAll(formHandler.dependOnXmlFieldList);
            List<String> fieldsToRemove = new ArrayList<>();
            for (String fld : dependOnXmlFieldList) {
                if (DataHandler.pages.containsKey(fld)) {
                    List<String> pageFieldsList = new ArrayList<>(DataHandler.pages.get(fld));
                    pageFieldsList.removeAll(formHandler.excludeXmlFieldList);
                    pageFilledFieldsMap.put(fld, new ArrayList<>(pageFieldsList));
                    pageFieldList.addAll(pageFieldsList);
                }

                if (!DataHandler.fields.containsKey(fld)) {
                    fieldsToRemove.add(fld);
                }
            }

            dependOnXmlFieldList.removeAll(fieldsToRemove);
        }


        Map<String, String> calcXmlFieldMap = new HashMap<>();

        if (formHandler.isHasCalculation) {
            calcXmlFieldMap.putAll(formHandler.calcXmlFieldMap);
            for (String fld : calcXmlFieldMap.keySet()) {
                if (!DataHandler.fields.containsKey(fld)) {
                    calcXmlFieldMap.remove(fld);
                }
            }
        }

        PsiMethod containingMethod = PsiTreeUtil.getParentOfType(psiElement, PsiMethod.class);

        if (containingClasee != null) {
            //containingClasee.getTextRange().getEndOffset() - 1  TODO если генерировать в конце класса, но не удобно
            document.insertString(containingMethod.getTextRange().getEndOffset() + 1,
                    doFlkMethodDeclaration(formHandler.mainXmlField, dependOnXmlFieldList, calcXmlFieldMap, pageFilledFieldsMap, pageFieldList));
        }

        if (containingMethod != null) {
            document.insertString(psiElement.getTextOffset(), doFlkMethodCall(formHandler.mainXmlField, dependOnXmlFieldList, pageFieldList));
        }

        PsiDocumentUtils.commitAndSaveDocument(psiDocumentManager, document);
    }


    private static String methodName(String pageField, String methodPrefix) {
        return methodPrefix + pageField.substring(0, 1).toUpperCase() + pageField.substring(1);
    }

    private static String addError(String xmlPageName, String xmlFieldName, String msg) {
        return "\naddError(FORM_NAME, null, \"" + xmlPageName.replace(".", "_") + "\", null, \"" + xmlFieldName.replace(".", "_") + "\", \"" + msg + "\");";
    }

    private static String checkFilledStr(DataHandler.FieldInfo fieldInfo, boolean filled) {
        String checkString = filledStr(fieldInfo, filled);

        if (fieldInfo.isVariablePageList) {
            checkString = "(" + fieldInfo.pageVariable + "!= null && " + fieldInfo.pageVariable + ".stream().anyMatch(p -> " + checkString + "))";
        }

        return checkString;
    }

    //отличне от метода checkFilledStr в том что здесь List  параметр объеденяется в один цикл
    private static String checkFilledStrList(List<String> xmlFieldInfoList, boolean filled, boolean isForPageCheck) {
        Map<String, List<String>> pageListVariableMap = new TreeMap<>();
        StringBuilder checkStringSb = new StringBuilder();
        for (int i = 0; i < xmlFieldInfoList.size(); i++) {
            DataHandler.FieldInfo fieldInfo = DataHandler.fields.get(xmlFieldInfoList.get(i));

            String checkStrForOne = filledStr(fieldInfo, filled);
            if(fieldInfo.isVariablePageList) {
                if(!pageListVariableMap.containsKey(fieldInfo.pageVariable )) {
                    List<String> stringList = new ArrayList<>();
                    stringList.add(checkStrForOne);
                    pageListVariableMap.put(fieldInfo.pageVariable , new ArrayList<>(stringList));
                } else {
                    pageListVariableMap.get(fieldInfo.pageVariable).add(checkStrForOne);
                }
            } else {
                checkStringSb.append(checkStrForOne);

                if (i != xmlFieldInfoList.size() - 1)  {
                    checkStringSb.append(" \n|| ");
                }
            }
        }

        if (pageListVariableMap.size()>0) {
            if(checkStringSb.length() > 0 && checkStringSb.charAt(checkStringSb.length()-2) != '|'){
                checkStringSb.append(" \n|| ");
            }
            int i =0;
            for (String listPageVar : pageListVariableMap.keySet()) {
                checkStringSb.append("(" + listPageVar+ "!= null && " + listPageVar + ".stream().anyMatch(p -> " + String.join(" || ", pageListVariableMap.get(listPageVar)) + "))");

                i++;
                if (i != pageListVariableMap.size())  {
                    checkStringSb.append(" \n|| ");
                }
            }
        }

        if(isForPageCheck)
            checkStringSb.append(";\n");

        return checkStringSb.toString();
    }

    private static String filledStr(DataHandler.FieldInfo fieldInfo, boolean filled) {
        String pageVariableTmp = fieldInfo.isVariablePageList ? "p" : fieldInfo.pageVariable;
        String filledStr = "";
        switch (fieldInfo.fieldType) {
            case "java.lang.String":
                filledStr = "StringUtils." + (filled ? "isNotBlank" : "isBlank")
                        + "(" + pageVariableTmp + "." + methodName(fieldInfo.fieldProperty, "get") + "())";
                break;
            case "java.lang.Boolean":
            case "boolean":
                filledStr = "BooleanUtils." + (filled ? "isTrue" : "isNotTrue")
                        + "(" + pageVariableTmp + "." + methodName(fieldInfo.fieldProperty, "is") + "())";
                break;
            default:
                filledStr = pageVariableTmp + "."
                        + methodName(fieldInfo.fieldProperty, "get") + "()" + (filled ? "!=" : "==") + " null";
                break;
        }

        return filledStr;
    }

    private static String variableExprStr(DataHandler.FieldInfo fieldInfo, boolean isDoubleExpr) {
        String pageVariableTmp = fieldInfo.isVariablePageList ? "p" : fieldInfo.pageVariable;
        String exprStr = (isDoubleExpr?"dv":"lv") + "(" + pageVariableTmp + "." + methodName(fieldInfo.fieldProperty, "get") + "())";

        if (fieldInfo.isVariablePageList) {
            exprStr = "(" + fieldInfo.pageVariable + "!= null ?" + fieldInfo.pageVariable + ".stream().filter(Objects::nonNull)."+(isDoubleExpr?"mapToDouble":"mapToLong")+"(p -> " + exprStr + ").sum():0L)";
        }

        return exprStr;
    }
    private static String doFlkMethodCall(String flkCheckXmlField, List<String> dependOnXmlFieldList, List<String> pageFieldList) {
        DataHandler.FieldInfo fieldInfo = DataHandler.fields.get(flkCheckXmlField);
        return methodName(fieldInfo.fieldProperty, "\ndoFlk") + parameterListStr(dependOnXmlFieldList, pageFieldList, true) + ";";
    }

    private static String doFlkMethodDeclaration(String xmlField,
                                                 List<String> dependOnXmlFieldList,
                                                 Map<String, String> calcXmlFieldMap,
                                                 Map<String, List<String>> pageFilledFieldsMap,
                                                 List<String> pageFieldList) {
        DataHandler.FieldInfo mainFieldInfo = DataHandler.fields.get(xmlField);

        StringBuilder sb = new StringBuilder();
        sb.append("\n//").append(mainFieldInfo.xmlPageName).append(".").append(mainFieldInfo.xmlFieldName).append("\n");
        sb.append("private void ").append(methodName(mainFieldInfo.fieldProperty, "doFlk"));

        sb.append(parameterListStr(dependOnXmlFieldList, pageFieldList, false)).append("{\n");


        //проверка на заполнненость страницы
        StringBuilder pageCheckerSb = new StringBuilder();
        List<String> pageCheckVarList = new ArrayList<>();
        for (String page: pageFilledFieldsMap.keySet()) {
            String pageVar =  "isFilled" + WordUtils.capitalizeFully(page, new char[]{'.'}).replaceAll("\\.", "");
            pageCheckVarList.add(pageVar);
            pageCheckerSb.append("boolean ").append(pageVar).append(" =");

            List<String> pageFields = pageFilledFieldsMap.get(page);

            pageCheckerSb.append(checkFilledStrList(pageFields, true, true));
//            for (int i = 0; i < pageFields.size(); i++) {
//                pageCheckerSb.append(checkFilledStr(DataHandler.fields.get(pageFields.get(i)), true));
//
//                if (i == pageFields.size() - 1) {
//                    pageCheckerSb.append(";\n");
//                } else {
//                    pageCheckerSb.append(" \n|| ");
//                }
//            }
        }

        sb.append(pageCheckerSb);

        //условия на отсутствие поля
        sb.append("if(").append(checkFilledStr(mainFieldInfo, false));
        StringBuilder conditionSb = new StringBuilder();

        for (int i = 0; i < pageCheckVarList.size(); i++) {
            conditionSb.append(pageCheckVarList.get(i));

            if (i != pageCheckVarList.size() - 1 || dependOnXmlFieldList.size() > 0)
                conditionSb.append(" \n|| ");
        }

        conditionSb.append(checkFilledStrList(dependOnXmlFieldList, true, false));

//        for (int i = 0; i < dependOnXmlFieldList.size(); i++) {
//            conditionSb.append(checkFilledStr(DataHandler.fields.get(dependOnXmlFieldList.get(i)), true));
//
//            if (i != dependOnXmlFieldList.size() - 1)
//                conditionSb.append(" \n|| ");
//        }

        if (conditionSb.length() > 0) {
            sb.append(" \n&& (").append(conditionSb).append(")");
        }
        sb.append(")");
        sb.append(addError(mainFieldInfo.xmlPageName, mainFieldInfo.xmlFieldName, "msgEmpty"));

        sb.append("\n");

        //Выражение для вычсиления
        boolean isDoubleExpr = calcXmlFieldMap.keySet().stream()
                .anyMatch(f -> {
                    String fieldType = DataHandler.fields.get(f).fieldType;
                    return fieldType.equals("java.math.BigDecimal") || fieldType.equals("float") || fieldType.equals("double");
                });


        StringBuilder exprSb = new StringBuilder();

        for (Map.Entry<String, String> calc : calcXmlFieldMap.entrySet()) {
            exprSb.append(calc.getValue())
                    .append(" ")
                    .append(variableExprStr(DataHandler.fields.get(calc.getKey()), isDoubleExpr));
        }

        if (exprSb.length() > 0) {
            String mainExprFieldMethod =variableExprStr(mainFieldInfo, isDoubleExpr);

            if(isDoubleExpr) {
                sb.append("if(")
                        .append(checkFilledStr(mainFieldInfo, true))
                        .append(")")
                        .append("{\n")
                        .append("double v = ")
                        .append(exprSb)
                        .append(";\n")
                        .append("if (v < (")
                        .append(mainExprFieldMethod)
                        .append(" - 0.5) || (v >= (")
                        .append(mainExprFieldMethod)
                        .append(") + 0.5)))\n")
                        .append(addError(mainFieldInfo.xmlPageName, mainFieldInfo.xmlFieldName, "msgCalcError"))
                        .append("\n}");
            } else {
                sb.append("if(")
                        .append(checkFilledStr(mainFieldInfo, true))
                        .append(" \n&& ")
                        .append(mainExprFieldMethod)
                        .append(" != ")
                        .append(exprSb)
                        .append(")")
                        .append(addError(mainFieldInfo.xmlPageName, mainFieldInfo.xmlFieldName, "msgCalcError"));
            }
        }

        sb.append("}\n");

        return sb.toString();
    }

    private static String parameterListStr(List<String> dependOnXmlFieldList, List<String> pageFieldList, boolean withoutType) {

        List<String> tmpList = new ArrayList<>();
        tmpList.addAll(dependOnXmlFieldList);
        tmpList.addAll(pageFieldList);

        Map<String, DataHandler.FieldInfo> fieldInfoMap = tmpList.stream()
                .filter(fld -> DataHandler.fields.get(fld).isLocalPageVariable)
                .map(fld -> DataHandler.fields.get(fld))
                .collect(Collectors.toMap(DataHandler.FieldInfo::getPageVariable, UnaryOperator.identity(), (p, d) -> p));

        DataHandler.FieldInfo[] infos = fieldInfoMap.values().toArray(new DataHandler.FieldInfo[0]);

        StringBuilder prameterSb = new StringBuilder();
        prameterSb.append("(");
        for (int i = 0; i < infos.length; i++) {
            DataHandler.FieldInfo dfi = infos[i];
            if (!withoutType)
                prameterSb.append(dfi.localPageVariableType);

            prameterSb.append(" ").append(dfi.pageVariable);

            if (i != infos.length - 1)
                prameterSb.append(" ,");
        }

        prameterSb.append(")");

        return prameterSb.toString();
    }
}
