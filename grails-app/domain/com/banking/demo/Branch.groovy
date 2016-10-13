package com.banking.demo

class Branch {
  Long externalId
  String name
  String address
  String city
  String state
  String zipCode
  String country
  String phoneNumber

  static hasMany = [
    accounts : Account
  ]

  static constraints = {
    externalId nullable: false, unique: true
    name nullable: false, blank: false, maxSize: 40
    address nullable: false, blank: false, maxSize: 50
    city nullable: false, blank: false, maxSize: 25
    state nullable: false, blank: false, maxSize: 25
    zipCode nullable: false, blank: false, maxSize: 10
    country nullable: false, blank: false, maxSize: 25
    phoneNumber nullable: false, blank: false, maxSize: 25
  }

}