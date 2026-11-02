package id.co.juaracoding.cucumber.api;

import id.co.juaracoding.restassured.util.RsaHelper;
import id.co.juaracoding.util.TestConfig;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

public class ApiSteps {

    private String captchaHash;
    private String captchaValue;
    private Response responseLogin;
    private Response responseRegister;
    private Response responseForgotPassword;

    @Before
    public void inisialisasiRestAssured() {
        RestAssured.baseURI = TestConfig.BASE_API_URL;
    }

    private RequestSpecification specDasar() {
        return given()
                .header("X-API-KEY", TestConfig.X_API_KEY)
                .contentType("application/json");
    }

    // ==========================================
    // Captcha Helper Step
    // ==========================================

    @Given("client telah mengambil captcha dari API")
    public void clientTelahMengambilCaptchaDariApi() {
        Response response = specDasar().get("/api/v1/captcha");
        captchaHash = response.jsonPath().getString("data.captcha_hash");
        captchaValue = response.jsonPath().getString("data.captcha_value");
        Assert.assertNotNull(captchaHash, "captcha_hash tidak boleh bernilai null");
        Assert.assertNotNull(captchaValue, "captcha_value tidak boleh bernilai null");
    }

    // ==========================================
    // Flow 1: Login API
    // ==========================================

    @When("client mengirim request login API dengan username {string} dan password valid")
    public void clientMengirimRequestLoginApiDenganUsernameDanPasswordValid(String username) {
        String ciphertext = RsaHelper.encrypt(TestConfig.VALID_PASSWORD);
        String body = String.format(
                "{\"username\":\"%s\",\"password\":\"%s\",\"captcha_answer\":\"%s\",\"captcha_hash\":\"%s\"}",
                username, ciphertext, captchaValue, captchaHash
        );
        responseLogin = specDasar().body(body).post("/api/v1/login");
    }

    @When("client mengirim request login API dengan username {string} dan password yang salah")
    public void clientMengirimRequestLoginApiDenganUsernameDanPasswordYangSalah(String username) {
        String ciphertext = RsaHelper.encrypt(TestConfig.INVALID_PASSWORD);
        String body = String.format(
                "{\"username\":\"%s\",\"password\":\"%s\",\"captcha_answer\":\"%s\",\"captcha_hash\":\"%s\"}",
                username, ciphertext, captchaValue, captchaHash
        );
        responseLogin = specDasar().body(body).post("/api/v1/login");
    }

    @Then("response API login memiliki status code {int}")
    public void responseApiLoginMemilikiStatusCode(int statusCode) {
        Assert.assertEquals(responseLogin.statusCode(), statusCode,
                "Status code response Login API tidak sesuai.");
    }

    @And("response API login berstatus {string} dan memiliki data token")
    public void responseApiLoginBerstatusDanMemilikiDataToken(String status) {
        responseLogin.then()
                .body("status", equalTo(status))
                .body("data.token", notNullValue());
    }

    @And("response API login berstatus {string} dengan error code {string}")
    public void responseApiLoginBerstatusDenganErrorCode(String status, String errorCode) {
        responseLogin.then()
                .body("status", equalTo(status))
                .body("error_code", equalTo(errorCode));
    }

    // ==========================================
    // Flow 2: Registrasi API
    // ==========================================

    @When("client mengirim request registrasi API dengan seluruh data valid dan field terenkripsi")
    public void clientMengirimRequestRegistrasiApiDenganSeluruhDataValidDanFieldTerenkripsi() {
        String unik = String.valueOf(System.currentTimeMillis());
        String username = "resta" + unik.substring(unik.length() - 8);
        String emailPlain = "resta" + unik + "@example.com";
        String phonePlain = "08" + unik.substring(unik.length() - 9);
        String ktpPlain = "300" + unik;
        String npwpPlain = "310" + unik;
        String birthDatePlain = "2000-05-10";

        // Enkripsi 6 field wajib lewat RsaHelper.encrypt(...)
        String passwordEncrypted = RsaHelper.encrypt("Resta123@1");
        String emailEncrypted = RsaHelper.encrypt(emailPlain);
        String phoneEncrypted = RsaHelper.encrypt(phonePlain);
        String birthDateEncrypted = RsaHelper.encrypt(birthDatePlain);
        String ktpEncrypted = RsaHelper.encrypt(ktpPlain);
        String npwpEncrypted = RsaHelper.encrypt(npwpPlain);

        String body = "{"
                + "\"username\":\"" + username + "\","
                + "\"password\":\"" + passwordEncrypted + "\","
                + "\"full_name\":\"Peserta RestAssured\","
                + "\"email\":\"" + emailEncrypted + "\","
                + "\"phone_number\":\"" + phoneEncrypted + "\","
                + "\"address\":\"Jalan Merdeka No 10\","
                + "\"birth_date\":\"" + birthDateEncrypted + "\","
                + "\"gender\":\"MALE\","
                + "\"last_education\":\"S1\","
                + "\"blood_type\":\"O\","
                + "\"id_card_number\":\"" + ktpEncrypted + "\","
                + "\"tax_id_number\":\"" + npwpEncrypted + "\","
                + "\"postal_code\":\"40123\","
                + "\"captcha_answer\":\"" + captchaValue + "\","
                + "\"captcha_hash\":\"" + captchaHash + "\""
                + "}";

        responseRegister = specDasar().body(body).post("/api/v1/register");
    }

    @When("client mengirim request registrasi API dengan nomor KTP tidak 16 digit")
    public void clientMengirimRequestRegistrasiApiDenganNomorKtpTidak16Digit() {
        String unik = String.valueOf(System.currentTimeMillis());
        String username = "restb" + unik.substring(unik.length() - 8);
        String emailPlain = "restb" + unik + "@example.com";
        String phonePlain = "08" + unik.substring(unik.length() - 9);
        String ktpTidakSah = RsaHelper.encrypt("123456789012345"); // 15 digit (bukan 16 digit)
        String npwpPlain = "320" + unik;
        String birthDatePlain = "2000-05-10";

        String body = "{"
                + "\"username\":\"" + username + "\","
                + "\"password\":\"" + RsaHelper.encrypt("Resta123@1") + "\","
                + "\"full_name\":\"Peserta RestAssured\","
                + "\"email\":\"" + RsaHelper.encrypt(emailPlain) + "\","
                + "\"phone_number\":\"" + RsaHelper.encrypt(phonePlain) + "\","
                + "\"address\":\"Jalan Merdeka No 10\","
                + "\"birth_date\":\"" + RsaHelper.encrypt(birthDatePlain) + "\","
                + "\"gender\":\"MALE\","
                + "\"last_education\":\"S1\","
                + "\"blood_type\":\"O\","
                + "\"id_card_number\":\"" + ktpTidakSah + "\","
                + "\"tax_id_number\":\"" + RsaHelper.encrypt(npwpPlain) + "\","
                + "\"postal_code\":\"40123\","
                + "\"captcha_answer\":\"" + captchaValue + "\","
                + "\"captcha_hash\":\"" + captchaHash + "\""
                + "}";

        responseRegister = specDasar().body(body).post("/api/v1/register");
    }

    @Then("response API registrasi memiliki status code {int}")
    public void responseApiRegistrasiMemilikiStatusCode(int statusCode) {
        Assert.assertEquals(responseRegister.statusCode(), statusCode,
                "Status code response Registrasi API tidak sesuai.");
    }

    @And("response API registrasi berstatus {string} dan memiliki data magic link")
    public void responseApiRegistrasiBerstatusDanMemilikiDataMagicLink(String status) {
        responseRegister.then()
                .body("status", equalTo(status))
                .body("data.magic_link", notNullValue());
    }

    @And("response API registrasi berstatus {string} dengan error code {string} dan field error {string}")
    public void responseApiRegistrasiBerstatusDenganErrorCodeDanFieldError(String status, String umbrellaErrorCode, String fieldErrorCode) {
        responseRegister.then()
                .body("status", equalTo(status))
                .body("error_code", equalTo(umbrellaErrorCode))
                .body("errors.field", hasItem("id_card_number"))
                .body("errors.find { it.field == 'id_card_number' }.error_code", equalTo(fieldErrorCode));
    }

    // ==========================================
    // Flow 3: Lupa Password API
    // ==========================================

    @When("client mengirim request lupa password API dengan email {string}")
    public void clientMengirimRequestLupaPasswordApiDenganEmail(String email) {
        String body = String.format(
                "{\"email\":\"%s\",\"captcha_answer\":\"%s\",\"captcha_hash\":\"%s\"}",
                email, captchaValue, captchaHash
        );
        responseForgotPassword = specDasar().body(body).post("/api/v1/forgot-password");
    }

    @When("client mengirim request lupa password API dengan email {string} dan captcha tidak valid")
    public void clientMengirimRequestLupaPasswordApiDenganEmailDanCaptchaTidakValid(String email) {
        String body = String.format(
                "{\"email\":\"%s\",\"captcha_answer\":\"jawaban_salah\",\"captcha_hash\":\"hash_salah\"}",
                email
        );
        responseForgotPassword = specDasar().body(body).post("/api/v1/forgot-password");
    }

    @Then("response API lupa password memiliki status code {int}")
    public void responseApiLupaPasswordMemilikiStatusCode(int statusCode) {
        Assert.assertEquals(responseForgotPassword.statusCode(), statusCode,
                "Status code response Lupa Password API tidak sesuai.");
    }

    @And("response API lupa password berstatus {string} dan memiliki data magic link")
    public void responseApiLupaPasswordBerstatusDanMemilikiDataMagicLink(String status) {
        responseForgotPassword.then()
                .body("status", equalTo(status))
                .body("data.magic_link", notNullValue());
    }

    @And("response API lupa password berstatus {string} dengan error code {string}")
    public void responseApiLupaPasswordBerstatusDenganErrorCode(String status, String errorCode) {
        responseForgotPassword.then()
                .body("status", equalTo(status))
                .body("error_code", equalTo(errorCode));
    }
}
