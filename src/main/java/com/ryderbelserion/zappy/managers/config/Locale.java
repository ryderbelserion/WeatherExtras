package com.ryderbelserion.zappy.managers.config;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import org.jetbrains.annotations.NotNull;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class Locale implements SettingsHolder {

    @Override
    public void registerComments(@NotNull CommentsConfiguration conf) {
        String[] header = {
                "Github: https://github.com/ryderbelserion",
                "",
                "Issues: https://github.com/ryderbelserion/Zappy/issues",
                "Features: https://github.com/ryderbelserion/Zappy/issues",
                "",
                "All messages allow the use of {prefix} unless stated otherwise.",
                ""
        };

        conf.setComment("root", header);
    }

    @Comment("A list of available placeholders: {command}")
    public static final Property<String> unknown_command = newProperty("root.unknown-command", "{prefix}<yellow>{command} <blue>is not a known command.");

    @Comment("A list of available placeholders: {usage}")
    public static final Property<String> correct_usage = newProperty("root.correct-usage", "{prefix}<blue>The correct usage for this command is <yellow>{usage}");

    public static final Property<String> no_permission = newProperty("root.no-permission", "{prefix}<blue>You do not have permission to use that command!");

    public static final Property<String> reloaded_plugin = newProperty("root.reload-plugin", "{prefix}<blue>You have reloaded <yellow>Zappy");

    public static final Property<String> must_be_console_sender = newProperty("root.not-console-sender", "{prefix}<blue>You must be using console to use this command.");

    public static final Property<String> not_a_player = newProperty("root.not-a-player", "{prefix}<blue>You must be a player to run this command.");

    public static final Property<String> not_a_block = newProperty("root.not-a-block", "{prefix}<blue>That is not a block.");
}