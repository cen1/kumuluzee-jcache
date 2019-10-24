package com.kumuluz.ee.jcache.caffeine.config;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * @author cen1
 * @since 1.0.0
 */
public class JCacheCaffeineConfigSupplier implements Supplier<Config> {

    private static final Logger log = Logger.getLogger(JCacheCaffeineConfigSupplier.class.getName());

    private final static String CONFIG_PREFIX = "kumuluzee.jcache.caffeine";

    @Override
    public Config get() {
        Config defaultConfig = ConfigFactory.load();

        ConfigurationUtil confUtil = ConfigurationUtil.getInstance();

        if (confUtil.get(CONFIG_PREFIX).isPresent()) {

            log.info("Custom JCache-Caffeine config detected");

            String eeConfig = confUtil.get(CONFIG_PREFIX).get();
            Config customConfig = ConfigFactory.parseString(eeConfig);

            Config finalConfig = customConfig.withFallback(defaultConfig);
            log.info(finalConfig.root().render());

            return finalConfig;
        }

        log.info("No {} prefix found, resolving to default JCache-Caffeine configuration");
        log.info(defaultConfig.root().render());

        return defaultConfig;
    }
}
