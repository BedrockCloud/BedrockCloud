package com.bedrockcloud.bedrockcloud.command;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.console.Loggable;

public class CommandManager extends Thread implements Loggable {
    private final Set<Command> commands;
    private String message;

    public CommandManager() {
        this.commands = new HashSet<Command>();
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (BedrockCloud.isRunning()) {
            String answer = scanner.nextLine();
            final String cmd = answer.split(" ")[0];
            final String[] args = this.dropFirstString(answer.split(" "));
            this.executeCommand(cmd, args);
        }
        scanner.close();
    }

    public String[] dropFirstString(final String[] input) {
        final String[] result = new String[input.length - 1];
        System.arraycopy(input, 1, result, 0, input.length - 1);
        return result;
    }

    public void addCommand(final Command command) {
        this.commands.add(command);
    }

    public void executeCommand(final String commandName, final String[] args) {
        final Command command = this.getCommand(commandName);
        if (command != null) {
            command.onCommand(args);
        } else if (!commandName.equals("")) {
            BedrockCloud.getLogger().command("This Command doesn't exist. Try help for more!");
        }
    }

    public Command getCommand(final String commandName) {
        for (final Command command : this.commands) {
            if (command.getCommand().equalsIgnoreCase(commandName)) {
                return command;
            }
        }
        return null;
    }

    public Command getCommand(final Class<? extends Command> commandClass) {
        for (final Command command : this.commands) {
            if (command.getClass().equals(commandClass)) {
                return command;
            }
        }
        return null;
    }

    public Set<Command> getCommands() {
        return this.commands;
    }
}