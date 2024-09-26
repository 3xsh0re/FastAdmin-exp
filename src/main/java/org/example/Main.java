package org.example;

import org.example.utils.ArgsUtils;

public class Main {
    public static void main(String[] args) throws Exception {
        ArgsUtils.getArgs(args);
        if (ArgsUtils.targetURL.isEmpty()&&ArgsUtils.fileName.isEmpty()){
            System.out.println("\033[31;1m[-]" + "请输入正确的参数!" + "\033[0m");
            return;
        }
        if (!ArgsUtils.ifRead) {
            Poc poc = new Poc(ArgsUtils.targetURL);
            poc.Exp();
        }else {
            // 从CSV文件中读取urls进行Fuzz
            Poc poc = new Poc();
            CSVFuzz.Fuzz(poc);
        }
    }

}