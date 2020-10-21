package de.marcermarc.mchelper;

import de.marcermarc.mchelper.run.Restarter;
import de.marcermarc.mchelper.run.Runner;
import de.marcermarc.mchelper.run.ShutdownHook;

import java.util.Arrays;

public class Main {
    private static final String DOWNLOAD = "download";
    private static final String START = "start";

    private final Controller controller = new Controller();

    private Main() {
    }

    public static void main(String[] args) {
        if (args == null || args.length < 1) {
            System.out.println("There has to be at least one a argument. The following arguments are allowed:");
            printArgInfo();
            return;
        } else if (!Arrays.stream(args).allMatch(x -> DOWNLOAD.equalsIgnoreCase(x) || START.equalsIgnoreCase(x))) {
            System.out.println("Only the argument below are allowed:");
            printArgInfo();
            return;
        }

        Main main = new Main();
        main.init();

        if (Arrays.stream(args).anyMatch(DOWNLOAD::equalsIgnoreCase)) {
            System.out.println("Parameter 'download' recognized. Start download.");
            main.download();
        }
        if (Arrays.stream(args).anyMatch(START::equalsIgnoreCase)) {
            System.out.println("Parameter 'start' recognized. Starting now.");
            main.start();
        }
    }

    private static void printArgInfo() {
        System.out.println("- " + DOWNLOAD + ": Download and if necessary build or install the specified version.");
        System.out.println("- " + START + ": Start the specified version.");
        System.out.println("It is allowed to use both arguments to download and start the game.");
    }

    private void init() {
        controller.getConfig().initWithEnvironment();
    }

    private void download() {

    }

    private void start() {
        ShutdownHook hook = new ShutdownHook(controller);
        hook.start();

        Restarter restarter = new Restarter(controller);
        restarter.start();

        Runner runner = new Runner(controller);
        runner.start();
        runner.waitForFinish();
    }
}
