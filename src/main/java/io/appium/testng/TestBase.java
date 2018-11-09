package io.appium.testng;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.impl.Log4jLoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

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

    DesiredCapabilities dc = new DesiredCapabilities();
    RemoteWebDriver driver = null;
    String os;
    Properties properties;
    private String deviceQuery;
    Logger LOGGER = new Log4jLoggerFactory().getLogger(this.getClass().getName());
    private String seetestCloudURL;


    /**
     * Core setup function, which sets up the selenium/appium drivers.
     * @param testContext Test Context for the Test.
     */
    @BeforeClass
    public void setUp(ITestContext testContext) {
        LOGGER.info("Enter TestBase setUp");
        this.loadInitProperties();
        this.initDefaultDesiredCapabilities();
        dc.setCapability("testName", testContext.getCurrentXmlTest().getName());
        if (os.equals("android")) {
            this.initAndroidDriver(dc);
        } else {
            this.initIOSDriver(dc);
        }
    }

    /**
     * Initialize default properties.
     *
     */
    protected void initDefaultDesiredCapabilities() {
        LOGGER.info("Setting up Desired Capabilities");
        String accessKey = System.getenv("SEETEST_IO_ACCESS_KEY");

        if (accessKey == null || accessKey.length() < 10) {
            LOGGER.error("Access key must be set in Environment variable SEETEST_IO_ACCESS_KEY");
            LOGGER.info("To get access get to to https://cloud.seetest.io or learn at https://docs.seetest.io/display/SEET/Execute+Tests+on+SeeTest+-+Obtaining+Access+Key", accessKey);
            throw new RuntimeException("Access key invalid : accessKey - " + accessKey);
        }
        dc.setCapability("accessKey", accessKey);
        dc.setCapability("fullReset", true);
        dc.setCapability("instrumented", true);
        deviceQuery = "@os='" + os + "'";
        dc.setCapability("deviceQuery", deviceQuery);
        LOGGER.info("Device Query = {}",deviceQuery);
        LOGGER.info("Desired Capabilities setup complete");
    }

    /**
     * Sets the Android Driver.
     */
    private void initAndroidDriver(DesiredCapabilities dc) {
        try {
            LOGGER.info("Initializing Android Driver ...");
            driver = new AndroidDriver<AndroidElement>(new URL(seetestCloudURL), dc);
            LOGGER.info("Android Driver Initialized ...");
        } catch (MalformedURLException malformedExc) {
            LOGGER.error("Cannot load the driver");
        }
    }

    /**
     * Sets the IOS Driver.
     */
    private void initIOSDriver(DesiredCapabilities dc) {
        try {
            LOGGER.info("Initializing IOS Driver ...");
            driver = new IOSDriver<IOSElement>(new URL(seetestCloudURL), dc);
            LOGGER.info("IOS Driver Initialized ...");
        } catch (MalformedURLException malformedExc) {
            LOGGER.error("Cannot load the driver");
        }
    }

    /**
     * Loads properties.
     */
    private void loadInitProperties() {
        String pathToProperties = Objects.requireNonNull(this.getClass().getClassLoader().getResource("seetest.properties")).getFile();
        properties = new Properties();
        try (FileReader fr = new FileReader(pathToProperties)) {
            properties.load(fr);
        } catch (FileNotFoundException e) {
            LOGGER.warn("Could not load init properties", pathToProperties, e);
        } catch (IOException e) {
            e.printStackTrace();
        }


        os = String.valueOf(properties.get("os"));
        seetestCloudURL = String.valueOf(properties.get("seetest.cloud.url"));
    }

    @AfterClass
    protected void tearDown() {
        driver.quit();
    }
}
