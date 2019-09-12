package com.kumuluz.ee.jcache.caffeine.config;

import com.github.benmanes.caffeine.jcache.configuration.TypesafeConfigSupplier;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.logging.Logger;

public class KumuluzEETypesafeConfigSupplier implements TypesafeConfigSupplier {

    private static final Logger log = Logger.getLogger(KumuluzEETypesafeConfigSupplier.class.getName());

    private final static String CONFIG_PREFIX = "kumuluzee.jcache.caffeine";

    @Override
    public Config getConfig() {
        Config defaultConfig = ConfigFactory.load();

        ConfigurationUtil confUtil = ConfigurationUtil.getInstance();

        if (confUtil.get(CONFIG_PREFIX).isPresent()) {

            log.info("Custom JCache config detected");

            String eeConfig = confUtil.get(CONFIG_PREFIX).get();
            Config customConfig = ConfigFactory.parseString(eeConfig);

            Config finalConfig = customConfig.withFallback(defaultConfig);
            log.info(finalConfig.root().render());

            return finalConfig;
        }

        log.info("No {} prefix found, resolving to default JCache configuration");
        log.info(defaultConfig.root().render());

        return defaultConfig;
    }
}
