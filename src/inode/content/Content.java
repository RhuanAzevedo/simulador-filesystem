package inode.content;


import inode.enums.ContentType;

public interface Content {

    long getSize();

    ContentType getType();
}
