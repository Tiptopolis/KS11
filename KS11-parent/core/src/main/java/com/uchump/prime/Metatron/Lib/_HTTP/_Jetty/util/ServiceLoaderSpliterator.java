package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Spliterator;
import java.util.function.Consumer;

class ServiceLoaderSpliterator<T> implements Spliterator<ServiceLoader.Provider<T>>
{
    private final Iterator<T> iterator;

    public ServiceLoaderSpliterator(ServiceLoader<T> serviceLoader)
    {
        iterator = serviceLoader.iterator();
    }

    @Override
    public boolean tryAdvance(Consumer<? super ServiceLoader.Provider<T>> action)
    {
        ServiceProvider<T> next;
        try
        {
            if (!iterator.hasNext())
                return false;
            next = new ServiceProvider<>(iterator.next());
        }
        catch (Throwable t)
        {
            next = new ServiceProvider<>(t);
        }

        action.accept(next);
        return true;
    }

    @Override
    public Spliterator<ServiceLoader.Provider<T>> trySplit()
    {
        return null;
    }

    @Override
    public long estimateSize()
    {
        return Long.MAX_VALUE;
    }

    @Override
    public int characteristics()
    {
        return Spliterator.ORDERED;
    }

    /**
     * An implementation of the {@link ServiceLoader.Provider} which contains either an instance of the service or
     * an error to be thrown when someone calls {@link #get()}.
     * @param <T> the service type.
     */
    private static class ServiceProvider<T> implements ServiceLoader.Provider<T>
    {
        private final T service;
        private final Throwable error;

        public ServiceProvider(T service)
        {
            this.service = service;
            this.error = null;
        }

        public ServiceProvider(Throwable error)
        {
            this.service = null;
            this.error = error;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Class<? extends T> type()
        {
            return (Class<? extends T>)get().getClass();
        }

        @Override
        public T get()
        {
            if (service == null)
                throw new ServiceConfigurationError("", error);
            return service;
        }
    }
}