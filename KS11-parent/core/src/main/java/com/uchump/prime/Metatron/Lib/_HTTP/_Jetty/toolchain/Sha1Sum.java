package com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Calculate the sha1sum for various content
 */
public class Sha1Sum
{

    public static String calculate(File file) throws NoSuchAlgorithmException, IOException
    {
        return calculate(file.toPath());
    }

    public static String calculate(Path path) throws NoSuchAlgorithmException, IOException
    {
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        try (InputStream in = Files.newInputStream(path, StandardOpenOption.READ);
             NoOpOutputStream noop = new NoOpOutputStream();
             DigestOutputStream digester = new DigestOutputStream(noop, digest))
        {
            IO.copy(in, digester);
            return Hex.asHex(digest.digest());
        }
    }

    public static String calculate(byte[] buf) throws NoSuchAlgorithmException
    {
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        digest.update(buf);
        return Hex.asHex(digest.digest());
    }

    public static String calculate(byte[] buf, int offset, int len) throws NoSuchAlgorithmException
    {
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        digest.update(buf, offset, len);
        return Hex.asHex(digest.digest());
    }

    public static String loadSha1(File sha1File) throws IOException
    {
        String contents = IO.readToString(sha1File);
        Pattern pat = Pattern.compile("^[0-9A-Fa-f]*");
        Matcher mat = pat.matcher(contents);
        assertTrue(mat.find(), "Should have found HEX code in SHA1 file: " + sha1File);
        return mat.group();
    }
}