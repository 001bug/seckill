package ohmygod.project.utils;

import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }
    private static final String SALT="aseds";
    public static String inPutKeyToMidKey(String key){
        return md5(SALT+key);
    }
    public static String midKeyToDBKey(String key,String salt){
        return md5(SALT+key+salt);
    }
    public static String inputKeyToDBKey(String key,String salt){
        String midKey=inPutKeyToMidKey(key);
        String DBKey=midKeyToDBKey(midKey,salt);
        return DBKey;
    }
}
