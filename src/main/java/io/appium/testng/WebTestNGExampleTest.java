package io.appium.testng;


import io.appium.java_client.remote.MobileBrowserType;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Example using TestNG for Web Browser Test in an IOS or Android Device.
 */
public class WebTestNGExampleTest extends TestBase {

    @BeforeClass
    public void setUp(ITestContext testContext) {
        super.setUp(testContext);
    }

    /**
     * Sets up the default Desired Capabilities.
     */
    protected void initDefaultDesiredCapabilities() {
        LOGGER.info("Enter initDefaultDesiredCapabilities");
        super.initDefaultDesiredCapabilities();
        if (os.equals("android")) {
            dc.setBrowserName(MobileBrowserType.CHROME);
        } else {
            dc.setBrowserName(MobileBrowserType.SAFARI);
        }
        LOGGER.info("Exit initDefaultDesiredCapabilities");
    }

    @Parameters ({"url" , "title"})
    @Test
    public void titleTest(@Optional("http://google.com") String url, @Optional("Google") String title) {
        LOGGER.info("Enter titleTest");
        LOGGER.info("url = " + url);
        driver.get(url);

        String actualTitle = driver.getTitle();
        LOGGER.info("Actual title = " + actualTitle + " ; Expected Title = " + title);
        Assert.assertEquals(actualTitle, title);
    }
}
