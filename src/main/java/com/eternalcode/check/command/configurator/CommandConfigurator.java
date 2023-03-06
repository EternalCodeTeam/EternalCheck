package com.eternalcode.check.command.configurator;

import com.eternalcode.check.command.CommandConfiguration;
import dev.rollczi.litecommands.factory.CommandEditor;

public class CommandConfigurator implements CommandEditor {

    private final CommandConfiguration commandConfiguration;

    public CommandConfigurator(CommandConfiguration commandConfiguration) {
        this.commandConfiguration = commandConfiguration;
    }

    @Override
    public State edit(State state) {
        Command command = this.commandConfiguration.commands.get(state.getName());

        if (command == null) {
            return state;
        }

        if (command.subCommands().size() >= 1) {
            for (SubCommand subCommand : command.subCommands()) {
                state = state.editChild(subCommand.name(), editor -> editor
                        .name(subCommand.alias())
                        .permission(subCommand.permissions(), true));
            }
        }

        return state.name(command.name())
                .aliases(command.aliases(), true)
                .permission(command.permissions(), true);

    }
}

