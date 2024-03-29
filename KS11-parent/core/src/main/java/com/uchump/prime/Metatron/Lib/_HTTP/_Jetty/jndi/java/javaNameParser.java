package com.uchump.prime.Metatron.Lib._HTTP._Jetty.jndi.java;

import java.util.Properties;
import javax.naming.CompoundName;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;

// This is a required name for JNDI
// @checkstyle-disable-check : TypeNameCheck

/**
 * javaNameParser
 */
public class javaNameParser implements NameParser
{

    static Properties syntax = new Properties();

    static
    {
        syntax.put("jndi.syntax.direction", "left_to_right");
        syntax.put("jndi.syntax.separator", "/");
        syntax.put("jndi.syntax.ignorecase", "false");
    }

    /**
     * Parse a name into its components.
     *
     * @param name The non-null string name to parse.
     * @return A non-null parsed form of the name using the naming convention
     * of this parser.
     * @throws NamingException If a naming exception was encountered.
     */
    @Override
    public Name parse(String name) throws NamingException
    {
        return new CompoundName(name, syntax);
    }
}