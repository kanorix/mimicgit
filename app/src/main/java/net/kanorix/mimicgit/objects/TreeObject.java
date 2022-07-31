package net.kanorix.mimicgit.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TreeObject extends GitObject {

    /** 名前とGitObjectのマップ */
    private Map<String, GitObject> entries = new HashMap<>();

    public TreeObject() {}

    public TreeObject(final Map<String, GitObject> entries) {
        this.entries = entries;
    }

    /**
     * Treeのエントリを取得します。
     *
     * @return エントリ
     */
    public Set<Entry<String, GitObject>> entrySet() {
        return entries.entrySet();
    }

    /**
     * 指定された名前のGitObjectを取得します。
     *
     * @param name 名前
     * @return GitObject
     */
    public Optional<GitObject> get(final String name) {
        return entries.containsKey(name)
                ? Optional.of(entries.get(name))
                : Optional.empty();
    }

    /**
     * 指定された名前でGitObjectを置きます。
     *
     * @param name 名前
     * @param object GitObject
     * @return 置かれたGitObject
     */
    public GitObject put(final String name, final GitObject object) {
        entries.put(name, object);
        return object;
    }

    @Override
    public ObjectType getType() {
        return ObjectType.Tree;
    }

    @Override
    public String getContent() {
        return entries.entrySet().stream()
                .map(e -> "%s %s".formatted(e.getKey(), e.getValue().getHash()))
                .collect(Collectors.joining("\n"));
    }
}
