package de.marcermarc.mchelper.run;

import de.marcermarc.mchelper.Controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

public class Runner {
    private final Controller controller;
    private ProcessBuilder processBuilder;

    private Process currentProcess;
    private OutputStream outputStream;

    private boolean restartTriggered = false;
    private Thread restartThread;

    public Runner(Controller controller) {
        this.controller = controller;
        controller.setRunner(this);
    }

    public void init() {
        String[] command = CommandBuilder.buildCommand(controller.getConfig());

        File workDir = new File(controller.getConfig().getWorkDir());

        processBuilder = new ProcessBuilder(command)
                .directory(workDir)
                .inheritIO()
                .redirectInput(ProcessBuilder.Redirect.PIPE);

    }

    public void start() {
        try {
            currentProcess = processBuilder.start();
            outputStream = currentProcess.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitForFinish() {
        try {
            while (true) {
                if (currentProcess.isAlive()) {
                    currentProcess.waitFor();
                } else if (restartTriggered && restartThread != null && restartThread.isAlive()) {
                    restartThread.wait();
                } else {
                    return;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void triggerRestart() {
        if (restartTriggered) {
            System.out.println("Restart triggered: But restart already in progress. Trigger will be ignored");
            return;
        }

        System.out.println("Restart triggered: Initiate restart process...");
        restartTriggered = true;
        restartThread = new Thread(() -> {
            System.out.println("Restart process: Stop now.");
            stop();
            System.out.println("Restart process: Stop was successful, start now.");
            start();
            restartTriggered = false;
        }, "Restart Thread");

        restartThread.start();
    }

    public void stop() {
        try {
            stopCommand();
            currentProcess.waitFor(controller.getConfig().getStopTimeout(), TimeUnit.SECONDS);
            outputStream = null;
        } catch (Exception ex) {
            System.out.println("Minecraft Shutdown Failed. Stopping container now...");

            Runtime.getRuntime().exit(-1);
        }
    }

    private void stopCommand() throws IOException {
        if (outputStream != null) {
            outputStream.write(new byte[]{(byte) 0x73, (byte) 0x74, (byte) 0x6f, (byte) 0x70, (byte) 0x0d, (byte) 0x0a}); // The byte-array represents "stop" and a return char
            outputStream.flush(); // write the buffer to the process
        }
    }

    public void sendCommand(String command) throws IOException {
        if (outputStream != null) {
            outputStream.write(command.getBytes()); // The byte-array represents "stop" and a return char
            outputStream.write("\n".getBytes());
            outputStream.flush(); // write the buffer to the process
        }
    }
}
