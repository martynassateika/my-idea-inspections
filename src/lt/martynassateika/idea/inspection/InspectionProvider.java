package lt.martynassateika.idea.inspection;

import com.intellij.codeInspection.InspectionToolProvider;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Created on 10/06/2018 by martynas.sateika</p>
 *
 * @author martynas.sateika
 * @since 1.0
 */
public class InspectionProvider implements InspectionToolProvider {

    @NotNull
    @Override
    public Class[] getInspectionClasses() {
        return new Class[]{
                ArquillianTestNotIntegrationTestInspection.class,
                NowWithoutArgumentsInspection.class,
                ObjectsHashWithSingleArgumentInspection.class,
                ObjectsRequireNonNullWithoutMessageInspection.class
        };
    }

}
