package net.kanorix.mimicgit.objects;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.codec.digest.DigestUtils;

public abstract class GitObject {

    /** GitObjectの種類 */
    public enum ObjectType {
        Blob, Tree, Commit
    }

    /**
     * GitObjectの種類を取得します。
     *
     * @return GitObjectの種類
     */
    public abstract ObjectType getType();

    /**
     * 内容を取得します。
     *
     * @return 内容
     */
    public abstract String getContent();

    /**
     * 内容からハッシュを取得します。
     *
     * @return ハッシュ
     */
    public String getHash() {
        return DigestUtils.sha1Hex(getContent());
    }

    public String inspect() {
        return Stream.of("""
                =========== < Inspect Git Object > ===========
                type: %s
                hash: %s
                ---------------------------------------------
                %s
                ==============================================
                """.formatted(getType(), getHash(), getContent())
                .split("\n")).map(s -> "    " + s)
                .collect(Collectors.joining("\n"));
    }

    /**
     * 文字列化します。
     */
    @Override
    public String toString() {
        return """
                %s
                %s
                """.formatted(getType(), getContent());
    }
}
