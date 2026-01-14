package inode.content;

import inode.enums.ContentType;

import java.util.ArrayList;
import java.util.List;

public class FileContent implements Content {

    private final List<String> lines = new ArrayList<>();

    @Override
    public long getSize() {
        return lines.stream()
                .mapToLong(String::length)
                .sum();
    }

    @Override
    public ContentType getType() {
        return ContentType.FILE;
    }

    public void write(String text, boolean append) {
        if (!append) {
            lines.clear();
        }
        lines.add(text);
    }

    public List<String> getLines() {
        return lines;
    }
}