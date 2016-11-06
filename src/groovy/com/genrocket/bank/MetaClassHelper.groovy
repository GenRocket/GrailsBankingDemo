package com.genrocket.bank

import java.text.DecimalFormat

class MetaClassHelper {

  public static void initialize() {
    Float.metaClass.format = { -> new DecimalFormat("#.00").format(delegate) }
    Long.metaClass.format = { -> new DecimalFormat("#.00").format(delegate) }
    Integer.metaClass.format = { -> new DecimalFormat("#.00").format(delegate) }
  }
}
