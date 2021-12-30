package de.marcermarc.mchelper.run.forge;

import de.marcermarc.mchelper.Controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeforeStart extends de.marcermarc.mchelper.run.BeforeStart {
    private static final String UNIX_ARGS_FILENAME = "unix_args.txt";

    public BeforeStart(Controller controller) {
        super(controller);
    }

    @Override
    public void run() throws Exception {
        super.run();

        Path unixArgs = Paths.get(controller.getConfig().getMcDir(), UNIX_ARGS_FILENAME);

        if (Files.exists(unixArgs)) {
            try (Stream<String> stream = Files.lines(unixArgs)) {
                String args = stream.collect(Collectors.joining(" "));

                controller.getConfig().setMcStartArgs(args);
            }
        }
    }
}
