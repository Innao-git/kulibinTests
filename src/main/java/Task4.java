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
import java.util.Random;

/*4. В разделе "Электроинструменты" / "Болгарки"
Для 10 рандомных товаров с акционным тикетом (может быть % скидки или Акция) провести расчет
акционной цены относительно старой используя процент скидки.
В assert упавшего теста вывести наименование товара его ожидаемую и фактическую цену.*/
public class Task4 {

    public static final int DISCOUNT_ITEMS_QUNTITY = 10;

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
            ArrayList<ArrayList<String>> discountItemsList = new ArrayList<ArrayList<String>>();

            do {
                bolgarkiWholeList = driver.findElements(By.xpath("//ul/li[@class='col-xs-4 js-product']"));
                //if item get class image_sticker_discount
                for (WebElement item : bolgarkiWholeList) {
                    if (item.findElement(By.xpath(".//span")).getText().contains("%")) {

                        WebElement discountItemWebElement = item.findElement(By.xpath(".//span[contains(@class, 'image_sticker_discount')]"));

                        String discountValue = discountItemWebElement.getText().replaceAll("[- %]", "");
                        String name = item.findElement(By.className("s_title")).getText();

                        String oldPrice = item.findElement(By.className("old-price")).getText().replaceAll("[ грн.]", "");

                        String currentPrice = item.findElement(By.xpath(".//span[@class='price']")).getText().replaceAll("[ грн.]", "");

                        ArrayList<String> discountListItem = new ArrayList();
                        discountListItem.add(name);
                        discountListItem.add(discountValue);
                        discountListItem.add(currentPrice);
                        discountListItem.add(oldPrice);
                        discountItemsList.add(discountListItem);
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
            System.out.println(discountItemsList.size());

            int maxIterations = Math.min(DISCOUNT_ITEMS_QUNTITY, discountItemsList.size());
            Random r = new Random(System.currentTimeMillis());
            for (int i = 0; i < maxIterations; i++) {
                int randomIndex = r.nextInt(discountItemsList.size());
                ArrayList<String> randomDiscountItem = discountItemsList.get(randomIndex);
                String name = randomDiscountItem.get(0);
                float discountValue = Float.parseFloat(randomDiscountItem.get(1));
                float currentPrice = Float.parseFloat(randomDiscountItem.get(2));
                float oldPrice = Float.parseFloat(randomDiscountItem.get(3));
                float priceCalculated = oldPrice - discountValue * oldPrice / 100;
                if (priceCalculated != currentPrice) {
                    System.out.println(name + " " + discountValue + " priceShouldBe " + String.valueOf(priceCalculated) + "/ " + String.valueOf(currentPrice));
                } else {
                    System.out.println(name + " " + discountValue + " price is right" + String.valueOf(priceCalculated));
                }
                discountItemsList.remove(randomIndex);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //Thread.sleep(20000);
            driver.quit();
        }
    }
}
