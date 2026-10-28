package id.co.juaracoding.util;

public class TextUtil {
    public static boolean isPalindrome(String text) {
        if (text == null) {
            return false;
        }
        String cleaned = text.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        String reversed = new StringBuilder(cleaned).reverse().toString();
        return cleaned.equals(reversed);
    }

    public static int countVowels(String text) {
        if (text == null) {
            return 0;
        }
        int count = 0;
        for (char c : text.toLowerCase().toCharArray()) {
            if (c == 'a' || c == 'i' || c == 'u' || c == 'e' || c == 'o') {
                count++;
            }
        }
        return count;
    }
}
