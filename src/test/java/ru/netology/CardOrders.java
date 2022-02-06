package ru.netology;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class CardOrders {

    private WebDriver driver;

    @BeforeAll
    public static void setUpAll() {
        System.setProperty("webdriver.chrome.driver", "./driver/win/chromedriver.exe");
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void ShouldSendFormSimple() {
        driver.get("http://localhost:9999");
        List<WebElement> textFields = driver.findElements(By.className("input__control"));
        textFields.get(0).sendKeys("Леонид");
        textFields.get(1).sendKeys("+79991114455");

        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector(".Success_successBlock__2L3Cw")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void ShouldSendFormWithDash() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Леонид-Матвей");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71112223344");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector(".Success_successBlock__2L3Cw")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void ShouldSendFormWithSpace() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Леонид Леонидович");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71112223344");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector(".Success_successBlock__2L3Cw")).getText().trim();
        assertEquals(expected, actual);
    }

//    негативные тесты

    @Test
    void ShouldSendFormWithEnglishName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Leonid");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71112223344");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        String expected = "Фамилия и имя\n" +
                "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'] span")).getText();
        assertEquals(expected, actual);
    }

    @Test
    void ShouldSendFormWithTelError() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+Иван");
        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        String expected = "Мобильный телефон\n" +
                "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'] span")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void ShouldSendFormWithoutCheckMark() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Артем");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71112223344");
//        driver.findElement(By.cssSelector(".checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        String expected = "rgba(255, 92, 92, 1)";
        String actual = driver.findElement(By.cssSelector("[data-test-id='agreement']")).getCssValue("color");
        assertEquals(expected, actual);
    }
}
