package kz.inessoft.sono.plugin.flk;

import com.intellij.openapi.editor.Document;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import kz.inessoft.sono.plugin.flk.utils.PsiDocumentUtils;

public class SonoFlkUtility {
    public static void generateCode(PsiElement psiElement) {
        PsiDocumentManager psiDocumentManager = PsiDocumentManager
                .getInstance(psiElement.getProject());
        PsiFile containingFile = psiElement.getContainingFile();
        Document document = psiDocumentManager.getDocument(containingFile);

        PsiClass containingClasee = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);

        if (containingClasee != null) {
            document.insertString(containingClasee.getTextRange().getEndOffset() - 1, "\n public void generatedTest() {}\n");
        }

        PsiMethod containingMethod = PsiTreeUtil.getParentOfType(psiElement, PsiMethod.class);
        if (containingMethod != null) {
            document.insertString(psiElement.getTextOffset(), "\nString generatedTest;");
        }

        PsiDocumentUtils.commitAndSaveDocument(psiDocumentManager, document);
    }
}
