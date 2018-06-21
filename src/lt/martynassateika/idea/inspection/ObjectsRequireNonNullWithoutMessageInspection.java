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
public class ObjectsRequireNonNullWithoutMessageInspection extends MyBaseJavaLocalInspectionTool {

    private LocalQuickFix myQuickFix = new MyQuickFix();

    @NotNull
    public String getDisplayName() {
        return "'Objects.requireNonNull' without 'message' argument";
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                PsiReferenceExpression methodExpression = expression.getMethodExpression();
                if (MyPsiUtil.isMethodCall(methodExpression, "requireNonNull", "java.util.Objects")) {
                    PsiExpressionList argumentList = expression.getArgumentList();
                    int argumentCount = argumentList.getExpressionCount();
                    if (argumentCount == 1) {
                        holder.registerProblem(expression, "Objects.requireNonNull called without \"message\" argument", myQuickFix);
                    }
                }
            }
        };
    }

    private class MyQuickFix implements LocalQuickFix {
        @Nls
        @NotNull
        @Override
        public String getName() {
            return "Add \"message\" parameter";
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
            if (psiElement instanceof PsiMethodCallExpression) {
                PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) psiElement;
                PsiExpressionList argumentList = methodCallExpression.getArgumentList();
                PsiExpression firstArgument = argumentList.getExpressions()[0];
                String firstArgumentText = firstArgument.getText().replace("\"", "");
                PsiElementFactory elementFactory = JavaPsiFacade.getInstance(project).getElementFactory();
                PsiExpression newExpression = elementFactory.createExpressionFromText('"' + firstArgumentText + '"', null);
                argumentList.add(newExpression);
            }
        }

    }

}
