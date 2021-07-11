package de.marcermarc.mchelper.run;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import de.marcermarc.mchelper.Controller;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.TimerTask;

public class Restarter {
    private final Controller controller;
    private RestarterTask task = null;
    private Cron cron = null;

    public Restarter(Controller controller) {
        this.controller = controller;
        controller.setRestarter(this);
    }

    public void start() {
        boolean started = testAndStartCron() || testAndStartInterval();
    }

    private boolean testAndStartCron() {
        if (cron == null || !initCron()) {
            return false;
        }

        ExecutionTime execTime = ExecutionTime.forCron(cron);

        Optional<Duration> duration = execTime.timeToNextExecution(ZonedDateTime.now());

        if (!duration.isPresent()) {
            return false;
        }

        initTask(duration.get().toMillis());

        return true;
    }

    private boolean initCron() {
        String cronString = controller.getConfig().getRestartCron();

        if (cronString == null || cronString.equals("")) {
            return false;
        }

        try {
            CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX));

            cron = parser.parse(cronString);
            return true;
        } catch (Exception ex) {
            System.out.println("Cron could not be parsed");
            System.out.println(ex.getMessage());
            return false;
        }
    }

    private boolean testAndStartInterval() {
        int interval = controller.getConfig().getRestartInterval();

        if (interval > 0) {

            if (task != null && !task.isCanceled()) {
                System.out.println("Restart-Task should be started but is already started.");
            }

            initTask(interval * 60L * 1000L); // scheduleInterval is in milliseconds.

            return true;
        }
        return false;
    }

    private void initTask(long scheduleInterval) {
        task = new RestarterTask(controller);
        controller.getTimer().schedule(task, scheduleInterval, scheduleInterval);
    }

    public void reset() {
        destroy();

        start();
    }

    public void destroy() {
        if (task != null && !task.isCanceled()) {
            task.cancel();
        }
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
