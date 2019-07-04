package org.savage.skyblock.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.savage.skyblock.API.IslandCreatedEvent;
import org.savage.skyblock.SkyBlock;
import org.savage.skyblock.island.Island;

public class IslandEvents implements Listener {

    @EventHandler
    public void islandCreate(IslandCreatedEvent e) {
        Island island = e.getIsland();
        //when an island is created without fail
        SkyBlock.getInstance().getIslandUtils().calculateIslandLevel(island);
        SkyBlock.getInstance().getIslandUtils().calculateIslandTop();
    }

    @EventHandler
    public void move(PlayerMoveEvent e){
        Player p = e.getPlayer();
        if (p.getLocation().getY() <= -10){
            //teleport them to their island
            SkyBlock.getInstance().safePlayers.add(p);
            //check if they have an island first, then teleport, if not teleport to is spawn
            if (SkyBlock.getInstance().getIslandUtils().getIsland(p.getUniqueId()) != null) {
                p.teleport(SkyBlock.getInstance().getIslandUtils().getIsland(p.getUniqueId()).getHome());
            }else{
                //is null, teleport to is spawn
                Location spawnLoc = SkyBlock.getInstance().getIslandUtils().getIslandSpawn();
               if (spawnLoc != null){
                   p.teleport(spawnLoc);
               }else{
                   p.sendMessage(SkyBlock.getInstance().getUtils().getMessage("noSpawn"));
                   p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
               }
            }
            SkyBlock.getInstance().safePlayers.remove(p);
        }
    }

    @EventHandler
    public void damage(EntityDamageEvent e){
        if (e.getEntity() instanceof Player){
            Player p = (Player)e.getEntity();
            if (SkyBlock.getInstance().safePlayers.contains(p)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent e){
        Player p = e.getPlayer();
        Block block = e.getBlock();

        Location loc = block.getLocation();

        Island island = SkyBlock.getInstance().getIslandUtils().getIslandFromLocation(loc);
        if (island != null){
            //check if the breaker is either the owner or the member
            if (SkyBlock.getInstance().getIslandUtils().isMember(p.getUniqueId(), island) || SkyBlock.getInstance().getIslandUtils().isOwner(p.getUniqueId(), island) || SkyBlock.getInstance().getIslandUtils().isOfficer(p.getUniqueId(), island)) {
                //is a part of that island
                if (SkyBlock.getInstance().getIslandUtils().isMember(p.getUniqueId(), island)) {
                    if (!island.canMemberBreak()){
                        e.setCancelled(true);
                    }
                }
                if (SkyBlock.getInstance().getIslandUtils().isOfficer(p.getUniqueId(), island)) {
                    if (!island.canOfficerBreak()){
                        e.setCancelled(true);
                    }
                }
            }else{
                e.setCancelled(true);
                p.sendMessage(SkyBlock.getInstance().getUtils().getMessage("notYours"));
            }
        }
    }

    @EventHandler
    public void placeBlock(BlockPlaceEvent e){
        Player p = e.getPlayer();
        Block block = e.getBlock();

        Location loc = block.getLocation();

        Island island = SkyBlock.getInstance().getIslandUtils().getIslandFromLocation(loc);
        if (island != null){
            //check if the breaker is either the owner or the member
            if (SkyBlock.getInstance().getIslandUtils().isMember(p.getUniqueId(), island) || SkyBlock.getInstance().getIslandUtils().isOwner(p.getUniqueId(), island) || SkyBlock.getInstance().getIslandUtils().isOfficer(p.getUniqueId(), island)) {
                //is a part of that island
                if (SkyBlock.getInstance().getIslandUtils().isMember(p.getUniqueId(), island)) {
                    if (!island.canMemberPlace()){
                        e.setCancelled(true);
                    }
                }
                if (SkyBlock.getInstance().getIslandUtils().isOfficer(p.getUniqueId(), island)) {
                    if (!island.canOfficerPlace()){
                        e.setCancelled(true);
                    }
                }
            }else{
                e.setCancelled(true);
                p.sendMessage(SkyBlock.getInstance().getUtils().getMessage("notYours"));
            }
        }
    }
}