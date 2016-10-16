package com.genrocket.bank

class User {
  String lastName
  String firstName
  String username
  String emailAddress
  String phoneNumber

  static constraints = {
    lastName nullable: false, blank: false, maxSize: 25
    firstName nullable: false, blank: false, maxSize: 25
    username nullable: false, blank: false, maxSize: 25, unique: true
    emailAddress nullable: false, blank: false, maxSize: 50, unique: true
    phoneNumber nullable: false, blank: false, maxSize: 25
  }
}
