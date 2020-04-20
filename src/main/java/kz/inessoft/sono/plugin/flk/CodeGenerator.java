package kz.inessoft.sono.plugin.flk;

import com.intellij.openapi.editor.Document;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import kz.inessoft.sono.plugin.flk.utils.PsiDocumentUtils;

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

        if (!formHandler.isMainOnlyRequired) {
            dependOnXmlFieldList.addAll(formHandler.dependOnXmlFieldList);
            dependOnXmlFieldList.removeAll(formHandler.excludeXmlFieldList);
            List<String> tmpPageFields = new ArrayList<>();
            List<String> tmpRemovePage = new ArrayList<>();
            for (String fld : dependOnXmlFieldList) {
                if (DataHandler.pages.containsKey(fld)) {
                    tmpPageFields.addAll(DataHandler.pages.get(fld));
                }

                if (!DataHandler.fields.containsKey(fld)) {
                    tmpRemovePage.add(fld);
                }
            }
            dependOnXmlFieldList.removeAll(tmpRemovePage);
            dependOnXmlFieldList.addAll(tmpPageFields);
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
            //containingClasee.getTextRange().getEndOffset() - 1  TODO если генерировать в конце класса, но не уобно
            document.insertString(containingMethod.getTextRange().getEndOffset() + 1, doFlkMethodDeclaration(formHandler.mainXmlField, dependOnXmlFieldList, calcXmlFieldMap));
        }

        if (containingMethod != null) {
            document.insertString(psiElement.getTextOffset(), doFlkMethodCall(formHandler.mainXmlField, dependOnXmlFieldList));
        }

        PsiDocumentUtils.commitAndSaveDocument(psiDocumentManager, document);
    }


    private static String methodName(String pageField, String methodPrefix) {
        return methodPrefix + pageField.substring(0, 1).toUpperCase() + pageField.substring(1);
    }

    private static String addError(String xmlPageName, String xmlFieldName, String msg) {
        return "\naddError(FORM_NAME, null, \"" + xmlPageName + "\", null, \"" + xmlFieldName + "\", \"" + msg + "\");";
    }

    private static String checkFilledStr(DataHandler.FieldInfo fieldInfo, boolean filled) {
        String pageVariableTmp = fieldInfo.isVariablePageList ? "p" : fieldInfo.pageVariable;
        String checkString = "";
        switch (fieldInfo.fieldType) {
            case "java.lang.String":
                checkString = "StringUtils." + (filled ? "isNotBlank" : "isBlank")
                        + "(" + pageVariableTmp + "." + methodName(fieldInfo.fieldProperty, "get") + "())";
                break;
            case "java.lang.Boolean":
            case "boolean":
                checkString = "BooleanUtils." + (filled ? "isTrue" : "isNotTrue")
                        + "(" + pageVariableTmp + "." + methodName(fieldInfo.fieldProperty, "is") + "())";
                break;
            default:
                checkString = pageVariableTmp + "."
                        + methodName(fieldInfo.fieldProperty, "get") + "()" + (filled ? "!=" : "==") + " null";
                break;
        }

        if (fieldInfo.isVariablePageList) {
            checkString = "(" + fieldInfo.pageVariable + "!= null && " + fieldInfo.pageVariable + ".stream().anyMatch(p -> " + checkString + "))";
        }

        return checkString;
    }

    private static String variableExprStr(DataHandler.FieldInfo fieldInfo, boolean isDoubleExpr) {
        String pageVariableTmp = fieldInfo.isVariablePageList ? "p" : fieldInfo.pageVariable;
        String exprStr = (isDoubleExpr?"dv":"lv") + "(" + pageVariableTmp + "." + methodName(fieldInfo.fieldProperty, "get") + "())";
        if (fieldInfo.isVariablePageList) {
            exprStr = "(" + fieldInfo.pageVariable + "!= null && " + fieldInfo.pageVariable + ".stream().anyMatch(p -> " + exprStr + "))";
        }

        return exprStr;
    }
    private static String doFlkMethodCall(String flkCheckXmlField, List<String> dependOnXmlFieldList) {
        DataHandler.FieldInfo fieldInfo = DataHandler.fields.get(flkCheckXmlField);
        return methodName(fieldInfo.fieldProperty, "doFlk") + parameterListStr(dependOnXmlFieldList, true) + ";";
    }

    private static String doFlkMethodDeclaration(String xmlField, List<String> dependOnXmlFieldList, Map<String, String> calcXmlFieldMap) {
        DataHandler.FieldInfo mainFieldInfo = DataHandler.fields.get(xmlField);

        StringBuilder sb = new StringBuilder();
        sb.append("\n//").append(mainFieldInfo.xmlPageName).append(".").append(mainFieldInfo.xmlFieldName).append("\n");
        sb.append("private void ").append(methodName(mainFieldInfo.fieldProperty, "doFlk"));

        sb.append(parameterListStr(dependOnXmlFieldList, false)).append("{\n");

        //условия не отсутствие
        sb.append("if(").append(checkFilledStr(mainFieldInfo, false));
        StringBuilder conditionSb = new StringBuilder();
        for (int i = 0; i < dependOnXmlFieldList.size(); i++) {
            conditionSb.append(checkFilledStr(DataHandler.fields.get(dependOnXmlFieldList.get(i)), true));

            if (i != dependOnXmlFieldList.size() - 1)
                conditionSb.append(" \n|| ");
        }

        if (conditionSb.length() > 0) {
            sb.append(" \n&& (").append(conditionSb).append(")");
        }
        sb.append(")");
        sb.append(addError(mainFieldInfo.xmlPageName, mainFieldInfo.xmlFieldName, "msgEmpty"));

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

    private static String parameterListStr(List<String> localVarList, boolean withoutType) {
        Set<String> set = new HashSet<>();
        Map<String, DataHandler.FieldInfo> fieldInfoMap = localVarList.stream()
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
