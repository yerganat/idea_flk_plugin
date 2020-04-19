package kz.inessoft.sono.plugin.flk;

import com.intellij.lang.jvm.annotation.JvmAnnotationConstantValue;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;
import kz.inessoft.sono.plugin.flk.dialog.UtilityDialogWrapper;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class to demonstrate how to interact with the IntelliJ Platform.
 * The only action this class performs is to provide the user with a popup dialog as feedback.
 * Typically this class is instantiated by the IntelliJ Platform framework based on declarations
 * in the plugin.xml file. But when added at runtime this class is instantiated by an action group.
 */
public class SonoFlkAction extends AnAction {

    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on whether a project is open
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);

        psiFile.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitLocalVariable(PsiLocalVariable variable) {
                super.visitLocalVariable(variable);
                fillVariables(variable.getType(), true, variable.getName(), variable.getTypeElement().getText());
            }
        });

        Editor editor = event.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile2 = event.getData(CommonDataKeys.PSI_FILE);
        //if (editor == null || psiFile == null) return;
        int offset = editor.getCaretModel().getOffset();
        PsiElement element = psiFile2.findElementAt(offset);

        //PsiMethod containingMethod = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
        //PsiClass containingClass = containingMethod.getContainingClass();

        PsiClass containingClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        for (PsiField psiField : containingClass.getFields()) {
            fillVariables(psiField.getType(), false, psiField.getName(), null);
        }

        PsiClass parentClass = containingClass.getSuperClass();
        for (PsiField psiField : parentClass.getFields()) {

            fillVariables(psiField.getType(), false, psiField.getName(), null);
        }

        UtilityDialogWrapper dialogWrapper = new UtilityDialogWrapper();
        if(!dialogWrapper.showAndGet()) {
            return;
        }

        WriteCommandAction.runWriteCommandAction(element.getProject(), () ->{
            SonoFlkUtility.generateCode(element);

        });
        // Using the event, create and show a dialog
        Project currentProject = event.getProject();
        StringBuffer dlgMsg = new StringBuffer(event.getPresentation().getText() + " Selected!");
        String dlgTitle = event.getPresentation().getDescription();
        // If an element is selected in the editor, add info about it.
        Navigatable nav = event.getData(CommonDataKeys.NAVIGATABLE);
        if (nav != null) {
            dlgMsg.append(String.format("\nSelected Element: %s", nav.toString()));
        }
        Messages.showMessageDialog(currentProject, dlgMsg.toString(), dlgTitle, Messages.getInformationIcon());
    }

    private static void fillVariables(PsiType psiType, boolean isLocalVariable, String variableName, String localVariableType) {
        boolean isFieldList = false;

        PsiClass psiPageDeclaration = null;
        if (psiType instanceof PsiClassType && psiType.getCanonicalText().contains("java.util.List")) {
            PsiClassType.ClassResolveResult psiGeneric =  ((PsiClassType) psiType).resolveGenerics();
            if(psiGeneric.getSubstitutor().getSubstitutionMap().size() > 0) {
                psiPageDeclaration = PsiTypesUtil.getPsiClass(psiGeneric.getSubstitutor().getSubstitutionMap().entrySet().iterator().next().getValue().getDeepComponentType());
            }
        }

        if(psiPageDeclaration != null) {
            isFieldList = true;
        }  else {
            psiPageDeclaration = PsiTypesUtil.getPsiClass(psiType);
        }

        if(psiPageDeclaration == null)
            return;

        String xmlPageName = "";
        if (psiPageDeclaration.getAnnotations().length > 1)
            xmlPageName = ((JvmAnnotationConstantValue) psiPageDeclaration.getAnnotations()[1].getAttributes().get(0).getAttributeValue()).getConstantValue().toString();


        List<String> pageFieldList = new ArrayList<>();

        for (PsiField psiField : psiPageDeclaration.getFields()) {
            String xmlFieldName = "";
            if (psiField.getAnnotations().length > 0 && psiField.getAnnotations()[0].getAttributes().size()>0)
                xmlFieldName = ((JvmAnnotationConstantValue) psiField.getAnnotations()[0].getAttributes().get(0).getAttributeValue()).getConstantValue().toString();

            DataHandler.PageInfo pageInfo = new DataHandler.PageInfo();
            pageInfo.isFieldList = isFieldList;
            pageInfo.isLocalVariable = isLocalVariable;
            pageInfo.localVariableType = localVariableType;
            pageInfo.xmlFieldName = xmlFieldName;
            pageInfo.xmlPageName = xmlPageName;
            pageInfo.pageField = psiField.getName();
            pageInfo.variable = variableName;

            if(!DataHandler.fields.containsKey(xmlFieldName) || isLocalVariable)
                DataHandler.fields.put(xmlFieldName, pageInfo);

            pageFieldList.add(xmlFieldName);
        }

        if(StringUtils.isNotBlank(xmlPageName))
            DataHandler.pages.put(xmlPageName, pageFieldList);
    }

}