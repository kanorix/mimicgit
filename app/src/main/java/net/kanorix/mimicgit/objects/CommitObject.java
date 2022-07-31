package net.kanorix.mimicgit.objects;

import java.time.ZonedDateTime;
import java.util.List;

public class CommitObject extends GitObject {

    /** ルートツリーのハッシュ */
    public final String tree;

    // ↓実際だと複数の親コミットを持つことができる
    /** 親コミットのハッシュ */
    public final String parent;

    /** コミットした時間 */
    public final ZonedDateTime timestamp;

    public CommitObject(final List<String> contents) {
        this.tree = contents.get(0).replaceAll("tree ", "");
        this.parent = contents.get(1).replaceAll("parent ", "");
        this.timestamp = ZonedDateTime.parse(contents.get(2).replaceAll("time ", ""));
    }

    public CommitObject(final String tree, final String parent) {
        this.tree = tree;
        this.parent = parent;
        this.timestamp = ZonedDateTime.now();
    }

    @Override
    public ObjectType getType() {
        return ObjectType.Commit;
    }

    @Override
    public String getContent() {
        final String format = """
                tree %s
                parent %s
                time %s
                """;
        return format.formatted(tree, parent, timestamp);
    }
}
