package eco.ywhc.xr.common.util;

/**
 * 数组工具类
 */
public class ArrayUtils {

    /**
     * 获取指定索引的元素或者返回默认值
     *
     * @param array        数组
     * @param index        索引
     * @param defaultValue 默认值
     */
    public static <T> T getOrDefault(final T[] array, int index, T defaultValue) {
        if (array == null) {
            return defaultValue;
        }
        if (index < 0) {
            return defaultValue;
        }
        if (index >= array.length) {
            return defaultValue;
        }
        return array[index];
    }

    /**
     * 获取指定索引的元素或者返回null
     *
     * @param array 数组
     * @param index 索引
     */
    public static <T> T getOrNull(final T[] array, int index) {
        return getOrDefault(array, index, null);
    }

}
