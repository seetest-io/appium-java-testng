package io.appium.testng;

import org.openqa.selenium.By;

/**
 * ENUM Constants representing all elements used in Eribank application.
 */
public enum ELEMENTS {

    LOGIN_USER("//*[@id='usernameTextField']"),
    LOGIN_PASS("//*[@id='passwordTextField']"),
    LOGIN_BUTTON("//*[@id='loginButton']"),
    LOGOUT_BUTTON("//*[@id='logoutButton']"),
    PAYMENT_BUTTON ("//*[@id='makePaymentButton']"),
    PHONE("//*[@id='phoneTextField']"),
    NAME("//*[@id='nameTextField']"),
    AMOUNT("//*[@id='amountTextField']"),
    COUNTRY("//*[@id='countryTextField']"),
    SEND_PAYMENT_BUTTON("//*[@id='sendPaymentButton']"),
    YES_BUTTON("//*[@text='Yes']");

    /**
     * Operating System for the ELEMENT.
     * Sometimes the location string are different for different operating system.
     *
     */
    enum TYPE {
        IOS, ANDROID;
    }

    private String androidLocateString;
    private String iosLocateString;

    /**
     * Constructs and ELEMENT with default location String.
     * @param locationString
     *
     */
    ELEMENTS(String locationString) {
        this.androidLocateString = locationString;
        this.iosLocateString = locationString;
    }

    /**
     * Constructs and ELEMENT when location string are different for IOS and Android.
     * @param androidLocateString
     * @param iosLocateString
     */
    ELEMENTS(String androidLocateString , String iosLocateString) {
        this.androidLocateString = androidLocateString;
        this.iosLocateString = iosLocateString;
    }

    /**
     * Gets the String identifying the location.
     * @param osType Type of OS.
     * @return String Representing the location String.
     */
    public String getLocationString(TYPE osType) {
        if (osType == TYPE.IOS) {
            return this.androidLocateString;
        } else {
            return this.iosLocateString;
        }
    }

    /**
     * Gets By for this element.
     *
     * @param elementType
     * @return by object
     */
    public By getBy(TYPE elementType) {
        if (elementType == TYPE.IOS) {
            return By.xpath(iosLocateString);
        } else {
            return By.xpath(androidLocateString);
        }
    }
}
