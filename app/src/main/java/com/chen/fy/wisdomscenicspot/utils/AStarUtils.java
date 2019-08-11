package com.chen.fy.wisdomscenicspot.utils;

import android.util.Log;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;

public class AStarUtils {

    //指定的经纬度，用来作为人流量影响的一个标准
    private static double mLatitude1 = 25.266559;      //云峰寺
    private static double mLongitude1 = 110.295011;
    private static double mLatitude2 = 25.266431;     //太平天国纪念馆
    private static double mLongitude2 = 110.295181;

    public static int A_star(int[] numbers, String myLocation, String targetLocation) {
        double[] f_n = new double[3];
        ArrayList<LatLng> latLngs = new ArrayList<>();
        if (myLocation.equals("醉乡") && targetLocation.equals("普贤塔")) {
            switch (zxToTown(numbers, f_n, latLngs)) {
                case 0:
                    return RoadPlanningUtils.ROAD_1;
                case 1:
                    return RoadPlanningUtils.ROAD_2;
                case 2:
                    return RoadPlanningUtils.ROAD_3;
            }
        }
        if(myLocation.equals("醉乡") && targetLocation.equals("象眼岩")){
            switch (zxToRock(numbers, f_n, latLngs)) {
                case 0:
                    return RoadPlanningUtils.ROAD_4;
                case 1:
                    return RoadPlanningUtils.ROAD_5;
                case 2:
                    return RoadPlanningUtils.ROAD_6;
            }
        }
        if(myLocation.equals("醉乡") && targetLocation.equals("桂林抗战遗址")){
            return RoadPlanningUtils.ROAD_7;
        }
        if(myLocation.equals("云峰寺") && targetLocation.equals("普贤塔")){
            switch (yfsToTown(numbers, f_n, latLngs)) {
                case 0:
                    return RoadPlanningUtils.ROAD_8;
                case 1:
                    return -1;
                case 2:
                    return RoadPlanningUtils.ROAD_9;
            }
        }
        if(myLocation.equals("云峰寺") && targetLocation.equals("象眼岩")){
            switch (yfsToRock(numbers, f_n, latLngs)) {
                case 0:
                    return RoadPlanningUtils.ROAD_10;
                case 1:
                    return RoadPlanningUtils.ROAD_11;
                case 2:
                    return RoadPlanningUtils.ROAD_12;
            }
        }
        if(myLocation.equals("云峰寺") && targetLocation.equals("桂林抗战遗址")){
            return RoadPlanningUtils.ROAD_13;
        }
        if(myLocation.equals("象山钟韵") && targetLocation.equals("普贤塔")){
            return RoadPlanningUtils.ROAD_14;
        }
        if(myLocation.equals("象山钟韵") && targetLocation.equals("象眼岩")){
            switch (zyToRock(numbers, f_n, latLngs)) {
                case 0:
                    return RoadPlanningUtils.ROAD_15;
                case 1:
                    return RoadPlanningUtils.ROAD_16;
                case 2:
                    return RoadPlanningUtils.ROAD_17;
            }
        }
        if(myLocation.equals("象山钟韵") && targetLocation.equals("桂林抗战遗址")){
            switch (zyToRuins(numbers, f_n, latLngs)) {
                case 0:
                    return RoadPlanningUtils.ROAD_18;
                case 1:
                    return -1;
                case 2:
                    return RoadPlanningUtils.ROAD_19;
            }
        }
        return -1;
    }

    private static int zyToRuins(int[] numbers, double[] f_n, ArrayList<LatLng> latLngs) {
        double g_n;
        int number;
        double h_n;
        for (int i = 0; i < 3; i++) {
            g_n = 0;
            switch (i) {
                case 0:     //求出到普贤塔的代价
                    number = numbers[i];
                    //求 g(n)
                    latLngs.clear();
                    RoadPlanningUtils.zyToRuins_road1(latLngs);
                    for (int j = 1; j < latLngs.size(); j++) {
                        LatLng latLng = latLngs.get(j);
                        LatLng latLng_pre = latLngs.get(j - 1);
                        g_n += Math.sqrt((latLng.latitude - latLng_pre.latitude) * (latLng.latitude - latLng_pre.latitude)
                                + (latLng.longitude - latLng_pre.longitude) * (latLng.longitude - latLng_pre.longitude));
                    }
                    //求 h(n)
                    number = number / 10;
                    if (number == 0) {
                        number = 1;
                    }
                    h_n = number * Math.sqrt((mLatitude1 - mLatitude2) * (mLatitude1 - mLatitude2)
                            + (mLongitude1 - mLongitude2) * (mLongitude1 - mLongitude2));
                    //得 f(n)
                    f_n[i] = g_n + h_n;
                    break;
                case 1:     //求出到象眼岩的代价
                    f_n[i] = 100000;
                    break;
                case 2:     //求出到桂林抗战遗址的代价
                    number = numbers[i];
                    //求 g(n)
                    latLngs.clear();
                    RoadPlanningUtils.zyToRuins_road3(latLngs);
                    for (int j = 1; j < latLngs.size(); j++) {
                        LatLng latLng = latLngs.get(j);
                        LatLng latLng_pre = latLngs.get(j - 1);
                        g_n += Math.sqrt((latLng.latitude - latLng_pre.latitude) * (latLng.latitude - latLng_pre.latitude)
                                + (latLng.longitude - latLng_pre.longitude) * (latLng.longitude - latLng_pre.longitude));
                    }
                    //求 h(n)
                    number = number / 10;
                    if (number == 0) {
                        number = 1;
                    }
                    h_n = number * Math.sqrt((mLatitude1 - mLatitude2) * (mLatitude1 - mLatitude2)
                            + (mLongitude1 - mLongitude2) * (mLongitude1 - mLongitude2));
                    //得 f(n)
                    f_n[i] = g_n + h_n;
                    break;
            }
        }
        double temp = f_n[0];
        int minFlag = 0;
        for (int i = 0; i < f_n.length; i++) {
            if (f_n[i] < temp) {
                temp = f_n[i];
                minFlag = i;
            }
        }
        return minFlag;
    }

    private static int zyToRock(int[] numbers, double[] f_n, ArrayList<LatLng> latLngs) {
        double g_n;
        int number;
        double h_n;
        for (int i = 0; i < 3; i++) {
            g_n = 0;
            switch (i) {
                case 0:     //求出到普贤塔的代价
                    number = numbers[i];
                    //求 g(n)
                    latLngs.clear();
                    RoadPlanningUtils.zyToRock_road1(latLngs);
                    for (int j = 1; j < latLngs.size(); j++) {
                        LatLng latLng = latLngs.get(j);
                        LatLng latLng_pre = latLngs.get(j - 1);
                        g_n += Math.sqrt((latLng.latitude - latLng_pre.latitude) * (latLng.latitude - latLng_pre.latitude)
                                + (latLng.longitude - latLng_pre.longitude) * (latLng.longitude - latLng_pre.longitude));
                    }
                    //求 h(n)
                    number = number / 10;
                    if (number == 0) {
                        number = 1;
                    }
                    h_n = number * Math.sqrt((mLatitude1 - mLatitude2) * (mLatitude1 - mLatitude2)
                            + (mLongitude1 - mLongitude2) * (mLongitude1 - mLongitude2));
                    //得 f(n)
                    f_n[i] = g_n + h_n;
                    break;
                case 1:     //求出到象眼岩的代价
                    number = numbers[i];
                    //求 g(n)
                    latLngs.clear();
                    RoadPlanningUtils.zyToRock_road1(latLngs);
                    for (int j = 1; j < latLngs.size(); j++) {
                        LatLng latLng = latLngs.get(j);
                        LatLng latLng_pre = latLngs.get(j - 1);
                        g_n += Math.sqrt((latLng.latitude - latLng_pre.latitude) * (latLng.latitude - latLng_pre.latitude)
                                + (latLng.longitude - latLng_pre.longitude) * (latLng.longitude - latLng_pre.longitude));
                    }
                    //求 h(n)
                    number = number / 10;
                    if (number == 0) {
                        number = 1;
                    }
                    h_n = number * Math.sqrt((mLatitude1 - mLatitude2) * (mLatitude1 - mLatitude2)
                            + (mLongitude1 - mLongitude2) * (mLongitude1 - mLongitude2));
                    //得 f(n)
                    f_n[i] = g_n + h_n;
                    break;
                case 2:     //求出到桂林抗战遗址的代价
                    number = numbers[i];
                    //求 g(n)
                    latLngs.clear();
                    RoadPlanningUtils.zyToRock_road1(latLngs);
                    for (int j = 1; j < latLngs.size(); j++) {
                        LatLng latLng = latLngs.get(j);
                        LatLng latLng_pre = latLngs.get(j - 1);
                        g_n += Math.sqrt((latLng.latitude - latLng_pre.latitude) * (latLng.latitude - latLng_pre.latitude)
                                + (latLng.longitude - latLng_pre.longitude) * (latLng.longitude - latLng_pre.longitude));
                    }
                    //求 h(n)
                    number = number / 10;
                    if (number == 0) {
                        number = 1;
                    }
                    h_n = number * Math.sqrt((mLatitude1 - mLatitude2) * (mLatitude1 - mLatitude2)
                            + (mLongitude1 - mLongitude2) * (mLongitude1 - mLongitude2));
                    //得 f(n)
                    f_n[i] = g_n + h_n;
                    break;
            }
        }
        double temp = f_n[0];
        int minFlag = 0;
        for (int i = 0; i < f_n.length; i++) {
            if (f_n[i] < temp) {
                temp = f_n[i];
                minFlag = i;
            }
        }
        return minFlag;
    }

    private static int yfsToRock(int[] numbers, double[] f_n, ArrayList<LatLng> latLngs) {
        double g_n;
        int number;
        double h_n;
        for (int i = 0; i < 3; i++) {
            g_n = 0;
            switch (i) {
                case 0:     //求出到普贤塔的代价
                    number = numbers[i];
                    //求 g(n)
                    latLngs.clear();
                    RoadPlanningUtils.yfsToRock_road1(latLngs);
                    for (int j = 1; j < latLngs.size(); j++) {
                        LatLng latLng = latLngs.get(j);
                        LatLng latLng_pre = latLngs.get(j - 1);
                        g_n += Math.sqrt((latLng.latitude - latLng_pre.latitude) * (latLng.latitude - latLng_pre.latitude)
                                + (latLng.longitude - latLng_pre.longitude) * (latLng.longitude - latLng_pre.longitude));
                    }
                    //求 h(n)
                    number = number / 10;
                    if (number == 0) {
                        number = 1;
                    }
                    h_n = number * Math.sqrt((mLatitude1 - mLatitude2) * (mLatitude1 - mLatitude2)
                            + (mLongitude1 - mLongitude2) * (mLongitude1 - mLongitude2));
                    //得 f(n)
                    f_n[i] = g_n + h_n;
                    break;
                case 1:     //求出到象眼岩的代价
                    number = numbers[i];
                    //求 g(n)
                    latLngs.clear();
                    RoadPlanningUtils.yfsToRock_road2(latLngs);
                    for (int j = 1; j < latLngs.size(); j++) {
                        LatLng latLng = latLngs.get(j);
                        LatLng latLng_pre = latLngs.get(j - 1);
                        g_n += Math.sqrt((latLng.latitude - latLng_pre.latitude) * (latLng.latitude - latLng_pre.latitude)
                                + (latLng.longitude - latLng_pre.longitude) * (latLng.longitude - latLng_pre.longitude));
                    }
                    //求 h(n)
                    number = number / 10;
                    if (number == 0) {
                        number = 1;
                    }
                    h_n = number * Math.sqrt((mLatitude1 - mLatitude2) * (mLatitude1 - mLatitude2)
                            + (mLongitude1 - mLongitude2) * (mLongitude1 - mLongitude2));
                    //得 f(n)
                    f_n[i] = g_n + h_n;
                    break;
                case 2:     //求出到桂林抗战遗址的代价
                    number = numbers[i];
                    //求 g(n)
                    latLngs.clear();
                    RoadPlanningUtils.yfsToRock_road3(latLngs);
                    for (int j = 1; j < latLngs.size(); j++) {
                        LatLng latLng = latLngs.get(j);
                        LatLng latLng_pre = latLngs.get(j - 1);
                        g_n += Math.sqrt((latLng.latitude - latLng_pre.latitude) * (latLng.latitude - latLng_pre.latitude)
                                + (latLng.longitude - latLng_pre.longitude) * (latLng.longitude - latLng_pre.longitude));
                    }
                    //求 h(n)
                    number = number / 10;
                    if (number == 0) {
                        number = 1;
                    }
                    h_n = number * Math.sqrt((mLatitude1 - mLatitude2) * (mLatitude1 - mLatitude2)
                            + (mLongitude1 - mLongitude2) * (mLongitude1 - mLongitude2));
                    //得 f(n)
                    f_n[i] = g_n + h_n;
                    break;
            }
        }
        double temp = f_n[0];
        int minFlag = 0;
        for (int i = 0; i < f_n.length; i++) {
            if (f_n[i] < temp) {
                temp = f_n[i];
                minFlag = i;
            }
        }
        return minFlag;
    }

    /**
     * 云峰寺-->普贤塔
     */
    private static int yfsToTown(int[] numbers, double[] f_n, ArrayList<LatLng> latLngs) {
        double g_n;
        int number;
        double h_n;
        for (int i = 0; i < 3; i++) {
            g_n = 0;
            switch (i) {
                case 0:     //求出到普贤塔的代价
                    number = numbers[i];
                    //求 g(n)
                    latLngs.clear();
                    RoadPlanningUtils.yfsToTown_road1(latLngs);
                    for (int j = 1; j < latLngs.size(); j++) {
                        LatLng latLng = latLngs.get(j);
                        LatLng latLng_pre = latLngs.get(j - 1);
                        g_n += Math.sqrt((latLng.latitude - latLng_pre.latitude) * (latLng.latitude - latLng_pre.latitude)
                                + (latLng.longitude - latLng_pre.longitude) * (latLng.longitude - latLng_pre.longitude));
                    }
                    //求 h(n)
                    number = number / 10;
                    if (number == 0) {
                        number = 1;
                    }
                    h_n = number * Math.sqrt((mLatitude1 - mLatitude2) * (mLatitude1 - mLatitude2)
                            + (mLongitude1 - mLongitude2) * (mLongitude1 - mLongitude2));
                    //得 f(n)
                    f_n[i] = g_n + h_n;
                    Log.d("chenyisheng...", String.valueOf(f_n[i]));
                    break;
                case 1:     //求出到象眼岩的代价
                    //得 f(n)
                    f_n[i] = 100000;
                    break;
                case 2:     //求出到桂林抗战遗址的代价
                    number = numbers[i];
                    //求 g(n)
                    latLngs.clear();
                    RoadPlanningUtils.yfsToTown_road3(latLngs);
                    for (int j = 1; j < latLngs.size(); j++) {
                        LatLng latLng = latLngs.get(j);
                        LatLng latLng_pre = latLngs.get(j - 1);
                        g_n += Math.sqrt((latLng.latitude - latLng_pre.latitude) * (latLng.latitude - latLng_pre.latitude)
                                + (latLng.longitude - latLng_pre.longitude) * (latLng.longitude - latLng_pre.longitude));
                    }
                    //求 h(n)
                    number = number / 10;
                    if (number == 0) {
                        number = 1;
                    }
                    h_n = number * Math.sqrt((mLatitude1 - mLatitude2) * (mLatitude1 - mLatitude2)
                            + (mLongitude1 - mLongitude2) * (mLongitude1 - mLongitude2));
                    //得 f(n)
                    f_n[i] = g_n + h_n;
                    break;
            }
        }
        double temp = f_n[0];
        int minFlag = 0;
        for (int i = 0; i < f_n.length; i++) {
            if (f_n[i] < temp) {
                temp = f_n[i];
                minFlag = i;
            }
        }
        return minFlag;
    }

    /**
     * 醉乡-->桂林抗战景区
     */
    private static int zxToRuins(int[] numbers, double[] f_n, ArrayList<LatLng> latLngs) {
        return RoadPlanningUtils.ROAD_4;
    }

    /**
     * 醉乡-->普贤塔
     */
    private static int zxToTown(int[] numbers, double[] f_n, ArrayList<LatLng> latLngs) {
        double g_n;
        int number;
        double h_n;
        for (int i = 0; i < 3; i++) {
            g_n = 0;
            switch (i) {
                case 0:     //求出到普贤塔的代价
                    number = numbers[i];
                    //求 g(n)
                    latLngs.clear();
                   RoadPlanningUtils.zxToTown_road1(latLngs);
                    for (int j = 1; j < latLngs.size(); j++) {
                        LatLng latLng = latLngs.get(j);
                        LatLng latLng_pre = latLngs.get(j - 1);
                        g_n += Math.sqrt((latLng.latitude - latLng_pre.latitude) * (latLng.latitude - latLng_pre.latitude)
                                + (latLng.longitude - latLng_pre.longitude) * (latLng.longitude - latLng_pre.longitude));
                    }
                    //求 h(n)
                    number = number / 10;
                    if (number == 0) {
                        number = 1;
                    }
                    h_n = number * Math.sqrt((mLatitude1 - mLatitude2) * (mLatitude1 - mLatitude2)
                            + (mLongitude1 - mLongitude2) * (mLongitude1 - mLongitude2));
                    //得 f(n)
                    f_n[i] = g_n + h_n;
                    Log.d("chenyisheng...", String.valueOf(f_n[i]));
                    break;
                case 1:     //求出到象眼岩的代价
                    number = numbers[i];
                    //求 g(n)
                    latLngs.clear();
                    RoadPlanningUtils.zxToTown_road2(latLngs);
                    for (int j = 1; j < latLngs.size(); j++) {
                        LatLng latLng = latLngs.get(j);
                        LatLng latLng_pre = latLngs.get(j - 1);
                        g_n += Math.sqrt((latLng.latitude - latLng_pre.latitude) * (latLng.latitude - latLng_pre.latitude)
                                + (latLng.longitude - latLng_pre.longitude) * (latLng.longitude - latLng_pre.longitude));
                    }
                    //求 h(n)
                    number = number / 10;
                    if (number == 0) {
                        number = 1;
                    }
                    h_n = number * Math.sqrt((mLatitude1 - mLatitude2) * (mLatitude1 - mLatitude2)
                            + (mLongitude1 - mLongitude2) * (mLongitude1 - mLongitude2));
                    //得 f(n)
                    f_n[i] = g_n + h_n;
                    Log.d("chenyisheng...", String.valueOf(f_n[i]));
                    break;
                case 2:     //求出到桂林抗战遗址的代价
                    number = numbers[i];
                    //求 g(n)
                    latLngs.clear();
                    RoadPlanningUtils.zxToTown_road3(latLngs);
                    for (int j = 1; j < latLngs.size(); j++) {
                        LatLng latLng = latLngs.get(j);
                        LatLng latLng_pre = latLngs.get(j - 1);
                        g_n += Math.sqrt((latLng.latitude - latLng_pre.latitude) * (latLng.latitude - latLng_pre.latitude)
                                + (latLng.longitude - latLng_pre.longitude) * (latLng.longitude - latLng_pre.longitude));
                    }
                    //求 h(n)
                    number = number / 10;
                    if (number == 0) {
                        number = 1;
                    }
                    h_n = number * Math.sqrt((mLatitude1 - mLatitude2) * (mLatitude1 - mLatitude2)
                            + (mLongitude1 - mLongitude2) * (mLongitude1 - mLongitude2));
                    //得 f(n)
                    f_n[i] = g_n + h_n;
                    Log.d("chenyisheng...", String.valueOf(f_n[i]));
                    break;
            }
        }
        double temp = f_n[0];
        int minFlag = 0;
        for (int i = 0; i < f_n.length; i++) {
            if (f_n[i] < temp) {
                temp = f_n[i];
                minFlag = i;
            }
        }
        return minFlag;
    }
    /**
     * 醉乡-->象眼岩
     */
    private static int zxToRock(int[] numbers, double[] f_n, ArrayList<LatLng> latLngs) {
        double g_n;
        int number;
        double h_n;
        for (int i = 0; i < 3; i++) {
            g_n = 0;
            switch (i) {
                case 0:     //求出到普贤塔的代价
                    number = numbers[i];
                    //求 g(n)
                    latLngs.clear();
                    RoadPlanningUtils.zxToRock_road1(latLngs);
                    for (int j = 1; j < latLngs.size(); j++) {
                        LatLng latLng = latLngs.get(j);
                        LatLng latLng_pre = latLngs.get(j - 1);
                        g_n += Math.sqrt((latLng.latitude - latLng_pre.latitude) * (latLng.latitude - latLng_pre.latitude)
                                + (latLng.longitude - latLng_pre.longitude) * (latLng.longitude - latLng_pre.longitude));
                    }
                    //求 h(n)
                    number = number / 10;
                    if (number == 0) {
                        number = 1;
                    }
                    h_n = number * Math.sqrt((mLatitude1 - mLatitude2) * (mLatitude1 - mLatitude2)
                            + (mLongitude1 - mLongitude2) * (mLongitude1 - mLongitude2));
                    //得 f(n)
                    f_n[i] = g_n + h_n;
                    break;
                case 1:     //求出到象眼岩的代价
                    number = numbers[i];
                    //求 g(n)
                    latLngs.clear();
                    RoadPlanningUtils.zxToRock_road2(latLngs);
                    for (int j = 1; j < latLngs.size(); j++) {
                        LatLng latLng = latLngs.get(j);
                        LatLng latLng_pre = latLngs.get(j - 1);
                        g_n += Math.sqrt((latLng.latitude - latLng_pre.latitude) * (latLng.latitude - latLng_pre.latitude)
                                + (latLng.longitude - latLng_pre.longitude) * (latLng.longitude - latLng_pre.longitude));
                    }
                    //求 h(n)
                    number = number / 10;
                    if (number == 0) {
                        number = 1;
                    }
                    h_n = number * Math.sqrt((mLatitude1 - mLatitude2) * (mLatitude1 - mLatitude2)
                            + (mLongitude1 - mLongitude2) * (mLongitude1 - mLongitude2));
                    //得 f(n)
                    f_n[i] = g_n + h_n;
                    break;
                case 2:     //求出到桂林抗战遗址的代价
                    number = numbers[i];
                    //求 g(n)
                    latLngs.clear();
                    RoadPlanningUtils.zxToRock_road3(latLngs);
                    for (int j = 1; j < latLngs.size(); j++) {
                        LatLng latLng = latLngs.get(j);
                        LatLng latLng_pre = latLngs.get(j - 1);
                        g_n += Math.sqrt((latLng.latitude - latLng_pre.latitude) * (latLng.latitude - latLng_pre.latitude)
                                + (latLng.longitude - latLng_pre.longitude) * (latLng.longitude - latLng_pre.longitude));
                    }
                    //求 h(n)
                    number = number / 10;
                    if (number == 0) {
                        number = 1;
                    }
                    h_n = number * Math.sqrt((mLatitude1 - mLatitude2) * (mLatitude1 - mLatitude2)
                            + (mLongitude1 - mLongitude2) * (mLongitude1 - mLongitude2));
                    //得 f(n)
                    f_n[i] = g_n + h_n;
                    break;
            }
        }
        double temp = f_n[0];
        int minFlag = 0;
        for (int i = 0; i < f_n.length; i++) {
            if (f_n[i] < temp) {
                temp = f_n[i];
                minFlag = i;
            }
        }
        return minFlag;
    }
}
