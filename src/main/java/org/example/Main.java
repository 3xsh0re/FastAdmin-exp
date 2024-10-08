package org.example;

import org.example.utils.ArgsUtils;

public class Main {
    public static void main(String[] args) throws Exception {
        ArgsUtils.getArgs(args);
        if (ArgsUtils.targetURL.isEmpty()&&ArgsUtils.fileName.isEmpty()){
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