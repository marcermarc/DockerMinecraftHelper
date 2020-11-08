package de.marcermarc.mchelper.run;

import de.marcermarc.mchelper.Controller;

import java.util.Scanner;

public class InputHandler implements Runnable {
    private final Controller controller;
    private final Thread inputHandlerThread;

    public InputHandler(Controller controller) {
        this.controller = controller;
        this.inputHandlerThread = new Thread(this, "Input Handler Thread");

        controller.setInputHandler(this);
    }

    public void start() {
        inputHandlerThread.start();
    }

    @Override
    public void run() {
        Scanner inScanner = new Scanner(System.in);
        while (true) {
            try {
                String in = inScanner.next(); // get the input and add the enter char

                System.out.println("Input recognized: '" + in + "'. Hand off to minecraft");

                controller.getRunner().sendCommand(in);
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
            }
        }
    }
}
