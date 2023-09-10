package com.rettichlp.unicacityaddon.base.gangzones;

import com.rettichlp.unicacityaddon.UnicacityAddon;
import com.rettichlp.unicacityaddon.base.enums.faction.Faction;
import net.labymod.api.util.Pair;
import net.labymod.api.util.math.vector.FloatVector3;

import java.util.List;

public class AttackableGangzoneHafen extends AbstractAttackableGangzone {

    public AttackableGangzoneHafen(UnicacityAddon unicacityAddon) {
        super(unicacityAddon);
    }

    @Override
    public Faction owner() {
        return null;
    }

    @Override
    public List<Pair<FloatVector3, FloatVector3>> gangzoneFacades() {
        return List.of(
                Pair.of(new FloatVector3(-423, 69, 2), new FloatVector3(-423, 69, 177)),
                Pair.of(new FloatVector3(-423, 69, 177), new FloatVector3(-322, 69, 177)),
                Pair.of(new FloatVector3(-322, 69, 177), new FloatVector3(-322, 69, 2)),
                Pair.of(new FloatVector3(-322, 69, 2), new FloatVector3(-423, 69, 2))
        );
    }

    @Override
    public List<Pair<FloatVector3, FloatVector3>> gangwarFacades() {
        return null;
    }
}
