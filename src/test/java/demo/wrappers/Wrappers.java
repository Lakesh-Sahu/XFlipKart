package demo.wrappers;

import java.util.ArrayList;
import java.util.Collections;
// import java.util.HashMap;
import java.util.List;
// import java.util.Map;
// import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
// import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
// import java.time.Duration;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */
    ChromeDriver driver;
    WebDriverWait wait;

    public Wrappers(ChromeDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void openUrl() {
        driver.get("https://www.flipkart.com");
    }

    public void click(WebElement element) {
        element.click();
    }

    public void sendKeys(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }

    public void searchProduct(String productName) throws InterruptedException {
        Thread.sleep(5000);
        WebElement searchBar = driver.findElement(By.xpath("//input[@title]"));
        searchBar.click();
        searchBar.clear();
        searchBar.sendKeys(productName);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@title and @value = '" + productName + "']")));
        searchBar.sendKeys(Keys.ENTER);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='_1G0WLw']/span[contains(text(), 'Page')]")));
            Thread.sleep(5000);
        } catch (Exception e) {
            // TO DO: handle exception
            System.out.println("No product found");
        }
    }

    public int offValue(WebElement element) {
        String nums = "0123456789";
        String s = element.getText();
        int i = 0;
        for (i = 0; i < s.length(); i++) {
            if (!nums.contains(Character.toString(s.charAt(i)))) {
                break;
            }
        }
        return Integer.parseInt(s.substring(0, i));
    }

    public void ratingSelection(int n) throws InterruptedException {
        WebElement checkBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), '" + n + "') and contains(text(),' & above')]/preceding-sibling::div")));
        System.out.println("4* found!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        checkBox.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='_6tw8ju']")));
    }

    public void reviewCounter() {
        ArrayList<Integer> ratingNums = new ArrayList<>();
        List<WebElement> reviewsCo = driver.findElements(By.xpath("//span[@class='Wphh3N']"));
        for (WebElement webElement : reviewsCo) {
            String ratingWithBrak = webElement.getText().replace("(", "");
            String ratingWOBrack = ratingWithBrak.replace(")", "");
            String ratingWOComma = ratingWOBrack.replace(",", "");
            int rating = Integer.parseInt(ratingWOComma);
            ratingNums.add(rating);
        }
        Collections.sort(ratingNums, Collections.reverseOrder());
        for(int i = 0; i < 5; i++) {
            int singleRating = ratingNums.get(i);
            String singleRatingString = String.valueOf(singleRating);
            String withComma;
            if(singleRating > 999) {
                if (singleRatingString.length() == 4) {
                    withComma = singleRatingString.substring(0, 1) + "," + singleRatingString.substring(1);
                }
                else if (singleRatingString.length() == 5) {
                    withComma = singleRatingString.substring(0, 2) + "," + singleRatingString.substring(2);
                }
                else {
                    withComma = "000";
                }
            }
            else {
                withComma = singleRatingString;
            }

            int sameCount = 1;
            for(int j = 0; j < i; j++) {
                if (singleRatingString.equals(String.valueOf(ratingNums.get(j)))) {
                    sameCount++;
                }
            }
            WebElement forTitle = driver.findElement(By.xpath("(//span[text()='(" + withComma + ")']/parent::div/preceding-sibling::a[1])[" + sameCount + "]"));
            String title = forTitle.getAttribute("title");
            WebElement forSRC = driver.findElement(By.xpath("(//span[text()='(" + withComma + ")']/parent::div/preceding-sibling::a[2]//img)[" + sameCount + "]"));
            String src = forSRC.getAttribute("src");
    
            System.out.println("Title : " + title);
            System.out.println("SRC : " + src);
        }
    }
}