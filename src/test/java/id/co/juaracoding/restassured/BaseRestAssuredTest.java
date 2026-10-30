package id.co.juaracoding.restassured;

import id.co.juaracoding.restassured.util.RsaHelper;
import id.co.juaracoding.util.TestConfig;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.given;

public class BaseRestAssuredTest {

    @BeforeClass
    public void setUpBaseUri() {
        RestAssured.baseURI = TestConfig.BASE_API_URL;
    }

    /**
     * RequestSpecification dasar yang menyertakan header wajib X-API-KEY dan Content-Type JSON.
     */
    protected RequestSpecification specDasar() {
        return given()
                .header("X-API-KEY", TestConfig.X_API_KEY)
                .contentType("application/json");
    }

    /**
     * RequestSpecification untuk endpoint yang memerlukan otorisasi Bearer JWT token.
     */
    protected RequestSpecification specDenganToken(String token) {
        return specDasar().header("Authorization", "Bearer " + token);
    }

    /**
     * Helper method untuk mengambil captcha dari endpoint GET /api/v1/captcha.
     * Mengembalikan array String berukuran 2: [captcha_hash, captcha_value].
     */
    protected String[] ambilCaptcha() {
        Response response = specDasar().get("/api/v1/captcha");
        String hash = response.jsonPath().getString("data.captcha_hash");
        String value = response.jsonPath().getString("data.captcha_value");
        return new String[] { hash, value };
    }

    /**
     * Helper method untuk melakukan login dan mengembalikan JWT token.
     */
    protected String loginDapatkanToken(String username, String plainPassword) {
        String[] captcha = ambilCaptcha();
        String ciphertext = RsaHelper.encrypt(plainPassword);

        Response response = specDasar().body(String.format(
                "{\"username\":\"%s\",\"password\":\"%s\",\"captcha_answer\":\"%s\",\"captcha_hash\":\"%s\"}",
                username, ciphertext, captcha[1], captcha[0]
        )).post("/api/v1/login");

        String token = response.jsonPath().getString("data.token");
        if (token == null) {
            throw new IllegalStateException("Login gagal untuk " + username + " - response: " + response.asString());
        }
        return token;
    }
}
