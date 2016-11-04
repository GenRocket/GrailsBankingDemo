package com.genrocket.bank.co

import com.genrocket.bank.Card
import grails.validation.Validateable

@Validateable
class LoginCO {
  String cardNumber
  String pin

  static constraints = {
    cardNumber(nullable: false, blank: false, validator: { value, object ->
      Card card = value ? Card.findByCardNumber(value) : null
      if (card) {
        if (!card.enabled) {
          return "card.not.enabled"
        } else if (card.dateExpired < new Date()) {
          return "card.expired"
        } else if (card.dateDeactivated) {
          return "card.deactivated"
        } else if (object.pin && !object.pin) {
          return "invalid.pin.number"
        } else if (object.pin != card.pin) {
          return "invalid.pin.number"
        }
      } else {
        return "invalid.card.number"
      }
    })
    pin(nullable: false, blank: false)
  }
}
