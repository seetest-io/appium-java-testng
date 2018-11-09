package io.appium.testng;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Eribank Appium test using TestNG.
 */
public class AppiumTestNGExampleTest extends TestBase {

    @DataProvider(name="makePaymentsData")
    public Object[][] makePaymentsDataProvider() {
       return new Object [][]
               {
                  {"123456","John Snow","10", "US"}
               };
    }

    /**
     * Login Request before every test, because it is a step for any independent test.
     *
     * Good example for Optional annotation.
     *
     * @param userName userName
     * @param password password
     */
    @Parameters({"userName","password"})
    @BeforeMethod
    public void eriBankLogin(@Optional("company") String userName, @Optional("company") String password) {
        LOGGER.info("Enter eriBankLogin - " + "userName = " + userName + " password = " + password);
        driver.findElement(By.xpath("//*[@id='usernameTextField']")).sendKeys(userName);
        driver.findElement(By.xpath("//*[@id='passwordTextField']")).sendKeys(password);
        driver.findElement(By.xpath("//*[@id='loginButton']")).click();
        LOGGER.info("Exit eriBankLogin");
    }

    @AfterMethod
    public void eriBankLogout() {
        LOGGER.info("Enter eriBankLogout");
        driver.findElement(By.xpath("//*[@id='logoutButton']")).click();
        LOGGER.info("Exit eriBankLogout");
    }

    /**
     * Test for makePaymentTest
     *
     *
     * @param phone phone
     * @param name  name
     * @param amount amount
     * @param country country
     */
    @Test (dataProvider = "makePaymentsData")
    public void makePaymentTest(String phone, String name, String amount, String country) {
        LOGGER.info("Enter makePaymentTest - Phone = " + phone + " name = "
                + name + " amount = " + amount + " country = " + country);
        driver.findElement(By.xpath("//*[@id='makePaymentButton']")).click();
        driver.findElement(By.xpath("//*[@id='phoneTextField']")).sendKeys(phone);
        driver.findElement(By.xpath("//*[@id='nameTextField']")).sendKeys(name);
        driver.findElement(By.xpath("//*[@id='amountTextField']")).sendKeys(amount);
        driver.findElement(By.xpath("//*[@id='countryTextField']")).sendKeys(country);
        driver.findElement(By.xpath("//*[@id='sendPaymentButton']")).click();
        driver.findElement(By.xpath("//*[@text='Yes']")).click();
        LOGGER.info("Exit makePaymentTest");
    }

    /**
     * Asserts for Balance.
     *
     * @param expectedBalance Expected Balance.
     */
    @Parameters({"expectedBalance"})
    @Test
    public void balanceTest(@Optional("100") String expectedBalance) {
        LOGGER.info("Enter balanceTest - expectedBalance = " + expectedBalance);
        int currentBalance = getCurrentBalance();
        LOGGER.info("Actual Balance = " + currentBalance);
        Assert.assertEquals(currentBalance , Integer.parseInt(expectedBalance));
        LOGGER.info("Exit balanceTest");
    }

    /**
     * Gets current Balance.
     * @return Current balance.
     */
    private int getCurrentBalance() throws NoSuchElementException {
        String balanceField = driver.findElement(By.xpath("//*[contains(text(),\"$\")]")).getText();
        Pattern pattern = Pattern.compile("\\d{1,10}");
        Matcher matcher = pattern.matcher(balanceField);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        } else {
            throw new NoSuchElementException("Current Balance Not Found");
        }
    }

}
