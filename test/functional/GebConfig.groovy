import org.openqa.selenium.chrome.ChromeDriver

driver = {
  def driver = new ChromeDriver()
  driver.manage().window().maximize()
  return driver
}

environments {
  chrome {
    driver = {
      ChromeDriver driver = new ChromeDriver()
      driver.manage().window().maximize()
      return driver
    }
  }
}
