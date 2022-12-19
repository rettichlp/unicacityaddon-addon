package com.rettichlp.unicacityaddon.base.config.reinforcement;

import net.labymod.api.configuration.loader.property.ConfigProperty;

public interface ReinforcementSetting {

    ConfigProperty<String> reinforcement();

    ConfigProperty<String> answer();

    ConfigProperty<Boolean> screen();
}