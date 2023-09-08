import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Flamingo {
    static WebDriver driver;

    @BeforeClass
    public void invokeBrowser() {
        System.setProperty(readProperty("driver"), readProperty("driverPath"));
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    public void search(String toSearch) {
        driver.findElement(By.id("search")).sendKeys(toSearch);
        driver.findElement(By.xpath("//button[@class='action search']")).click();
    }

    public void selectSize(String size) {
        switch (size) {
            case "xs" -> driver.findElement(By.id("option-label-size-143-item-166")).click();
            case "s" -> driver.findElement(By.id("option-label-size-143-item-167")).click();
            case "m" -> driver.findElement(By.id("option-label-size-143-item-168")).click();
            case "l" -> driver.findElement(By.id("option-label-size-143-item-169")).click();
            case "xl" -> driver.findElement(By.id("option-label-size-143-item-170")).click();
            default -> System.out.println("No Size Selected");
        }
    }

    public void selectColor(String color) {
        switch (color) {
            case "Black" -> driver.findElement(By.id("option-label-color-93-item-49")).click();
            case "Gray" -> driver.findElement(By.id("option-label-color-93-item-52")).click();
            case "Orange" -> driver.findElement(By.id("option-label-color-93-item-56")).click();
            case "Purple" -> driver.findElement(By.id("option-label-color-93-item-57")).click();
            case "White" -> driver.findElement(By.id("option-label-color-93-item-59")).click();
            case "Blue" -> driver.findElement(By.id("option-label-color-93-item-50")).click();
            default -> System.out.println("No Color Selected");
        }
    }

    public void addProduct(String item, String size, String color) {
        driver.findElement(By.partialLinkText(item)).click();
        selectSize(size);
        selectColor(color);
        driver.findElement(By.id("product-addtocart-button")).click();
        //driver.manage().timeouts().implicitlyWait(10, SECONDS);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readProperty(String key) {
        Properties prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream("resources/config.properties");
            prop.load(fis);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return prop.getProperty(key);
    }




    @Test(priority = 1)
    public void homePage() {
        driver.navigate().to(readProperty("url"));
        String title = driver.getTitle();
        Assert.assertEquals(title, "Home Page", "Title Assertion Failed");
    }

    @Test(priority = 2)
    public void addItems() throws IOException {
        driver.manage().timeouts().implicitlyWait(10, SECONDS);
        Actions builder = new Actions(driver);
        builder.moveToElement(driver.findElement(By.id("ui-id-4"))).build().perform();
        builder.moveToElement(driver.findElement(By.id("ui-id-9"))).build().perform();
        driver.findElement(By.id("ui-id-11")).click();
       addProduct(readProperty("item1"), readProperty("size1"), readProperty("color1"));
        //addProduct(excelData(1,1),excelData(1,2),excelData(1,3));
        builder.moveToElement(driver.findElement(By.id("ui-id-5"))).build().perform();
        builder.moveToElement(driver.findElement(By.id("ui-id-17"))).build().perform();
        driver.findElement(By.id("ui-id-19")).click();
        addProduct(readProperty("item2"), readProperty("size2"), readProperty("color2"));
        //addProduct(excelData(2,1),excelData(2,2),excelData(2,3));
        showCart();
    }

    @Test(priority = 3)
    public void showCart() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='action showcart']/span[2]"))).click();
        driver.findElement(By.partialLinkText("View and Edit Cart")).click();
    }

    @Test(priority = 4)
    public void removeItem() {
        driver.findElement(By.xpath("//div[@class='actions-toolbar']/a[2]")).click();
    }

    @Test(priority = 5)
    public void addAnotherItem() throws IOException {
        Actions builder = new Actions(driver);
        builder.moveToElement(driver.findElement(By.id("ui-id-5"))).build().perform();
        builder.moveToElement(driver.findElement(By.id("ui-id-17"))).build().perform();
        driver.findElement(By.id("ui-id-19")).click();
        addProduct(readProperty("item3"),readProperty("size3"),readProperty("color3"));
        //addProduct(excelData(3,1),excelData(3,2),excelData(3,3));
        showCart();
    }

    @Test(priority = 6)
    public void checkout(){
        driver.findElement(By.xpath("//button[@data-role=\"proceed-to-checkout\"]")).click();
    }

    @Test(priority = 7)
    public void formFill(){

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        driver.findElement(By.cssSelector("div.control._with-tooltip input#customer-email")).sendKeys("kartik3162@gmail.com");
        driver.findElement(By.xpath("//input[@name='firstname']")).click();
        driver.findElement(By.xpath("//input[@name='firstname']")).sendKeys(""+readProperty("fname"));
        driver.findElement(By.xpath("//input[@name='lastname']")).click();
        driver.findElement(By.xpath("//input[@name='lastname']")).sendKeys(""+readProperty("lname"));
        driver.findElement(By.xpath("//input[@name='company']")).click();
        driver.findElement(By.xpath("//input[@name='company']")).sendKeys(""+readProperty("company"));
        driver.findElement(By.xpath("//input[@name='street[0]']")).click();
        driver.findElement(By.xpath("//input[@name='street[0]']")).sendKeys(""+readProperty("street"));
        driver.findElement(By.xpath("//input[@name='city']")).click();
        driver.findElement(By.xpath("//input[@name='city']")).sendKeys(""+readProperty("city"));
        WebElement stateDropdown = driver.findElement(By.xpath("//select[@name='region_id']"));
        stateDropdown.click();

        List<WebElement> stateOptions = stateDropdown.findElements(By.tagName("option"));
        for (WebElement option : stateOptions) {
            if (option.getText().equalsIgnoreCase("Nevada")) {
                option.click();
                break;
            }
        }
        driver.findElement(By.xpath("//input[@name='postcode']")).click();
        driver.findElement(By.xpath("//input[@name='postcode']")).sendKeys(""+readProperty("code"));
        WebElement countryDropdown = driver.findElement(By.xpath("//select[@name='country_id']"));
        countryDropdown.click();
        List<WebElement> countryOptions = countryDropdown.findElements(By.tagName("option"));
        for (WebElement option : countryOptions) {
            if (option.getText().equalsIgnoreCase("India")) {
                option.click();
                break;
            }
        }

        driver.findElement(By.xpath("//input[@name='telephone']")).click();
        driver.findElement(By.xpath("//input[@name='telephone']")).sendKeys(""+readProperty("number"));
        //driver.manage().timeouts().implicitlyWait(10, SECONDS);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        driver.findElement(By.xpath("//button[@class=\"button action continue primary\"]/span")).click();
    }

    @Test(priority = 8)
    public void payment(){
        //driver.manage().timeouts().implicitlyWait(10, SECONDS);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        driver.findElement(By.xpath("//button[@class=\"action primary checkout\"]/span")).click();
    }
    @AfterClass
    public void quit(){
        driver.quit();
    }
}