package io.appium.testng;

import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;
import utils.SeeTestProperties;

import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IOSTestNGExampleTest extends TestBase {

    @DataProvider(name="makePaymentsData")
    public Object[][] makePaymentsDataProvider() {
        return new Object [][]
                {
                        {"123456","John Snow","10", "US"}
                };
    }

    /**
     * Sets up the default Desired Capabilities.
     */
    protected void initDefaultDesiredCapabilities() {
        LOGGER.info("Enter initDefaultDesiredCapabilities");
        super.initDefaultDesiredCapabilities();
        String iosAppName = String.valueOf(properties.get("ios.app.name"));
            // Sets up the capabilities for iOS App.
        LOGGER.info("MobileCapabilityType.APP = " + "cloud:"+iosAppName);
        dc.setCapability(MobileCapabilityType.APP, "cloud:"+iosAppName);
        LOGGER.info("IOSMobileCapabilityType.BUNDLE_ID = " + iosAppName);
        dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, iosAppName);
        LOGGER.info("Exit initDefaultDesiredCapabilities");
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
        // Find Element commands for Find Login elements.
        driver.findElement(ELEMENTS.LOGIN_USER.getBy(ELEMENTS.TYPE.IOS)).sendKeys(userName);
        driver.findElement(ELEMENTS.LOGIN_PASS.getBy(ELEMENTS.TYPE.IOS)).sendKeys(password);
        driver.findElement(ELEMENTS.LOGIN_BUTTON.getBy(ELEMENTS.TYPE.IOS)).click();
        LOGGER.info("Exit eriBankLogin");
    }

    @AfterMethod
    public void eriBankLogout() {
        LOGGER.info("Enter eriBankLogout");
        driver.findElement(ELEMENTS.LOGOUT_BUTTON.getBy(ELEMENTS.TYPE.IOS)).click();
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
        driver.findElement(ELEMENTS.PAYMENT_BUTTON.getBy(ELEMENTS.TYPE.IOS)).click();
        driver.findElement(ELEMENTS.PHONE.getBy(ELEMENTS.TYPE.IOS)).sendKeys(phone);
        driver.findElement(ELEMENTS.NAME.getBy(ELEMENTS.TYPE.IOS)).sendKeys(name);
        driver.findElement(ELEMENTS.AMOUNT.getBy(ELEMENTS.TYPE.IOS)).sendKeys(amount);
        driver.findElement(ELEMENTS.COUNTRY.getBy(ELEMENTS.TYPE.IOS)).sendKeys(country);
        driver.findElement(ELEMENTS.SEND_PAYMENT_BUTTON.getBy(ELEMENTS.TYPE.IOS)).click();
        driver.findElement(ELEMENTS.YES_BUTTON.getBy(ELEMENTS.TYPE.IOS)).click();
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

    /**
     * sets the application ("app") capability based on the OS and the property which was defined in the seetest.properties file
     *
     * @param os
     */
    private void setAppCapability(@Optional("android") String os) {
        String appName = os.equals("android") ?
                properties.getProperty(SeeTestProperties.Names.ANDROID_APP_NAME) :
                properties.getProperty(SeeTestProperties.Names.IOS_APP_NAME);

        appName = String.format("%s%s", "cloud:", appName);
        LOGGER.info("Setting up {} as app capability", appName);
        dc.setCapability(MobileCapabilityType.APP, appName);
    }
}
