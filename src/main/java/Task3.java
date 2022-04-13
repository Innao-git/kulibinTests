/*
3. Перейти в раздел "Электроинструменты" / "Шуруповерты"
        Вывести "Наименование" всех товаров у которых есть иконка с американским флагом на первых 3х страницах
*/

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class Task3 {

    public static final int COUNT_OF_PAGES_TO_CHECK = 3;
    @Test
    void main() {
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
            //click on shurupoverty
            WebElement shurupoverty = driver.findElement(By.xpath("//a[@href='/catalog/shurupoverty/']"));
            action.moveToElement(shurupoverty).click().build().perform();
            int counter = 0;
            for (int i = 0; i < COUNT_OF_PAGES_TO_CHECK; i++) {
                // xpath //ul[@class='catalog row']/li[@class='col-xs-3']
                List<WebElement> shurupovertyList = driver.findElements(By.xpath("//ul/li[@class='col-xs-4 js-product']"));
                int pageNumber = i+1;
                System.out.println("Items from page: "+pageNumber);
                for (WebElement shurupovert : shurupovertyList) {

                    try {
                        //div[@class='item-brand-country'/img.getAttribute('src')=="https://kulibin.com.ua/upload/resize_cache/iblock/0f5/30_20_1/United_states.jpg"
                        String flag = shurupovert.findElement(By.xpath(".//div[@class='item-brand-country']/img")).getAttribute("src");
                        if (flag.contains("United_states")) {
                            String name = "";
                            //(By.xpath(".//h4[@class='s_title']")).getText();
                            try {
                                name = shurupovert.findElement(By.className("s_title")).getText();

                                counter++;
                                System.out.println("number: " + counter + " name: " + name);
                            } catch (Exception e) {
                                System.out.println("name is not found");
                            }
                            //failed test if name =""
                            Assert.assertNotEquals(name, "");
                        }

                    } catch (Exception e) {
                        System.out.println("flag is not found");
                    }
                }

                if (i < COUNT_OF_PAGES_TO_CHECK-1) {
                    WebElement nextPage = driver.findElement(By.cssSelector("div.paging a.next.btn-blue"));
                    new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(nextPage));
                    nextPage.click();

                    //wait until page is loaded
                    new WebDriverWait(driver, Duration.ofSeconds(30)).until(
                            webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

    }


}
