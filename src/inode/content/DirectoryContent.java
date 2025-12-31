package inode.content;

import inode.enums.ContentType;
import inode.Inode;

import java.util.HashMap;
import java.util.Map;

public class DirectoryContent implements Content {
    private final Map<String, Inode> entries = new HashMap<>();

    @Override
    public long getSize() {
        return entries.values()
                .stream()
                .filter(i -> !isSpecialEntry(i))
                .mapToLong(Inode::getSize)
                .sum();
    }

    @Override
    public ContentType getType() {
        return ContentType.DIRECTORY;
    }

    public void init(Inode self, Inode parent) {
        entries.put(".", self);
        entries.put("..", parent != null ? parent : self);
    }

    public Map<String, Inode> getEntries() {
        return this.entries;
    }

    public boolean contains(String name) {
        return this.entries.containsKey(name);
    }

    public void addChild(String name, Inode inode) {
        this.entries.put(name, inode);
    }

    public void remove(String name) {
        this.entries.remove(name);
    }

    private boolean isSpecialEntry(Inode inode) {
        return inode == this.entries.get(".") || inode == this.entries.get("..");
    }


}
