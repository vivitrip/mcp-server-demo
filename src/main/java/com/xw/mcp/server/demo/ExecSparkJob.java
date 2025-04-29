package com.xw.mcp.server.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class ExecSparkJob {

  @Tool(description = "启动spark任务")
  public String executeSparkJob() {

    // 构建 Spark 提交命令
    String command = "spark-submit --master local[4] --class com.xw.MySQLToMaxCompute "
        + "practice-1.0-SNAPSHOT-jar-with-dependencies.jar ";

    try {
      // 执行命令
      Process process = Runtime.getRuntime().exec(command);
      int exitCode = process.waitFor();

      if (exitCode == 0) {
        System.out.println("Spark job executed successfully.");
        return "Spark job executed successfully";
      } else {
        System.err.println(
            "Spark job failed with exit code: " + exitCode + ". Error: " + new String(
                process.getErrorStream().readAllBytes(), StandardCharsets.UTF_8));
        return "Spark job failed with exit code: " + exitCode + ". Error: " + new String(
            process.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to execute Spark job", e);
    }
  }
}
