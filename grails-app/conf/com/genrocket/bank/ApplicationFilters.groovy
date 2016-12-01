package com.genrocket.bank

class ApplicationFilters {
    def bankingService

    def filters = {
        all(controller:'account|branch|user', action:'*') {
            before = {
                Card card = bankingService.selectedCard
                if (!card) {
                    redirect(controller: 'home', action: 'index')
                    return false
                }
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
