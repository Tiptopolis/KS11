package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedOperation;

/**
 * <p>A Destroyable is an object which can be destroyed.</p>
 * <p>Typically a Destroyable is a {@link LifeCycle} component that can hold onto
 * resources over multiple start/stop cycles.   A call to destroy will release all
 * resources and will prevent any further start/stop cycles from being successful.</p>
 */
@ManagedObject
public interface Destroyable
{
    @ManagedOperation(value = "Destroys this component", impact = "ACTION")
    void destroy();
}