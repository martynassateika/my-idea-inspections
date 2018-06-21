package lt.martynassateika.idea.inspection;

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Provides a base class for custom inspections.</p>
 * <p>
 * <p>Created on 10/06/2018 by martynas.sateika</p>
 *
 * @author martynas.sateika
 * @since 1.0
 */
public abstract class MyBaseJavaLocalInspectionTool extends AbstractBaseJavaLocalInspectionTool {

    @NotNull
    public String getGroupDisplayName() {
        return "Martynas Sateika - My IDEA Inspections";
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

}
