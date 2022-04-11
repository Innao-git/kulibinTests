/*2. Перейти в раздел "Электроинструменты" / "Дрели"
Проверить, что у всех товаров этого раздела есть цена на первых двух страницах.*/

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Task2 {

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
            //see price of goods on the list xpath //li[@class='col-xs-4 js-product']//span[@class='price']
            List<WebElement> dreliListWebElements = driver.findElements(By.xpath("//ul/li[@class='col-xs-4 js-product']"));


            //load info in new list in order to keep it even after the page reloaded
            List<WebElement> drelis = new ArrayList<>(dreliListWebElements);

            WebElement nextPage = driver.findElement(By.cssSelector("div.paging a.next.btn-blue"));

            int lastItemIndex = dreliListWebElements.size()-1;
            nextPage.click();
            //wait until last element from prev page is hide
            wait.until(ExpectedConditions.stalenessOf(dreliListWebElements.get(lastItemIndex)));

            //load new information from next page to drelis list
            dreliListWebElements = driver.findElements(By.xpath("//ul/li[@class='col-xs-4 js-product']"));
            for (WebElement drelFromNextPage:dreliListWebElements){
                drelis.add(drelFromNextPage);
            }
            /*for (WebElement drel: drelis){
                System.out.println(drel.getText());
            }*/

            System.out.println(drelis.size());

            /*
            List<WebElement> pages = driver.findElements(By.cssSelector("div.paging ul li  a"));
            for (WebElement link:pages){
                System.out.println(link.getAttribute("href"));
            }*/



        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //Thread.sleep(20000);
            driver.quit();
        }
    }
}
