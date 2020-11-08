package de.marcermarc.mchelper;

import de.marcermarc.mchelper.run.InputHandler;
import de.marcermarc.mchelper.run.Restarter;
import de.marcermarc.mchelper.run.Runner;
import de.marcermarc.mchelper.run.ShutdownHook;

import java.util.Timer;

public class Controller {

    private final Configuration config = new Configuration();
    private Timer timer;
    private Runner runner;
    private Restarter restarter;
    private ShutdownHook shutdownHook;
    private InputHandler inputHandler;

    public Configuration getConfig() {
        return config;
    }

    public Timer getTimer() {
        if (timer == null) {
            timer = new Timer(true);
        }

        return timer;
    }

    public Runner getRunner() {
        return runner;
    }

    public void setRunner(Runner runner) {
        this.runner = runner;
    }

    public Restarter getRestarter() {
        return restarter;
    }

    public void setRestarter(Restarter restarter) {
        this.restarter = restarter;
    }

    public ShutdownHook getShutdownHook() {
        return shutdownHook;
    }

    public void setShutdownHook(ShutdownHook shutdownHook) {
        this.shutdownHook = shutdownHook;
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }
}
