package id.co.juaracoding.testng;

import id.co.juaracoding.util.TextUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TextUtilTest {

    @BeforeClass
    public void set_up() {
        // Lifecycle setup
    }

    @Test
    public void is_palindrome_returns_true_for_valid_palindrome() {
        Assert.assertTrue(TextUtil.isPalindrome("racecar"), "kata 'racecar' seharusnya terdeteksi sebagai palindrome");
        Assert.assertTrue(TextUtil.isPalindrome("Madam, I'm Adam"), "frasa 'Madam, I'm Adam' seharusnya terdeteksi sebagai palindrome");
    }

    @Test
    public void is_palindrome_returns_false_for_non_palindrome() {
        Assert.assertFalse(TextUtil.isPalindrome("hello"), "kata 'hello' bukan palindrome");
    }

    @Test
    public void is_palindrome_returns_false_for_null_input() {
        Assert.assertFalse(TextUtil.isPalindrome(null), "null input seharusnya mengembalikan false");
    }

    @Test
    public void count_vowels_returns_correct_count_for_normal_text() {
        Assert.assertEquals(TextUtil.countVowels("automation"), 6, "jumlah vokal pada 'automation' seharusnya 6");
        Assert.assertEquals(TextUtil.countVowels("SQA Testing"), 3, "jumlah vokal pada 'SQA Testing' seharusnya 3");
    }

    @Test
    public void count_vowels_returns_zero_for_null_or_no_vowels() {
        Assert.assertEquals(TextUtil.countVowels(null), 0, "null seharusnya menghasilkan 0 vokal");
        Assert.assertEquals(TextUtil.countVowels("rhythm"), 0, "kata tanpa vokal seharusnya menghasilkan 0");
    }
}
