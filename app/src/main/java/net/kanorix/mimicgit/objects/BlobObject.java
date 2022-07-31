package net.kanorix.mimicgit.objects;

import java.util.List;
import java.util.stream.Collectors;

public class BlobObject extends GitObject {

    /** ファイルの内容 */
    private String content;

    public BlobObject(List<String> contents) {
        this.content = contents.stream().collect(Collectors.joining("\n"));
    }

    @Override
    public ObjectType getType() {
        return ObjectType.Blob;
    }

    @Override
    public String getContent() {
        return content;
    }
}
