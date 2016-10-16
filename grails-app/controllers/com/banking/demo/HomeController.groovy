package com.banking.demo

import com.banking.demo.co.LoginCO

class HomeController {
    def bankingService

    def index() {
        if(bankingService.getSelectedCard()) {
            redirect(action: 'menu')
        }
    }

    def login(LoginCO loginCO) {
        if(loginCO.validate()) {
            Card card = Card.findByCardNumber(loginCO.cardNumber)
            bankingService.setSelectedCard(card)
            redirect(action: 'menu')
        } else {
            render(view: "index", model: [loginCO: loginCO])
        }
    }

    def menu() {

    }
}
