package com.xw.mcp.server.demo;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.time.Duration;

@SpringBootApplication
public class McpServerDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(McpServerDemoApplication.class, args);
  }

  @Bean
  public ToolCallbackProvider dataTools(ExecSparkJob execSparkJob) {
    return MethodToolCallbackProvider.builder().toolObjects(execSparkJob).build();
  }
}

