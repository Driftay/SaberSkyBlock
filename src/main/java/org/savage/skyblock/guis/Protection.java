package org.savage.skyblock.guis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.savage.skyblock.SkyBlock;
import org.savage.skyblock.island.Island;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Protection implements Listener {

    public static HashMap<UUID, Enum> role = new HashMap<>();
    public static HashMap<UUID, Enum> protection = new HashMap<>();

    public enum roleType{
        MEMBER,OFFICER
    }

    public enum protectionType{
        PLACE,BREAK,INTERACT
    }

    public static void openProtectionMenu(Player p) {
        Inventory i = Bukkit.createInventory(null, SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getInt("protection.rows") * 9, SkyBlock.getInstance().getUtils().color(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection.name")));

        //String materialName, int data, String name, List<String> lore, int amount
        ItemStack place = SkyBlock.getInstance().getUtils().createItem(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection.place.item-id"), 0, SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection.place.name")
                , SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getStringList("protection.place.lore"), 1);

        ItemStack breaK = SkyBlock.getInstance().getUtils().createItem(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection.break.item-id"), 0, SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection.break.name")
                , SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getStringList("protection.break.lore"), 1);

        ItemStack interact = SkyBlock.getInstance().getUtils().createItem(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection.interact.item-id"), 0, SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection.interact.name")
                , SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getStringList("protection.interact.lore"), 1);

        i.setItem(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getInt("protection.place.slot") - 1, place);
        i.setItem(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getInt("protection.break.slot") - 1, breaK);
        i.setItem(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getInt("protection.interact.slot") - 1, interact);

        p.openInventory(i);
    }

    public static void openRoleMenu(Player p) {
        Inventory i = Bukkit.createInventory(null, SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getInt("protection-permissions.rows") * 9, SkyBlock.getInstance().getUtils().color(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection-permissions.name").replace("%type%", SkyBlock.getInstance().getUtils().capitalizeFirstLetter(protection.get(p.getUniqueId()).name()))));

        //String materialName, int data, String name, List<String> lore, int amount
        ItemStack member = SkyBlock.getInstance().getUtils().createItem(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection-permissions.members-item.item-id"), 0, SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection-permissions.members-item.name")
                , SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getStringList("protection-permissions.members-item.lore"), 1);

        ItemStack officer = SkyBlock.getInstance().getUtils().createItem(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection-permissions.officers-item.item-id"), 0, SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection-permissions.officers-item.name")
                , SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getStringList("protection-permissions.officers-item.lore"), 1);

        i.setItem(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getInt("protection-permissions.members-item.slot") - 1, member);
        i.setItem(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getInt("protection-permissions.officers-item.slot") - 1, officer);

        p.openInventory(i);
    }

    public static void openConfirmMenu(Player p) {
        Inventory i = Bukkit.createInventory(null, SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getInt("protection-permissions.permissions.rows") * 9, SkyBlock.getInstance().getUtils().color(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection-permissions.permissions.name").replace("%type%", SkyBlock.getInstance().getUtils().capitalizeFirstLetter(protection.get(p.getUniqueId()).name())).replace("%role%", SkyBlock.getInstance().getUtils().capitalizeFirstLetter(role.get(p.getUniqueId()).name()))));

        //String materialName, int data, String name, List<String> lore, int amount
        //%type%
        List<String> allowLore = new ArrayList<>();
        for (String s : SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getStringList("protection-permissions.permissions.allow-item.lore")) {
            allowLore.add(s.replace("%type%", SkyBlock.getInstance().getUtils().capitalizeFirstLetter(protection.get(p.getUniqueId()).name())));
        }

        List<String> denyLore = new ArrayList<>();
        for (String s : SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getStringList("protection-permissions.permissions.deny-item.lore")) {
            denyLore.add(s.replace("%type%", SkyBlock.getInstance().getUtils().capitalizeFirstLetter(protection.get(p.getUniqueId()).name())));
        }

        ItemStack allow = SkyBlock.getInstance().getUtils().createItem(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection-permissions.permissions.allow-item.item-id"), 0, SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection-permissions.permissions.allow-item.name")
                , allowLore, 1);

        ItemStack deny = SkyBlock.getInstance().getUtils().createItem(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection-permissions.permissions.deny-item.item-id"), 0, SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection-permissions.permissions.deny-item.name")
                , denyLore, 1);

        i.setItem(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getInt("protection-permissions.permissions.allow-item.slot") - 1, allow);
        i.setItem(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getInt("protection-permissions.permissions.deny-item.slot") - 1, deny);

        p.openInventory(i);
    }

    @EventHandler
    public void click(InventoryClickEvent e){
        Player p = (Player)e.getWhoClicked();
        Inventory i = e.getClickedInventory();
        if (i != null){
            if (i.getName().equalsIgnoreCase(SkyBlock.getInstance().getUtils().color(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection.name")))) {
                e.setCancelled(true);
                if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()){
                    ItemStack clicked = e.getCurrentItem();
                    String name = clicked.getItemMeta().getDisplayName();

                    if (name.equalsIgnoreCase(SkyBlock.getInstance().getUtils().color(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection.place.name")))) {
                        protection.remove(p.getUniqueId());
                        protection.put(p.getUniqueId(), protectionType.PLACE);
                    }
                    if (name.equalsIgnoreCase(SkyBlock.getInstance().getUtils().color(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection.break.name")))) {
                        protection.remove(p.getUniqueId());
                        protection.put(p.getUniqueId(), protectionType.BREAK);
                    }
                    if (name.equalsIgnoreCase(SkyBlock.getInstance().getUtils().color(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection.interact.name")))) {
                        protection.remove(p.getUniqueId());
                        protection.put(p.getUniqueId(), protectionType.INTERACT);
                    }
                    if (protection.get(p.getUniqueId()) != null){
                        openRoleMenu(p);
                    }
                }
            }

            if (protection.get(p.getUniqueId()) != null) {
                if (i.getName().equalsIgnoreCase(SkyBlock.getInstance().getUtils().color(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection-permissions.name").replace("%type%", SkyBlock.getInstance().getUtils().capitalizeFirstLetter(protection.get(p.getUniqueId()).name()))))) {
                    e.setCancelled(true);
                    if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                        ItemStack clicked = e.getCurrentItem();
                        String name = clicked.getItemMeta().getDisplayName();

                        if (name.equalsIgnoreCase(SkyBlock.getInstance().getUtils().color(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection-permissions.members-item.name")))) {
                            role.remove(p.getUniqueId());
                            role.put(p.getUniqueId(), roleType.MEMBER);
                        }
                        if (name.equalsIgnoreCase(SkyBlock.getInstance().getUtils().color(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection-permissions.officers-item.name")))) {
                            role.remove(p.getUniqueId());
                            role.put(p.getUniqueId(), roleType.OFFICER);
                        }
                        if (role.get(p.getUniqueId()) != null) {
                            openConfirmMenu(p);
                        }
                    }
                }
            }

            if (protection.get(p.getUniqueId()) != null && role.get(p.getUniqueId()) != null) {

                if (i.getName().contains(SkyBlock.getInstance().getUtils().capitalizeFirstLetter(protection.get(p.getUniqueId()).name()))) {
                    e.setCancelled(true);
                    if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                        ItemStack clicked = e.getCurrentItem();
                        String name = clicked.getItemMeta().getDisplayName();

                        if (name.equalsIgnoreCase(SkyBlock.getInstance().getUtils().color(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection-permissions.permissions.allow-item.name")))) {
                            //allow the perm
                            Island island = SkyBlock.getInstance().getIslandUtils().getIsland(p.getUniqueId());
                            if (island != null && SkyBlock.getInstance().getIslandUtils().isOwner(p.getUniqueId(), island)) {
                                String roleS = role.get(p.getUniqueId()).name();
                                String protectS = protection.get(p.getUniqueId()).name();

                                if (roleS.equalsIgnoreCase("Member")) {
                                    if (protectS.equalsIgnoreCase("Place")) {
                                        island.setPermissionMemberPlace(true);
                                    }
                                    if (protectS.equalsIgnoreCase("Break")) {
                                        island.setPermissionMemberBreak(true);
                                    }
                                    if (protectS.equalsIgnoreCase("Interact")) {
                                        island.setPermissionMemberInteract(true);
                                    }
                                }
                                if (roleS.equalsIgnoreCase("Officer")) {
                                    if (protectS.equalsIgnoreCase("Place")) {
                                        island.setPermissionOfficerPlace(true);
                                    }
                                    if (protectS.equalsIgnoreCase("Break")) {
                                        island.setPermissionOfficerBreak(true);
                                    }
                                    if (protectS.equalsIgnoreCase("Interact")) {
                                        island.setPermissionOfficerInteract(true);
                                    }
                                }

                                p.closeInventory();
                                role.remove(p.getUniqueId());
                                protection.remove(p.getUniqueId());
                                //'&aYou have updated &9%role%&a''s permission for &9%type%&a to &d%outcome%&a!'
                                p.sendMessage(SkyBlock.getInstance().getUtils().getMessage("setPermission").replace("%role%", SkyBlock.getInstance().getUtils().capitalizeFirstLetter(roleS)).replace("%type%", SkyBlock.getInstance().getUtils().capitalizeFirstLetter(protectS)).replace("%outcome%", "Allow"));
                            } else {
                                p.closeInventory();
                            }
                        }
                        if (name.equalsIgnoreCase(SkyBlock.getInstance().getUtils().color(SkyBlock.getInstance().getFileManager().guiFile.getFileConfig().getString("protection-permissions.permissions.deny-item.name")))) {
                            //deny the perm
                            Island island = SkyBlock.getInstance().getIslandUtils().getIsland(p.getUniqueId());
                            if (island != null && SkyBlock.getInstance().getIslandUtils().isOwner(p.getUniqueId(), island)) {
                                String roleS = role.get(p.getUniqueId()).name();
                                String protectS = protection.get(p.getUniqueId()).name();

                                if (roleS.equalsIgnoreCase("Member")) {
                                    if (protectS.equalsIgnoreCase("Place")) {
                                        island.setPermissionMemberPlace(false);
                                    }
                                    if (protectS.equalsIgnoreCase("Break")) {
                                        island.setPermissionMemberBreak(false);
                                    }
                                    if (protectS.equalsIgnoreCase("Interact")) {
                                        island.setPermissionMemberInteract(false);
                                    }
                                }
                                if (roleS.equalsIgnoreCase("Officer")) {
                                    if (protectS.equalsIgnoreCase("Place")) {
                                        island.setPermissionOfficerPlace(false);
                                    }
                                    if (protectS.equalsIgnoreCase("Break")) {
                                        island.setPermissionOfficerBreak(false);
                                    }
                                    if (protectS.equalsIgnoreCase("Interact")) {
                                        island.setPermissionOfficerInteract(false);
                                    }
                                }
                                p.closeInventory();
                                //'&aYou have updated &9%role%&a''s permission for &9%type%&a to &d%outcome%&a!'
                                p.sendMessage(SkyBlock.getInstance().getUtils().getMessage("setPermission").replace("%role%", SkyBlock.getInstance().getUtils().capitalizeFirstLetter(roleS)).replace("%type%", SkyBlock.getInstance().getUtils().capitalizeFirstLetter(protectS)).replace("%outcome%", "Deny"));
                            } else {
                                p.closeInventory();
                            }
                        }
                    }
                }
            }
        }
    }
}