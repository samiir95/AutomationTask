package org.example.stepDefs;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.github.bonigarcia.wdm.WebDriverManager;
import managers.FileReaderManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import static io.github.bonigarcia.wdm.DriverManagerType.CHROME;


public class Hooks {

    public static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @Before(value = "@smoke")
    public void openBrowser() {

        if (System.getProperty("executionAddress", "local").equals("local")) {
            WebDriverManager.chromedriver().setup();
            WebDriver webDriver = new ChromeDriver();
            driver.set(webDriver);
        } else {
            // Define remote web-driver capabilities
            ChromeOptions capabilities = new ChromeOptions();
            capabilities.setPlatformName("LINUX");
            capabilities.setBrowserVersion("111.0");

            // Define the remote webdriver URL
            String remoteURL = "http://" + System.getProperty("host") + ":" + System.getProperty("port") + "/wd/hub";
            System.out.println("remote url is: " + remoteURL);
            // Initialize the RemoteWebdriver instance
            try {
                driver.set(new RemoteWebDriver(new URL(remoteURL), capabilities));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        driver.get().manage().window().maximize();
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(FileReaderManager.getInstance().getConfigReader().getImplicitlyWait()));
        driver.get().get(FileReaderManager.getInstance().getConfigReader().getApplicationUrl());
    }

    @After
    public void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
