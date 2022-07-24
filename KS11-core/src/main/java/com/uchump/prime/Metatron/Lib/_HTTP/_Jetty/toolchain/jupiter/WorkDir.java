package com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain.jupiter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain.FS;

public class WorkDir
{
    private final Path path;

    public WorkDir(Path path)
    {
        this.path = path;
    }

    /**
     * Get the test specific directory to use for work directory.
     * <p>
     * Name is derived from the test classname &amp; method name.
     *
     * @return the test specific directory.
     */
    public Path getPath()
    {
        if (Files.exists(path))
        {
            return path;
        }

        FS.ensureDirExists(path);
        try
        {
            return path.normalize().toRealPath();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Unable to obtain real path: " + path, e);
        }
    }

    /**
     * Get a {@link Path} file reference for content inside of the test specific work directory.
     * <p>
     * Note: No assertions are made if the file exists or not.
     *
     * @param name the path name of the file (supports deep paths)
     * @return the file reference.
     */
    public Path getPathFile(String name)
    {
        return path.resolve(name);
    }

    /**
     * Ensure that the work directory is empty.
     * <p>
     * Useful for repeated testing without using the maven <code>clean</code> goal (such as within Eclipse).
     */
    public void ensureEmpty()
    {
        FS.ensureEmpty(path);
    }

    /**
     * Get the unique work directory while ensuring that it is empty (if not).
     *
     * @return the unique work directory, created, and empty.
     */
    public Path getEmptyPathDir()
    {
        FS.ensureEmpty(path);
        return path;
    }
}