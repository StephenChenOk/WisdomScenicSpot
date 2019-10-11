package com.chen.fy.wisdomscenicspot.utils;

import android.util.Log;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * 路径规划类
 */
public class RoadPlanningUtils {

    //1 醉乡-->普贤塔
    public static final int ROAD_1 = 1;     // 途经普贤塔
    public static final int ROAD_2 = 2;     // 途经象眼岩
    public static final int ROAD_3 = 3;     // 途经桂林抗战遗址

    //2 醉乡-->象眼岩
    public static final int ROAD_4 = 4;     // 途经普贤塔
    public static final int ROAD_5 = 5;     // 途经象眼岩
    public static final int ROAD_6 = 6;     // 途经桂林抗战遗址

    //3 醉乡-->桂林抗战遗址
    public static final int ROAD_7 = 7;     //途经桂林抗战遗址

    //4 云峰寺-->普贤塔
    public static final int ROAD_8 = 8;     //途经普贤塔
    public static final int ROAD_9 = 9;     //途经桂林抗战遗址

    //5 云峰寺-->象眼岩
    public static final int ROAD_10 = 10;     // 途经普贤塔
    public static final int ROAD_11 = 11;     // 途经象眼岩
    public static final int ROAD_12 = 12;     // 途经桂林抗战遗址

    //6 云峰寺-->桂林抗战遗址
    public static final int ROAD_13 = 13;     //途经桂林抗战遗址

    //7 象山钟韵-->普贤塔
    public static final int ROAD_14 = 14;     //途经桂林抗战遗址

    //8 象山钟韵-->象眼岩
    public static final int ROAD_15 = 15;     // 途经普贤塔
    public static final int ROAD_16 = 16;     // 途经象眼岩
    public static final int ROAD_17 = 17;     // 途经桂林抗战遗址

    //9 象山钟韵-->桂林抗战遗址
    public static final int ROAD_18 = 18;     //途经普贤塔
    public static final int ROAD_19 = 19;     //途经桂林抗战遗址

    /**
     * 1 醉乡-->普贤塔  ok
     */
    //途经普贤塔
    public static void zxToTown_road1(List<LatLng> latLngs) {
        //                             增加往上       增加往右
        LatLng latLng0 = new LatLng(25.266416, 110.295621);       //醉乡
        LatLng latLng1 = new LatLng(25.266453, 110.295620);       //地标1
        LatLng latLng2 = new LatLng(25.266413, 110.295356);       //地标2
        LatLng latLng3 = new LatLng(25.266510, 110.295420);       //地标3
        LatLng latLng4 = new LatLng(25.266820, 110.295275);       //地标3
        LatLng latLng5 = new LatLng(25.266950, 110.295231);       //地标4
        LatLng latLng6 = new LatLng(25.267153, 110.295631);       //地标5
        LatLng latLng7 = new LatLng(25.267168, 110.295691);       //地标6
        LatLng latLng8 = new LatLng(25.267109, 110.295895);       //地标7
        LatLng latLng9 = new LatLng(25.267157, 110.296042);       //地标8
        LatLng latLng10 = new LatLng(25.267242, 110.296046);      //普贤塔

        latLngs.add(latLng0);
        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
    }
    //途经普贤塔, 导航用
    public static void zxToTown_road1_dh(List<LatLng> latLngs) {
        //                             增加往上       增加往右
        LatLng latLng0 = new LatLng(25.266416, 110.295621);      //醉乡
        LatLng latLng1 = new LatLng(25.266453, 110.295620);      //地标1
        LatLng latLng2 = new LatLng(25.266400, 110.295345);      //地标2
        LatLng latLng2_ = new LatLng(25.266410, 110.295348);      //地标2
        LatLng latLng3 = new LatLng(25.266490, 110.295430);      //地标3
        LatLng latLng4 = new LatLng(25.266570, 110.295390);      //地标3
        LatLng latLng4_ = new LatLng(25.266580, 110.295370);      //地标3
        LatLng latLng5 = new LatLng(25.266700, 110.295320);      //地标3
        LatLng latLng6 = new LatLng(25.266820, 110.295284);      //地标3
        LatLng latLng6_ = new LatLng(25.266840, 110.295274);      //地标3
        LatLng latLng7 = new LatLng(25.266963, 110.295227);      //地标4
        LatLng latLng8 = new LatLng(25.267153, 110.295631);      //地标5
        LatLng latLng8_ = new LatLng(25.267166, 110.295637);      //地标5
        LatLng latLng9 = new LatLng(25.267164, 110.295690);      //地标6
        LatLng latLng10 = new LatLng(25.267107, 110.295890);      //地标7
        LatLng latLng10_ = new LatLng(25.267117, 110.295899);      //地标7
        LatLng latLng11 = new LatLng(25.267166, 110.296050);       //地标8
        LatLng latLng11_ = new LatLng(25.267160, 110.296055);       //地标8
        LatLng latLng12 = new LatLng(25.267220, 110.296040);      //地标10
        LatLng latLng12_ = new LatLng(25.267230, 110.296043);      //地标10

        latLngs.add(latLng0);
        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng2_);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng4_);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng6_);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng8_);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
        latLngs.add(latLng10_);
        latLngs.add(latLng11);
        latLngs.add(latLng11_);
        latLngs.add(latLng12);
        latLngs.add(latLng12_);
    }
    //途经象眼岩
    public static void zxToTown_road2(List<LatLng> latLngs) {
        //                            增加往上       增加往右
        LatLng latLng1 = new LatLng(25.266416, 110.295621);      //醉乡
        LatLng latLng2 = new LatLng(25.266370, 110.295597);      //地标2
        LatLng latLng3 = new LatLng(25.266387, 110.296083);      //地标3
        LatLng latLng4 = new LatLng(25.266425, 110.296220);      //地标4
        LatLng latLng5 = new LatLng(25.266415, 110.296490);      //地标5
        LatLng latLng6 = new LatLng(25.266460, 110.296535);      //地标6
        LatLng latLng7 = new LatLng(25.266793, 110.296583);      //地标7
        LatLng latLng8 = new LatLng(25.267015, 110.296490);      //地标8
        LatLng latLng9 = new LatLng(25.267215, 110.296620);      //地标9
        LatLng latLng10 = new LatLng(25.267370, 110.296630);     //地标10
        LatLng latLng11 = new LatLng(25.267300, 110.296520);     //地标11
        LatLng latLng12 = new LatLng(25.267240, 110.296480);     //地标12
        LatLng latLng13 = new LatLng(25.267160, 110.296300);     //地标13
        LatLng latLng14 = new LatLng(25.267157, 110.296042);     //地标14
        LatLng latLng15 = new LatLng(25.267242, 110.296046);     //普贤塔

        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
        latLngs.add(latLng11);
        latLngs.add(latLng12);
        latLngs.add(latLng13);
        latLngs.add(latLng14);
        latLngs.add(latLng15);
    }
    //途经桂林抗战遗址
    public static void zxToTown_road3(List<LatLng> latLngs) {
        //                             增加往上       增加往右
        LatLng latLng1 = new LatLng(25.266416, 110.295621);         //醉乡
        LatLng latLng2 = new LatLng(25.266453, 110.295620);          //地标2
        LatLng latLng3 = new LatLng(25.266440, 110.295681);          //地标3
        LatLng latLng4 = new LatLng(25.266463, 110.295721);          //地标4
        LatLng latLng5 = new LatLng(25.266735, 110.295805);         //地标5
        LatLng latLng6 = new LatLng(25.266745, 110.295845);         //地标6
        LatLng latLng7 = new LatLng(25.266745, 110.296030);         //地标6
        LatLng latLng8 = new LatLng(25.266930, 110.296095);         //地标7
        LatLng latLng9 = new LatLng(25.267160, 110.296300);         //地标8
        LatLng latLng10 = new LatLng(25.267157, 110.296042);        //地标9
        LatLng latLng11 = new LatLng(25.267242, 110.296046);        //普贤塔

        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
        latLngs.add(latLng11);
    }

    /**
     * 2 醉乡-->象眼岩  ok
     */
    //途经普贤塔
    public static void zxToRock_road1(List<LatLng> latLngs) {
        //                             增加往上       增加往右
        LatLng latLng0 = new LatLng(25.266416, 110.295621);      //醉乡
        LatLng latLng1 = new LatLng(25.266453, 110.295620);      //地标1
        LatLng latLng2 = new LatLng(25.266413, 110.295356);      //地标2  （转角）
        LatLng latLng3 = new LatLng(25.266510, 110.295420);      //地标3  （转角）
        LatLng latLng4 = new LatLng(25.266820, 110.295275);      //地标3
        LatLng latLng5 = new LatLng(25.266950, 110.295231);      //地标4  （转角）
        LatLng latLng6 = new LatLng(25.267153, 110.295631);      //地标5
        LatLng latLng7 = new LatLng(25.267168, 110.295691);      //地标6  （转角）
        LatLng latLng8 = new LatLng(25.267109, 110.295895);      //地标7
        LatLng latLng9 = new LatLng(25.267157, 110.296042);      //地标8  （转角）
        LatLng latLng10 = new LatLng(25.267160, 110.296300);     //地标9   （遗址转角处）
        LatLng latLng11 = new LatLng(25.267200, 110.296380);      //地标10
        LatLng latLng12 = new LatLng(25.267088, 110.296427);      //象眼岩

        latLngs.add(latLng0);
        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
        latLngs.add(latLng11);
        latLngs.add(latLng12);
    }
    //途经象眼岩
    public static void zxToRock_road2(List<LatLng> latLngs) {
        //                            增加往上       增加往右
        LatLng latLng1 = new LatLng(25.266416, 110.295621);      //醉乡
        LatLng latLng2 = new LatLng(25.266370, 110.295597);      //地标1
        LatLng latLng3 = new LatLng(25.266387, 110.296083);      //地标2
        LatLng latLng4 = new LatLng(25.266425, 110.296220);      //地标3
        LatLng latLng5 = new LatLng(25.266415, 110.296490);      //地标4
        LatLng latLng6 = new LatLng(25.266460, 110.296535);      //地标5  （转角）
        LatLng latLng7 = new LatLng(25.266793, 110.296583);      //地标6
        LatLng latLng8 = new LatLng(25.266935, 110.296500);      //地标7
        LatLng latLng9 = new LatLng(25.267015, 110.296490);      //地标8
        LatLng latLng10 = new LatLng(25.267088, 110.296427);     //象眼岩

        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
    }
    //途经桂林抗战遗址
    public static void zxToRock_road3(List<LatLng> latLngs) {
        //                             增加往上       增加往右
        LatLng latLng1 = new LatLng(25.266416, 110.295621);         //醉乡
        LatLng latLng2 = new LatLng(25.266453, 110.295620);         //地标1
        LatLng latLng3 = new LatLng(25.266440, 110.295681);         //地标2
        LatLng latLng4 = new LatLng(25.266463, 110.295721);         //地标3
        LatLng latLng5 = new LatLng(25.266735, 110.295805);         //地标4
        LatLng latLng6 = new LatLng(25.266745, 110.295845);         //地标5
        LatLng latLng7 = new LatLng(25.266745, 110.296030);         //地标6
        LatLng latLng8 = new LatLng(25.266930, 110.296095);         //地标7
        LatLng latLng9 = new LatLng(25.267160, 110.296300);         //地标8
        LatLng latLng10 = new LatLng(25.267200, 110.296380);        //地标9
        LatLng latLng11 = new LatLng(25.267088, 110.296427);        //象眼岩

        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
        latLngs.add(latLng11);
    }

    /**
     * 3 醉乡-->桂林抗战遗址  ok
     */
    //途经桂林抗战遗址
    public static void zxToRuins_road1(List<LatLng> latLngs) {
        //                             增加往上       增加往右
        LatLng latLng1 = new LatLng(25.266416, 110.295621);         //醉乡
        LatLng latLng2 = new LatLng(25.266453, 110.295620);         //地标1
        LatLng latLng3 = new LatLng(25.266440, 110.295681);         //地标2
        LatLng latLng4 = new LatLng(25.266463, 110.295721);         //地标3 （转角）
        LatLng latLng5 = new LatLng(25.266735, 110.295805);         //地标4
        LatLng latLng6 = new LatLng(25.266745, 110.295845);         //地标5  (转角)
        LatLng latLng7 = new LatLng(25.266745, 110.295960);         //地标6
        LatLng latLng8 = new LatLng(25.266798, 110.295988);         //桂林抗战遗址

        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
    }

    /**
     * 4 云峰寺-->普贤塔 ok
     */
    //途经普贤塔
    public static void yfsToTown_road1(List<LatLng> latLngs) {
        //                             增加往上       增加往右
        LatLng latLng0 = new LatLng(25.266559, 110.295011);      //云峰寺
        LatLng latLng1 = new LatLng(25.266509, 110.295081);      //地标1
        LatLng latLng2 = new LatLng(25.266820, 110.295275);      //地标2
        LatLng latLng3 = new LatLng(25.266950, 110.295231);      //地标3
        LatLng latLng4 = new LatLng(25.267153, 110.295631);      //地标4
        LatLng latLng5 = new LatLng(25.267168, 110.295691);      //地标5
        LatLng latLng6 = new LatLng(25.267109, 110.295895);      //地标6
        LatLng latLng7 = new LatLng(25.267157, 110.296042);      //地标7
        LatLng latLng8 = new LatLng(25.267242, 110.296046);      //普贤塔


        latLngs.add(latLng0);
        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
    }
    //途经桂林抗战遗址
    public static void yfsToTown_road3(List<LatLng> latLngs) {

        //                             增加往上       增加往右
        LatLng latLng0 = new LatLng(25.266559, 110.295011);      //云峰寺
        LatLng latLng1 = new LatLng(25.266620, 110.295031);      //地标1
        LatLng latLng2 = new LatLng(25.266565, 110.295145);      //地标2
        LatLng latLng3 = new LatLng(25.266400, 110.295371);      //地标3
        LatLng latLng4 = new LatLng(25.266453, 110.295620);      //地标4
        LatLng latLng5 = new LatLng(25.266440, 110.295681);      //地标5
        LatLng latLng6 = new LatLng(25.266463, 110.295721);      //地标6
        LatLng latLng7 = new LatLng(25.266735, 110.295805);      //地标7
        LatLng latLng8 = new LatLng(25.266745, 110.295845);      //地标8
        LatLng latLng9 = new LatLng(25.266745, 110.296030);      //地标9
        LatLng latLng10 = new LatLng(25.266930, 110.296095);     //地标10
        LatLng latLng11 = new LatLng(25.267160, 110.296300);     //地标11
        LatLng latLng12 = new LatLng(25.267157, 110.296042);     //地标12
        LatLng latLng13 = new LatLng(25.267242, 110.296046);     //普贤塔

        latLngs.add(latLng0);
        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
        latLngs.add(latLng11);
        latLngs.add(latLng12);
        latLngs.add(latLng13);

    }

    /**
     * 5 云峰寺-->象眼岩  ok
     */
    //途经普贤塔
    public static void yfsToRock_road1(List<LatLng> latLngs) {
        //                             增加往上       增加往右
        LatLng latLng0 = new LatLng(25.266559, 110.295011);      //云峰寺
        LatLng latLng1 = new LatLng(25.266509, 110.295081);      //地标1
        LatLng latLng2 = new LatLng(25.266820, 110.295275);      //地标3
        LatLng latLng3 = new LatLng(25.266950, 110.295231);      //地标4
        LatLng latLng4 = new LatLng(25.267153, 110.295631);      //地标5
        LatLng latLng5 = new LatLng(25.267168, 110.295691);      //地标6
        LatLng latLng6 = new LatLng(25.267109, 110.295895);      //地标7
        LatLng latLng7 = new LatLng(25.267157, 110.296042);      //地标8
        LatLng latLng8 = new LatLng(25.267160, 110.296300);      //地标9   （遗址转角处）
        LatLng latLng9 = new LatLng(25.267200, 110.296380);      //地标10
        LatLng latLng10 = new LatLng(25.267088, 110.296427);     //象眼岩



        latLngs.add(latLng0);
        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
    }
    //途经象眼岩
    public static void yfsToRock_road2(List<LatLng> latLngs) {
        //                             增加往上       增加往右
        LatLng latLng0 = new LatLng(25.266559, 110.295011);      //云峰寺
        LatLng latLng1 = new LatLng(25.266620, 110.295031);      //地标1
        LatLng latLng2 = new LatLng(25.266565, 110.295145);      //地标2
        LatLng latLng3 = new LatLng(25.266400, 110.295371);      //地标3
        LatLng latLng4 = new LatLng(25.266365, 110.295481);      //地标3
        LatLng latLng5 = new LatLng(25.266370, 110.295597);      //地标1
        LatLng latLng6 = new LatLng(25.266387, 110.296083);      //地标2
        LatLng latLng7 = new LatLng(25.266425, 110.296220);      //地标3
        LatLng latLng8 = new LatLng(25.266415, 110.296490);      //地标4
        LatLng latLng9 = new LatLng(25.266460, 110.296535);      //地标5  （转角）
        LatLng latLng10 = new LatLng(25.266793, 110.296583);     //地标6
        LatLng latLng11 = new LatLng(25.266935, 110.296500);     //地标7
        LatLng latLng12 = new LatLng(25.267015, 110.296490);     //地标8
        LatLng latLng13 = new LatLng(25.267088, 110.296427);     //象眼岩

        latLngs.add(latLng0);
        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
        latLngs.add(latLng11);
        latLngs.add(latLng12);
        latLngs.add(latLng13);
    }
    //途经桂林抗战遗址
    public static void yfsToRock_road3(List<LatLng> latLngs) {
        //                             增加往上       增加往右
        LatLng latLng0 = new LatLng(25.266559, 110.295011);      //云峰寺
        LatLng latLng1 = new LatLng(25.266620, 110.295031);      //地标1
        LatLng latLng2 = new LatLng(25.266565, 110.295145);      //地标2
        LatLng latLng3 = new LatLng(25.266400, 110.295371);      //地标3
        LatLng latLng4 = new LatLng(25.266453, 110.295620);      //地标4
        LatLng latLng5 = new LatLng(25.266440, 110.295681);      //地标5
        LatLng latLng6 = new LatLng(25.266463, 110.295721);      //地标6
        LatLng latLng7 = new LatLng(25.266735, 110.295805);      //地标7
        LatLng latLng8 = new LatLng(25.266745, 110.295845);      //地标8
        LatLng latLng9 = new LatLng(25.266745, 110.296030);      //地标9
        LatLng latLng10 = new LatLng(25.266930, 110.296095);     //地标10
        LatLng latLng11 = new LatLng(25.267160, 110.296300);     //地标11
        LatLng latLng12 = new LatLng(25.267200, 110.296380);     //地标12
        LatLng latLng13 = new LatLng(25.267088, 110.296427);     //象眼岩

        latLngs.add(latLng0);
        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
        latLngs.add(latLng11);
        latLngs.add(latLng12);
        latLngs.add(latLng13);
    }

    /**
     * 6 云峰寺-->桂林抗战遗址  ok
     */
    //途经桂林抗战遗址
    public static void yfsToRuins_road1(List<LatLng> latLngs) {
        //                             增加往上       增加往右
        LatLng latLng0 = new LatLng(25.266559, 110.295011);      //云峰寺
        LatLng latLng1 = new LatLng(25.266620, 110.295031);      //地标1
        LatLng latLng2 = new LatLng(25.266565, 110.295145);      //地标2
        LatLng latLng3 = new LatLng(25.266400, 110.295371);      //地标3
        LatLng latLng4 = new LatLng(25.266453, 110.295620);      //地标1
        LatLng latLng5 = new LatLng(25.266440, 110.295681);      //地标2
        LatLng latLng6 = new LatLng(25.266463, 110.295721);      //地标3 （转角）
        LatLng latLng7 = new LatLng(25.266735, 110.295805);      //地标4
        LatLng latLng8 = new LatLng(25.266745, 110.295845);      //地标5  (转角)
        LatLng latLng9 = new LatLng(25.266745, 110.295960);      //地标6
        LatLng latLng10 = new LatLng(25.266798, 110.295988);     //桂林抗战遗址

        latLngs.add(latLng0);
        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
    }

    /**
     * 7 象山钟韵-->普贤塔 ok
     */
    //途经普贤塔
    public static void zyToTown_road1(List<LatLng> latLngs) {
        //                             增加往上       增加往右
        LatLng latLng0 = new LatLng(25.267466, 110.295334);      //象山钟韵
        LatLng latLng1 = new LatLng(25.267438, 110.295334);      //地标1
        LatLng latLng2 = new LatLng(25.267435, 110.295484);      //地标2
        LatLng latLng3 = new LatLng(25.267450, 110.295660);      //地标3
        LatLng latLng4 = new LatLng(25.267410, 110.295690);      //地标4
        LatLng latLng5 = new LatLng(25.267230, 110.295350);      //地标5
        LatLng latLng6 = new LatLng(25.267085, 110.295233);      //地标6
        LatLng latLng7 = new LatLng(25.266950, 110.295231);      //地标7      （拐角处，拐普贤塔）
        LatLng latLng8 = new LatLng(25.267153, 110.295631);      //地标8
        LatLng latLng9 = new LatLng(25.267168, 110.295691);      //地标9
        LatLng latLng10 = new LatLng(25.267109, 110.295895);     //地标10
        LatLng latLng11 = new LatLng(25.267157, 110.296042);     //地标11
        LatLng latLng12 = new LatLng(25.267242, 110.296046);     //普贤塔


        latLngs.add(latLng0);
        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
        latLngs.add(latLng11);
        latLngs.add(latLng12);
    }

    /**
     * 8 象山钟韵-->象眼岩  ok
     */
    //途经普贤塔
    public static void zyToRock_road1(List<LatLng> latLngs) {
        //                             增加往上       增加往右
        LatLng latLng0 = new LatLng(25.267466, 110.295334);      //象山钟韵
        LatLng latLng1 = new LatLng(25.267438, 110.295334);      //地标1
        LatLng latLng2 = new LatLng(25.267435, 110.295484);      //地标2
        LatLng latLng3 = new LatLng(25.267450, 110.295660);      //地标3
        LatLng latLng4 = new LatLng(25.267410, 110.295690);      //地标4
        LatLng latLng5 = new LatLng(25.267230, 110.295350);      //地标5
        LatLng latLng6 = new LatLng(25.267085, 110.295233);      //地标6
        LatLng latLng7 = new LatLng(25.266950, 110.295231);      //地标7    （拐角处，拐普贤塔）
        LatLng latLng8 = new LatLng(25.267153, 110.295631);      //地标8
        LatLng latLng9 = new LatLng(25.267168, 110.295691);      //地标9
        LatLng latLng10 = new LatLng(25.267109, 110.295895);     //地标10
        LatLng latLng11 = new LatLng(25.267157, 110.296042);     //地标11
        LatLng latLng12 = new LatLng(25.267160, 110.296300);      //地标9   （遗址转角处）
        LatLng latLng13 = new LatLng(25.267200, 110.296380);      //地标10
        LatLng latLng14 = new LatLng(25.267088, 110.296427);     //象眼岩

        latLngs.add(latLng0);
        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
        latLngs.add(latLng11);
        latLngs.add(latLng12);
        latLngs.add(latLng13);
        latLngs.add(latLng14);
    }
    //途经象眼岩
    public static void zyToRock_road2(List<LatLng> latLngs) {
        //                             增加往上       增加往右
        LatLng latLng0 = new LatLng(25.267466, 110.295334);      //象山钟韵
        LatLng latLng1 = new LatLng(25.267438, 110.295334);      //地标1
        LatLng latLng2 = new LatLng(25.267435, 110.295484);      //地标2
        LatLng latLng3 = new LatLng(25.267450, 110.295660);      //地标3
        LatLng latLng4 = new LatLng(25.267410, 110.295690);      //地标4
        LatLng latLng5 = new LatLng(25.267230, 110.295350);      //地标5
        LatLng latLng6 = new LatLng(25.267085, 110.295233);      //地标6
        LatLng latLng7 = new LatLng(25.266950, 110.295231);      //地标7    （拐角处，拐普贤塔）
        LatLng latLng8 = new LatLng(25.266820, 110.295275);      //地标8
        LatLng latLng9 = new LatLng(25.266510, 110.295420);      //地标9
        LatLng latLng10 = new LatLng(25.266413, 110.295356);     //地标10     (转角，转遗址)
        LatLng latLng11 = new LatLng(25.266365, 110.295481);      //地标3
        LatLng latLng12 = new LatLng(25.266370, 110.295597);      //地标1
        LatLng latLng13 = new LatLng(25.266387, 110.296083);      //地标2
        LatLng latLng14 = new LatLng(25.266425, 110.296220);      //地标3
        LatLng latLng15 = new LatLng(25.266415, 110.296490);      //地标4
        LatLng latLng16 = new LatLng(25.266460, 110.296535);      //地标5  （转角）
        LatLng latLng17 = new LatLng(25.266793, 110.296583);     //地标6
        LatLng latLng18 = new LatLng(25.266935, 110.296500);     //地标7
        LatLng latLng19 = new LatLng(25.267015, 110.296490);     //地标8
        LatLng latLng20 = new LatLng(25.267088, 110.296427);     //象眼岩

        latLngs.add(latLng0);
        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
        latLngs.add(latLng11);
        latLngs.add(latLng12);
        latLngs.add(latLng13);
        latLngs.add(latLng14);
        latLngs.add(latLng15);
        latLngs.add(latLng16);
        latLngs.add(latLng17);
        latLngs.add(latLng18);
        latLngs.add(latLng19);
        latLngs.add(latLng20);
    }
    //途经桂林抗战遗址
    public static void zyToRock_road3(List<LatLng> latLngs) {
        //                             增加往上       增加往右
        LatLng latLng0 = new LatLng(25.267466, 110.295334);      //象山钟韵
        LatLng latLng1 = new LatLng(25.267438, 110.295334);      //地标1
        LatLng latLng2 = new LatLng(25.267435, 110.295484);      //地标2
        LatLng latLng3 = new LatLng(25.267450, 110.295660);      //地标3
        LatLng latLng4 = new LatLng(25.267410, 110.295690);      //地标4
        LatLng latLng5 = new LatLng(25.267230, 110.295350);      //地标5
        LatLng latLng6 = new LatLng(25.267085, 110.295233);      //地标6
        LatLng latLng7 = new LatLng(25.266950, 110.295231);      //地标7    （拐角处，拐普贤塔）
        LatLng latLng8 = new LatLng(25.266820, 110.295275);      //地标8
        LatLng latLng9 = new LatLng(25.266510, 110.295420);      //地标9
        LatLng latLng10 = new LatLng(25.266413, 110.295356);     //地标10     (转角，转遗址)
        LatLng latLng11 = new LatLng(25.266453, 110.295620);     //地标11
        LatLng latLng12 = new LatLng(25.266440, 110.295681);     //地标12
        LatLng latLng13 = new LatLng(25.266463, 110.295721);     //地标13
        LatLng latLng14 = new LatLng(25.266735, 110.295805);     //地标14
        LatLng latLng15 = new LatLng(25.266745, 110.295845);     //地标15
        LatLng latLng16 = new LatLng(25.266745, 110.296030);     //地标16
        LatLng latLng17 = new LatLng(25.266930, 110.296095);     //地标17
        LatLng latLng18 = new LatLng(25.267160, 110.296300);     //地标18
        LatLng latLng19 = new LatLng(25.267200, 110.296380);     //地标19
        LatLng latLng20 = new LatLng(25.267088, 110.296427);     //象眼岩

        latLngs.add(latLng0);
        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
        latLngs.add(latLng11);
        latLngs.add(latLng12);
        latLngs.add(latLng13);
        latLngs.add(latLng14);
        latLngs.add(latLng15);
        latLngs.add(latLng16);
        latLngs.add(latLng17);
        latLngs.add(latLng18);
        latLngs.add(latLng19);
        latLngs.add(latLng20);
    }

    /**
     * 9 象山钟韵-->桂林抗战遗址  ok
     */
    //途经普贤塔
    public static void zyToRuins_road1(List<LatLng> latLngs) {
        //                             增加往上       增加往右
        LatLng latLng0 = new LatLng(25.267466, 110.295334);      //象山钟韵
        LatLng latLng1 = new LatLng(25.267438, 110.295334);      //地标1
        LatLng latLng2 = new LatLng(25.267435, 110.295484);      //地标2
        LatLng latLng3 = new LatLng(25.267450, 110.295660);      //地标3
        LatLng latLng4 = new LatLng(25.267410, 110.295690);      //地标4
        LatLng latLng5 = new LatLng(25.267230, 110.295350);      //地标5
        LatLng latLng6 = new LatLng(25.267085, 110.295233);      //地标6
        LatLng latLng7 = new LatLng(25.266950, 110.295231);      //地标7      （拐角处，拐普贤塔）
        LatLng latLng8 = new LatLng(25.267153, 110.295631);      //地标8
        LatLng latLng9 = new LatLng(25.267168, 110.295691);      //地标9
        LatLng latLng10 = new LatLng(25.267109, 110.295895);     //地标10
        LatLng latLng11 = new LatLng(25.267157, 110.296042);     //地标11
        LatLng latLng12 = new LatLng(25.267160, 110.296300);     //地标12   （遗址转角处）
        LatLng latLng13 = new LatLng(25.266930, 110.296095);     //地标13
        LatLng latLng14 = new LatLng(25.266745, 110.296030);     //地标14
        LatLng latLng15 = new LatLng(25.266745, 110.295980);     //地标15
        LatLng latLng16 = new LatLng(25.266798, 110.295988);     //桂林抗战遗址

        latLngs.add(latLng0);
        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
        latLngs.add(latLng11);
        latLngs.add(latLng12);
        latLngs.add(latLng13);
        latLngs.add(latLng14);
        latLngs.add(latLng15);
        latLngs.add(latLng16);
    }
    //途经桂林抗战遗址
    public static void zyToRuins_road3(List<LatLng> latLngs) {
        //                             增加往上       增加往右
        LatLng latLng0 = new LatLng(25.267466, 110.295334);      //象山钟韵
        LatLng latLng1 = new LatLng(25.267438, 110.295334);      //地标1
        LatLng latLng2 = new LatLng(25.267435, 110.295484);      //地标2
        LatLng latLng3 = new LatLng(25.267450, 110.295660);      //地标3
        LatLng latLng4 = new LatLng(25.267410, 110.295690);      //地标4
        LatLng latLng5 = new LatLng(25.267230, 110.295350);      //地标5
        LatLng latLng6 = new LatLng(25.267085, 110.295233);      //地标6
        LatLng latLng7 = new LatLng(25.266950, 110.295231);      //地标7      （拐角处，拐普贤塔）
        LatLng latLng8 = new LatLng(25.266820, 110.295275);      //地标8
        LatLng latLng9 = new LatLng(25.266510, 110.295420);      //地标9
        LatLng latLng10 = new LatLng(25.266413, 110.295356);     //地标10     (转角，转遗址)
        LatLng latLng11 = new LatLng(25.266453, 110.295620);     //地标11
        LatLng latLng12 = new LatLng(25.266440, 110.295681);     //地标12
        LatLng latLng13 = new LatLng(25.266463, 110.295721);     //地标13
        LatLng latLng14 = new LatLng(25.266735, 110.295805);     //地标14
        LatLng latLng15 = new LatLng(25.266745, 110.295845);     //地标15
        LatLng latLng16 = new LatLng(25.266745, 110.295980);     //地标15
        LatLng latLng17 = new LatLng(25.266798, 110.295988);     //桂林抗战遗址

        latLngs.add(latLng0);
        latLngs.add(latLng1);
        latLngs.add(latLng2);
        latLngs.add(latLng3);
        latLngs.add(latLng4);
        latLngs.add(latLng5);
        latLngs.add(latLng6);
        latLngs.add(latLng7);
        latLngs.add(latLng8);
        latLngs.add(latLng9);
        latLngs.add(latLng10);
        latLngs.add(latLng11);
        latLngs.add(latLng12);
        latLngs.add(latLng13);
        latLngs.add(latLng14);
        latLngs.add(latLng15);
        latLngs.add(latLng16);
        latLngs.add(latLng17);
    }
}