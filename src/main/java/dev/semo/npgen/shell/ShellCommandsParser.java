/*
 * Copyright (c) 2018.  semo
 */

package dev.semo.npgen.shell;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ShellCommandsParser {

    private Map<String, String> arguments = new HashMap<>();

    public ShellCommandsParser(String[] args) {
        parse(args);
    }

    private void parse(String... args) {
        for (int i = 0; i < args.length; i++) {
            arguments.put(args[i], args[i]);
        }
    }

    public int size() {
        return arguments.size();
    }

    public boolean hasOption(String option) {
        return arguments.containsKey(option);
//        return arguments.stream().anyMatch(argument -> arguments.contains(option));
    }

    public String valueOf(String option) {
        return arguments.get(option);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShellCommandsParser that = (ShellCommandsParser) o;
        return Objects.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arguments);
    }
}
