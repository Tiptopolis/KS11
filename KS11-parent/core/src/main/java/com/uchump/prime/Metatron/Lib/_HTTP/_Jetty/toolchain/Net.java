package com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public final class Net
{
    public static boolean isInterfaceAvailableFor(Class<? extends InetAddress> addrClass)
    {
        try
        {
            Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
            while (ifaces.hasMoreElements())
            {
                NetworkInterface iface = ifaces.nextElement();
                Enumeration<InetAddress> addrs = iface.getInetAddresses();
                while (addrs.hasMoreElements())
                {
                    InetAddress addr = addrs.nextElement();
                    if (addrClass.isAssignableFrom(addr.getClass()))
                    {
                        return true;
                    }
                }
            }
        }
        catch (SocketException ignore)
        {
        }
        return false;
    }

    public static boolean isIpv6InterfaceAvailable()
    {
        return isInterfaceAvailableFor(Inet6Address.class);
    }

    public static boolean isIpv4InterfaceAvailable()
    {
        return isInterfaceAvailableFor(Inet4Address.class);
    }
}