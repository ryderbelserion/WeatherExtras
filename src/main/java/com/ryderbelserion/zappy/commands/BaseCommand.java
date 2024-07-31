package com.ryderbelserion.zappy.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.vital.paper.commands.Command;
import com.ryderbelserion.vital.paper.commands.CommandData;
import com.ryderbelserion.zappy.Zappy;
import com.ryderbelserion.zappy.enums.Messages;
import com.ryderbelserion.zappy.managers.ConfigManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

public class BaseCommand extends Command {

    private final Zappy plugin = Zappy.getPlugin();

    @Override
    public void execute(final CommandData data) {
        ConfigManager.refresh();

        this.plugin.getZappy().apply(zappy -> {
            // reload the module loader.
            zappy.getModuleLoader().reload();
        });

        Messages.reloaded_plugin.sendMessage(data.getCommandSender());
    }

    @Override
    public @NotNull final String getPermission() {
        return "zappy.access";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("zappy")
                .requires(source -> source.getSender().hasPermission(getPermission()))
                .executes(context -> {
                    execute(new CommandData(context));

                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                })
                .build();
    }

    @Override
    public @NotNull final Command registerPermission() {
        final Permission permission = this.plugin.getServer().getPluginManager().getPermission(getPermission());

        if (permission == null) {
            this.plugin.getServer().getPluginManager().addPermission(new Permission(getPermission(), PermissionDefault.OP));
        }

        return this;
    }
}