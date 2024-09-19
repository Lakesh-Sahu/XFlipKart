package demo.wrappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */
    ChromeDriver driver;
    WebDriverWait wait;

    //Constuctor of Wrappers class
    public Wrappers(ChromeDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    //Opening flipkart url
    public void openUrl() {
        driver.get("https://www.flipkart.com");
    }

    //Clicking on an WebElement
    public void click(WebElement element) {
        element.click();
    }

    //Entering text to an WebElement
    public void sendKeys(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }

    //Searching for given product name
    public void searchProduct(String productName) throws InterruptedException {
        Thread.sleep(5000);

        //Locating search bar WebElement
        WebElement searchBar = driver.findElement(By.xpath("//input[@title]"));

        //Performing click on search bar
        click(searchBar);

        //Enetering productName to the search bar
        sendKeys(searchBar, productName);
        
        //Waiting for product name to be appear on search bar
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@title and @value = '" + productName + "']")));
        
        //Entering Enter key
        searchBar.sendKeys(Keys.ENTER);

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='_1G0WLw']/span[contains(text(), 'Page')]")));
            Thread.sleep(5000);
        } catch (Exception e) {
            // TO DO: handle exception
            System.out.println("No product found");
        }
    }

    //Seperating Integer from "% off"
    public int offValue(WebElement element) {
        String nums = "0123456789";
        String s = element.getText();
        int i = 0;
        for (i = 0; i < s.length(); i++) {

            //breaking if for loop reaches Non-Integer value
            if (!nums.contains(Character.toString(s.charAt(i)))) {
                break;
            }
        }
        //Retruning only the Integer part
        return Integer.parseInt(s.substring(0, i));
    }


    public void ratingSelection(int n) throws InterruptedException {
        //Getting the checkbox for WebElement which contains "4 and above" text
        WebElement checkBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), '" + n + "') and contains(text(),' & above')]/preceding-sibling::div")));
        
        //Clicking on the checkbox
        checkBox.click();

        //Waiting for the element "4* & above" to show on the selected filter area
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='_6tw8ju']")));
    }

    public void reviewCounter() {
        //Creating Object of ArrayList to store the rating count
        ArrayList<Integer> ratingNums = new ArrayList<>();

        //Getting the review WebElement
        List<WebElement> reviewsCo = driver.findElements(By.xpath("//span[@class='Wphh3N']"));

        //Traversing through each review WebElement
        for (WebElement webElement : reviewsCo) {

            //Replacing the "(" with ""
            String ratingWithBrak = webElement.getText().replace("(", "");

            //Replacing the ")" with ""
            String ratingWOBrack = ratingWithBrak.replace(")", "");

            //Replacing the "," with ""
            String ratingWOComma = ratingWOBrack.replace(",", "");

            //Converting the review String into Integer
            int rating = Integer.parseInt(ratingWOComma);

            //Storing review count in ratingNums as Integer
            ratingNums.add(rating);
        }

        //Sorting all the review count in descending order
        Collections.sort(ratingNums, Collections.reverseOrder());

        //Traversing through top 5 most review count
        for(int i = 0; i < 5; i++) {

            //Getting the review count at ith place from ratingNums (descending order)
            int singleRating = ratingNums.get(i);

            //Storing singleRating as String
            String singleRatingString = String.valueOf(singleRating);
            String withComma;
            if(singleRating > 999) {

                //Converting four digit review count into String with comma so that we can locate the WebElement according to text
                if (singleRatingString.length() == 4) {
                    withComma = singleRatingString.substring(0, 1) + "," + singleRatingString.substring(1);
                }

                //Converting five digit review count into String with comma so that we can locate the WebElement according to text
                else if (singleRatingString.length() == 5) {
                    withComma = singleRatingString.substring(0, 2) + "," + singleRatingString.substring(2);
                }

                //Converting six digit review count into String with comma so that we can locate the WebElement according to text                
                else if (singleRatingString.length() == 6) {
                    withComma = singleRatingString.substring(0, 1) + "," + singleRatingString.substring(1, 3) + "," + singleRatingString.substring(3);
                }

                //Converting seven digit review count into String with comma so that we can locate the WebElement according to text
                else if (singleRatingString.length() == 7) {
                    withComma = singleRatingString.substring(0, 2) + "," + singleRatingString.substring(2, 4) + "," + singleRatingString.substring(4);
                }

                //If no review or greater than seven digit is there than give as 000
                else {
                    withComma = "000";
                }
            }

            //Converting five digit review count into String with comma show that we can locate the WebElement according to text
            else {
                withComma = singleRatingString;
            } 

            //Counting if two or more reviews count are same
            int sameCount = 1;
            for(int j = 0; j < i; j++) {

                //if yes than increasing the count by one
                if (singleRatingString.equals(String.valueOf(ratingNums.get(j)))) {
                    sameCount++;
                }
            }

            //Getting the title WebElement of heighest
            WebElement forTitle = driver.findElement(By.xpath("(//span[text()='(" + withComma + ")']/parent::div/preceding-sibling::a[1])[" + sameCount + "]"));
            
            //Getting the title as String
            String title = forTitle.getAttribute("title");

            //Getting the product image as WebElement
            WebElement forSRC = driver.findElement(By.xpath("(//span[text()='(" + withComma + ")']/parent::div/preceding-sibling::a[2]//img)[" + sameCount + "]"));
            
            //Getting the value of src attribute of above image WebElement 
            String src = forSRC.getAttribute("src");
    
            //Printing title and src
            System.out.println("Title : " + title);
            System.out.println("SRC : " + src);
        }
    }
}