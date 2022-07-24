package com.uchump.prime.Metatron.Lib._HTTP._Jetty.security;

/**
 * @version $Rev: 4466 $ $Date: 2009-02-10 23:42:54 +0100 (Tue, 10 Feb 2009) $
 */
public enum UserDataConstraint
{
    None, Integral, Confidential;

    public static UserDataConstraint get(int dataConstraint)
    {
        if (dataConstraint < -1 || dataConstraint > 2)
            throw new IllegalArgumentException("Expected -1, 0, 1, or 2, not: " + dataConstraint);
        if (dataConstraint == -1)
            return None;
        return values()[dataConstraint];
    }

    public UserDataConstraint combine(UserDataConstraint other)
    {
        if (this.compareTo(other) < 0)
            return this;
        return other;
    }
}