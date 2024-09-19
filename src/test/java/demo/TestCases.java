package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.List;
import java.util.logging.Level;
import demo.wrappers.Wrappers;

public class TestCases {
    ChromeDriver driver;
    WebDriverWait wait;
    Wrappers wp;

    /*
     * Go to www.flipkart.com. Search "Washing Machine". Sort by popularity and
     * print the count of items with rating less than or equal to 4 stars.
     */
    @Test
    public void testCase01() throws InterruptedException {
        try {
        System.out.println("Starting Test case: testCase01");

        //Opening flipkart url
        wp.openUrl();

        //Searching for product 
        wp.searchProduct("Washing Machine");

        //Waiting and Locating popularity button
        WebElement popularityBtn = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//div[@class='zg-M3Z' and text()='Popularity']")));

        //Performing click on popularity button
        popularityBtn.click();

        //Waiting for appearance of popularity button after clicking
        wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//div[@class='zg-M3Z _0H7xSG' and text()='Popularity']")));

        //Locating rating WebElements having rating less than 4
        List<WebElement> ratings = driver
                .findElements(By.xpath("//div[@class='DOjaWF gdgoEp']//div[@class='XQDdHH' and text()<=4]"));

        System.out.println("Number of items whose rating is equal to or less than 4 = " + ratings.size());

        System.out.println("testCase01 Passed");
        } catch (Exception e) {
            System.out.println("testCase01 Failed");
            e.printStackTrace();
        }
        System.out.println("end Test case: testCase01");
    }

    /*
     * Search "iPhone", print the Titles and discount % of items with more than 17%
     * discount
     */
    @Test
    public void testCase02() throws InterruptedException {
        try {
        System.out.println("Starting Test case: testCase02");

        //Searching for product
        wp.searchProduct("iPhone");

        //Storing List of WebElements which contains "% off" text
        List<WebElement> off = driver
                .findElements(By.xpath("//div[@class='DOjaWF gdgoEp']//span[contains(text(),'% off')]"));
        
        //Iterating through each WebElement stored above
        for (WebElement element : off) {

            //Getting the Integer off and comparing with > 17
            if (wp.offValue(element) > 17) {

                //Getting the title of product whose Integer off is greater than 17 
                WebElement title = driver.findElement(RelativeLocator.with(By.className("KzDlHZ")).toLeftOf(element));

                //Printing the title
                System.out.println(title.getText());
            }
        }
        System.out.println("testCase02 Passed");
        } catch (Exception e) {
            System.out.println("testCase02 Failed");
            e.printStackTrace();
        }
        System.out.println("end Test case: testCase02");
    }

    /*
     * Search "Coffee Mug", select 4 stars and above, and print the Title and image
     * URL of the 5 items with highest number of reviews
     */
    @Test
    public void testCase03() throws InterruptedException {
        try {
        System.out.println("Starting Test case: testCase03");

        //Searching for product
        wp.searchProduct("Coffee Mug");

        //Selecting rating and above
        wp.ratingSelection(4);

        //Printing the title and src value of top 5 most review count product
        wp.reviewCounter();

        System.out.println("testCase03 Passed");
        } catch (Exception e) {
            System.out.println("testCase03 Failed");
            e.printStackTrace();
        }
        System.out.println("end Test case: testCase03");
    }

    @BeforeTest
    public void startBrowser() {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        //Creating object of ChromeOptions class
        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

        //Initializing a new object of ChromeDriver class
        driver = new ChromeDriver(options);

        //Maximizing the browser window
        driver.manage().window().maximize();

        //Creating a new instance of WebDriverWait class with driver and 30 Second of wait
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        //Instantiating a new object of Wrappers class
        wp = new Wrappers(driver, wait);
    }

    @AfterTest
    public void endTest() {
        //Quiting the browser or closing all the window
        // driver.close();
        driver.quit();

    }
}