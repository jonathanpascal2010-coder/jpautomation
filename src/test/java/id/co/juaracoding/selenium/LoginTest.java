package id.co.juaracoding.selenium;

import id.co.juaracoding.selenium.pages.DashboardPage;
import id.co.juaracoding.selenium.pages.LoginPage;
import id.co.juaracoding.util.TestConfig;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTest extends BaseSeleniumTest {

    @Test
    public void should_redirect_to_dashboard_when_login_valid() {
        bukaHalamanLogin();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs(TestConfig.VALID_USERNAME, TestConfig.VALID_PASSWORD);

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/dashboard"));

        Assert.assertTrue(driver.getCurrentUrl().contains("/dashboard"),
                "Setelah login valid, browser harus diarahkan ke /dashboard. URL aktual: " + driver.getCurrentUrl());

        DashboardPage dashboardPage = new DashboardPage(driver);
        Assert.assertTrue(dashboardPage.getLoggedInUserName().contains(TestConfig.VALID_USER_FULL_NAME),
                "Nama user yang login ('" + TestConfig.VALID_USER_FULL_NAME + "') harus tampil di navbar");
    }

    @Test
    public void should_show_error_toast_when_password_wrong() {
        bukaHalamanLogin();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs(TestConfig.VALID_USERNAME, TestConfig.INVALID_PASSWORD);

        Assert.assertTrue(loginPage.isErrorToastShown(),
                "Toast error (toast-icon-error) harus muncul saat login dengan password yang salah");
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"),
                "Saat login gagal, browser harus tetap berada di halaman /login. URL aktual: " + driver.getCurrentUrl());
    }
}
