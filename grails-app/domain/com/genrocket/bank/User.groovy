package com.genrocket.bank

class User {
  String title
  String firstName
  String middleInitial
  String lastName
  String suffix
  String username
  String emailAddress
  String phoneNumber

  static constraints = {
    title nullable: false, blank: false, maxSize: 5
    firstName nullable: false, blank: false, maxSize: 25
    middleInitial nullable: false, blank: false, maxSize: 1
    lastName nullable: false, blank: false, maxSize: 25
    suffix nullable: true, blank: false, maxSize: 10
    username nullable: false, blank: false, maxSize: 25, unique: true
    emailAddress nullable: false, blank: false, maxSize: 50, unique: true
    phoneNumber nullable: false, blank: false, maxSize: 25
  }
}
