package com.genrocket.bank

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement

@XmlAccessorType(XmlAccessType.NONE)
class AccountType {

  @XmlElement
  String name

  static constraints = {
    name nullable: false, blank: false, maxSize: 25, unique: true
  }
}
