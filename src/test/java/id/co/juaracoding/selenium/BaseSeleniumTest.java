package id.co.juaracoding.selenium;

import id.co.juaracoding.selenium.pages.LicenseGatePage;
import id.co.juaracoding.selenium.pages.LoginPage;
import id.co.juaracoding.util.TestConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseSeleniumTest {
    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1366,900");
        if ("true".equalsIgnoreCase(System.getProperty("headless"))) {
            options.addArguments("--headless=new");
        }
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void bukaHalaman(String path) {
        driver.get(TestConfig.BASE_URL + path);
        LicenseGatePage licenseGatePage = new LicenseGatePage(driver);
        if (licenseGatePage.isDisplayed()) {
            licenseGatePage.activate(TestConfig.LICENSE_KEY);
            // Sesuai jebakan setup-Selenium.docx §8.2: setelah aktivasi lisensi browser selalu mendarat di /login,
            // sehingga perlu meminta kembali path yang dituju satu kali lagi.
            driver.get(TestConfig.BASE_URL + path);
        }
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains(path));
    }

    protected void bukaHalamanLogin() {
        bukaHalaman("/login");
    }

    protected void loginSebagai(String username, String password) {
        bukaHalamanLogin();
        new LoginPage(driver).loginAs(username, password);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("/dashboard"));
    }
}
