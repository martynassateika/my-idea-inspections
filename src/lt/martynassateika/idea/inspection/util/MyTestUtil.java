package lt.martynassateika.idea.inspection.util;

import com.intellij.psi.*;

/**
 * <p>Provides test-related PSI methods.</p>
 * <p>
 * <p>Created on 10/06/2018 by martynas.sateika</p>
 *
 * @author martynas.sateika
 * @since 1.0
 */
public class MyTestUtil {

    /**
     * @param aClass a class PSI element
     * @return {@code true} if the class is an 'Arquillian' test class
     */
    public static boolean isArquillianTest(PsiClass aClass) {
        PsiAnnotation[] annotations = aClass.getAnnotations();
        for (PsiAnnotation annotation : annotations) {
            if ("org.junit.runner.RunWith".equals(annotation.getQualifiedName())) {
                PsiAnnotationParameterList parameterList = annotation.getParameterList();
                PsiNameValuePair[] attributes = parameterList.getAttributes();
                if (attributes.length == 1) {
                    PsiNameValuePair attribute = attributes[0];
                    PsiAnnotationMemberValue value = attribute.getValue();
                    if (value instanceof PsiClassObjectAccessExpression) {
                        PsiType type = ((PsiClassObjectAccessExpression) value).getType();
                        if (type.getCanonicalText().equals("java.lang.Class<org.jboss.arquillian.junit.Arquillian>")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
