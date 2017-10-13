/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package builder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Variables
        String COMMAND_EXPORT = "export";
        String COMMAND_EXPORT_CCACHE = "USE_CCACHE=";
        String COMMAND_EXPORT_CCACHE_DIR = "CCACHE_DIR=";
        String COMMAND_MAKE = "make";
        String COMMAND_MAKE_INSTALLCLEAN = "installclean";
        String COMMAND_REPO = "repo";
        String COMMAND_REPO_SYNC = "sync -f";
        String COMMAND_THREADS = "-j64";
        String COMMAND_UPLOAD = "curl";
        String COMMAND_UPLOAD_FILE = "out/target/product/*/*-ota-$BUILD_NUMBER.zip";
        String COMMAND_UPLOAD_URL = "uploads.androidfilehost.com";
        String TARGET = "";
        String COMMAND_LUNCH = "lunch";
        String COMMAND_SOURCE = "source";
        String ENVSETUP = "build/envsetup.sh";
        String COMMAND_MAKE_TARGET = "";
        String COMMAND = "";
        Path CCACHE_DIR_PATH = Paths.get("");
        Boolean CCACHE = false;
        Boolean UPLOAD = false;
        
        System.out.println(" ____        _ _     _           ");
        System.out.println("|  _ \\      (_) |   | |          ");
        System.out.println("| |_) |_   _ _| | __| | ___ _ __ ");
        System.out.println("|  _ <| | | | | |/ _` |/ _ \\ '__|");
        System.out.println("| |_) | |_| | | | (_| |  __/ |   ");
        System.out.println("|____/ \\__,_|_|_|\\__,_|\\___|_|   \n");
        
        // Arguments
        if (args.length > 0) {
            // ccache
            if ( ArgumentsController.argumentsController(args[2]) ) {
                System.out.println("\n- - - DETERMINED CCACHE USE STATUS: " + args[2] + " - - -");
                System.out.println("\n- - - USE OF CCACHE IS ENABLED - - -");
                CCACHE_DIR_PATH = Paths.get(args[3]);
                System.out.println("\n - - - DETERMINED CCACHE DIR PATH: " + args[3] + " - - -");
                if ( Files.notExists(CCACHE_DIR_PATH) ) {
                    System.err.println("\n- - - ERROR: CCACHE DIR IS WRONG - - -");
                    System.exit(1);
                }
                CCACHE = true;
                COMMAND = CommandsController.commandBuilder(COMMAND, COMMAND_EXPORT + " " + COMMAND_EXPORT_CCACHE + "1" + " && " + COMMAND_EXPORT + " " + COMMAND_EXPORT_CCACHE_DIR + args[3], true);
            } else {
                CCACHE = false;
                COMMAND = CommandsController.commandBuilder(COMMAND, COMMAND_EXPORT + " " + COMMAND_EXPORT_CCACHE + "0", true);
            }
            
            // repo sync
            if ( ArgumentsController.argumentsController(args[1]) ) {
                System.out.println("\n- - - DETERMINED REPO SYNC STATUS: " + args[1] + " - - -");
                System.out.println("\n- - - REPO SYNC IS ENABLED - - -");
                COMMAND = CommandsController.commandBuilder(COMMAND, " && " + COMMAND_REPO + " " + COMMAND_REPO_SYNC + " " + COMMAND_THREADS, true);
            }
            
            // Initialize
            if (CCACHE) {
                TARGET = args[4];
            } else {
                TARGET = args[3];
            }
            System.out.println("\n- - - FOUND TARGET NAME: " + TARGET + " - - -");
            System.out.println("\n- - - ADD INITIALIZATION STEP - - -");
            COMMAND = CommandsController.commandBuilder(COMMAND, " && " + COMMAND_SOURCE + " " + ENVSETUP + " && " + COMMAND_LUNCH + " " + TARGET, true);
            
            // Clean up
            if ( ArgumentsController.argumentsController(args[0]) ) {
                System.out.println("\n- - - DETERMINED CLEAN STATUS: " + args[0] + " - - -");
                System.out.println("\n- - - CLEAN IS DISABLED - - -");
                COMMAND = CommandsController.commandBuilder(COMMAND, " && " + COMMAND_MAKE + " " + COMMAND_MAKE_INSTALLCLEAN + " " + COMMAND_THREADS, true);
            }
            
            if (CCACHE) {
                COMMAND_MAKE_TARGET = args[5];
            } else {
                COMMAND_MAKE_TARGET = args[4];
            }
            
            // Build
            COMMAND = CommandsController.commandBuilder(COMMAND, " && " + COMMAND_MAKE + " " + COMMAND_MAKE_TARGET + " " + COMMAND_THREADS, true);
            
            // UPLOAD
            // DO NOT DEBUG (DO NOT SHOW PASSWORD OFF)
            if (CCACHE) {
                if ( ArgumentsController.argumentsController(args[6]) ) {
                    UPLOAD = true;
                    System.out.println("\n- - - UPLOAD DATA FOUND - - -");
                    COMMAND = CommandsController.commandBuilder(COMMAND, " && " + COMMAND_UPLOAD + " -T " + COMMAND_UPLOAD_FILE + " -s " + COMMAND_UPLOAD_URL + " --user " + args[7], false);
                }
            } else if ( ArgumentsController.argumentsController(args[5]) ) {
                UPLOAD = true;
                System.out.println("\n- - - UPLOAD DATA FOUND - - -");
                COMMAND = CommandsController.commandBuilder(COMMAND, COMMAND_UPLOAD + " -T " + COMMAND_UPLOAD_FILE + " -s" + COMMAND_UPLOAD_URL + " --user " + args[6], false);
            }
            // EXECUTE TIME
            if (UPLOAD) {
                System.out.println("\n- - - COMMAND READY - - -");
                System.out.println("\n- - - EXECUTING - - -\n");
            } else {
                System.out.println("\n- - - COMMAND READY: " + COMMAND + " - - -");
                System.out.println("\n- - - EXECUTING: " + COMMAND + " - - -\n");
            }

            CommandsController.executeCommands(COMMAND);
        } else {
            System.out.println("Usage: java -jar Builder.jar [clean] [sync] [ccache] [ccachedir] [target] [make target] [upload] [upload credentials]");
            System.out.println("clean: 1 or 0: Whether perform clean or no (int)");
            System.out.println("sync: 1 or 0: Whether sync or not (int)");
            System.out.println("ccache: 1 or 0: Whether use ccache or not (int)");
            System.out.println("ccachedir: If using ccache only! Define dir path for ccache (String)");
            System.out.println("target: e.g. aosp_bullhead-userdebug");
            System.out.println("make target: e.g. otapackage; dist: Make target (String)");
            System.out.println("upload: 1 or 0: Whether upload or not (int)");
            System.out.println("upload credentials: user:password: Credentials for upload (String) ONLY IF upload == 1");
        }
    }
}
