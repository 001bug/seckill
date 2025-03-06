import ohmygod.project.utils.MD5Util;
import org.junit.jupiter.api.Test;

public class MD5UtilTest {
    @Test
    public void t1(){
        String midKey=MD5Util.inPutKeyToMidKey("12345");
        System.out.println(MD5Util.inPutKeyToMidKey("12345"));
        System.out.println(MD5Util.midKeyToDBKey(midKey,"sss"));
        System.out.println(MD5Util.inputKeyToDBKey("12345","sss"));

    }
}
