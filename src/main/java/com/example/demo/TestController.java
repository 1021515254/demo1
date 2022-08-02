package com.example.demo;


import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
public class TestController {


    @GetMapping("getUrl")
    public String getUrl(@RequestParam("name") String name) {
        int num = 0 / 0;
        System.out.println(name);
        return "Hello World!!!";
    }

    public static void main(String[] args) {
        ArrayList<Map<String, Object>> getExcelData = getExcelData();
        ArrayList<Map<String, Object>> getSingleList = new ArrayList<>();
        ArrayList<Map<String, Object>> getDoubleList = new ArrayList<>();
        for (int i = 0; i < getExcelData.size(); i++) {
            if ("虎茅".equals(getExcelData.get(i).get("品种").toString())) {
                getSingleList.add(getExcelData.get(i));
            } else {
                getDoubleList.add(getExcelData.get(i));
            }
        }
        ArrayList<Map<String, Object>> getMax = getMax(getSingleList);
        log.info("虎茅中单次几率最高的是{}", getMax.get(0));
        log.info("虎茅中单次几率前五的是{}", getMax.subList(0, 4));
        log.info("虎茅中单次几率最差的是{}", getMax.get(getMax.size() - 1));
        log.info("虎茅中单次几率倒数五名的是{}", getMax.subList(getMax.size() - 5, getMax.size() - 1));
        ArrayList<Map<String, Object>> getMaxLh = getMax(getDoubleList);
        log.info("虎茅礼盒中单次几率最高的是{}", getMaxLh.get(0));
        log.info("虎茅礼盒中几率前五的是{}", getMaxLh.subList(0, 4));
        log.info("虎茅礼盒中单次几率最差的是{}", getMaxLh.get(getMaxLh.size() - 1));
        log.info("虎茅礼盒中单次几率倒数五名的是{}", getMaxLh.subList(getMaxLh.size() - 5, getMaxLh.size() - 1));
        log.info("虎茅店铺平均率（排除数据量小于申购三次的）{}", averageMap(getSingleList));
        log.info("虎茅礼盒店铺平均率（排除数据量小于申购三次的）{}", averageMap(getDoubleList));
    }

    /**
     * @param getSingleList 根据种类分类后的集合
     * @return 返回平均概率, 数据量小于三的 排除
     */
    public static Map<String, Object> averageMap(ArrayList<Map<String, Object>> getSingleList) {
        Map<String, ArrayList<Map<String, Object>>> getHuMao = getHuMao(getSingleList);
        Map<String, Object> averageMap = new HashMap<>();
        for (Map.Entry entry : getHuMao.entrySet()) {
            ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) entry.getValue();
            if (list.size() > 2) {
                double num = 0;
                double peoples = 0;
                for (int i = 0; i < list.size(); i++) {
                    num += Integer.parseInt(list.get(i).get("投放量").toString());
                    peoples += Integer.parseInt(list.get(i).get("申购人次").toString());
                }
                double chance = num / peoples * 100;
                BigDecimal bigDecimal = new BigDecimal(chance);
                chance = bigDecimal.setScale(4, RoundingMode.HALF_UP).doubleValue();
                averageMap.put(entry.getKey().toString(), chance);
            }
        }
        return averageMap;
    }

    /**
     * @param getList 排序后的虎茅集合
     * @return 根据店名分类虎茅
     */
    public static Map<String, ArrayList<Map<String, Object>>> getHuMao(ArrayList<Map<String, Object>> getList) {
        Map<String, ArrayList<Map<String, Object>>> getStringArrayListMap = new HashMap<>();
        ArrayList<Map<String, Object>> getMax = getMax(getList);
        for (int i = 0; i < getMax.size(); i++) {
            String key = getMax.get(i).get("店名").toString();
            if (!getStringArrayListMap.containsKey(key)) {
                ArrayList<Map<String, Object>> list = new ArrayList<>();
                list.add(getMax.get(i));
                getStringArrayListMap.put(key, list);
            } else {
                getStringArrayListMap.get(key).add(getMax.get(i));
            }
        }
        return getStringArrayListMap;
    }

    /**
     * @param getExcelData 申购数据
     * @return 申购几率从高到低返回数据
     */
    public static ArrayList<Map<String, Object>> getMax(ArrayList<Map<String, Object>> getExcelData) {
        for (int i = 0; i < getExcelData.size(); i++) {
            for (int l = 0; l < getExcelData.size() - 1; l++) {
                double num = Integer.parseInt(getExcelData.get(l).get("投放量").toString());
                double peoples = Integer.parseInt(getExcelData.get(l).get("申购人次").toString());
                double chance = num / peoples * 100;
                BigDecimal bigDecimal = new BigDecimal(chance);
                chance = bigDecimal.setScale(4, RoundingMode.HALF_UP).doubleValue();
                getExcelData.get(l).put("概率", chance);
                double num1 = Integer.parseInt(getExcelData.get(l + 1).get("投放量").toString());
                double peoples1 = Integer.parseInt(getExcelData.get(l + 1).get("申购人次").toString());
                double chance1 = num1 / peoples1 * 100;
                BigDecimal bigDecimal1 = new BigDecimal(chance1);
                chance1 = bigDecimal1.setScale(4, RoundingMode.HALF_UP).doubleValue();
                getExcelData.get(l + 1).put("概率", chance1);
                if (chance1 > chance) {
                    Map<String, Object> map = new HashMap<>(getExcelData.get(l));
                    getExcelData.set(l, getExcelData.get(l + 1));
                    getExcelData.set(l + 1, map);
                }
            }
        }
        return getExcelData;
    }

    /**
     * 解析exlce
     *
     * @return 解析exlce，返回list
     */
    public static ArrayList<Map<String, Object>> getExcelData() {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        try {
            //  System.out.println( getDate("44751"));
            FileInputStream fileInputStream = new FileInputStream("F:\\maotai\\data.xls");
            try {
                Workbook workbook = Workbook.getWorkbook(fileInputStream);
                Sheet sheet = workbook.getSheet(0);
                int rows = sheet.getRows();
                int clomns = sheet.getColumns();
                log.info("rows:{},clomns:{}", rows, clomns);
                // 从第二行开始
                for (int i = 1; i < rows; i++) {
                    Map<String, Object> objectMap = new HashMap<>();
                    for (int j = 0; j < clomns; j++) {
                        Cell cell = sheet.getCell(j, i);
                        String s = cell.getContents();
                        if (j == 0) {
                            s = getDate(s);
                        }
                        // System.out.print(s + ",");
                        objectMap.put(sheet.getCell(j, 0).getContents(), s);
                    }
                    list.add(objectMap);
                }
                log.info("解析excel完成,数据：{}", list);
                // System.out.println(list);
            } catch (IOException | BiffException e) {
                throw new RuntimeException(e);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private static final Calendar fromCal = Calendar.getInstance();

    /**
     * 转换读取excel日期类型字段 java自动转为五位数字
     *
     * @param time 读取Excel日期字段
     * @return yyyy/mm/dd
     */
    public static String getDate(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date;
        try {
            date = dateFormat.parse("1900/01/01");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        int day = Integer.parseInt(time);
        Date importDate = addDate(date, (day - 2));
        assert importDate != null;
        return dateFormat.format(importDate.getTime());
    }

    /**
     * 按日期加天数得出全新日期
     *
     * @param date 需要加天数的日期
     * @param day  需要增加的天数
     * @return 新的日期
     */
    public static Date addDate(Date date, int day) {
        try {
            fromCal.setTime(date);
            fromCal.add(Calendar.DATE, day);

            return fromCal.getTime();
        } catch (Exception e) {
            return null;
        }
    }
}
