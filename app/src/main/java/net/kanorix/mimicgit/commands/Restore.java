package net.kanorix.mimicgit.commands;

import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.Callable;

import net.kanorix.mimicgit.objects.BlobObject;
import net.kanorix.mimicgit.objects.CommitObject;
import net.kanorix.mimicgit.objects.TreeObject;
import net.kanorix.mimicgit.utils.GitRepositoryUtil;
import net.kanorix.mimicgit.utils.RefUtil;
import net.kanorix.mimicgit.utils.TreeUtil;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "restore")
public class Restore implements Callable<Integer> {

    @Option(names = { "--source" }, description = "対象のコミットハッシュ値")
    private String source;

    @Parameters(description = "復元するファイルのパス")
    private Path filename;

    @Override
    public Integer call() throws Exception {
        // 指定されたコミットかHEADが指すコミット
        final var commitHash = Optional.ofNullable(source)
                .or(() -> RefUtil.resolveHead())
                .orElseThrow();

        // CommitのハッシュからObjectを取得する
        final var commit = (CommitObject) GitRepositoryUtil.find(commitHash);
        final var tree = (TreeObject) GitRepositoryUtil.find(commit.tree);

        // ワーキングディレクトリを復元する
        final var target = TreeUtil.get(tree, filename).orElseThrow();
        if (target instanceof BlobObject) {
            GitRepositoryUtil.restore(filename, (BlobObject) target);
        } else {
            GitRepositoryUtil.restore(filename, (TreeObject) target);
        }

        return 0;
    }
}
