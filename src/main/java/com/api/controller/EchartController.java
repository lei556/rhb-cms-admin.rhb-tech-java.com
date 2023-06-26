package com.api.controller;


import com.api.service.BaseStatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api")
@Api(tags = "报表接口")
public class EchartController {
    //list下标为类型
    public static List<String> searchType = new ArrayList<>();//存储
    public static List<String> countOrTimeType = new ArrayList<>();//存储s
    public static List<String> countOrTimeTypeDescription = new ArrayList<>();//存储
    public static Map<String, String> mapType = new HashMap<>();

    public static String WEEK = "7";//近一周(7天)
    public static String MONTH = "30";//近一月（30天）
    public static String HALF_YEAR = "180";//近半年（6月）
    public static String ONE_YEAR = "365";//近一年(12月)

    static {
        searchType.add(" ");
        mapType.put(" ", "总训练");
        searchType.add("and sport_mode=0");
        mapType.put("and sport_mode=0", "主动训练");
        searchType.add("and sport_mode=1");
        mapType.put("and sport_mode=1", "被动训练");
        searchType.add("and sport_mode=2");
        mapType.put("and sport_mode=2", "主被动训练");
        searchType.add("and sport_mode=3");
        mapType.put("and sport_mode=3", "助力训练");
        searchType.add("and sport_way=0");
        mapType.put("and sport_way=0", "垂直平行训练");
        searchType.add("and sport_way=1");
        mapType.put("and sport_way=1", "垂直交叉训练");
        searchType.add("and sport_way=2");
        mapType.put("and sport_way=2", "水平训练");
        searchType.add("and up_down=0");
        mapType.put("and up_down=0", "上肢训练");
        searchType.add("and up_down=1");
        mapType.put("and up_down=1", "下肢训练");

        countOrTimeType.add("count(1)");//人次
        countOrTimeType.add("sum(second_time)");//时长
        countOrTimeTypeDescription.add("人次");
        countOrTimeTypeDescription.add("时长");
    }

    @Autowired(required = false)
    BaseStatService baseStatService;


    @GetMapping("/getData")
    @ApiOperation(value = "查询统计数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchTypes", value = "(非必传)运动类型，当不传参数时默认查询全部模式类型\n" +
                    "多选时传递参数时用英文逗号隔开，例如 0,1,2,3\n" +
                    "0：总训练\n" +
                    "1：主动训练\n" +
                    "2：被动训练\n" +
                    "3：主被动训练\n" +
                    "4：助力训练\n" +
                    "5：垂直平行训练\n" +
                    "6：垂直交叉训练\n" +
                    "7：水平训练\n" +
                    "8：上肢训练\n" +
                    "9：下肢训练", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "timeType", value = "(必传)时段\n" +
                    "7：近七天\n" +
                    "30：近30天\n" +
                    "180：近半年\n" +
                    "365：近一年", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "countOrTime", value = "(必传)查询人次或时长\n" +
                    "0：人次\n" +
                    "1：时长", dataType = "String", paramType = "query", required = true)
    })
    public Object getData(String searchTypes, String timeType, String countOrTime) {
        List<String> stypes = new ArrayList<>();
        if (StringUtils.isEmpty(searchTypes)) {
            //空则全部类型
            stypes.addAll(searchType);
        } else {
            String[] split = searchTypes.split(",");
            for (String indexString : split) {
                int index = Integer.parseInt(indexString);
                stypes.add(searchType.get(index));
            }
        }
        if (WEEK.equals(timeType)) {
            return getWeek(stypes, countOrTime);
        }
        if (MONTH.equals(timeType)) {
            return getMonth(stypes, countOrTime);
        }
        if (HALF_YEAR.equals(timeType)) {
            return getHaldYear(stypes, countOrTime);
        }
        if (ONE_YEAR.equals(timeType)) {
            return getOneYear(stypes, countOrTime);
        }

        //去查询
        return null;
    }

    @GetMapping("/getSum")
    @ApiOperation(value = "查询统计数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "timeType", value = "(必传)时段\n" +
                    "7：近七天\n" +
                    "30：近30天\n" +
                    "180：近半年\n" +
                    "365：近一年", dataType = "String", paramType = "query", required = true)
    })
    public Object getSum(Integer timeType) {
        LocalDateTime localDateTime = LocalDateTime.now();
        String endDocTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String startDocTime = localDateTime.minusDays(timeType-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String  endTrainLogTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String startTrainLogTime = localDateTime.minusDays(timeType-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> sumMap = new LinkedHashMap<>();
        int patientCount = baseStatService.getPatientCountByTimeRange(startDocTime,endDocTime);
        int trianPatientCount = baseStatService.getTrianPatientCountByTimeRange(startTrainLogTime,endTrainLogTime);
        int trianAllCount = baseStatService.getTrianAllCountByTimeRange(startTrainLogTime,endTrainLogTime);
        int trianAllMinute = baseStatService.getTrianAllSecondByTimeRange(startTrainLogTime,endTrainLogTime)/60;

        sumMap.put("patientCount",patientCount);
        sumMap.put("trianPatientCount",trianPatientCount);
        sumMap.put("trianAllCount",trianAllCount);
        sumMap.put("trianAllMinute",trianAllMinute);


        //去查询
        return sumMap;
    }



    Object getWeek(List<String> stypes, String countOrTime) {
        return getByDays(stypes, countOrTime, 7);
    }

    Object getMonth(List<String> stypes, String countOrTime) {
        return getByDays(stypes, countOrTime, 30);
    }

    //
    Object getByDays(List<String> stypes, String countOrTime, int days) {
        Map<String, Object> remap = new LinkedHashMap<>();
        int countOrTimeInt = Integer.parseInt(countOrTime);
        List<String> dates = new ArrayList<>();

        for (int i = days - 1; i >= 0; i--) {
            dates.add(LocalDate.now().minusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        System.out.println(dates);
        Map<String, Object> remap1 = new LinkedHashMap<>();
        remap1.put("type", "category");
        remap1.put("data", dates);
        remap.put("xAxis", remap1);
        List<Map<String, Object>> series = new ArrayList<>();
        for (String stype : stypes) {
            //每个类型的时段数据
            List<Integer> reArray = new ArrayList<>();
            for (int i = 0; i < dates.size(); i++) {
                String result = baseStatService.getByDay(stype, dates.get(i), countOrTimeType.get(countOrTimeInt));
                if (countOrTimeInt == 0) {
                    reArray.add(Integer.valueOf(result));
                } else {
                    reArray.add(Integer.parseInt(result) / 60);//时长单位分钟
                }

            }
            System.out.println(mapType.get(stype) + countOrTimeTypeDescription.get(countOrTimeInt));
            System.out.println(reArray);
            Map<String, Object> tmpMap = new LinkedHashMap<>();
            tmpMap.put("name", mapType.get(stype) + countOrTimeTypeDescription.get(countOrTimeInt));
            tmpMap.put("data", reArray);
            series.add(tmpMap);
        }
        remap.put("series", series);
        return remap;
    }

    Object getHaldYear(List<String> stypes, String countOrTime) {
        return getByMonths(stypes, countOrTime, 6);
    }

    Object getOneYear(List<String> stypes, String countOrTime) {
        return getByMonths(stypes, countOrTime, 12);
    }

    Object getByMonths(List<String> stypes, String countOrTime, int mos) {
        Map<String, Object> remap = new LinkedHashMap<>();
        int countOrTimeInt = Integer.parseInt(countOrTime);
        List<String> mouths = new ArrayList<>();

        for (int i = mos - 1; i >= 0; i--) {
            mouths.add(LocalDate.now().minusMonths(i).format(DateTimeFormatter.ofPattern("yyyy-MM")));
        }
        Map<String, Object> remap1 = new LinkedHashMap<>();
        remap1.put("type", "category");
        remap1.put("data", mouths);
        remap.put("xAxis", remap1);
        System.out.println(mouths);
        List<Map<String, Object>> series = new ArrayList<>();
        for (String stype : stypes) {
            //每个类型的时段数据
            List<Integer> reArray = new ArrayList<>();
            for (int i = 0; i < mouths.size(); i++) {
                String result = baseStatService.getByMonths(stype, mouths.get(i), countOrTimeType.get(countOrTimeInt));
                if (countOrTimeInt == 0) {
                    reArray.add(Integer.valueOf(result));
                } else {
                    reArray.add(Integer.parseInt(result) / 60);//时长单位分钟
                }
            }

            System.out.println(mapType.get(stype) + countOrTimeTypeDescription.get(countOrTimeInt));
            System.out.println(reArray);
            Map<String, Object> tmpMap = new LinkedHashMap<>();
            tmpMap.put("name", mapType.get(stype) + countOrTimeTypeDescription.get(countOrTimeInt));
            tmpMap.put("data", reArray);
            series.add(tmpMap);
        }
        remap.put("series", series);
        return remap;
    }
}
