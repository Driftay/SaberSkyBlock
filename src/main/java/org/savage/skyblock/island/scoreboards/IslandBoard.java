package org.savage.skyblock.island.scoreboards;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.savage.skyblock.PluginHook;
import org.savage.skyblock.SkyBlock;
import org.savage.skyblock.Storage;
import org.savage.skyblock.island.MemoryPlayer;

public class IslandBoard {


    public void createScoreBoard(MemoryPlayer memoryPlayer){
        CScoreboard scoreboard = new CScoreboard("name", "criterion", "title");
        FileConfiguration f = SkyBlock.getInstance().getFileManager().getScoreboard().getFileConfig();

        String title = f.getString("scoreboard-title");

        for (String s : f.getStringList("scoreboard-rows")){
            CScoreboard.Row row = scoreboard.addRow(s);
        }

        scoreboard.setTitle(SkyBlock.getInstance().getUtils().color(title));

        scoreboard.finish();

        memoryPlayer.setScoreboard(scoreboard);
        memoryPlayer.getScoreboard().display(memoryPlayer.getPlayer());
    }

    public void updateBoard(){
        boolean PAPI = PluginHook.isEnabled("PlaceholderAPI");
        boolean MVdW = PluginHook.isEnabled("MVdWPlaceholderAPI");
        new BukkitRunnable() {
            @Override
            public void run() {
                String none = SkyBlock.getInstance().getFileManager().getScoreboard().getFileConfig().getString("placeholders.none");
                for (MemoryPlayer memoryPlayer : Storage.memoryPlayerList){
                    Player p = memoryPlayer.getPlayer();
                    if (p == null) continue;
                    CScoreboard scoreboard = memoryPlayer.getScoreboard();
                    if (scoreboard == null){
                        createScoreBoard(memoryPlayer);
                        scoreboard = memoryPlayer.getScoreboard();
                    }
                    for (CScoreboard.Row row: scoreboard.getRows()){
                        String oldMessage = row.getOriginalMessage();
                       // Bukkit.broadcastMessage("old: "+oldMessage);

                        oldMessage = SkyBlock.getInstance().getUtils().color(oldMessage);

                        if (PAPI){
                            oldMessage = PlaceholderAPI.setPlaceholders(p, oldMessage); // for PAPI
                           // Bukkit.broadcastMessage("papi: "+oldMessage);
                        }

                        if (MVdW){
                            oldMessage = be.maximvdw.placeholderapi.PlaceholderAPI.replacePlaceholders(p, oldMessage); // for MVdW
                           // Bukkit.broadcastMessage("Mvd: "+oldMessage);
                        }

                        oldMessage = oldMessage.replace("%player%", p.getName());
                        oldMessage = oldMessage.replace("%money%", SkyBlock.getInstance().getUtils().getBalance(p.getUniqueId())+"");
                        if (memoryPlayer.getIsland() != null){
                            oldMessage = oldMessage.replace("%island%", memoryPlayer.getIsland().getName());
                            oldMessage = oldMessage.replace("%is-top%", memoryPlayer.getIsland().getTopPlace()+"");
                            oldMessage = oldMessage.replace("%is-worth%", memoryPlayer.getIsland().getWorth()+"");
                            oldMessage = oldMessage.replace("%is-bank%", memoryPlayer.getIsland().getBankBalance()+"");
                        }else{
                            oldMessage = oldMessage.replace("%island%", none);
                            oldMessage = oldMessage.replace("%is-top%", none);
                            oldMessage = oldMessage.replace("%is-worth%", none);
                            oldMessage = oldMessage.replace("%is-bank%", none);
                        }
                        row.setMessage(SkyBlock.getInstance().getUtils().color(oldMessage));
                    }
                }
            }
        }.runTaskTimer(SkyBlock.getInstance(), 0, SkyBlock.getInstance().getFileManager().getScoreboard().getFileConfig().getInt("scoreboard-update-time"));
    }
}