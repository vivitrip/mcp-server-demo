package com.xw.mcp.server.demo.util;

import java.io.FileWriter;
import java.io.IOException;

public class GenerateDataFile {
    public static void main(String[] args) {
        String filePath = "src/main/resources/test_user_data.csv"; // 输出的CSV文件路径
        String[] regions = {"North", "South", "East", "West"}; // 分区列表

        try (FileWriter writer = new FileWriter(filePath)) {
            // 写入CSV文件头
            writer.write("id,name,age,region\n");

            // 生成200行数据
            for (int i = 1; i <= 200; i++) {
                String name = "User" + i; // 生成用户名
                int age = 18 + (i % 50); // 年龄在18到67之间循环
                String region = regions[i % regions.length]; // 按顺序分配分区
                writer.write(i + "," + name + "," + age + "," + region + "\n");
            }

            System.out.println("CSV文件已成功生成: " + filePath);
        } catch (IOException e) {
            System.err.println("生成CSV文件时出错: " + e.getMessage());
        }
    }
}
