package id.co.juaracoding.restassured;

import id.co.juaracoding.restassured.util.RsaHelper;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

public class RegisterApiTest extends BaseRestAssuredTest {

    private String angkaUnik() {
        return String.valueOf(System.currentTimeMillis());
    }

    @Test
    public void register_dengan_seluruh_field_valid_berhasil() {
        String unik = angkaUnik();
        String[] captcha = ambilCaptcha();

        String username = "resta" + unik.substring(unik.length() - 8);
        String emailPlain = "resta" + unik + "@example.com";
        String phonePlain = "08" + unik.substring(unik.length() - 9);
        String ktpPlain = "300" + unik; // 3 + 13 = 16 digit
        String npwpPlain = "310" + unik; // 3 + 13 = 16 digit
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
                + "\"captcha_answer\":\"" + captcha[1] + "\","
                + "\"captcha_hash\":\"" + captcha[0] + "\""
                + "}";

        Response response = specDasar().body(body).post("/api/v1/register");

        response.then()
                .statusCode(200)
                .body("status", equalTo("SUCCESS"))
                .body("data.magic_link", notNullValue());
    }

    @Test
    public void register_dengan_id_card_number_bukan_16_digit_gagal() {
        String unik = angkaUnik();
        String[] captcha = ambilCaptcha();

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
                + "\"captcha_answer\":\"" + captcha[1] + "\","
                + "\"captcha_hash\":\"" + captcha[0] + "\""
                + "}";

        Response response = specDasar().body(body).post("/api/v1/register");

        // Assertion kegagalan WAJIB memeriksa error_code, DILARANG memeriksa teks message
        response.then()
                .statusCode(400)
                .body("status", equalTo("ERROR"))
                .body("error_code", equalTo("API-ECM010001")) // Kode payung registrasi
                .body("errors.field", hasItem("id_card_number"))
                .body("errors.find { it.field == 'id_card_number' }.error_code", equalTo("API-ECM010009"));
    }
}
