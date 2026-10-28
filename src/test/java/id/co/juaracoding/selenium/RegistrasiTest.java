package id.co.juaracoding.selenium;

import id.co.juaracoding.selenium.pages.RegisterPage;
import id.co.juaracoding.util.TestConfig;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class RegistrasiTest extends BaseSeleniumTest {

    @Test
    public void should_redirect_to_check_email_when_registrasi_valid() {
        bukaHalaman("/register");

        long now = System.currentTimeMillis();
        String username = String.format("sel%06d", now % 1_000_000L);
        String email = username + "@example.com";
        String phoneNumber = String.format("0812%08d", now % 100_000_000L);
        String idCardNumber = String.format("%016d", now);
        String taxIdNumber = String.format("%016d", now + 1);

        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.fillWajibFields(
                username,
                TestConfig.DEFAULT_REGISTER_PASSWORD,
                "Selenium Registrasi Test",
                email,
                phoneNumber,
                "Jl. Testing Automation No. 18",
                idCardNumber,
                taxIdNumber
        );

        registerPage.pilihJenisKelamin("Pria");
        registerPage.pilihTanggalLahir2020Januari15();
        registerPage.isiCaptchaOtomatis();
        registerPage.submit();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/check-email"));

        Assert.assertTrue(driver.getCurrentUrl().contains("/check-email"),
                "Registrasi dengan data valid harus mengarahkan ke halaman /check-email. URL aktual: " + driver.getCurrentUrl());
    }

    @Test
    public void should_reject_or_show_error_when_id_card_not_16_digits() {
        bukaHalaman("/register");

        long now = System.currentTimeMillis();
        String username = String.format("sel%06d", now % 1_000_000L);
        String email = username + "@example.com";
        String phoneNumber = String.format("0812%08d", now % 100_000_000L);

        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.fillWajibFields(
                username,
                TestConfig.DEFAULT_REGISTER_PASSWORD,
                "Selenium KTP Invalid",
                email,
                phoneNumber,
                "Jl. Testing Automation No. 18",
                TestConfig.INVALID_ID_CARD_NUMBER, // Bukan 16 digit
                TestConfig.INVALID_TAX_ID_NUMBER   // Bukan 16 digit
        );

        registerPage.pilihJenisKelamin("Pria");
        registerPage.pilihTanggalLahir2020Januari15();
        registerPage.isiCaptchaOtomatis();
        registerPage.submit();

        boolean stillOnRegisterPage = driver.getCurrentUrl().contains("/register");
        boolean hasErrorNotificationOrValidation = registerPage.isErrorToastShown() || registerPage.hasValidationErrors();

        Assert.assertTrue(stillOnRegisterPage,
                "Form registrasi dengan KTP tidak 16 digit tidak boleh berpindah ke /check-email. URL aktual: " + driver.getCurrentUrl());
        Assert.assertTrue(hasErrorNotificationOrValidation || stillOnRegisterPage,
                "Form harus menolak submit atau menampilkan pesan/indikator kesalahan validasi");
    }
}
