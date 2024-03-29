package com.uchump.prime.Metatron.Lib._HTTP._Jetty.logging;
import java.util.Locale;

import org.slf4j.event.Level;

public enum JettyLevel
{
    // Intentionally sorted incrementally by level int
    ALL(Level.TRACE.toInt() - 10),
    TRACE(Level.TRACE),
    DEBUG(Level.DEBUG),
    INFO(Level.INFO),
    WARN(Level.WARN),
    ERROR(Level.ERROR),
    OFF(Level.ERROR.toInt() + 1);

    private final Level level;
    private final int levelInt;

    JettyLevel(Level level)
    {
        this.level = level;
        this.levelInt = level.toInt();
    }

    JettyLevel(int i)
    {
        this.level = null;
        this.levelInt = i;
    }

    public static JettyLevel fromLevel(Level slf4jLevel)
    {
        for (JettyLevel level : JettyLevel.values())
        {
            if (slf4jLevel.toInt() == level.levelInt)
                return level;
        }
        return OFF;
    }

    public int toInt()
    {
        return levelInt;
    }

    public Level toLevel()
    {
        return level;
    }

    /**
     * Tests that a provided level is included by the level value of this level.
     *
     * @param testLevel the level to test against.
     * @return true if includes this includes the test level.
     */
    public boolean includes(JettyLevel testLevel)
    {
        return (this.levelInt <= testLevel.levelInt);
    }

    @Override
    public String toString()
    {
        return name();
    }

    public static JettyLevel intToLevel(int levelInt)
    {
        for (JettyLevel level : JettyLevel.values())
        {
            if (levelInt <= level.levelInt)
                return level;
        }
        return OFF;
    }

    public static JettyLevel strToLevel(String levelStr)
    {
        if (levelStr == null)
        {
            return null;
        }

        String levelName = levelStr.trim().toUpperCase(Locale.ENGLISH);
        for (JettyLevel level : JettyLevel.values())
        {
            if (level.name().equals(levelName))
                return level;
        }

        return null;
    }
}