package org.example;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

// 用于解析命令行中的参数
public class CmdArgs {
    @Parameter(names = {"-u", "--url"}, description = "hacking url")
    public static String targetURL;
    @Parameter(names = {"-r", "--read"}, description = "read urls from .txt")
    public static boolean ifRead;
    @Parameter(names = {"-h", "--help"}, help = true, description = "the tool's usage")
    private static boolean helpInfo = false;

    public static void getArgs(String[] args) {
        JCommander jc = JCommander.newBuilder()
                .addObject(new CmdArgs())
                .build();
        System.out.println("\033[34;1m" + "___________                __     _____       .___      .__                                        \n" +
                "\\_   _____/____    _______/  |_  /  _  \\    __| _/_____ |__| ____             ____ ___  _________  \n" +
                " |    __) \\__  \\  /  ___/\\   __\\/  /_\\  \\  / __ |/     \\|  |/    \\   ______ _/ __ \\\\  \\/  /\\____ \\ \n" +
                " |     \\   / __ \\_\\___ \\  |  | /    |    \\/ /_/ |  Y Y  \\  |   |  \\ /_____/ \\  ___/ >    < |  |_> >\n" +
                " \\___  /  (____  /____  > |__| \\____|__  /\\____ |__|_|  /__|___|  /          \\___  >__/\\_ \\|   __/ \n" +
                "     \\/        \\/     \\/               \\/      \\/     \\/        \\/               \\/      \\/|__|    " + "\033[0m");
        System.out.println("\t\t\t\t\t\t\t\t"+ "\033[34;1m" + "————created by 3xsh0re" + "\033[0m");
        System.out.println("\033[32;1m" + "[+]Log4j2Exploiter Running!" + "\033[0m");
        try{
            jc.parse(args);
        }catch(Exception e){
            if(!helpInfo){
                System.out.println("\033[31;1m" +"[-]Error: " + e.getMessage() + "\033[0m");
                helpInfo = true;
            }
        }

        if (helpInfo){
            System.out.println("\033[32;1m" +"" + "\033[0m");
        }
    }

}
