package com.rettichlp.unicacityaddon.hudwidgets;

import com.rettichlp.unicacityaddon.UnicacityAddon;
import com.rettichlp.unicacityaddon.base.AddonPlayer;
import com.rettichlp.unicacityaddon.base.events.WeaponUpdateEvent;
import com.rettichlp.unicacityaddon.base.text.ColorCode;
import net.labymod.api.client.entity.LivingEntity;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Subscribe;

import java.util.Optional;

/**
 * @author RettichLP
 */
public class AmmunitionHudWidget extends TextHudWidget<TextHudWidgetConfig> {

    private TextLine textLine;

    private final UnicacityAddon unicacityAddon;

    public AmmunitionHudWidget(UnicacityAddon unicacityAddon) {
        super("ammunition");
        this.unicacityAddon = unicacityAddon;
    }

    @Override
    public void load(TextHudWidgetConfig config) {
        super.load(config);
        this.textLine = super.createLine("Munition", ColorCode.RED.getCode() + "0" + ColorCode.DARK_GRAY.getCode() + "/" + ColorCode.GOLD.getCode() + "0");
        this.setIcon(this.unicacityAddon.utilService().icon());
    }

    @Override
    public boolean isVisibleInGame() {
        AddonPlayer p = this.unicacityAddon.player();

        boolean itemAllowed = Optional.ofNullable(p.getPlayer())
                .map(LivingEntity::getMainHandItemStack)
                .map(ItemStack::getDisplayName)
                .map(component -> this.unicacityAddon.utilService().text().plain(component)).stream()
                .anyMatch(s -> s.equalsIgnoreCase("Baseballschläger")
                        || s.equalsIgnoreCase("Einsatzschild")
                        || s.equalsIgnoreCase("Messer"));

        return p.getWeaponInMainHand() != null || itemAllowed;
    }

    @Subscribe
    public void onWeaponShot(WeaponUpdateEvent e) {
        this.textLine.updateAndFlush(e.getWeaponAmmunitionText());
    }
}