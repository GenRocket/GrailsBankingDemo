package pages

import geb.spock.GebReportingSpec
import spock.lang.Stepwise

@Stepwise
class LoginSpec extends GebReportingSpec {

  def "no credentials passed to login"() {
    when:
    to LoginPage

    then:
    at LoginPage

    when:
    submitButton.click()

    then:
    at LoginPage
    error.text()?.trim() == "Please enter card number.\nPlease enter pin."
  }

  def "successful login redirects to menu page"() {
    when:
    to LoginPage

    then:
    at LoginPage

    when:
    cardNumber.value("4100000000000001")
    pin.value("123456")
    submitButton.click()

    then:
    at MenuPage
  }

}
