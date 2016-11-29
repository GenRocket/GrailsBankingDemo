package com.genrocket.bank

class BankingTagLib {
  def bankingService

  static namespace = "b"
  static defaultEncodeAs = [taglib: 'none']
  //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]


  def ifLoggedIn = { attr, body ->
    if (bankingService.getSelectedCard()) {
      out << body()
    }
  }

}
