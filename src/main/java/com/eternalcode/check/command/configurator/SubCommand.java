package com.eternalcode.check.command.configurator;

import java.util.List;

public class SubCommand {

    private String name;
    private String alias;
    private List<String> permissions;

    public SubCommand(String name, String alias, List<String> permissions) {
        this.name = name;
        this.alias = alias;
        this.permissions = permissions;
    }

    public SubCommand() {
    }

    public String name() {
        return this.name;
    }

    public String alias() {
        return this.alias;
    }

    public List<String> permissions() {
        return this.permissions;
    }
}
