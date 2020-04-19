package kz.inessoft.sono.plugin.flk;

import com.intellij.openapi.editor.Document;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import kz.inessoft.sono.plugin.flk.utils.PsiDocumentUtils;

import java.util.List;
import java.util.stream.Collectors;

public class CodeGenerator {
    public static void generateCode(PsiElement psiElement, FormHandler formHandler) {
        PsiDocumentManager psiDocumentManager = PsiDocumentManager
                .getInstance(psiElement.getProject());
        PsiFile containingFile = psiElement.getContainingFile();
        Document document = psiDocumentManager.getDocument(containingFile);

        PsiClass containingClasee = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);

        if (containingClasee != null) {
            document.insertString(containingClasee.getTextRange().getEndOffset() - 1, doFlkMethodDeclaration(formHandler.mainXmlField, formHandler.dependOnXmlFieldList));
            //document.insertString(containingClasee.getTextRange().getEndOffset() - 1, "\n public void generatedTest() {}\n");
        }

        PsiMethod containingMethod = PsiTreeUtil.getParentOfType(psiElement, PsiMethod.class);
        if (containingMethod != null) {
            document.insertString(psiElement.getTextOffset(), doFlkMethodCall(formHandler.mainXmlField, formHandler.dependOnXmlFieldList));
            //document.insertString(psiElement.getTextOffset(), "\nString generatedTest;");
        }

        PsiDocumentUtils.commitAndSaveDocument(psiDocumentManager, document);
    }


    private static String methodName(String pageField, String methodPrefix) {
        return methodPrefix + pageField.substring(0, 1).toUpperCase() + pageField.substring(1);
    }

    private static String addError(String xmlPageName, String xmlFieldName) {
        return "\naddError(FORM_NAME, null, \"" + xmlPageName+ "\", null, \""+xmlFieldName+"\", \"msgEmpty\");";
    }

    private static String checkStr(DataHandler.FieldInfo fieldInfo, boolean filled) {
        String pageVariableTmp = fieldInfo.isVariablePageList?"p":fieldInfo.pageVariable;
        String checkString = "";
        switch (fieldInfo.fieldType) {
            case "java.lang.String":
                checkString = "StringUtils." +(filled?"isNotBlank":"isBlank")
                        + "("+ pageVariableTmp + "." + methodName(fieldInfo.fieldProperty, "get") + "())";
                break;
            case "java.lang.Boolean":
            case "boolean":
                checkString = "BooleanUtils." +(filled?"isTrue":"isNotTrue")
                        + "("+ pageVariableTmp + "." + methodName(fieldInfo.fieldProperty, "is") + "())";
                break;
            default:
                checkString = pageVariableTmp + "."
                        + methodName(fieldInfo.fieldProperty, "get") + "()"  +(filled?"!=":"==") + " null";
                break;
        }

        if(fieldInfo.isVariablePageList) {
            checkString = "("+fieldInfo.pageVariable+"!= null && "+fieldInfo.pageVariable+".stream().anyMatch(p -> "+checkString+"))";
        }

        return checkString;
    }

    private static String doFlkMethodCall(String flkCheckXmlField, List<String> dependOnXmlFields) {
        DataHandler.FieldInfo fieldInfo = DataHandler.fields.get(flkCheckXmlField);
        return methodName(fieldInfo.fieldProperty, "doFlk") + parameterListStr(dependOnXmlFields.stream().filter(fld->DataHandler.fields.get(fld).isLocalPageVariable).collect(Collectors.toList())) + ";";
    }

    private static String doFlkMethodDeclaration(String xmlField, List<String> dependOnXmlFields) {
        DataHandler.FieldInfo fieldInfo = DataHandler.fields.get(xmlField);

        StringBuilder sb = new StringBuilder();
        sb.append("\n//" + fieldInfo.xmlPageName + "." + fieldInfo.xmlFieldName + "\n");
        sb.append("private void ").append(methodName(fieldInfo.fieldProperty, "doFlk"));

        sb.append(parameterListStr(dependOnXmlFields.stream().filter(fld->DataHandler.fields.get(fld).isLocalPageVariable).collect(Collectors.toList()))).append("{\n");

        sb.append("if(" + checkStr(fieldInfo, false));

        StringBuilder conditionSb = new StringBuilder();
        for (int i = 0; i < dependOnXmlFields.size(); i++ ) {
            conditionSb.append(checkStr(DataHandler.fields.get(dependOnXmlFields.get(i)), true));

            if(i != dependOnXmlFields.size() - 1)
                conditionSb.append(" \n|| ");
        }

        if(conditionSb.length() > 0) {
            sb.append(" \n&& (").append(conditionSb).append(")");
        }

        sb.append(")");

        sb.append(addError(fieldInfo.xmlPageName, fieldInfo.xmlFieldName));

        sb.append("}\n");

        return sb.toString();
    }

    private static String parameterListStr(List<String> localVarList) {
        StringBuilder prameterSb = new StringBuilder();
        for (int i = 0; i < localVarList.size(); i++ ) {
            DataHandler.FieldInfo dfi = DataHandler.fields.get(localVarList.get(i));
            if(dfi.isLocalPageVariable) {
                prameterSb.append(dfi.localPageVariableType).append(" ").append(dfi.pageVariable);

                if(i != localVarList.size() - 1)
                    prameterSb.append(" ,");
            }

        }

        if(prameterSb.length() > 0) {
            prameterSb.append("(").append(prameterSb).append(")");
        } else {
            prameterSb.append("()");
        }

        return prameterSb.toString();
    }
}
