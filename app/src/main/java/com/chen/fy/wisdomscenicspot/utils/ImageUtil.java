package com.chen.fy.wisdomscenicspot.utils;

import com.chen.fy.wisdomscenicspot.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取图片的工具类
 */
public class ImageUtil {

    //通过存在数据库的图片名找到相应图片的集合    不可以直接往数据库中加入资源id,因为当有新的文件导入项目中时,资源id可能发生变化
    private static Map<String, Integer> imageMap = new HashMap<>();

    //把图片名称与图片资源id一一对应起来
    static {
        imageMap.put("象鼻山", R.drawable.elephant_hill);
        imageMap.put("普贤塔", R.drawable.px_town);
        imageMap.put("太平天国纪念馆", R.drawable.jng);
        imageMap.put("云峰寺", R.drawable.yfs);
    }

    //获取相应的图片资源id
    public static Integer getImageId(String storeName) {
        //如果集合中有这个图片,则返回其资源id
        if (imageMap.containsKey(storeName)) {
            return imageMap.get(storeName);
        }
        return 0;
    }


}

