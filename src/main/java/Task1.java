import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*в разделе "Электроинструменты" / "Перфораторы" на  трех товарах с акционным тикетом  проверить есть ли в карточке товара акционная цена
Для этого, из раздела "Электроинструменты"  перейти в раздел "Перфораторы"  используя меню.
Рандомно, на страничке выбрать товар, провалиться в карточку товара и проверить наличие акционной и старой цены
Так для 3-х товаров (выбор количества проверяемых товаров сделать гибким)*/

public class Task1 {

    public static final int GOODS_QUANTITY_TO_CHECK = 3;
    @Test
    void main() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\tools\\chromedriver\\chromedriver_100_0_4896_60\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

        try {
            driver.get("https://kulibin.com.ua");
            Thread.sleep(2000);
            WebElement aElektroinstrument = driver.findElement(By.xpath("//a[@href='/catalog/elektroinstrument/']"));

            Actions action = new Actions(driver);
            //Performing the mouse hover action on the target element.
            action.moveToElement(aElektroinstrument).clickAndHold().build().perform();
            //click on perforatory
            WebElement perforatory = driver.findElement(By.xpath("//a[@href='/catalog/perforatory/']"));
            perforatory.click();
            List<WebElement> listOfPerforatory = driver.findElements(By.xpath("//ul[@class='catalog catalog-full js-catalog']//li[@class='col-xs-4 js-product']"));

            List<String> listOfPromotionalGoodsLinks = new ArrayList();

            for (WebElement item : listOfPerforatory) {
                //make list of goodsLinks with promotional price - xpath span[@class='old-price']//a[@class='google_detail_link']
                try {
                    item.findElement(By.xpath(".//span[@class='old-price']"));
                    WebElement link = item.findElement(By.xpath(".//a[@class='google_detail_link']"));
                    listOfPromotionalGoodsLinks.add(link.getAttribute("href"));

                } catch (Exception e) {
                } //not found
            }
            //situation when promoGoods are less than 3 (we need no more than 3 iterations)
            int maxIterations = Math.min(GOODS_QUANTITY_TO_CHECK, listOfPromotionalGoodsLinks.size());
            for (int i = 0; i < maxIterations; i++) {
                Random r = new Random(System.currentTimeMillis());
                int randomIndex = r.nextInt(listOfPromotionalGoodsLinks.size());

                String randomLink = listOfPromotionalGoodsLinks.get(randomIndex);
                //System.out.println(randomLink);

                driver.get(randomLink);
                //check page is loaded - check prices - go back

                WebElement priceRow = (new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='price-row']"))));
                //System.out.println(priceRow.getText());
                WebElement oldPrice = priceRow.findElement(By.xpath(".//span[@class='item_old_price old-price']"));
                WebElement price = priceRow.findElement(By.xpath(".//span[@class='price']"));
                String priceText= "";
                if (oldPrice.isDisplayed()) {
                    priceText+= " oldPrice: " + oldPrice.getText();
                }
                if (price.isDisplayed()) {
                    priceText+= " currentPrice: " + price.getText();
                }

                System.out.println(randomLink+priceText);
                Assert.assertTrue(oldPrice.isDisplayed(),"oldPrice is not displayed for: "+randomLink);
                Assert.assertTrue(price.isDisplayed(),"currentPrice is not displayed for: "+randomLink);

                listOfPromotionalGoodsLinks.remove(randomIndex);
                driver.navigate().back();
            }
            //System.out.println(listOfPromotionalGoods.size());

    } catch(
    InterruptedException e)

    {
        e.printStackTrace();
    } finally {
        Thread.sleep(20000);
        driver.quit();
    }

}

/*public void test1(){
        System.out.println("Test 1");
}*/

}
