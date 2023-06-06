package com.rettichlp.unicacityaddon.base.services;

import com.rettichlp.unicacityaddon.UnicacityAddon;
import com.rettichlp.unicacityaddon.base.enums.location.ATM;
import com.rettichlp.unicacityaddon.base.enums.location.Bus;
import com.rettichlp.unicacityaddon.base.enums.location.Job;
import com.rettichlp.unicacityaddon.base.models.api.NaviPoint;
import net.labymod.api.util.math.vector.FloatVector3;
import org.spongepowered.include.com.google.common.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author RettichLP
 * @see <a href="https://github.com/paulzhng/UCUtils/blob/e1e4cc90a852a24fbb552413eb478097f865c6f3/src/main/java/de/fuzzlemann/ucutils/utils/location/navigation/NavigationUtil.java">UCUtils by paulzhng</a>
 */
public class NavigationService {

    private final UnicacityAddon unicacityAddon;

    public NavigationService(UnicacityAddon unicacityAddon) {
        this.unicacityAddon = unicacityAddon;
    }

    public Map.Entry<Double, ATM> getNearestATM() {
        return getNearest(this.unicacityAddon.player().getLocation(), ATM.values(), ATM::getLocation);
    }

    public Map.Entry<Double, Job> getNearestJob() {
        return getNearest(this.unicacityAddon.player().getLocation(), Job.values(), Job::getLocation);
    }

    public Map.Entry<Double, Bus> getNearestBus(FloatVector3 position) {
        return getNearest(position, Bus.values(), Bus::getLocation);
    }

    public Map.Entry<Double, NaviPoint> getNearestNaviPoint(int x, int y, int z) {
        return getNearestNaviPoint(new FloatVector3(x, y, z));
    }

    public Map.Entry<Double, NaviPoint> getNearestNaviPoint(FloatVector3 position) {
        return getNearest(position, this.unicacityAddon.api().getNaviPointList(), NaviPoint::getLocation);
    }

    public <T> Map.Entry<Double, T> getNearest(FloatVector3 position, T[] elements, Function<T, FloatVector3> positionExtractor) {
        return getNearest(position, List.of(elements), positionExtractor);
    }

    public <T> Map.Entry<Double, T> getNearest(FloatVector3 position, Collection<T> elements, Function<T, FloatVector3> positionExtractor) {
        T nearestElement = null;
        double nearestDistance = Double.MAX_VALUE;

        for (T element : elements) {
            double distance = position.distance(positionExtractor.apply(element));
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestElement = element;
            }
        }

        return Maps.immutableEntry(nearestDistance, nearestElement);
    }
}