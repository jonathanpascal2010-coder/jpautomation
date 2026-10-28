package id.co.juaracoding.util;

public class TestConfig {
    /**
     * Alamat base URL Simple Apps.
     * Menggunakan port 9090 sesuai VERIFY-aplikasi.docx §0.1 (karena port 8080 sering kena reservasi OS Windows),
     * dengan opsi override via system property jika dijalankan di port lain.
     */
    public static final String BASE_URL = System.getProperty("baseUrl", "http://localhost:9090");

    /**
     * Kunci Lisensi aktivasi aplikasi Simple Apps (README.docx §3).
     */
    public static final String LICENSE_KEY = "d461265dd7323fef9755bb3257275d67";

    /**
     * Kredensial akun seed valid (README.docx §4 & DataSeeder).
     */
    public static final String VALID_USERNAME = "admin1";
    public static final String VALID_PASSWORD = "Admin1@123";
    public static final String VALID_USER_FULL_NAME = "Admin Satu";
    public static final String VALID_EMAIL = "admin1@simpleapps.test";

    /**
     * Kredensial dan data uji untuk skenario gagal (negatif).
     */
    public static final String INVALID_PASSWORD = "PasswordSalah123!";
    public static final String INVALID_EMAIL = "tidak_terdaftar_9999@example.com";
    public static final String INVALID_ID_CARD_NUMBER = "12345"; // Bukan 16 digit
    public static final String INVALID_TAX_ID_NUMBER = "98765";  // Bukan 16 digit

    /**
     * Password default untuk registrasi akun baru.
     */
    public static final String DEFAULT_REGISTER_PASSWORD = "Selenium1@123";
}
