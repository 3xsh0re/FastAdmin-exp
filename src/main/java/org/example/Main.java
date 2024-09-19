package org.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException, SQLException {
        CmdArgs.getArgs(args);
        FuzzPoc fuzzPoc = new FuzzPoc("https://test.jonlv.com/");
        fuzzPoc.Exp();
    }

}