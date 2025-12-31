package commands;

import commands.implementations.LsCommand;
import commands.implementations.MkdirCommand;

import java.util.*;

public class CommandParser {

    private final Map<String, Command> commands = new HashMap<>();

    public CommandParser() {
        register("mkdir", new MkdirCommand());
        register("ls", new LsCommand());
        // register("touch", new TouchCommand());
        // register("cd", new CdCommand());
    }

    public void parseAndExecute(String input, CommandContext context) throws CommandException {

        if (input == null || input.isBlank()) return;

        // cada parte do comando é separada em tokens
        String[] tokens = input.trim().split("\\s+");
        // o primeiro token representa o nome do comando
        String name = tokens[0];
        // o restante dos tokens sao argumentos, que variam de comando para comando
        String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);

        // a insancia da classe correspondente ao comenado, mapeada no construtor e acessada
        Command command = commands.get(name);

        if (command == null) { // caso o comando nao tenha sido previamente mapeado, é lançada uma excess�o
            throw new CommandException("Comando não encontrado: " + name);
        }

        // caso o comando exista, é chamado o metodo execute, implementado na respectiva classe
        command.execute(args, context);
    }

    private void register(String name, Command command) {
        commands.put(name, command);
    }
}
