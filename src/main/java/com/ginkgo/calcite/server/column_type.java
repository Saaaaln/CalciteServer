package com.ginkgo.calcite.server; /**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */

import org.apache.thrift.TEnum;

public enum column_type implements TEnum {
  INT(0),
  FLOAT(1),
  DOUBLE(2),
  STRING(3);

  private final int value;

  private column_type(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static column_type findByValue(int value) { 
    switch (value) {
      case 0:
        return INT;
      case 1:
        return FLOAT;
      case 2:
        return DOUBLE;
      case 3:
        return STRING;
      default:
        return null;
    }
  }
}
