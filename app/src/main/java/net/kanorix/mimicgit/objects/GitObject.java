package net.kanorix.mimicgit.objects;

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
