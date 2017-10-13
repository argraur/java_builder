/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package builder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

/**
 *
 * @author darth
 */
public class CommandsController {
    public static void executeCommands(String COMMAND) throws IOException, InterruptedException {

        File TEMP_SCRIPT = createTempScript(COMMAND);
        
        try {
            ProcessBuilder PB = new ProcessBuilder("bash", TEMP_SCRIPT.toString());
            PB.inheritIO();
            Process PROCESS = PB.start();
            PROCESS.waitFor();
        } finally {
            TEMP_SCRIPT.delete();
        }
    }

    public static File createTempScript(String COMMAND) throws IOException {
        File TEMP_SCRIPT = File.createTempFile("script", null);

        Writer STREAM_WRITER = new OutputStreamWriter(new FileOutputStream(
                TEMP_SCRIPT));
        PrintWriter PRINT_WRITER = new PrintWriter(STREAM_WRITER);

        PRINT_WRITER.println("#!/bin/bash");
        PRINT_WRITER.println(COMMAND);

        PRINT_WRITER.close();

        return TEMP_SCRIPT;
    }
    
    public static String commandBuilder(String COMMAND, String ADD, Boolean DEBUG) {
        if (DEBUG) {
            System.out.println("\n- - - ADDING '" + ADD + "' TO '" + COMMAND + "' - - -");
        }
        COMMAND = COMMAND + ADD;
        return COMMAND;
    }
}
