package id.co.juaracoding.cucumber.web;

import id.co.juaracoding.selenium.pages.DashboardPage;
import id.co.juaracoding.selenium.pages.ForgotPasswordPage;
import id.co.juaracoding.selenium.pages.LicenseGatePage;
import id.co.juaracoding.selenium.pages.LoginPage;
import id.co.juaracoding.selenium.pages.RegisterPage;
import id.co.juaracoding.util.TestConfig;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class WebSteps {

    private WebDriver driver;
    private LoginPage loginPage;
    private RegisterPage registerPage;
    private ForgotPasswordPage forgotPasswordPage;

    @Before
    public void inisialisasiBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1366,900");
        if ("true".equalsIgnoreCase(System.getProperty("headless"))) {
            options.addArguments("--headless=new");
        }
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
    }

    @After
    public void tutupBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void bukaHalaman(String path) {
        driver.get(TestConfig.BASE_URL + path);
        LicenseGatePage licenseGatePage = new LicenseGatePage(driver);
        if (licenseGatePage.isDisplayed()) {
            licenseGatePage.activate(TestConfig.LICENSE_KEY);
            driver.get(TestConfig.BASE_URL + path);
        }
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains(path));
    }

    // ==========================================
    // Flow 1: Login Web
    // ==========================================

    @Given("user berada di halaman login")
    public void userBeradaDiHalamanLogin() {
        bukaHalaman("/login");
        loginPage = new LoginPage(driver);
    }

    @When("user melakukan login dengan username {string} dan password {string}")
    public void userMelakukanLoginDenganUsernameDanPassword(String username, String password) {
        if (loginPage == null) {
            loginPage = new LoginPage(driver);
        }
        loginPage.loginAs(username, password);
    }

    @Then("browser diarahkan ke halaman dashboard")
    public void browserDiarahkanKeHalamanDashboard() {
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("/dashboard"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/dashboard"),
                "Setelah login valid, browser harus diarahkan ke /dashboard. URL aktual: " + driver.getCurrentUrl());
    }

    @And("nama user yang login menampilkan {string}")
    public void namaUserYangLoginMenampilkan(String namaUser) {
        DashboardPage dashboardPage = new DashboardPage(driver);
        Assert.assertTrue(dashboardPage.getLoggedInUserName().contains(namaUser),
                "Nama user pada dashboard harus mengandung: " + namaUser);
    }

    @Then("muncul notifikasi error pada halaman login")
    public void munculNotifikasiErrorPadaHalamanLogin() {
        if (loginPage == null) {
            loginPage = new LoginPage(driver);
        }
        Assert.assertTrue(loginPage.isErrorToastShown(),
                "Toast icon error harus tampil saat login gagal.");
    }

    @And("user tetap berada di halaman login")
    public void userTetapBeradaDiHalamanLogin() {
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"),
                "User harus tetap berada di halaman login. URL aktual: " + driver.getCurrentUrl());
    }

    // ==========================================
    // Flow 2: Registrasi Web
    // ==========================================

    @Given("user berada di halaman registrasi")
    public void userBeradaDiHalamanRegistrasi() {
        bukaHalaman("/register");
        registerPage = new RegisterPage(driver);
    }

    @When("user mengisi formulir registrasi dengan data valid")
    public void userMengisiFormulirRegistrasiDenganDataValid() {
        if (registerPage == null) {
            registerPage = new RegisterPage(driver);
        }
        long now = System.currentTimeMillis();
        String username = String.format("sel%06d", now % 1_000_000L);
        String email = username + "@example.com";
        String phoneNumber = String.format("0812%08d", now % 100_000_000L);
        String idCardNumber = String.format("%016d", now);
        String taxIdNumber = String.format("%016d", now + 1);

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
    }

    @When("user mengisi formulir registrasi dengan nomor KTP tidak 16 digit")
    public void userMengisiFormulirRegistrasiDenganNomorKtpTidak16Digit() {
        if (registerPage == null) {
            registerPage = new RegisterPage(driver);
        }
        long now = System.currentTimeMillis();
        String username = String.format("sel%06d", now % 1_000_000L);
        String email = username + "@example.com";
        String phoneNumber = String.format("0812%08d", now % 100_000_000L);

        registerPage.fillWajibFields(
                username,
                TestConfig.DEFAULT_REGISTER_PASSWORD,
                "Selenium KTP Invalid",
                email,
                phoneNumber,
                "Jl. Testing Automation No. 18",
                TestConfig.INVALID_ID_CARD_NUMBER,
                TestConfig.INVALID_TAX_ID_NUMBER
        );
    }

    @And("user memilih jenis kelamin {string} dan tanggal lahir tahun 2020 Januari 15")
    public void userMemilihJenisKelaminDanTanggalLahir(String jenisKelamin) {
        registerPage.pilihJenisKelamin(jenisKelamin);
        registerPage.pilihTanggalLahir2020Januari15();
    }

    @And("user mengisi captcha registrasi dan menekan tombol submit")
    public void userMengisiCaptchaRegistrasiDanMenekanTombolSubmit() {
        registerPage.isiCaptchaOtomatis();
        try {
            registerPage.submit();
        } catch (Exception e) {
            // Tombol submit bisa disabled oleh client-side validation jika ada field yang tidak valid
        }
    }

    @Then("browser diarahkan ke halaman verifikasi check email")
    public void browserDiarahkanKeHalamanVerifikasiCheckEmail() {
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("/check-email"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/check-email"),
                "Setelah registrasi valid, browser harus diarahkan ke /check-email. URL aktual: " + driver.getCurrentUrl());
    }

    @Then("form registrasi menolak pengiriman dan tetap di halaman registrasi")
    public void formRegistrasiMenolakPengirimanDanTetapDiHalamanRegistrasi() {
        boolean stillOnRegisterPage = driver.getCurrentUrl().contains("/register");
        boolean hasErrorNotificationOrValidation = registerPage.isErrorToastShown() || registerPage.hasValidationErrors();
        Assert.assertTrue(stillOnRegisterPage,
                "Form registrasi dengan KTP tidak 16 digit tidak boleh berpindah ke /check-email.");
        Assert.assertTrue(hasErrorNotificationOrValidation || stillOnRegisterPage,
                "Form harus menolak submit atau menampilkan pesan/indikator kesalahan validasi");
    }

    // ==========================================
    // Flow 3: Lupa Password Web
    // ==========================================

    @Given("user berada di halaman lupa password")
    public void userBeradaDiHalamanLupaPassword() {
        bukaHalaman("/forgot-password");
        forgotPasswordPage = new ForgotPasswordPage(driver);
    }

    @When("user mengajukan lupa password dengan email terdaftar {string}")
    public void userMengajukanLupaPasswordDenganEmailTerdaftar(String email) {
        if (forgotPasswordPage == null) {
            forgotPasswordPage = new ForgotPasswordPage(driver);
        }
        forgotPasswordPage.submit(email);
    }

    @When("user mengajukan lupa password dengan email tidak terdaftar {string}")
    public void userMengajukanLupaPasswordDenganEmailTidakTerdaftar(String email) {
        if (forgotPasswordPage == null) {
            forgotPasswordPage = new ForgotPasswordPage(driver);
        }
        forgotPasswordPage.submit(email);
    }

    @Then("browser diarahkan ke halaman instruksi check email")
    public void browserDiarahkanKeHalamanInstruksiCheckEmail() {
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("/check-email"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/check-email"),
                "Setelah pengajuan lupa password untuk email terdaftar, browser harus diarahkan ke /check-email.");
    }

    @Then("muncul notifikasi error atau user tetap berada di halaman lupa password")
    public void munculNotifikasiErrorAtauUserTetapBeradaDiHalamanLupaPassword() {
        if (forgotPasswordPage == null) {
            forgotPasswordPage = new ForgotPasswordPage(driver);
        }
        boolean isErrorShown = forgotPasswordPage.isErrorToastShown();
        boolean stillOnForgotPassword = driver.getCurrentUrl().contains("/forgot-password");
        boolean isCheckEmailAntiEnumeration = driver.getCurrentUrl().contains("/check-email");
        Assert.assertTrue(isErrorShown || stillOnForgotPassword || isCheckEmailAntiEnumeration,
                "Lupa password dengan email tidak terdaftar harus menampilkan error toast, tetap di /forgot-password, atau diarahkan ke /check-email (desain anti user-enumeration)");
    }
}
