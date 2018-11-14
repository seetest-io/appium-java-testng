# seetest.io - Java - TestNG sample project

This project demonstrates TestNG framework and how it can be used to test Mobile and Web Application.

This example will cover:

1. Tests are run against seetest cloud.
2. Usage of Appium and Selenium libraries to test Mobile and Web Application.
3. Some examples of passing external Parameters from the TestNG Suite to add dynamism in the tests.
3. Running tests on iOS \ Android.

### Steps to run demo test

1. Clone this git repository

	```
	git clone
	```

2. Obtain access key from seetest.io cloud

    https://docs.seetest.io/display/SEET/Execute+Tests+on+SeeTest+-+Obtaining+Access+Key

    note :  you need to have a valid subscription or a trial to run this test (Trial \ paid)

3. Upload the eribank application to your project
    Download the Android app : https://experitest.s3.amazonaws.com/eribank.apk
    Download the iOS app : https://experitest.s3.amazonaws.com/EriBank.ipa

    Go to the cloud "Mobile Application Center" and upload both apps
    https://cloud.seetest.io/index.html#/applications

4. To run the tests,

    Please ensure that following environment variables are set.

    1. JAVA_HOME to JDK/JRE HOME and update it in the PATH variable.
    2. SEETEST_IO_ACCESS_KEY to valid access key obtained before in Step 2.

        ```
    	set SEETEST_IO_ACCESS_KEY = <your access key>
    	```

    Now run the tests using following command in command line.

    ```
    gradlew runTests
    ```

    ### Note :
    If you are using IDE like IntelliJ, make sure you create a Run configuration to use src/main/java/testng.xml.
    Set the Environment variable and then proceed to execute the test.

    ![Imgur](https://imgur.com/a/lMuRRmx)

5. To Run tests in parallel, use src/main/java/testng-parallel-class.xml file

	Right click on the file and run from IntelliJ \ Eclipse
	Or use the command line :

	```
	gradlew runTestsParallel
	```

### How to change to your own application

1. Upload you application to the cloud

    (review step two in guide)

2. Change the android application name or iOS application name in the src/main/java/resources/seetest.properties file

    For IOS,

	```
	ios.app.name = com.company.app
	```

    For Android,

    ```
    android.app.name = com.company.app/.appActivity
    ```

3. Modify the tests

	Change the @Test methods in IOSTestNGExampleTest \ AndroidTestNGExampleTest \ WebTestNGExampleTest sources.

	You can paste the code you've exported from SeeTestAutomation

