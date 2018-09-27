package lt.martynassateika.idea.inspection.util;

import com.intellij.util.ArrayUtil;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 * Utility methods for dealing with Enums.
 *
 * @author martynas.sateika
 * @since 1.1.0
 */
public class MyEnumUtil {

  /**
   * @param firstInclusive index of first constant (in the order they're declared), inclusive
   * @param lastInclusive index of last constant (in the order they're declared), inclusive
   * @param clazz enum class
   * @param <E> enum type
   * @return a set with all constants of {@link E} between the two indices
   */
  public static <E extends Enum<E>> Set<E> constantsBetween(E firstInclusive, E lastInclusive,
      Class<E> clazz) {
    E[] enumConstants = clazz.getEnumConstants();
    int firstIdx = ArrayUtil.indexOf(enumConstants, firstInclusive);
    int lastIdx = ArrayUtil.indexOf(enumConstants, lastInclusive);
    if (firstIdx > lastIdx) {
      throw new IllegalArgumentException(String.format(
          "firstInclusive is defined after lastInclusive, firstInclusive: %s, lastInclusive: %s, clazz: %s",
          firstInclusive, lastInclusive, clazz.getName()));
    }
    return EnumSet.copyOf(Arrays.asList(enumConstants).subList(firstIdx, lastIdx + 1));
  }

}
