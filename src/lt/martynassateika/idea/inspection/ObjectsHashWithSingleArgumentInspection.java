package lt.martynassateika.idea.inspection;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import lt.martynassateika.idea.inspection.util.MyPsiUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Created on 10/06/2018 by martynas.sateika</p>
 *
 * @author martynas.sateika
 * @since 1.0
 */
public class ObjectsHashWithSingleArgumentInspection extends MyBaseJavaLocalInspectionTool {

    private LocalQuickFix myQuickFix = new MyObjectsHashFix();

    @NotNull
    public String getDisplayName() {
        return "Single argument passed to 'Objects.hash'";
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                PsiReferenceExpression methodExpression = expression.getMethodExpression();
                if (MyPsiUtil.isMethodCall(methodExpression, "hash", "java.util.Objects")) {
                    PsiExpressionList argumentList = expression.getArgumentList();
                    int argumentCount = argumentList.getExpressionCount();
                    if (argumentCount == 1) {
                        holder.registerProblem(methodExpression, "Objects.hash called with 1 argument.", myQuickFix);
                    }
                }
            }
        };
    }

    private static class MyObjectsHashFix implements LocalQuickFix {

        @Nls
        @NotNull
        @Override
        public String getName() {
            return "Replace with \"hashCode\"";
        }

        @Nls
        @NotNull
        @Override
        public String getFamilyName() {
            return getName();
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            PsiElement psiElement = descriptor.getPsiElement();
            PsiElement lastChild = psiElement.getLastChild();
            if (lastChild instanceof PsiIdentifier) {
                PsiIdentifier replacement = JavaPsiFacade.getElementFactory(project).createIdentifier("hashCode");
                lastChild.replace(replacement);
            }
        }

    }

}
