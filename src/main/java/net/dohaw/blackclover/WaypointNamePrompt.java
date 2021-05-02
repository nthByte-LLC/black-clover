package net.dohaw.blackclover;

import net.dohaw.blackclover.util.SpellUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import java.util.Map;

public class WaypointNamePrompt extends StringPrompt {

    private Player player;
    private Map<String, Location> waypoints;

    public WaypointNamePrompt(Player player, Map<String, Location> waypoints){
        this.waypoints = waypoints;
        this.player = player;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return "What would you like to name this waypoint?";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        if(!waypoints.containsKey(input)){
            this.waypoints.put(input, player.getLocation());
            context.getForWhom().sendRawMessage("This waypoint has been marked!");
            SpellUtils.spawnParticle(player, Particle.FIREWORKS_SPARK, 30, 1, 1, 1);
            SpellUtils.playSound(player, Sound.BLOCK_ANVIL_PLACE);
        }else{
            context.getForWhom().sendRawMessage("This is already a waypoint name!");
            return this;
        }
        return null;
    }

}
