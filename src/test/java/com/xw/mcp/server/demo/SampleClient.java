package com.xw.mcp.server.demo;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.spec.ClientMcpTransport;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;


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

    // 获取北京的空气质量信息
    CallToolResult airQualityResult = client.callTool(new CallToolRequest("executeSparkJob", null));
    System.out.println("任务执行信息: " + airQualityResult);

    client.closeGracefully();
  }
}
