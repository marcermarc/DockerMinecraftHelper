package de.marcermarc.mchelper.run;

import de.marcermarc.mchelper.Controller;

public class ShutdownHook implements Runnable {
    private final Controller controller;
    private final Thread shutdownHookThread;

    public ShutdownHook(Controller controller) {
        this.controller = controller;
        this.shutdownHookThread = new Thread(this, "Shutdown Hook Thread");

        controller.setShutdownHook(this);
    }

    public void start() {
        Runtime.getRuntime().addShutdownHook(shutdownHookThread);
    }

    public void destroy() {
        Runtime.getRuntime().removeShutdownHook(shutdownHookThread);

        controller.setShutdownHook(null);
    }

    @Override
    public void run() {
        controller.getRunner().stop();

        Runtime.getRuntime().halt(0); // return 0 so the process end is success
    }
}
