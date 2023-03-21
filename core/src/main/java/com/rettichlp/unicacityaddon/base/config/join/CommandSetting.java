package com.rettichlp.unicacityaddon.base.config.join;

import net.labymod.api.configuration.loader.property.ConfigProperty;

/**
 * @author RettichLP
 */
public interface CommandSetting {

    ConfigProperty<Boolean> enabled();

    ConfigProperty<String> first();

    ConfigProperty<String> second();

    ConfigProperty<String> third();
}