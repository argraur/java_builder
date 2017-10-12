/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package builder;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

public class Builder {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Variables
        String COMMAND_EXPORT = "export";
        String COMMAND_EXPORT_CCACHE = "USE_CCACHE=";
        String COMMAND_EXPORT_CCACHE_DIR = "CCACHE_DIR=";
        String COMMAND_MAKE = "make";
        String COMMAND_MAKE_INSTALLCLEAN = "installclean";
        String COMMAND_REPO = "repo";
        String COMMAND_REPO_SYNC = "sync -f";
        String COMMAND_UPLOAD = "bash upload.sh"; // Not being used at the moment
        String COMMAND_THREADS = "-j64";
        // String DEVICE = "bullhead"; // Deprecated
        // String PREFIX = "aosp"; // Deprecated
        // String BUILD_TYPE = "userdebug"; // Deprecated
        String TARGET = ""; // Instead of DEVICE, PREFIX, BUILD_TYPE
        String COMMAND_LUNCH = "lunch";
        String COMMAND_SOURCE = "source";
        String ENVSETUP = "build/envsetup.sh";
        String COMMAND_MAKE_TARGET = "";
        String COMMAND = "";
        Path CCACHE_DIR_PATH = Paths.get("");
        Boolean CCACHE = false;
        
        System.out.println(" ____        _ _     _           ");
        System.out.println("|  _ \\      (_) |   | |          ");
        System.out.println("| |_) |_   _ _| | __| | ___ _ __ ");
        System.out.println("|  _ <| | | | | |/ _` |/ _ \\ '__|");
        System.out.println("| |_) | |_| | | | (_| |  __/ |   ");
        System.out.println("|____/ \\__,_|_|_|\\__,_|\\___|_|   ");
        
        // Arguments
        if (args.length > 0) {
            // ccache
            if ( argumentsController(args[2]) ) {
                System.out.println("\n- - - DETERMINED CCACHE USE STATUS: " + args[2] + " - - -");
                System.out.println("\n- - - USE OF CCACHE IS ENABLED - - -");
                CCACHE_DIR_PATH = Paths.get(args[3]);
                System.out.println("\n - - - DETERMINED CCACHE DIR PATH: " + args[3] + " - - -");
                if ( Files.notExists(CCACHE_DIR_PATH) ) {
                    System.err.println("\n- - - ERROR: CCACHE DIR IS WRONG - - -");
                    System.exit(1);
                }
                CCACHE = true;
                COMMAND = commandBuilder(COMMAND, COMMAND_EXPORT + " " + COMMAND_EXPORT_CCACHE + "1" + " && " + COMMAND_EXPORT + " " + COMMAND_EXPORT_CCACHE_DIR + args[3]);
            } else {
                CCACHE = false;
                COMMAND = commandBuilder(COMMAND, COMMAND_EXPORT + " " + COMMAND_EXPORT_CCACHE + "0");
            }
            
            // repo sync
            if ( argumentsController(args[1]) ) {
                System.out.println("\n- - - DETERMINED REPO SYNC STATUS: " + args[1] + " - - -");
                System.out.println("\n- - - REPO SYNC IS ENABLED - - -");
                COMMAND = commandBuilder(COMMAND, " && " + COMMAND_REPO + " " + COMMAND_REPO_SYNC + " " + COMMAND_THREADS);
            }
            
            // Initialize
            if (CCACHE) {
                TARGET = args[4];
            } else {
                TARGET = args[3];
            }
            System.out.println("- - - FOUND TARGET NAME: " + TARGET + " - - -");
            System.out.println("- - - ADD INITIALIZATION STEP - - -");
            COMMAND = commandBuilder(COMMAND, " && " + COMMAND_SOURCE + " " + ENVSETUP + " && " + COMMAND_LUNCH + " " + TARGET);
            
            // Clean up
            if ( argumentsController(args[0]) ) {
                System.out.println("\n- - - DETERMINED CLEAN STATUS: " + args[0] + " - - -");
                System.out.println("\n- - - CLEAN IS DISABLED - - -");
                COMMAND = commandBuilder(COMMAND, " && " + COMMAND_MAKE + " " + COMMAND_MAKE_INSTALLCLEAN + " " + COMMAND_THREADS);
            }
            
            if (CCACHE) {
                COMMAND_MAKE_TARGET = args[5];
            } else {
                COMMAND_MAKE_TARGET = args[4];
            }
            
            // Build
            COMMAND = commandBuilder(COMMAND, " && " + COMMAND_MAKE + " " + COMMAND_MAKE_TARGET + " " + COMMAND_THREADS);
            
            // EXECUTE TIME
            System.out.println("\n- - - COMMAND READY: " + COMMAND + " - - -");
            System.out.println("\n- - - EXECUTING: " + COMMAND + " - - -\n");
            executeCommands(COMMAND);
        } else {
            usage();
        }
    }
    
    public static Boolean argumentsController(String ARGUMENT) {
        try {
            int VALUE = Integer.parseInt(ARGUMENT);
            return VALUE == 1;
        }
        catch (NumberFormatException E) {
            System.err.println("Argument" + ARGUMENT + " must be an integer.");
            System.exit(1);
            return false;
        }
    }
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
    
    public static String commandBuilder(String COMMAND, String ADD) {
        System.out.println("\n- - - ADDING '" + ADD + "' TO '" + COMMAND + "'");
        COMMAND = COMMAND + ADD;
        return COMMAND;
    }
    
    public static void usage() {
        System.out.println("Usage: java -jar Builder.jar [clean] [sync] [ccache] [ccachedir] [target]");
        System.out.println("clean: 1 or 0: Whether perform clean or no (int)");
        System.out.println("sync: 1 or 0: Whether sync or not (int)");
        System.out.println("ccache: 1 or 0: Whether use ccache or not (int)");
        System.out.println("ccachedir: If using ccache only! Define dir path for ccache (String)");
        System.out.println("target: e.g. aosp_bullhead-userdebug");
        // System.out.println("upload: 1 or 0: Whether sync or not (int)");
    }
}
