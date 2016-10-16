package com.banking.demo

import com.banking.demo.co.LoginCO

class HomeController {

    def index() {}

    def login(LoginCO loginCO) {
        if(loginCO.validate()) {
           render("hey")
        } else {
            render(view: "index", model: [loginCO: loginCO])
        }
    }
}
