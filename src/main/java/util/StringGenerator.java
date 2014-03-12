package util;

/**
 * Created by Andrew Govorovsky on 12.03.14
 */
public class StringGenerator {
    public static String getRandomString(int len) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append((char) (Math.random() * (122 - 97 + 1) + 97)); /* a - z */
        }
        return builder.toString();
    }
}
