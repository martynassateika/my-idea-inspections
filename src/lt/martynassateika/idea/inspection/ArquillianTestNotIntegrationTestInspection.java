package lt.martynassateika.idea.inspection;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiIdentifier;
import lt.martynassateika.idea.inspection.util.MyTestUtil;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Created on 10/06/2018 by martynas.sateika</p>
 *
 * @author martynas.sateika
 * @since 1.0
 */
public class ArquillianTestNotIntegrationTestInspection extends MyBaseJavaLocalInspectionTool {

    @NotNull
    public String getDisplayName() {
        return "'Arquillian' test class name does not end in 'IT'";
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitClass(PsiClass aClass) {
                PsiIdentifier nameIdentifier = aClass.getNameIdentifier();
                if (nameIdentifier != null) {
                    if (MyTestUtil.isArquillianTest(aClass)) {
                        String qualifiedName = aClass.getQualifiedName();
                        if (qualifiedName != null && !qualifiedName.endsWith("IT")) {
                            holder.registerProblem(nameIdentifier, "'Arquillian' test class does not end in 'IT'");
                        }
                    }
                }
            }
        };
    }

}
