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

/*4. В разделе "Электроинструменты" / "Болгарки"
Для 10 рандомных товаров с акционным тикетом (может быть % скидки или Акция) провести расчет
акционной цены относительно старой используя процент скидки.
В assert упавшего теста вывести наименование товара его ожидаемую и фактическую цену.*/
public class Task4 {
    public static void main(String[] args) {
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
            //click on bolgarki
            WebElement bolgarkiMenu = driver.findElement(By.xpath("//a[@href='/catalog/bolgarki/']"));
            action.moveToElement(bolgarkiMenu).click().build().perform();
            List<WebElement> bolgarkiWholeList;

            do {
                bolgarkiWholeList = driver.findElements(By.xpath("//ul/li[@class='col-xs-4 js-product']"));
                //if item get class image_sticker_discount
                for (WebElement item : bolgarkiWholeList) {
                    if (item.findElement(By.xpath(".//span")).getText().contains("%")){
                        WebElement discountItem = item.findElement(By.xpath(".//span[contains(@class, 'image_sticker_discount')]"));

                        String discountPersentage = discountItem.getText().replaceAll("[- %]","");
                        String name = item.findElement(By.className("s_title")).getText();

                        String oldPrice = item.findElement(By.className("old-price")).getText().replaceAll("[ грн.]","");
                        float priceCalculated = Integer.parseInt(oldPrice)-Float.parseFloat(discountPersentage)*Integer.parseInt(oldPrice)/100;
                        String currentPrice = item.findElement(By.xpath(".//span[@class='price']")).getText();

                        System.out.println(name + " " + discountPersentage+ "priceShouldBe "+ String. valueOf(priceCalculated)+"/ "+ currentPrice);
                    }

                }

                WebElement nextPageButton = driver.findElement(By.cssSelector("div.paging a.next.btn-blue"));
                new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(nextPageButton));
                if (nextPageButton.isEnabled()) {
                    nextPageButton.click();
                    new WebDriverWait(driver, Duration.ofSeconds(30)).until(
                            webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
                } else {
                    break;
                }

            } while ((bolgarkiWholeList.size() != 0));
            //add check if button is desabled


        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //Thread.sleep(20000);
            driver.quit();
        }
    }
}
