package com.rettichlp.unicacityaddon.base.utils;

import com.sun.tools.javac.comp.Todo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.concurrent.TimeUnit;

/**
 * @author RettichLP
 */
public class TextUtils {

    /**
     * Converts textComponent to a legacy string - <code>&6Hello &b&lworld&c!</code>
     *
     * @param component component to be converted to formatted string
     * @return converted string of component
     */
    public static String legacy(Component component) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(component);
    }

    /**
     * Converts textComponent to a plain string - <code>Hello world!</code>
     *
     * @param component component to be converted to unformatted string
     * @return converted string of component
     */
    public static String plain(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);

        // TODO: 19.12.2022 Laby.labyAPI().renderPipeline().componentRenderer().plainSerializer() ?
    }

    /**
     * Converts a given <code>Long</code> with specific <code>TimeUnit</code> to an easy readable time <code>String</code>
     *
     * @param timeUnit TimeUnit in which the given value is provided
     * @param time Long of the time to be converted
     * @return converted time in readable format
     */
    public static String parseTime(TimeUnit timeUnit, long time) {
        long dd = timeUnit.toDays(time);
        long hh = timeUnit.toHours(time) % 24;
        long mm = timeUnit.toMinutes(time) % 60;
        long ss = timeUnit.toSeconds(time) % 60;

        String days = dd > 0 ? dd + ":" : "";
        String hours = dd > 0 || hh > 0 ? (hh < 10 ? "0" + hh : hh) + ":" : "";
        String minutes = (mm < 10 ? "0" + mm : mm) + ":";
        String seconds = (ss < 10 ? "0" + ss : String.valueOf(ss));

        return days + hours + minutes + seconds;
    }

    /**
     * Converts an array of <code>Object</code> to a <code>String</code> with the space between the objects
     *
     * @param args Array of <code>Object</code>
     * @param space String of space between objects
     * @return String of objects with the space between them
     */
    public static String makeStringByArgs(Object[] args, String space) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object o : args) {
            stringBuilder.append(o).append(space);
        }
        return stringBuilder.substring(0, stringBuilder.length() - space.length());
    }
}
