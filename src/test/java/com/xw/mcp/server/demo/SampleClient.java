package com.xw.mcp.server.demo;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.spec.ClientMcpTransport;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;
import java.util.Map;


public class SampleClient {

  private final ClientMcpTransport transport;

  public SampleClient(ClientMcpTransport transport) {
    this.transport = transport;
  }

  public void run() {

    var client = McpClient.sync(this.transport).build();

    client.initialize();

    client.ping();

    // 列出并展示可用的工具
    ListToolsResult toolsList = client.listTools();
    System.out.println("可用工具 = " + toolsList);

    // 执行Spark任务
    CallToolResult sparkTaskResult = client.callTool(new CallToolRequest("executeSparkJob",
        Map.of("args",
            "spark-submit --master local[4] --class com.xw.MySQLToMaxCompute practice-1.0-SNAPSHOT-jar-with-dependencies.jar")));
    System.out.println("任务执行结果: " + sparkTaskResult.content());
    client.closeGracefully();

  }
}
