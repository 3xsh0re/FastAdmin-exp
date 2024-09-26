package org.example.utils;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

// 用于解析命令行中的参数
public class ArgsUtils {
    public static boolean ifRead = false;
    @Parameter(names = {"-u", "--url"}, description = "hacking url")
    public static String targetURL = "";
    @Parameter(names = {"-r", "--read"}, description = "read urls from .csv")
    public static String fileName = "";
    public static void getArgs(String[] args) {
        JCommander jc = JCommander.newBuilder()
                .addObject(new ArgsUtils())
                .build();
        System.out.println("\033[34;1m" + "___________                __     _____       .___      .__                                        \n" +
                "\\_   _____/____    _______/  |_  /  _  \\    __| _/_____ |__| ____             ____ ___  _________  \n" +
                " |    __) \\__  \\  /  ___/\\   __\\/  /_\\  \\  / __ |/     \\|  |/    \\   ______ _/ __ \\\\  \\/  /\\____ \\ \n" +
                " |     \\   / __ \\_\\___ \\  |  | /    |    \\/ /_/ |  Y Y  \\  |   |  \\ /_____/ \\  ___/ >    < |  |_> >\n" +
                " \\___  /  (____  /____  > |__| \\____|__  /\\____ |__|_|  /__|___|  /          \\___  >__/\\_ \\|   __/ \n" +
                "     \\/        \\/     \\/               \\/      \\/     \\/        \\/               \\/      \\/|__|    " + "\033[0m");
        System.out.println("\t\t\t\t\t\t\t\t"+ "\033[34;1m" + "————created by 3xsh0re" + "\033[0m");
        System.out.println("\033[32;1m" + "[+]FastAdmin-exp Running!" + "\033[0m");
        try{
            jc.parse(args);
        }catch(Exception e){
            System.out.println("\033[31;1m[-]" + "请输入正确的参数!" + "\033[0m");
        }
        if (!fileName.isEmpty()){
            ifRead = true;
        }
    }

}
