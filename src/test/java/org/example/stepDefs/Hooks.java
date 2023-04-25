package org.example.stepDefs;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import managers.FileReaderManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import org.openqa.selenium.remote.RemoteWebDriver;


public class Hooks {

    public static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    public static ThreadLocal<RemoteWebDriver> remoteDriver = new ThreadLocal<>();


    @Before(value = "@smoke")
    public void openBrowser() {
    
    if (System.getProperty("executionAddress").equals("local")) {
        driver.set(new ChromeDriver());
        }
        
        else {
         // Define remote web-driver capabilities
            EdgeOptions capabilities = new EdgeOptions();
            capabilities.setPlatformName("LINUX");
            capabilities.setBrowserVersion("111.0");

            // Define the remote webdriver URL
            String remoteURL = "http://" + System.getProperty("host") + ":" + System.getProperty("port") + "/wd/hub";
            System.out.println("remote url is: " + remoteURL);
            // Initialize the RemoteWebdriver instance
            try {
                remoteDriver = new RemoteWebDriver(new URL(remoteURL), capabilities);
                driver = remoteDriver;
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
        driver.get().quit();
    }
}
