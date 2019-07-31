/*
 * Copyright (c) 2018.  semo
 */

package dev.semo.npgen.shell;

import dev.semo.npgen.service.PostClient;
import dev.semo.npgen.utils.NumberPlateUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@ShellComponent
public class NumberplateClientCommands {

    private static Logger log = LogManager.getLogger(NumberplateClientCommands.class);

    @Autowired
    private PostClient postClient;

    Runnable runnable = () -> {
        try {
            one();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    };

    @ShellMethod("Saying hi to a given person's name.")
    public String sayHi(
            @ShellOption(help = "needs a name") String text
    ) {
        return "Hi, " + text;
    }

    @ShellMethod("Add up to sum.")
    public int sum(
            @ShellOption int a,
            @ShellOption int b) {
        return a + b;
    }

    @ShellMethod("Generates a numberplate at the given location.")
    public String simple(
            @ShellOption(help = "path to image") String path
    ) {
        NumberPlateUtility np = new NumberPlateUtility();
        np.buildPlateImageAsFile(path);
        return "Done.";
    }

    @ShellMethod("Sends one simple POST request.")
    public String one() throws FileNotFoundException {
        NumberPlateUtility np = new NumberPlateUtility();
        HttpStatus response = postClient.postNumberPlate(np.completeImage());
        if (response == HttpStatus.ACCEPTED) {
            return "Request sent successfully.";
        }
        return String.format("Request failed: %s", response.getReasonPhrase());
    }

    @ShellMethod("Sends many POST request.")
    public String many(@ShellOption String[] argv) {
        if (argv.length <= 0) {
            log.error(Arrays.toString(argv));
            throw new IllegalArgumentException("Given parameters were wrong or insufficient: " + Arrays.toString(argv));
        }

        ShellCommandsParser commands = new ShellCommandsParser(argv);

        // Do "i" POST request(s) and stop :-D :-D Multiverschachtelung-Powa!
        if (argv[0].chars().allMatch(Character::isDigit)) {
            if (Integer.valueOf(commands.valueOf(String.valueOf(argv[0]))) > 0) {
                int boundary = Integer.valueOf(commands.valueOf(String.valueOf(argv[0])));
                for (int i = 0; i <= boundary; i++) {
                    try {
                        one();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return "Done";
            }
        }

        // Do POST requests at a random time
        if (commands.hasOption("nuke")) {
            ScheduledExecutorService service = Executors
                    .newSingleThreadScheduledExecutor();
            for (int i = 0; i < 10; i++) {
                service.scheduleAtFixedRate(runnable, 0, ThreadLocalRandom.current().nextInt(1, 3000), TimeUnit.MILLISECONDS);
                try {
                    one();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return "Done";
        }

        log.info("Sending many POST requests completed without error.");
        return "Done";

    }

}
