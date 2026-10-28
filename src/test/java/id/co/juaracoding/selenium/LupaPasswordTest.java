package id.co.juaracoding.selenium;

import id.co.juaracoding.selenium.pages.ForgotPasswordPage;
import id.co.juaracoding.util.TestConfig;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class LupaPasswordTest extends BaseSeleniumTest {

    @Test
    public void should_redirect_to_check_email_when_email_terdaftar() {
        bukaHalaman("/forgot-password");

        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage(driver);
        forgotPasswordPage.submit(TestConfig.VALID_EMAIL);

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/check-email"));

        Assert.assertTrue(driver.getCurrentUrl().contains("/check-email"),
                "Lupa password untuk email terdaftar harus mengarahkan ke /check-email. URL aktual: " + driver.getCurrentUrl());
    }

    @Test
    public void should_show_error_when_email_tidak_terdaftar() {
        bukaHalaman("/forgot-password");

        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage(driver);
        forgotPasswordPage.submit(TestConfig.INVALID_EMAIL);

        boolean isErrorShown = forgotPasswordPage.isErrorToastShown();
        boolean stillOnForgotPassword = driver.getCurrentUrl().contains("/forgot-password");

        Assert.assertTrue(isErrorShown || stillOnForgotPassword,
                "Lupa password dengan email tidak terdaftar harus menampilkan error toast atau tetap berada di halaman /forgot-password");
    }
}
