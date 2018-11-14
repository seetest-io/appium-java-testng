package io.appium.testng;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.boon.core.Sys;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.impl.Log4jLoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.experitest.appium.SeeTestCapabilityType;
import utils.SeeTestProperties;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;

/**
 * Base class for all Eribank tests.
 */
public class TestBase {

    public static final String ENV_VAR_ACCESS_KEY = "SEETEST_IO_ACCESS_KEY";
    public static final boolean FULL_RESET = true;
    public static final boolean INSTRUMENT_APP = true;

    DesiredCapabilities dc = new DesiredCapabilities();
    RemoteWebDriver driver = null;
    String os;
    Properties properties;
    private String deviceQuery;
    Logger LOGGER = new Log4jLoggerFactory().getLogger(this.getClass().getName());

    /**
     * Core setup function, which sets up the selenium/appium drivers.
     * @param testContext Test Context for the Test.
     */
    @Parameters("os")
    @BeforeClass
    public void setUp(@Optional("android") String os, ITestContext testContext) {
        LOGGER.info("Enter TestBase setUp");
        this.os = os;
        properties = SeeTestProperties.getSeeTestProperties();
        this.initDefaultDesiredCapabilities();
        dc.setCapability("testName",
                testContext.getCurrentXmlTest().getName() + "." + this.getClass().getSimpleName());
        driver = os.equals("android") ?
                new AndroidDriver(SeeTestProperties.SEETEST_IO_APPIUM_URL, dc) :
                new IOSDriver(SeeTestProperties.SEETEST_IO_APPIUM_URL, dc);
    }

    /**
     * Initialize default properties.
     *
     */
    protected void initDefaultDesiredCapabilities() {
        LOGGER.info("Setting up Desired Capabilities");
        String accessKey = System.getenv(ENV_VAR_ACCESS_KEY);

        if (accessKey == null || accessKey.length() < 10) {
            LOGGER.error("Access key must be set in Environment variable SEETEST_IO_ACCESS_KEY");
            LOGGER.info("To get access get to to https://cloud.seetest.io or learn at https://docs.seetest.io/display/SEET/Execute+Tests+on+SeeTest+-+Obtaining+Access+Key", accessKey);
            throw new RuntimeException("Access key invalid : accessKey - " + accessKey);
        }

        dc.setCapability(SeeTestCapabilityType.ACCESS_KEY, accessKey);
        dc.setCapability(MobileCapabilityType.FULL_RESET, FULL_RESET);
        String query = String.format("@os='%s'", os);
        dc.setCapability(SeeTestCapabilityType.DEVICE_QUERY, query);
        LOGGER.info("Device Query = {}", query);
        LOGGER.info("Desired Capabilities setup complete");
    }


    @AfterClass
    protected void tearDown() {
        driver.quit();
    }
}
