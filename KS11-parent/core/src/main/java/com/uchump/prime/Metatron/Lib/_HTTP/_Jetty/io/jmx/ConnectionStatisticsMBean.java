package com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.jmx;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ConnectionStatistics;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.jmx.ObjectMBean;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedAttribute;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;


@ManagedObject
public class ConnectionStatisticsMBean extends ObjectMBean
{
    public ConnectionStatisticsMBean(Object object)
    {
        super(object);
    }

    @ManagedAttribute("ConnectionStatistics grouped by connection class")
    public Collection<String> getConnectionStatisticsGroups()
    {
        ConnectionStatistics delegate = (ConnectionStatistics)getManagedObject();
        Map<String, ConnectionStatistics.Stats> groups = delegate.getConnectionStatisticsGroups();
        return groups.values().stream()
            .sorted(Comparator.comparing(ConnectionStatistics.Stats::getName))
            .map(stats -> stats.dump())
            .map(dump -> dump.replaceAll("[\r\n]", " "))
            .collect(Collectors.toList());
    }
}