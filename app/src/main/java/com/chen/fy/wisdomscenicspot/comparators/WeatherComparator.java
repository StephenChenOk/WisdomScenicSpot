package com.chen.fy.wisdomscenicspot.comparators;

import com.chen.fy.wisdomscenicspot.model.ViewPointInfo;
import java.util.Comparator;

/**
 * 天气情况比较器
 */
public class WeatherComparator implements Comparator<ViewPointInfo> {

    @Override
    public int compare(ViewPointInfo viewPointInfo1, ViewPointInfo viewPointInfo2) {
        return Double.compare(viewPointInfo2.getWeight(), viewPointInfo1.getWeight());
    }
}