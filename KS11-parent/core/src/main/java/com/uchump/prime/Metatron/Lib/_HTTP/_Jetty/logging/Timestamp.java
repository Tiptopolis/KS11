package com.uchump.prime.Metatron.Lib._HTTP._Jetty.logging;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * <p>
 * This is a super stripped down version of {@code DateCache} from jetty-util.
 * </p>
 *
 * <p>
 * This is a private class, only 1 method is exposed. {@link #formatNow(long, StringBuilder)},
 * Caching of the formatted timestamp up to the current second is still performed.
 * Format updates the StringBuilder directly.
 * And millisecond formatting is done by this class.
 * </p>
 */
class Timestamp
{
    private final DateTimeFormatter tzFormatter;
    private final ZoneId zoneId;
    private volatile Tick tick;

    public static class Tick
    {
        private final long seconds;
        private final String formattedString;

        public Tick(long seconds, String string)
        {
            this.seconds = seconds;
            formattedString = string;
        }
    }

    public Timestamp(TimeZone timeZone)
    {
        tzFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        zoneId = timeZone.toZoneId();
        tzFormatter.withZone(zoneId);
        tick = null;
    }

    /**
     * Format a timestamp according to our stored formatter.
     * The passed time is expected to be close to the current time, so it is
     * compared to the last value passed and if it is within the same second,
     * the format is reused.  Otherwise a new cached format is created.
     *
     * @param now the milliseconds since unix epoch
     */
    public void formatNow(long now, StringBuilder builder)
    {
        long seconds = now / 1000;
        int ms = (int)(now % 1000);

        Tick tick = this.tick;

        // Is this the cached time
        if (tick != null && tick.seconds == seconds)
        {
            builder.append(tick.formattedString);
        }
        else
        {
            formatTick(now, builder);
        }

        if (ms > 99)
        {
            builder.append('.');
        }
        else if (ms > 9)
        {
            builder.append(".0");
        }
        else
        {
            builder.append(".00");
        }
        builder.append(ms);
    }

    protected void formatTick(long now, StringBuilder builder)
    {
        long seconds = now / 1000;

        Tick tick = this.tick;
        // recheck the tick, to save multiple formats
        if (tick == null || tick.seconds != seconds)
        {
            String s = ZonedDateTime.ofInstant(Instant.ofEpochMilli(now), zoneId).format(tzFormatter);
            builder.append(s);
            this.tick = new Tick(seconds, s);
        }
    }
}