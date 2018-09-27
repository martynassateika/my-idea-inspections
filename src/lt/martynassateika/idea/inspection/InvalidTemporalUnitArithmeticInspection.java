package lt.martynassateika.idea.inspection;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.impl.compiled.ClsEnumConstantImpl;
import com.intellij.util.containers.ContainerUtil.ImmutableMapBuilder;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lt.martynassateika.idea.inspection.util.MyEnumUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * @author martynas.sateika
 * @since 1.1.0
 */
public class InvalidTemporalUnitArithmeticInspection extends MyBaseJavaLocalInspectionTool {

  private static final Map<String, Set<String>> ALLOWED_UNITS;

  static {
    ALLOWED_UNITS = new ImmutableMapBuilder<String, Set<String>>()
        .put(LocalDate.class.getName(), unitsBetween(ChronoUnit.DAYS, ChronoUnit.ERAS))
        .put(LocalDateTime.class.getName(), unitsBetween(ChronoUnit.NANOS, ChronoUnit.ERAS))
        .put(LocalTime.class.getName(), unitsBetween(ChronoUnit.NANOS, ChronoUnit.HALF_DAYS))
        .put(OffsetDateTime.class.getName(), unitsBetween(ChronoUnit.NANOS, ChronoUnit.ERAS))
        .put(OffsetTime.class.getName(), unitsBetween(ChronoUnit.NANOS, ChronoUnit.HALF_DAYS))
        .put(Instant.class.getName(), unitsBetween(ChronoUnit.NANOS, ChronoUnit.DAYS))
        .put(ZonedDateTime.class.getName(), unitsBetween(ChronoUnit.NANOS, ChronoUnit.ERAS))
        .build();
  }

  @Nls
  @NotNull
  @Override
  public String getDisplayName() {
    return "Invalid ChronoUnit added or subtracted from JSR310 class";
  }

  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    return new JavaElementVisitor() {
      @Override
      public void visitMethodCallExpression(PsiMethodCallExpression expression) {
        PsiMethod psiMethod = expression.resolveMethod();
        if (psiMethod != null) {
          String methodName = psiMethod.getName();

          // Right method
          if (methodName.equals("plus") || methodName.equals("minus")) {
            PsiClass containingClass = psiMethod.getContainingClass();
            if (containingClass != null) {
              String qualifiedName = containingClass.getQualifiedName();
              Set<String> allowedUnits = ALLOWED_UNITS.get(qualifiedName);

              // Right class
              if (allowedUnits != null) {
                PsiExpression chronoUnitExpression = getChronoUnitExpression(expression);
                if (chronoUnitExpression instanceof PsiReferenceExpression) {
                  PsiElement resolved = ((PsiReferenceExpression) chronoUnitExpression).resolve();
                  if (resolved instanceof ClsEnumConstantImpl) {

                    // Enum constant of type ChronoUnit
                    if (isChronoUnitConstant(resolved)) {
                      String enumConstantName = ((ClsEnumConstantImpl) resolved).getName();
                      if (!allowedUnits.contains(enumConstantName)) {
                        holder.registerProblem(chronoUnitExpression,
                            String.format(
                                "Cannot add or subtract this ChronoUnit from class: '%s', allowed elements are: %s",
                                qualifiedName, allowedUnits));
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    };
  }

  private static PsiExpression getChronoUnitExpression(PsiMethodCallExpression expression) {
    PsiExpressionList argumentList = expression.getArgumentList();
    PsiExpression[] expressions = argumentList.getExpressions();
    if (expressions.length == 2) {
      return expressions[1];
    }
    return null;
  }

  private static boolean isChronoUnitConstant(PsiElement psiElement) {
    if (psiElement instanceof PsiField) {
      PsiElement parent = psiElement.getParent();
      if (parent instanceof PsiClass) {
        String qualifiedName = ((PsiClass) parent).getQualifiedName();
        if (qualifiedName != null) {
          return qualifiedName.equals(ChronoUnit.class.getName());
        }
      }
    }
    return false;
  }

  private static Set<String> unitsBetween(ChronoUnit first, ChronoUnit last) {
    Set<ChronoUnit> units = MyEnumUtil.constantsBetween(first, last, ChronoUnit.class);
    return units.stream()
        .map(Enum::name)
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

}
