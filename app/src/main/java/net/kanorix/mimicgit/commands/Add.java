package net.kanorix.mimicgit.commands;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import net.kanorix.mimicgit.utils.IndexUtil;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "add")
public class Add implements Callable<Integer> {

    @Parameters(description = "indexに追加するファイル")
    private Path path;

    @Override
    public Integer call() throws IOException {

        // 指定されたパスをIndexに追加する
        IndexUtil.update(path);

        return 0;
    }
}
