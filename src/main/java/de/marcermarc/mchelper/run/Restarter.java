package de.marcermarc.mchelper.run;

import de.marcermarc.mchelper.Controller;

import java.util.TimerTask;

public class Restarter {
    private final Controller controller;
    private RestarterTask task = null;

    public Restarter(Controller controller) {
        this.controller = controller;
        controller.setRestarter(this);
    }

    public void start() {
        int interval = controller.getConfig().getRestartInterval();

        if (interval > 0) {

            if (task != null && !task.isCanceled()) {
                System.out.println("Restart-Task should be started but is already started.");
            }

            long scheduleInterval = interval * 60L * 1000L; // scheduleInterval is in milliseconds.

            task = new RestarterTask(controller);
            controller.getTimer().schedule(task, scheduleInterval, scheduleInterval);
        }
    }

    public void reset() {
        if (task != null && !task.isCanceled()) {
            task.cancel();
        }

        start();
    }

    private static class RestarterTask extends TimerTask {
        private final Controller controller;
        private boolean canceled = false;

        public RestarterTask(Controller controller) {
            this.controller = controller;
        }

        @Override
        public void run() {
            controller.getRunner().triggerRestart();
        }

        @Override
        public boolean cancel() {
            canceled = true;
            return super.cancel();
        }

        public boolean isCanceled() {
            return canceled;
        }
    }
}
