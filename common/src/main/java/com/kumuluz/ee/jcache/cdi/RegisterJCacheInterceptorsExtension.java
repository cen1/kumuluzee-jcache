package com.kumuluz.ee.jcache.cdi;

import org.jsr107.ri.annotations.cdi.CachePutInterceptor;
import org.jsr107.ri.annotations.cdi.CacheRemoveAllInterceptor;
import org.jsr107.ri.annotations.cdi.CacheRemoveEntryInterceptor;
import org.jsr107.ri.annotations.cdi.CacheResultInterceptor;
import org.tomitribe.jcache.cdi.CacheManagerBean;
import org.tomitribe.jcache.cdi.CacheProviderBean;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterTypeDiscovery;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Logger;

public class RegisterJCacheInterceptorsExtension implements Extension {

    private static final Logger log = Logger.getLogger(RegisterJCacheInterceptorsExtension.class.getName());

    private CacheManager cacheManager;
    private CachingProvider cachingProvider;

    public void observeAfterTypeDiscovery(@Observes AfterTypeDiscovery afterTypeDiscovery) {
        afterTypeDiscovery.getInterceptors().add(CacheResultInterceptor.class);
        afterTypeDiscovery.getInterceptors().add(CacheRemoveEntryInterceptor.class);
        afterTypeDiscovery.getInterceptors().add(CacheRemoveAllInterceptor.class);
        afterTypeDiscovery.getInterceptors().add(CachePutInterceptor.class);
    }

    public void observeAfterBeanDiscovery(@Observes AfterBeanDiscovery afterBeanDiscovery) {

        cachingProvider = Caching.getCachingProvider();
        cacheManager = cachingProvider.getCacheManager();

        afterBeanDiscovery.addBean(new CacheManagerBean(cacheManager));
        log.info("Added CacheManager bean");
        afterBeanDiscovery.addBean(new CacheProviderBean(cachingProvider));
        log.info("Added CacheProvider bean");
    }

    public void destroyIfCreated(final @Observes BeforeShutdown beforeShutdown)
    {
        if (cacheManager != null) {
            cacheManager.close();
            log.info("Closed CacheManager");
        }
        if (cachingProvider != null) {
            cachingProvider.close();
            log.info("Closed CachingProvider");
        }
    }
}
