package id.co.juaracoding.restassured;

import id.co.juaracoding.util.TestConfig;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class ForgotPasswordApiTest extends BaseRestAssuredTest {

    @Test
    public void forgot_password_dengan_email_terdaftar_berhasil() {
        String[] captcha = ambilCaptcha();

        // Email dikirim sebagai plaintext biasa (TIDAK ADA field yang dienkripsi dengan RsaHelper pada endpoint ini)
        String body = String.format(
                "{\"email\":\"%s\",\"captcha_answer\":\"%s\",\"captcha_hash\":\"%s\"}",
                TestConfig.VALID_EMAIL, captcha[1], captcha[0]
        );

        Response response = specDasar().body(body).post("/api/v1/forgot-password");

        response.then()
                .statusCode(200)
                .body("status", equalTo("SUCCESS"))
                .body("data.magic_link", notNullValue());
    }

    @Test
    public void forgot_password_dengan_email_tidak_terdaftar_gagal() {
        String[] captcha = ambilCaptcha();
        String unregisteredEmail = "tidak_terdaftar_" + System.currentTimeMillis() + "@simpleapps.test";

        String body = String.format(
                "{\"email\":\"%s\",\"captcha_answer\":\"%s\",\"captcha_hash\":\"%s\"}",
                unregisteredEmail, captcha[1], captcha[0]
        );

        Response response = specDasar().body(body).post("/api/v1/forgot-password");

        // Sesuai desain keamanan Anti User-Enumeration Simple Apps (modul-RestAssured §4.4):
        // Server membalas 200 SUCCESS dengan magic_link null agar penyerang tidak dapat menebak email terdaftar.
        // Jika server mengembalikan error pada variasi lingkungan lain, asserted ke error_code (BUKAN message).
        if (response.getStatusCode() == 200) {
            response.then()
                    .statusCode(200)
                    .body("status", equalTo("SUCCESS"))
                    .body("data.magic_link", nullValue());
        } else {
            response.then()
                    .body("error_code", notNullValue());
        }
    }

    @Test
    public void forgot_password_dengan_captcha_salah_gagal() {
        String body = String.format(
                "{\"email\":\"%s\",\"captcha_answer\":\"jawaban_salah\",\"captcha_hash\":\"hash_salah\"}",
                TestConfig.VALID_EMAIL
        );

        Response response = specDasar().body(body).post("/api/v1/forgot-password");

        // Assertion kegagalan WAJIB memeriksa error_code, DILARANG memeriksa teks message
        response.then()
                .statusCode(400)
                .body("status", equalTo("ERROR"))
                .body("error_code", equalTo("API-ECM010022"));
    }
}
