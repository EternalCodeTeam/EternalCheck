package com.eternalcode.check.command.configurer;

import java.util.ArrayList;
import java.util.List;

public class Command {

    private String name;
    private List<String> aliases;
    private List<String> permissions;
    private List<SubCommand> subCommands = new ArrayList<>();

    public Command(String name, List<String> aliases, List<String> permissions, List<SubCommand> subCommands) {
        this.name = name;
        this.aliases = aliases;
        this.permissions = permissions;
        this.subCommands = subCommands;
    }

    public Command(String name, List<String> aliases, List<String> permissions) {
        this.name = name;
        this.aliases = aliases;
        this.permissions = permissions;
    }

    public Command() {
    }

    public String name() {
        return this.name;
    }

    public List<String> aliases() {
        return this.aliases;
    }

    public List<String> permissions() {
        return this.permissions;
    }

    public List<SubCommand> subCommands() {
        return this.subCommands;
    }
}
