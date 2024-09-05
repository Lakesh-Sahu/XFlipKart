package demo;

import org.openqa.selenium.By;
// import org.openqa.selenium.Keys;
// import org.openqa.selenium.WebDriver;
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
// import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases {
    ChromeDriver driver;
    WebDriverWait wait;
    Wrappers wp;

    /*
     * TO DO: Write your tests here with testng @Test annotation.
     * Follow `testCase01` `testCase02`... format or what is provided in
     * instructions
     */
    @Test
    public void testCase01() throws InterruptedException {
        System.out.println("Starting Test case: testCase01");

        wp.openUrl();

        wp.searchProduct("Washing Machine");

        WebElement popularityBtn = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//div[@class='zg-M3Z' and text()='Popularity']")));
        popularityBtn.click();

        wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//div[@class='zg-M3Z _0H7xSG' and text()='Popularity']")));

        List<WebElement> ratings = driver
                .findElements(By.xpath("//div[@class='DOjaWF gdgoEp']//div[@class='XQDdHH' and text()<=4]"));

        System.out.println("Number of items whose rating is equal to or less than 4 = " + ratings.size());

        System.out.println("end Test case: testCase01");
    }

    @Test
    public void testCase02() throws InterruptedException {
        System.out.println("Starting Test case: testCase02");
        wp.searchProduct("iPhone");
        List<WebElement> off = driver.findElements(By.xpath("//div[@class='DOjaWF gdgoEp']//span[contains(text(),'% off')]"));
        for (WebElement element : off) {
            if (wp.offValue(element) > 17) {
                WebElement title = driver.findElement(RelativeLocator.with(By.className("KzDlHZ")).toLeftOf(element));
                System.out.println(title.getText());
            }
        }
        System.out.println("end Test case: testCase02");
    }

    @Test
    public void testCase03() throws InterruptedException {
        System.out.println("Starting Test case: testCase03");

        wp.searchProduct("Coffee Mug");
        
        wp.ratingSelection(4);

        wp.reviewCounter();

        System.out.println("end Test case: testCase03");
    }

    /*
     * Do not change the provided methods unless necessary, they will help in
     * automation and assessment
     */
    @BeforeTest
    public void startBrowser() {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        wp = new Wrappers(driver, wait);
    }

    @AfterTest
    public void endTest() {
        // driver.close();
        driver.quit();

    }
}