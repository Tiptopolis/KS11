package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;

import java.util.EventListener;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.ContentProvider;

/**
 * A {@link ContentProvider} that notifies listeners that content is available.
 *
 * @deprecated no replacement, use {@link Request.Content} instead.
 */
@Deprecated
public interface AsyncContentProvider extends ContentProvider
{
    /**
     * @param listener the listener to be notified of content availability
     */
    public void setListener(Listener listener);

    /**
     * A listener that is notified of content availability
     */
    public interface Listener extends EventListener
    {
        /**
         * Callback method invoked when content is available
         */
        public void onContent();
    }
}