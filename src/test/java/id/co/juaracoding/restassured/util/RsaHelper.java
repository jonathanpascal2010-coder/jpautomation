package id.co.juaracoding.restassured.util;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import javax.crypto.spec.PSource;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaHelper {

    /**
     * Public key aplikasi Simple Apps (X.509 SubjectPublicKeyInfo, DER, base64).
     * Sumber: rsa-keys.env (RSA_PUBLIC_KEY_BASE64) — dipakai apa adanya, jangan diganti.
     */
    private static final String PUBLIC_KEY_BASE64 =
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzMX6OysWzaizAsyFuablBfHUASPRRnqMosG/0dVIeOS9C2ckEotm5Z/aKEYUriYwuPqQOCm45VB+tpMChaBGi6tFt4ytu3crvBKvctziIgsR+dy2qZJKWDqyxjCJgso/0S/XDOVKfMYbqJ4Cy/e0nehBaage48nY2h5reEmrEOhMVbh+6izp31bQDYaA20/ouyuBTHwtLZdPsJOmSkVddNh7yHKJZqyHWMExyX+2Dvr4heCtIebanz5t5AHpeHvKLgIcgycfRv4wrKq4Lx3PacJfXmW0QqAO3jSW+i9dF0zAWmAr2PlAUok47zZa11OBeePD15jeJgb2UMwR5p9gsQIDAQAB";

    public static String encrypt(String plaintext) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(PUBLIC_KEY_BASE64);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey = kf.generatePublic(spec);

            OAEPParameterSpec oaepSpec = new OAEPParameterSpec(
                    "SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey, oaepSpec);
            byte[] encrypted = cipher.doFinal(plaintext.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Gagal mengenkripsi nilai dengan RsaHelper", e);
        }
    }
}
