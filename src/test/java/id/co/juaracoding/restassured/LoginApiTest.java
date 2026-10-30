package id.co.juaracoding.restassured;

import id.co.juaracoding.restassured.util.RsaHelper;
import id.co.juaracoding.util.TestConfig;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginApiTest extends BaseRestAssuredTest {

    @Test
    public void login_dengan_kredensial_valid_berhasil() {
        String[] captcha = ambilCaptcha();
        String ciphertext = RsaHelper.encrypt(TestConfig.VALID_PASSWORD);

        String body = String.format(
                "{\"username\":\"%s\",\"password\":\"%s\",\"captcha_answer\":\"%s\",\"captcha_hash\":\"%s\"}",
                TestConfig.VALID_USERNAME, ciphertext, captcha[1], captcha[0]
        );

        Response response = specDasar().body(body).post("/api/v1/login");

        response.then()
                .statusCode(200)
                .body("status", equalTo("SUCCESS"))
                .body("data.token", notNullValue())
                .body("data.user.username", equalTo(TestConfig.VALID_USERNAME));
    }

    @Test
    public void login_dengan_password_salah_gagal() {
        String[] captcha = ambilCaptcha();
        String ciphertext = RsaHelper.encrypt(TestConfig.INVALID_PASSWORD);

        String body = String.format(
                "{\"username\":\"%s\",\"password\":\"%s\",\"captcha_answer\":\"%s\",\"captcha_hash\":\"%s\"}",
                TestConfig.VALID_USERNAME, ciphertext, captcha[1], captcha[0]
        );

        Response response = specDasar().body(body).post("/api/v1/login");

        // Assertion kegagalan WAJIB memeriksa error_code, DILARANG memeriksa teks message
        response.then()
                .statusCode(401)
                .body("status", equalTo("ERROR"))
                .body("error_code", equalTo("API-ECMXS40107"));
    }
}
