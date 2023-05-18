package com.rettichlp.unicacityaddon.base.nametags;

import com.rettichlp.unicacityaddon.UnicacityAddon;
import com.rettichlp.unicacityaddon.base.config.UnicacityAddonConfiguration;
import com.rettichlp.unicacityaddon.base.text.ColorCode;
import com.rettichlp.unicacityaddon.base.text.Message;
import com.rettichlp.unicacityaddon.listener.faction.badfaction.blacklist.BlacklistListener;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.render.font.RenderableComponent;
import org.jetbrains.annotations.Nullable;

/**
 * @author RettichLP
 */
public class OutlawTag extends NameTag {

    private final UnicacityAddon unicacityAddon;

    private OutlawTag(UnicacityAddon unicacityAddon) {
        this.unicacityAddon = unicacityAddon;
    }

    public static OutlawTag create(UnicacityAddon unicacityAddon) {
        return new OutlawTag(unicacityAddon);
    }

    @Override
    protected @Nullable RenderableComponent getRenderableComponent() {
        UnicacityAddonConfiguration unicacityAddonConfiguration = this.unicacityAddon.configuration();
        boolean isEnabled = unicacityAddonConfiguration.enabled().get() && unicacityAddonConfiguration.nameTagSetting().specificNameTagSetting().enabled().get();

        return isEnabled ? this.unicacityAddon.player().getWorld().getPlayers().stream()
                .filter(p -> p.gameUser().getUniqueId().equals(this.entity.getUniqueId()))
                .findFirst()
                .map(player -> getComponent(player.getName())).orElse(null) : null;
    }

    private RenderableComponent getComponent(String playerName) {
        Component component = Message.getBuilder()
                .of("[").color(ColorCode.DARK_GRAY).advance()
                .of("V").color(ColorCode.RED).advance()
                .of("]").color(ColorCode.DARK_GRAY).advance()
                .createComponent();

        boolean isOutlaw = BlacklistListener.BLACKLIST_MAP.containsKey(playerName) && BlacklistListener.BLACKLIST_MAP.get(playerName);
        return isOutlaw ? RenderableComponent.of(component) : null;
    }
}