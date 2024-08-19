package newamazingpvp.spigot;

import me.scarsz.jdaappender.ChannelLoggingHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class ConsoleBridge extends JavaPlugin {
    public JDA jda;
    public String consoleChannel;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        new BukkitRunnable() {
            @Override
            public void run() {
                intializeBot();
                //getServer().dispatchCommand(getServer().getConsoleSender(), "chunky continue");
                ChannelLoggingHandler handler = new ChannelLoggingHandler(() -> jda.getTextChannelById(consoleChannel), config -> {
                    config.setColored(getConfig().getBoolean("colored"));
                    config.setSplitCodeBlockForLinks(getConfig().getBoolean("split-code-block-for-links"));
                    config.setAllowLinkEmbeds(getConfig().getBoolean("allow-link-embeds"));
                    /*config.addFilter(logItem -> {
                        String message = logItem.getMessage();
                        return message.contains("not pass event");
                    });*/
                    config.mapLoggerName("net.dv8tion.jda", "JDA");
                    config.mapLoggerName("net.minecraft.server.MinecraftServer", "Server");
                    config.mapLoggerNameFriendly("net.minecraft.server", s -> "Server/" + s);
                    config.mapLoggerNameFriendly("net.minecraft", s -> "Minecraft/" + s);
                    config.mapLoggerName("github.scarsz.discordsrv.dependencies.jda", s -> "DiscordSRV/JDA/" + s);
                }).attachLog4jLogging().schedule();
                handler.schedule();
            }
        }.runTaskLaterAsynchronously(this, 0L);
    }

    public  void intializeBot() {
        String token = getConfig().getString("bot-token");
        consoleChannel = getConfig().getString("console-channel-id");
        //EnumSet<GatewayIntent> allIntents = EnumSet.allOf(GatewayIntent.class);

        JDABuilder jdaBuilder = JDABuilder.createDefault(token);
        //jdaBuilder.enableIntents(allIntents);
        jda = jdaBuilder.build();
        jda.addEventListener(new ConsoleCommand(consoleChannel, this));
    }

    @Override
    public void onDisable() {
    }
}
