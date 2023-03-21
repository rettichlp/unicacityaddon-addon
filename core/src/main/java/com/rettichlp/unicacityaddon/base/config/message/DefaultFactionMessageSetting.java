package com.rettichlp.unicacityaddon.base.config.message;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;

/**
 * @author RettichLP
 */
public class DefaultFactionMessageSetting extends Config implements FactionMessageSetting {

    @SwitchSetting
    private final ConfigProperty<Boolean> hq = new ConfigProperty<>(true);

    @SwitchSetting
    private final ConfigProperty<Boolean> service = new ConfigProperty<>(true);

    @SwitchSetting
    private final ConfigProperty<Boolean> dBank = new ConfigProperty<>(true);

    @Override
    public ConfigProperty<Boolean> hq() {
        return this.hq;
    }

    @Override
    public ConfigProperty<Boolean> service() {
        return this.service;
    }

    @Override
    public ConfigProperty<Boolean> dBank() {
        return this.dBank;
    }
}