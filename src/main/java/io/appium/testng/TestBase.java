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

    private DesiredCapabilities dc = new DesiredCapabilities();
    AppiumDriver driver = null;
    Logger LOGGER = new Log4jLoggerFactory().getLogger(this.getClass().getName());

    private String iosAppName;
    private String androidAppName;
    private String os;
    private String seetestCloudURL;

    @BeforeClass
    public void setUp(ITestContext testContext) {
        LOGGER.info("Enter TestBase setUp");
        this.loadInitProperties();
        this.initDefaultDesiredCapabilities();
        dc.setCapability("testName", testContext.getCurrentXmlTest().getName());
        if (os.equals("android")) {
            dc.setCapability(MobileCapabilityType.APP, "cloud:"+androidAppName);
            dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".LoginActivity");
            this.initAndroidDriver(dc);
        } else {
            dc.setCapability(MobileCapabilityType.APP, "cloud:"+iosAppName);
            dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, iosAppName);
            this.initIOSDriver(dc);
        }
    }

    /**
     * Initialize default properties.
     *
     */
    private void initDefaultDesiredCapabilities() {
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
        String query = "@os='" + os + "'";
        dc.setCapability("deviceQuery", query);
        LOGGER.info("Device Query = {}",query);
        LOGGER.info("Desired Capabilities setup complete");
    }

    /**
     * Gets the Android Driver.
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
     * Gets the IOS Driver.
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
        Properties properties = new Properties();
        try (FileReader fr = new FileReader(pathToProperties)) {
            properties.load(fr);
        } catch (FileNotFoundException e) {
            LOGGER.warn("Could not load init properties", pathToProperties, e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        iosAppName = String.valueOf(properties.get("ios.app.name"));
        androidAppName = String.valueOf(properties.get("android.app.name"));
        os = String.valueOf(properties.get("os"));
        seetestCloudURL = String.valueOf(properties.get("seetest.cloud.url"));
    }

    @AfterClass
    protected void tearDown() {
        driver.quit();
    }
}
