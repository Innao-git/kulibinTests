import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Task2_LoadAnother15 {
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "C:\\tools\\chromedriver\\chromedriver_100_0_4896_60\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            driver.get("https://kulibin.com.ua");
            Thread.sleep(2000);
            WebElement aElektroinstrument = driver.findElement(By.xpath("//a[@href='/catalog/elektroinstrument/']"));

            Actions action = new Actions(driver);
            //Performing the mouse hover action on the target element.
            action.moveToElement(aElektroinstrument).clickAndHold().build().perform();
            //click on dreli
            WebElement dreli = driver.findElement(By.xpath("//a[@href='/catalog/dreli/']"));
            action.moveToElement(dreli).click().build().perform();



            WebElement loadMore = driver.findElement(By.cssSelector("a.btn-blue.show-more-link"));
            loadMore.click();
            new WebDriverWait(driver, Duration.ofSeconds(30)).until(
                    webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
            List<WebElement> dreliList = driver.findElements(By.xpath("//ul/li[@class='col-xs-4 js-product']"));
            System.out.println(dreliList.size());
            HashMap<String,String> dreliFromTwoPages=new HashMap<String,String>();
            for (WebElement drel:dreliList){
                String price = drel.findElement(By.xpath(".//span[@class='price']")).getText();
                String href = drel.findElement(By.xpath(".//a[@class='title google_detail_link']")).getAttribute("href");
                dreliFromTwoPages.put(href, price);
            }
            System.out.println(dreliFromTwoPages);






        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //Thread.sleep(20000);
            driver.quit();
        }
    }
}
