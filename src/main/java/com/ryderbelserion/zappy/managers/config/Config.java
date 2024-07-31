package com.ryderbelserion.zappy.managers.config;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import static ch.jalu.configme.properties.PropertyInitializer.newListProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class Config implements SettingsHolder {

    @Override
    public void registerComments(@NotNull CommentsConfiguration conf) {
        conf.setComment("modules.weather.worlds.thunder", """
                All settings related to how thunder works in all worlds,
                The formula used to define the interval check, how long a thunderstorm will last is explained below.
                
                20 ticks = 1 second, so 300 is 15 seconds, the formula is x * s, x is ticks, s is seconds.
                """);
    }

    @Comment("The prefix used in commands")
    public static final Property<String> command_prefix = newProperty("root.prefix", "<blue>[<yellow>Zappy<blue>] <reset>");

    @Comment("Do you want verbose logs in console?")
    public static final Property<Boolean> verbose_logging = newProperty("root.verbose-logging", false);

    @Comment({
            "How often should we check if the world needs a bit of thunder? Set to -1 to disable.",
            "This option will make the world have a thunderstorm, if there isn't one already or if the weather is clear."
    })
    public static final Property<Integer> world_time = newProperty("modules.weather.worlds.thunder.time", 300);

    @Comment({
            "A list of worlds where thunderstorms will be enabled more frequently.",
            "If you wish to disable the feature, set the config option above to -1"
    })
    public static final Property<List<String>> worlds = newListProperty("modules.weather.worlds.thunder.list", List.of("world"));
}