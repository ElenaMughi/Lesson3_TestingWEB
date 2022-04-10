package ru.netology;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.List;

public class CardOrders {

    private WebDriver driver;

    @BeforeAll
    public static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);

        driver.get("http://localhost:9999");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSendFormSimple() {
        List<WebElement> textFields = driver.findElements(By.className("input__control"));
        textFields.get(0).sendKeys("Леонид");
        textFields.get(1).sendKeys("+79991114455");

        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldSendFormWithDash() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Леонид-Матвей");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71112223344");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldSendFormWithSpace() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Леонид Леонидович");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71112223344");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        assertEquals(expected, actual);
    }

//    негативные тесты

    @Test
    void shouldSendFormWithEnglishName() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Leonid");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71112223344");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldSendFormWithTelError() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+Иван");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldSendFormWithoutCheckMark() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Артем");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71112223344");
//        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        boolean actual = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid")).isDisplayed();
        assertTrue(actual);
    }

    @Test
    void shouldSendFormWithEmptyName() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71112223344");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldSendFormWithEmptyPhone() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }
}
