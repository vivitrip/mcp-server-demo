package com.xw.mcp.server.demo;

import com.xw.mcp.server.demo.constant.CommonConstant;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class WriteDataToMaxCompute {

    public void writeData() throws SQLException, IOException {
        // 加载配置文件
        Properties properties = new Properties();
        properties.load(new FileReader(CommonConstant.DB_PROPERTIES_PATH));
        String jdbcUrl = properties.getProperty(CommonConstant.JDBC_URL_KEY);
        String username = properties.getProperty(CommonConstant.JDBC_USERNAME_KEY);
        String password = properties.getProperty(CommonConstant.JDBC_PASSWORD_KEY);
        String csvFilePath = properties.getProperty(CommonConstant.CSV_FILE_PATH_KEY);

        // 加载驱动
        try {
            Class.forName("com.aliyun.odps.jdbc.OdpsDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("无法加载MaxCompute JDBC驱动", e);
        }

        // 建立连接
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {

            final String tableName = "write_data_from_file";

            // 每次都先删除表再重新创建
            dropTable(conn, tableName);
            createTable(conn, tableName);

            // 查询并打印表中的数据（初始状态）
            System.out.println("表 " + tableName + " 的初始数据：");
            printTableData(conn, tableName);

            // 读取 CSV 文件并批量插入数据
            batchInsertData(conn, csvFilePath, tableName);

            // 查询并打印表中的数据（写入后）
            System.out.println("表 " + tableName + " 的数据（写入后）：");
            printTableData(conn, tableName);
        }
    }

    private void dropTable(Connection conn, String tableName) throws SQLException {
        String dropTableSQL = "DROP TABLE IF EXISTS " + tableName;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(dropTableSQL);
            System.out.println("表 " + tableName + " 已删除");
        }
    }

    private void createTable(Connection conn, String tableName) throws SQLException {
        String createTableSQL = "CREATE TABLE " + tableName + " (" +
            "id BIGINT, " +
            "name STRING, " +
            "age BIGINT, " +
            "region STRING)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("表 " + tableName + " 已创建");
        }
    }

    private void batchInsertData(Connection conn, String csvFilePath, String tableName)
        throws IOException, SQLException {
        String insertSQL = "INSERT INTO " + tableName + " (id, name, age, region) VALUES (?, ?, ?, ?)";
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // 跳过表头
                    continue;
                }
                String[] data = line.split(",");
                preparedStatement.setLong(1, Long.parseLong(data[0])); // id
                preparedStatement.setString(2, data[1]); // name
                preparedStatement.setLong(3, Long.parseLong(data[2])); // age
                preparedStatement.setString(4, data[3]); // region
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            System.out.println("数据已成功写入表 " + tableName);
        }
    }

    private void printTableData(Connection conn, String tableName) throws SQLException {
        String querySQL = "SELECT * FROM " + tableName;
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(querySQL)) {
            while (rs.next()) {
                System.out.println("id: " + rs.getLong("id") +
                    ", name: " + rs.getString("name") +
                    ", age: " + rs.getInt("age") +
                    ", region: " + rs.getString("region"));
            }
        }
    }
}
