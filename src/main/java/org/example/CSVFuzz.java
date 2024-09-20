package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.example.utils.ArgsUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CSVFuzz {
    public static void Fuzz(Poc poc) {
        try (CSVReader reader = new CSVReader(new FileReader(ArgsUtils.fileName))) {
            List<String[]> allData = reader.readAll();
            if (!allData.isEmpty()) {
                // 找到url列的索引
                String[] header = allData.get(0);
                int urlColumnIndex = -1;
                for (int i = 0; i < header.length; i++) {
                    if ("url".equalsIgnoreCase(header[i])) {
                        urlColumnIndex = i;
                        break;
                    }
                }
                if (urlColumnIndex == -1) {
                    System.out.println("\033[31;1m[-]当前.csv文件中不存在url列!\033[0m");
                    return;
                }
                // 提取url并Fuzz漏洞是否存在
                for (int i = 1; i < allData.size(); i++) {
                    String[] row = allData.get(i);
                    if (row.length > urlColumnIndex) {
                        String url = row[urlColumnIndex];
                        System.out.println("\033[32;1m[+]--------------------------------------------------\033[0m");
                        System.out.println("\033[32;1m[+]FuzzUrl:" + url + "\033[0m");
                        poc.setFuzzUrl(url);
                        poc.Exp();
                    }
                }
                System.out.println("\033[32;1m[+]--------------------------------------------------\033[0m");
                System.out.println("\033[32;1m[+]Fuzz ending!\033[0m");
            }

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("\033[31;1m[-]运行出错!\033[0m");
        }
    }
}
