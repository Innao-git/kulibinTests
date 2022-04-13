import org.openqa.selenium.*;
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

    public static final int DISCOUNT_ITEMS_QUANTITY = 10;

   static class DiscountedItem {
      String name;
      int discountValue;
      float currentPrice;
      float oldPrice;
   }

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
            ArrayList<DiscountedItem> discountItemsList = new ArrayList();

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

                        DiscountedItem discountedItem = new DiscountedItem();
                        discountedItem.name = name;
                        discountedItem.discountValue = Integer.parseInt(discountValue);
                        discountedItem.currentPrice = Float.parseFloat(currentPrice);
                        discountedItem.oldPrice = Float.parseFloat(oldPrice);
                        discountItemsList.add(discountedItem);
                    }
                }


                WebElement nextPageButton = driver.findElement(By.cssSelector("div.paging a.next.btn-blue"));
                new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.elementToBeClickable(nextPageButton));
                if (nextPageButton.isEnabled()) {
                    try{
                        nextPageButton.click();

                    }catch(ElementClickInterceptedException e){
                        Thread.sleep(10000);
                        nextPageButton.click();
                    }

                    new WebDriverWait(driver, Duration.ofSeconds(30)).until(
                            webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
                } else {
                    break;
                }

            } while ((bolgarkiWholeList.size() != 0));
            //add check if button is disabled
            System.out.println(discountItemsList.size());

            int maxIterations = Math.min(DISCOUNT_ITEMS_QUANTITY, discountItemsList.size());
            Random r = new Random(System.currentTimeMillis());
            System.out.println("Random "+maxIterations +"items are:");
            for (int i = 0; i < maxIterations; i++) {
                int randomIndex = r.nextInt(discountItemsList.size());
                DiscountedItem randomDiscountItem = discountItemsList.get(randomIndex);

                String name = randomDiscountItem.name;
                int discountValue = randomDiscountItem.discountValue;
                float currentPrice = randomDiscountItem.currentPrice;
                float oldPrice =randomDiscountItem.oldPrice;
                float priceCalculated = oldPrice - discountValue * oldPrice / 100;
                if (priceCalculated != currentPrice) {
                    System.out.println(name + " " + discountValue + " Error, the price shouldBe " + String.valueOf(priceCalculated) + "/ " + String.valueOf(currentPrice));
                } else {
                    System.out.println(name + " " + "the price is correct" + String.valueOf(priceCalculated));
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
