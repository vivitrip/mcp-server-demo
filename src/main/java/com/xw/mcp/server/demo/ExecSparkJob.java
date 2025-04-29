package com.xw.mcp.server.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class ExecSparkJob {

  @Tool(description = "启动spark任务")
  public String executeSparkJob(@ToolParam(description = "命令") String args) {
    try {
      // 执行命令
      Process process = Runtime.getRuntime().exec(args);
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
