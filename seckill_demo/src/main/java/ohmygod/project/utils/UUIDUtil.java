package ohmygod.project.utils;

public class UUIDUtil {
    /**
     * 生成一个无横线的UUID字符串。
     *
     * @return 返回一个无横线的UUID字符串
     */
    public static String uuid(){
        // 生成一个随机的UUID
        return java.util.UUID.randomUUID().toString().replace("-","");
    }

}
