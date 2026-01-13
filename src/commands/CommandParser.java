package commands;

import commands.implementations.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser {

    private final Map<String, Command> commands = new HashMap<>();

    public CommandParser() {
        register("mkdir", new MkdirCommand());
        register("ls", new LsCommand());
        register("rename", new RenameCommand());
        register("cd", new CdCommand());
        register("rmdir", new RmdirCommand());
        register("touch", new TouchCommand());
        register("pwd", new  PwdCommand());

    }

    public void parseAndExecute(String input, CommandContext context) throws CommandException {

        if (input == null || input.isBlank()) return;

        // cada parte do comando é separada em tokens
        List<String> rawTokens = new ArrayList<>();
        Matcher matcher = Pattern.compile("\"([^\"]*)\"|(\\S+)").matcher(input);
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                rawTokens.add(matcher.group(1)); // Conteúdo entre aspas
            } else {
                rawTokens.add(matcher.group(2)); // Palavra normal
            }
        }
        String[] tokens = rawTokens.toArray(new String[0]);

        if (tokens.length == 0) return; // Proteção extra caso o regex não encontre nada

        // o primeiro token representa o nome do comando
        String name = tokens[0];
        // o restante dos tokens sao argumentos, que variam de comando para comando
        String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);

        // a insancia da classe correspondente ao comenado, mapeada no construtor e acessada
        Command command = commands.get(name);

        if (command == null) { // caso o comando nao tenha sido previamente mapeado, é lançada uma exceção
            throw new CommandException("Comando não encontrado: " + name);
        }

        // caso o comando exista, é chamado o metodo execute, implementado na respectiva classe
        command.execute(args, context);
    }

    private void register(String name, Command command) {
        commands.put(name, command);
    }
}
