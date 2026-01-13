package commands;

import core.VirtualFileSystem;
import inode.Inode;
import inode.content.DirectoryContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CommandContext {

    private final VirtualFileSystem vfs;
    private Inode currentDirectory;
    private final LinkedList<String> pathStack;
    private final List<String> history;

    public CommandContext(VirtualFileSystem vfs) {
        this.vfs = vfs;
        this.currentDirectory = vfs.getRoot();
        this.pathStack = new LinkedList<>();
        this.history = new ArrayList<>();
    }

    // --- Getters básicos ---

    public VirtualFileSystem getFileSystem() {
        return vfs;
    }

    public Inode getCurrentDirectory() {
        return currentDirectory;
    }

    public String getCurrentPath() {
        if (pathStack.isEmpty()) return "/";
        return "/" + String.join("/", pathStack);
    }

    // --- Métodos de Alteração de Estado (Usados pelo CD) ---

    public void enterDirectory(String name, Inode nextDir) {
        this.currentDirectory = nextDir;
        this.pathStack.add(name);
    }

    public void leaveDirectory(Inode parentDir) {
        this.currentDirectory = parentDir;
        if (!pathStack.isEmpty()) {
            this.pathStack.removeLast();
        }
    }

    public void goToRoot() {
        this.currentDirectory = vfs.getRoot();
        this.pathStack.clear();
    }

    public void setCurrentDirectory(Inode dir) {
        this.currentDirectory = dir;
    }

    public void addHistory(String command) {
        if (command != null && !command.isBlank()) {
            history.add(command);
        }
    }

    public List<String> getHistory() {
        return Collections.unmodifiableList(history);
    }

    // Metodo Utilitário para achar caminhos.
    // Recebe uma string (ex: "home/user/../docs") e retorna o Inode correspondente
    public Inode resolvePath(String path) {
        if (path == null || path.isBlank()) return null;

        Inode node = this.currentDirectory;

        // Se for absoluto, começa da raiz
        if (path.startsWith("/")) {
            node = vfs.getRoot();
        }

        String[] steps = path.split("/");

        for (String step : steps) {
            // Ignora vazios e o próprio diretório
            if (step.isEmpty() || step.equals(".")) {
                continue;
            }

            // Se no meio do caminho achar algo que não é diretório, falhou
            if (!node.isDirectory()) {
                return null;
            }

            DirectoryContent dir = node.asDirectory();

            if (step.equals("..")) {
                // Navega para o pai
                Inode parent = dir.getEntries().get("..");
                if (parent != null) {
                    node = parent;
                }
            } else {
                // Navega para o filho
                Inode next = dir.getEntries().get(step);
                if (next == null) {
                    return null; // Não existe
                }
                node = next;
            }
        }

        return node;
    }
}
