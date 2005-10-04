/*
 * This class represents attributes that are not of numerical types, for
 * example, Strings and booleans.
 */

package simse.modelbuilder.objectbuilder;

public class NonNumericalAttribute extends Attribute {
  public NonNumericalAttribute(String name, int type, boolean visible,
      boolean key, boolean visAtEnd) {
    super(name, type, visible, key, visAtEnd);
  }

  public NonNumericalAttribute(NumericalAttribute n, int newType) // casts n
                                                                  // into a new
                                                                  // NonNumericalAttribute
                                                                  // with type
                                                                  // newType
  {
    super(n.getName(), newType, n.isVisible(), n.isKey(), n
        .isVisibleOnCompletion());
  }
}