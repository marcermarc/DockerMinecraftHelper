package de.marcermarc.mchelper.run;

import de.marcermarc.mchelper.Controller;

public class BeforeStart {
    protected final Controller controller;

    public BeforeStart(Controller controller) {
        this.controller = controller;
    }

    public void run() throws Exception {
        // ToDo Auto Eula
    }
}
