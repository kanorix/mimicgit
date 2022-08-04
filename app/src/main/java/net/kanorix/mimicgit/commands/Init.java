package net.kanorix.mimicgit.commands;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

import net.kanorix.mimicgit.utils.GitRepositoryUtil;
import net.kanorix.mimicgit.utils.RefUtil;
import picocli.CommandLine.Command;

@Command(name = "init")
public class Init implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        Files.createDirectories(GitRepositoryUtil.DIR);
        Files.createDirectories(RefUtil.REFS);
        if (Files.notExists(RefUtil.HEAD)) {
            Files.createFile(RefUtil.HEAD);
        }
        RefUtil.createBranch("main", "");
        System.out.println("Initialized Git repository in " + Paths.get(".").toAbsolutePath());
        return 0;
    }
}
