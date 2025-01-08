package com.byls.boat.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestDataGenerator {

    public static void main(String[] args) {
        List<JSONObject> testData = new ArrayList<>();

        for (int i = 0; i < 3000; i++) {
            JSONObject course = new JSONObject();
            course.put("longitude", 121.4737 + (i * 0.0001));
            course.put("latitude", 31.2304 + (i * 0.0001));
            testData.add(course);
        }

        JSONArray jsonArray = new JSONArray(Collections.singletonList(testData));
        String jsonData = jsonArray.toJSONString();

        // 保存到文件
        try (FileWriter file = new FileWriter("test_data.json")) {
            file.write(jsonData);
            System.out.println("测试数据已保存到 test_data.json 文件中");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
