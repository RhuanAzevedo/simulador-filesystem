package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import inode.Inode;
import inode.content.DirectoryContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TreeCommand implements Command {

    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        Inode current = context.getCurrentDirectory();

        System.out.println(".");

        printSubTree(current, "");
    }

    private void printSubTree(Inode inode, String prefix) {
        // Segurança: se por algum motivo cair num arquivo, para.
        if (!inode.isDirectory()) return;

        DirectoryContent content = inode.asDirectory();
        Map<String, Inode> entries = content.getEntries();

        // Lista auxiliar para ignorar "." e ".." (evita loop infinito)
        List<String> visibleNames = new ArrayList<>();

        for (String name : entries.keySet()) {
            if (!name.equals(".") && !name.equals("..")) {
                visibleNames.add(name);
            }
        }

        Collections.sort(visibleNames);

        // Loop para imprimir e descer
        for (int i = 0; i < visibleNames.size(); i++) {
            String name = visibleNames.get(i);
            Inode childNode = entries.get(name);

            // Verifica se é o último item da lista atual
            boolean isLastItem = (i == visibleNames.size() - 1);

            String connector = isLastItem ? "└── " : "├── ";

            // Prefixo acumulado + Conector + Nome
            System.out.println(prefix + connector + name);

            // Se for diretório, chama a recursão (desce o nível)
            if (childNode.isDirectory()) {
                // Truque visual:
                // Se o pai era o último, os filhos não precisam da barra vertical "|" ao lado
                // Se o pai NÃO era o último, os filhos precisam da barra "|" para conectar com o de baixo
                String newPrefix = prefix + (isLastItem ? "    " : "│   ");

                printSubTree(childNode, newPrefix);
            }
        }
    }
}