
      package com.genrocket.bank.testData

      import com.genRocket.tdl.LoaderDTO
      import org.springframework.transaction.annotation.Transactional

      import com.genrocket.bank.testDataLoader.CardTestDataLoader
      import com.genrocket.bank.Card
      import com.genrocket.bank.CardType
      import com.genrocket.bank.Customer

      /**
       * Generated By GenRocket 10/16/2016.
       */
      @Transactional
      class CardTestDataService {
        static transactional = true

        def cardService
        def cardTypeTestDataService
        def customerTestDataService

        def loadData(Integer loopCount = 1, Map<String, Object> domainMap = null) {
          println "Loading data for Card..."
          
          CardType cardType = null
          Customer customer = null
          if (domainMap) {
             cardType = (CardType) domainMap['cardType']
             customer = (Customer) domainMap['customer']
          }

          
          if (Card.count() == 0) {
            if(!cardType) { 
              cardTypeTestDataService.loadData()
              cardType = CardType.first()
            }

            if(!customer) { 
              customerTestDataService.loadData()
              customer = Customer.first()
            }

            def cardList = (LoaderDTO[]) CardTestDataLoader.load(loopCount)

            cardList.each { node ->
              def card = (Card) node.object
      
              card.cardType = cardType
              card.customer = customer
              cardService.save(card)
            }
          }
        }
      }
    