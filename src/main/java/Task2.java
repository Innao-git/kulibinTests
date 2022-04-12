/*2. Перейти в раздел "Электроинструменты" / "Дрели"
Проверить, что у всех товаров этого раздела есть цена на первых двух страницах.*/

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
import java.util.List;

public class Task2 {

    public static final int COUNT_OF_PAGES_TO_CHECK = 3;

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
            int counter = 0;
            for (int i = 0; i< COUNT_OF_PAGES_TO_CHECK; i++){

                List<WebElement> dreliList = driver.findElements(By.xpath("//ul/li[@class='col-xs-4 js-product']"));


                //load prices in new list in order to keep it even after the page reloaded

                for (WebElement drel:dreliList){

                    try{
                        String price = drel.findElement(By.xpath(".//span[@class='price']")).getText();
                        counter++;
                        System.out.println("number: "+counter+ " price: "+price);
                    }catch(Exception e){
                        System.out.println("price is nos not found");
                    }
                }
                if (i < (COUNT_OF_PAGES_TO_CHECK-1)) {
                WebElement nextPage = driver.findElement(By.cssSelector("div.paging a.next.btn-blue"));

                nextPage.click();

                //wait until page is loaded
                new WebDriverWait(driver, Duration.ofSeconds(30)).until(
                            webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //Thread.sleep(20000);
            driver.quit();
        }
    }
}
