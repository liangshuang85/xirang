package eco.ywhc.xr.common.util;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

public class ChecksumUtils {

    public static String sha256Hex(byte[] bytes) {
        return Hashing.sha256().hashBytes(bytes).toString();
    }

    public static String shortHash(String s) {
        return Hashing.sha256().hashString(s, Charsets.UTF_8).toString().substring(0, 12);
    }

}
