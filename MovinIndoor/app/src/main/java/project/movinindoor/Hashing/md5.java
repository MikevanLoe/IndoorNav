package project.movinindoor.Hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Thomas on 7-1-2015.
 * Salt: ofzhWAF1VsnkqVmCrD6V
 */
public class md5 {
    public static final String md5(final String s) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(s.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return "";
        }
    }
}
