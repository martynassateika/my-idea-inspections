package lt.martynassateika.idea.inspection.util;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.impl.compiled.ClsClassImpl;

/**
 * <p>Provides various PSI utility methods.</p>
 * <p>
 * <p>Created on 10/06/2018 by martynas.sateika</p>
 *
 * @author martynas.sateika
 * @since 1.0
 */
public class MyPsiUtil {

    /**
     * @param methodExpression method expression reference
     * @param methodName       expected method name, e.g. 'hashCode'
     * @param classQname       qualified name of the class housing the {@code methodName} method, e.g. 'java.util.Objects'
     * @return {@code true} if {@code methodExpression} represents the method {@code methodName} in {@code classQname}
     */
    public static boolean isMethodCall(PsiReferenceExpression methodExpression, String methodName, String classQname) {
        PsiReference reference = methodExpression.getReference();
        if (methodName.equals(methodExpression.getReferenceName())) {
            if (reference != null) {
                PsiElement resolve = reference.resolve();
                if (resolve != null) {
                    PsiElement parent = resolve.getOriginalElement().getParent();
                    if (parent instanceof ClsClassImpl) {
                        ClsClassImpl clsClass = (ClsClassImpl) parent;
                        return classQname.equals(clsClass.getQualifiedName());
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param aClass           a class PSI element
     * @param parentClassQname qualified name of a class
     * @return {@code true} if {@code aClass} is a sub-class of {@code parentClassQname}
     */
    public static boolean isSubclassOf(PsiClass aClass, String parentClassQname) {
        PsiClass superClass = aClass.getSuperClass();
        while (superClass != null) {
            if (parentClassQname.equals(superClass.getQualifiedName())) {
                return true;
            }
            superClass = superClass.getSuperClass();
        }
        return false;
    }

}
