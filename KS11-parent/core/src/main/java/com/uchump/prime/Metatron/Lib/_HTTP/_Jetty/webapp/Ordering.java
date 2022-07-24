package com.uchump.prime.Metatron.Lib._HTTP._Jetty.webapp;

import java.util.List;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.resource.Resource;

/**
 * Ordering options for jars in WEB-INF lib.
 */
public interface Ordering {
	public List<Resource> order(List<Resource> fragments);
}