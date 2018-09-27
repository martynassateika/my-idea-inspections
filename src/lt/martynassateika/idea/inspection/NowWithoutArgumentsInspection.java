package lt.martynassateika.idea.inspection;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import org.jetbrains.annotations.NotNull;

/**
 * @author martynas.sateika
 * @since 1.1.0
 */
public class NowWithoutArgumentsInspection extends MyBaseJavaLocalInspectionTool {

  @NotNull
  public String getDisplayName() {
    return "JSR310 class 'now' method called with no arguments";
  }

  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    return new JavaElementVisitor() {
      @Override
      public void visitMethodCallExpression(PsiMethodCallExpression expression) {
        PsiMethod psiMethod = expression.resolveMethod();
        if (psiMethod != null && psiMethod.getName().equals("now")) {
          PsiClass containingClass = psiMethod.getContainingClass();
          if (isJsr310Class(containingClass)) {
            if (expression.getArgumentList().isEmpty()) {
              holder.registerProblem(expression,
                  "JSR310 class 'now' method called with no arguments");
            }
          }
        }
      }
    };
  }

  private static boolean isJsr310Class(PsiClass containingClass) {
    if (containingClass != null) {
      String qualifiedName = containingClass.getQualifiedName();
      return qualifiedName != null && qualifiedName.startsWith("java.time.");
    }
    return false;
  }

}
