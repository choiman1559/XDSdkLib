package lib.xdsdk.passport.httpUrlConnectionUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public interface Key {
    public static final String STRING_CHARSET_NAME = "UTF-8";

    boolean equals(Object obj);

    int hashCode();
}