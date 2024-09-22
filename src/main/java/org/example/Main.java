package org.example;

import org.example.utils.ArgsUtils;

public class Main {
    public static void main(String[] args) throws Exception {
        ArgsUtils.getArgs(args);
        if (!ArgsUtils.ifRead) {
            Poc poc = new Poc("http://xqhz.hnzzsz.com/");
            poc.Exp();
        }else {
            // 从CSV文件中读取urls进行Fuzz
            Poc poc = new Poc();
            CSVFuzz.Fuzz(poc);
        }
    }

}