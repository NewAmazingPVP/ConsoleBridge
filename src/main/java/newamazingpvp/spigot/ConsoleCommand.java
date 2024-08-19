package newamazingpvp.spigot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getServer;

public class ConsoleCommand extends ListenerAdapter {
    String channel;
    Plugin plugin;
    public ConsoleCommand(String channel, Plugin plugin){
        this.channel = channel;
        this.plugin = plugin;
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.isWebhookMessage()) {
            return;
        }
        if (!event.getChannel().getId().equals(channel)) return;
        String messageContent = event.getMessage().getContentRaw();
        Bukkit.getScheduler().runTask(plugin, () -> {
            getServer().dispatchCommand(getServer().getConsoleSender(), messageContent);
        });
    }
}
