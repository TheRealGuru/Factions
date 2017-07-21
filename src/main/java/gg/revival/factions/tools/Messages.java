package gg.revival.factions.tools;

import com.google.common.base.Joiner;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.file.FileManager;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.*;

public class Messages {

    public static String getValue(String path) {
        return ChatColor.translateAlternateColorCodes('&', FileManager.getMessages().getString(path));
    }

    public static String gameTag() {
        return getValue("misc.game-tag");
    }

    public static String memberOnline(String member) {
        return getValue("notifications.member-online").replace("%player%", member);
    }

    public static String memberOffline(String member) {
        return getValue("notifications.member-offline").replace("%player%", member);
    }

    public static String friendlyFire() {
        return getValue("notifications.friendly-fire");
    }

    public static String allyFire() {
        return getValue("notifications.ally-fire");
    }

    public static String enteringClaim(String claimInformation) {
        return getValue("notifications.entering-claim").replace("%claim%", claimInformation);
    }

    public static String leavingClaim(String claimInformation) {
        return getValue("notifications.leaving-claim").replace("%claim%", claimInformation);
    }

    public static String formattedGlobalFaction(String faction, String player, String message) {
        return getValue("chat-formatting.global-faction-member")
                .replace("%fac%", faction)
                .replace("%player%", player)
                .replace("%msg%", message);
    }

    public static String formattedGlobalAlly(String faction, String player, String message) {
        return getValue("chat-formatting.global-ally-member")
                .replace("%fac%", faction)
                .replace("%player%", player)
                .replace("%msg%", message);
    }

    public static String formattedGlobalEnemy(String faction, String player, String message) {
        return getValue("chat-formatting.global-enemy")
                .replace("%fac%", faction)
                .replace("%player%", player)
                .replace("%msg%", message);
    }

    public static String formattedFactionChat(String faction, String player, String message) {
        return getValue("chat-formatting.faction-chat")
                .replace("%fac%", faction)
                .replace("%player%", player)
                .replace("%msg%", message);
    }

    public static String formattedAllyChat(String faction, String player, String message) {
        return getValue("chat-formatting.ally-chat")
                .replace("%fac%", faction)
                .replace("%player%", player)
                .replace("%msg%", message);
    }

    public static String factionCreated(String faction, String player) {
        return getValue("broadcasts.fac-created").replace("%fac%", faction).replace("%player%", player);
    }

    public static String factionDisbanded(String faction, String player) {
        return getValue("broadcasts.fac-disbanded").replace("%fac%", faction).replace("%player%", player);
    }

    public static String factionRenamed(String oldName, String newName, String changer) {
        return getValue("broadcasts.fac-renamed")
                .replace("%oldname%", oldName)
                .replace("%newname%", newName)
                .replace("%player%", changer);
    }

    public static String noPermission() {
        return getValue("errors.no-permission");
    }

    public static String noConsole() {
        return getValue("errors.no-console");
    }

    public static String profileNotFound() {
        return getValue("errors.profile-not-found");
    }

    public static String playerNotFound() {
        return getValue("errors.player-not-found");
    }

    public static String factionNotFound() {
        return getValue("errors.faction-not-found");
    }

    public static String alreadyInFaction() {
        return getValue("errors.already-in-faction");
    }

    public static String alreadyInFactionOther() {
        return getValue("errors.already-in-faction-other");
    }

    public static String badFactionName() {
        return getValue("errors.bad-fac-name");
    }

    public static String facNameInUse() {
        return getValue("errors.fac-name-in-use");
    }

    public static String noSubclaimAccess() {
        return getValue("errors.no-subclaim-access");
    }

    public static String subclaimCreated() {
        return getValue("notifications.subclaim-created");
    }

    public static String subclaimCreatedFaction(String creator) {
        return getValue("notifications.subclaim-created-faction")
                .replace("%player%", creator);
    }

    public static String subclaimDeleted() {
        return getValue("notifications.subclaim-deleted");
    }

    public static String subclaimDeletedFaction(String deleter) {
        return getValue("notifications.subclaim-deleted-faction")
                .replace("%player%", deleter);
    }

    public static String subclaimTooClose() {
        return getValue("errors.subclaim-too-close");
    }

    public static String leaderRequired() {
        return getValue("errors.leader-required");
    }

    public static String officerRequired() {
        return getValue("errors.officer-required");
    }

    public static String unraidableRequired() {
        return getValue("errors.unraidable-required");
    }

    public static String unfrozenRequired() {
        return getValue("errors.unfrozen-required");
    }

    public static String notInFaction() {
        return getValue("errors.not-in-faction");
    }

    public static String landClaimedBy(String claimOwner) {
        return getValue("notifications.cant-break-claimed-land")
                .replace("%claimowner%", claimOwner);
    }

    public static String nearbyLandClaimedBy(String claimOwner) {
        return getValue("notifications.cant-break-nearby-claimed-land")
                .replace("%claimowner%", claimOwner);
    }

    public static String cantClaimWarzone() {
        return getValue("errors.cant-claim-warzone");
    }

    public static String claimOverlapping() {
        return getValue("errors.claim-overlapping");
    }

    public static String claimTooClose() {
        return getValue("errors.claim-too-close");
    }

    public static String claimPointSet(int claimNumber) {
        return getValue("notifications.claim-point-set")
                .replace("%claimpoint%", String.valueOf(claimNumber));
    }

    public static String claimReset() {
        return getValue("notifications.claim-reset");
    }

    public static String claimCost(double totalValue) {
        return getValue("notifications.claim-cost")
                .replace("%totalvalue%", String.valueOf(totalValue));
    }

    public static String claimUnfinished() {
        return getValue("errors.claim-unfinished");
    }

    public static String claimTooExpensive() {
        return getValue("errors.claim-too-expensive");
    }

    public static String landClaimSuccess() {
        return getValue("notifications.land-claim-success");
    }

    public static String landClaimSuccessOther(String claimer, double totalValue) {
        return getValue("notifications.land-claim-success-other")
                .replace("%player%", claimer)
                .replace("%totalvalue%", String.valueOf(totalValue));
    }

    public static String claimNotConnected() {
        return getValue("errors.claim-not-connected");
    }

    public static String claimTooSmall() {
        return getValue("errors.claim-too-small");
    }

    public static String inventoryFull() {
        return getValue("errors.inventory-full");
    }

    public static String alreadyClaimingLand() {
        return getValue("errors.claim-in-progress");
    }

    public static String invitedPlayer(String inviter, String invited) {
        return getValue("notifications.invited-player")
                .replace("%player%", inviter)
                .replace("%invited%", invited);
    }

    public static String uninvitedPlayer(String uninviter, String uninvited) {
        return getValue("notifications.uninvited-player")
                .replace("%player%", uninviter)
                .replace("%uninvited%", uninvited);
    }

    public static String invitationRevoked(String factionName) {
        return getValue("notifications.uninvited-player-other")
                .replace("%faction%", factionName);
    }

    public static String noPendingInvite() {
        return getValue("errors.player-no-pending-invite");
    }

    public static String playerAlreadyInvited() {
        return getValue("errors.player-already-invited");
    }

    public static String noNearbyFactions() {
        return getValue("notifications.no-nearby-factions");
    }

    public static String noPendingInviteOther() {
        return getValue("errors.player-no-pending-invite-other");
    }

    public static String factionFull() {
        return getValue("errors.faction-full");
    }

    public static String unfrozenRequiredOther() {
        return getValue("errors.unfrozen-required-other");
    }

    public static String joinedFaction(String factionName) {
        return getValue("notifications.joined-faction")
                .replace("%fac%", factionName);
    }

    public static String joinedFactionOther(String joiner) {
        return getValue("notifications.joined-faction-other")
                .replace("%player%", joiner);
    }

    public static String notLookingAtChest() {
        return getValue("errors.not-looking-at-chest");
    }

    public static String subclaimAccessDenied() {
        return getValue("errors.subclaim-access-denied");
    }

    public static String subclaimOutsideClaim() {
        return getValue("errors.subclaim-outside-claim");
    }

    public static String currentBalance(double balance) {
        DecimalFormat format = new DecimalFormat("#,###.00");

        return getValue("economy.current-balance")
                .replace("%bal%", format.format(balance));
    }

    public static String currentBalanceOther(String player, double balance) {
        DecimalFormat format = new DecimalFormat("#,###.00");

        return getValue("economy.current-balance-other")
                .replace("%player%", player)
                .replace("%bal%", format.format(balance));
    }

    public static String balanceModified(double balance) {
        DecimalFormat format = new DecimalFormat("#,###.00");

        return getValue("economy.balance-modified")
                .replace("%bal%", format.format(balance));
    }

    public static String balanceModifiedOther(String player, double balance) {
        DecimalFormat format = new DecimalFormat("#,###.00");

        return getValue("economy.balance-modified-other")
                .replace("%bal%", format.format(balance))
                .replace("%player%", player);
    }

    public static String invalidAmount() {
        return getValue("economy.invalid-amount");
    }

    public static String cooldownMessage(String duration) {
        return getValue("errors.cooldown")
                .replace("%dur%", duration);
    }

    public static String homeSet() {
        return getValue("notifications.home-set");
    }

    public static String homeSetOther(String changer) {
        return getValue("notifications.home-set-other")
                .replace("%player%", changer);
    }

    public static String homeOutsideClaims() {
        return getValue("errors.home-outside-claim");
    }

    public static String factionDamageDisabled() {
        return getValue("notifications.faction-damage-disabled");
    }

    public static String allyDamage() {
        return getValue("notifications.ally-damage");
    }

    public static String combatDisabledSafezone() {
        return getValue("errors.combat-disabled-safezone");
    }

    public static String notEnoughMoney() {
        return getValue("errors.not-enough-money");
    }

    public static String paidPlayer(double amt, String paidPlayer) {
        return getValue("notifications.paid-player")
                .replace("%amount%", String.valueOf(amt))
                .replace("%player%", paidPlayer);
    }

    public static String paidPlayerOther(double amt, String payingPlayer) {
        return getValue("notifications.paid-player-other")
                .replace("%amount%", String.valueOf(amt))
                .replace("%player%", payingPlayer);
    }

    public static String cantPaySelf() {
        return getValue("notifications.cant-pay-self");
    }

    public static String powerNotFrozen() {
        return getValue("errors.power-not-frozen");
    }

    public static String powerThawed(String factionName) {
        return getValue("notifications.power-thawed")
                .replace("%faction%", factionName);
    }

    public static String powerThawedOther() {
        return getValue("notifications.power-thawed-other");
    }

    public static String noClaims() {
        return getValue("errors.no-claims");
    }

    public static String notStandingInClaims() {
        return getValue("errors.not-standing-in-claims");
    }

    public static String unclaimNotConnected() {
        return getValue("errors.unclaim-not-connected");
    }

    public static String landUnclaimed(String claimValue) {
        return getValue("notifications.land-unclaimed")
                .replace("%claimvalue%", claimValue);
    }

    public static String landUnclaimedOther(String unclaimer) {
        return getValue("notifications.land-unclaimed-other")
                .replace("%player%", unclaimer);
    }

    public static String factionAnnouncement(String announcement) {
        return getValue("notifications.faction-announcement")
                .replace("%announcement%", announcement);
    }

    public static String badAnnouncement() {
        return getValue("errors.bad-announcement");
    }

    public static String noAnnouncement() {
        return getValue("errors.no-announcement");
    }

    public static String factionInfo(PlayerFaction faction, Player displayedTo) {
        StringBuilder info = new StringBuilder();
        DecimalFormat format = new DecimalFormat("#,###.00");
        Map<UUID, String> namedRoster = new HashMap<UUID, String>();

        try {
            namedRoster = new NameFetcher(faction.getRoster(false)).call();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        info.append(
                ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH + "----------------" +
                        ChatColor.GOLD + "" + ChatColor.BOLD + "[ " + ChatColor.YELLOW + faction.getDisplayName() + ChatColor.GOLD + "" + ChatColor.BOLD + " ]" +
                        ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH + "----------------" + "\n");

        if (faction.getRoster(false).contains(displayedTo.getUniqueId()) && faction.getAnnouncement() != null) {
            info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Announcement" + ChatColor.WHITE + ": " + faction.getAnnouncement() + "\n");
        }

        if (faction.getHomeLocation() != null) {
            int x = faction.getHomeLocation().getBlockX();
            int y = faction.getHomeLocation().getBlockY();
            int z = faction.getHomeLocation().getBlockZ();

            info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Home" + ChatColor.WHITE + ": " + x + ", " + y + ", " + z + "\n");
        } else {
            info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Home" + ChatColor.WHITE + ": Unset" + "\n");
        }

        info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Deaths Until Raidable" + ChatColor.WHITE + ": ");

        if (faction.getDtr().doubleValue() < 0.0) {
            info.append(ChatColor.DARK_RED + "" + faction.getDtr().doubleValue());
        } else if (faction.getDtr().doubleValue() < 1.0) {
            info.append(ChatColor.RED + "" + faction.getDtr().doubleValue());
        } else {
            info.append(faction.getDtr().doubleValue());
        }

        if (faction.isFrozen()) {
            info.append(ChatColor.GRAY + " (Frozen)" + "\n");
        } else if (faction.getDtr().doubleValue() == faction.getMaxDTR()) {
            info.append(ChatColor.BLUE + " (Max)" + "\n");
        } else {
            info.append("\n");
        }

        info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Balance" + ChatColor.WHITE + ": $" + format.format(faction.getBalance()) + "\n");

        if(!faction.getAllies().isEmpty()) {
            List<String> allies = new ArrayList<String>();

            for (UUID allyID : faction.getAllies()) {
                String factionName = FactionManager.getFactionByUUID(allyID).getDisplayName();

                allies.add(factionName);
            }

            info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Allies" + ChatColor.WHITE + ": " + Joiner.on(", ").join(allies) + "\n");
        }

        if(Bukkit.getPlayer(faction.getLeader()) != null && Bukkit.getPlayer(faction.getLeader()).isOnline()) {
            info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Leader" + ChatColor.WHITE + ": " + ChatColor.GREEN + "**" + namedRoster.get(faction.getLeader()) + "\n");
        } else {
            info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Leader" + ChatColor.WHITE + ": " + ChatColor.GRAY + "**" + namedRoster.get(faction.getLeader()) + "\n");
        }

        List<String> onlineOfficers = new ArrayList<>();
        List<String> onlineMembers = new ArrayList<>();
        List<String> offlineOfficers = new ArrayList<>();
        List<String> offlineMembers = new ArrayList<>();

        for (UUID officerID : faction.getOfficers()) {
            if (Bukkit.getPlayer(officerID) != null && Bukkit.getPlayer(officerID).isOnline()) {
                onlineOfficers.add(ChatColor.GREEN + "*" + namedRoster.get(officerID));
            } else {
                offlineOfficers.add(ChatColor.GRAY + "*" + namedRoster.get(officerID));
            }
        }

        for (UUID memberID : faction.getMembers()) {
            if (Bukkit.getPlayer(memberID) != null && Bukkit.getPlayer(memberID).isOnline()) {
                onlineMembers.add(ChatColor.GREEN + namedRoster.get(memberID));
            } else {
                offlineMembers.add(ChatColor.GRAY + namedRoster.get(memberID));
            }
        }

        if (onlineOfficers.isEmpty() && offlineOfficers.isEmpty()) {
            info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Officers" + ChatColor.WHITE + ": " + "N/A" + "\n");
        } else {
            info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Officers" + ChatColor.WHITE + ": " + Joiner.on(ChatColor.WHITE + ", ").join(onlineOfficers));
            info.append(Joiner.on(ChatColor.WHITE + ", ").join(offlineOfficers) + "\n");
        }

        if (onlineMembers.isEmpty() && offlineMembers.isEmpty()) {
            info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Members" + ChatColor.WHITE + ": " + "N/A" + "\n");
        } else {
            info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Members" + ChatColor.WHITE + ": " + Joiner.on(ChatColor.WHITE + ", ").join(onlineMembers));
            info.append(Joiner.on(ChatColor.WHITE + ", ").join(offlineMembers) + "\n");
        }

        if (faction.isFrozen()) {
            int seconds, minutes, hours;
            long dur = faction.getUnfreezeTime() - System.currentTimeMillis();

            seconds = (int) dur / 1000;
            minutes = seconds     / 60;
            hours = minutes       / 60;

            String unfreezeTime = null;

            if (hours > 0) {
                unfreezeTime = hours + " hours";
            } else if (minutes > 0) {
                unfreezeTime = minutes + " minutes";
            } else {
                unfreezeTime = seconds + " seconds";
            }

            info.append("     " + "\n");
            info.append(ChatColor.GOLD + "This faction will begin regenerating DTR in " + ChatColor.WHITE + unfreezeTime + "\n");
        }

        info.append(ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH + "-------------------------------------------------");

        return info.toString();
    }

    public static void sendList(Player player, int page) {
        HashMap<PlayerFaction, Integer> factionCounts = new HashMap<>();

        for(Faction factions : FactionManager.getFactions()) {
            if(!(factions instanceof PlayerFaction)) continue;
            if(factionCounts.containsKey(factionCounts)) continue;

            PlayerFaction playerFactions = (PlayerFaction)factions;

            factionCounts.put(playerFactions, playerFactions.getRoster(true).size());
        }

        Map<PlayerFaction, Integer> sortedFactionCounts = ToolBox.sortByValue(factionCounts);

        int startingPlace = page * 10;
        int finishingPlace = page + 10;
        int cursor = 1;

        if(startingPlace > sortedFactionCounts.size()) {
            player.sendMessage(ChatColor.RED + "Invalid page number");
            return;
        }

        if(finishingPlace > sortedFactionCounts.size()) {
            finishingPlace = sortedFactionCounts.size();
        }

        player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH +
                "---------------" + ChatColor.GOLD + "" + ChatColor.BOLD +
                "[ " + ChatColor.YELLOW + "Faction List (Page #" + (page + 1) + ") " + ChatColor.GOLD + "" + ChatColor.BOLD + "]"
                + ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH + "---------------");

        player.sendMessage("     " + "\n" + ChatColor.YELLOW + "Click a faction name to view more information" + ChatColor.RESET + "\n" + "     ");

        for(PlayerFaction factions : sortedFactionCounts.keySet()) {
            if(cursor < startingPlace) {
                cursor++;
                continue;
            }

            if(cursor > finishingPlace) {
                break;
            }

            // 1. FactionName - [16/20] [4.2DTR]

            new FancyMessage(cursor + ". ")
                    .color(ChatColor.YELLOW)
                    .then(factions.getDisplayName())
                    .color(ChatColor.BLUE)
                    .command("/f who " + factions.getDisplayName() + " -f")
                    .then(" - ")
                    .color(ChatColor.WHITE)
                    .then("[" + factions.getRoster(true).size() + "/" + factions.getRoster(false).size() + "]")
                    .color(ChatColor.YELLOW)
                    .then(" [" + factions.getDtr().doubleValue() + "/" + factions.getMaxDTR() + "]")
                    .color(ChatColor.YELLOW)
                    .send(player);
        }

        player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH + "-------------------------------------------------");
    }

    public static void sendMultiFactionList(List<Faction> results, Player displayedTo, String query) {
        displayedTo.sendMessage(ChatColor.AQUA + "There were multiple results for your search request. Click one of the following:");

        int i = 1;
        for(Faction faction : results) {
            if(faction.getDisplayName().equalsIgnoreCase(query)) {
                new
                        FancyMessage(i + ". ")
                        .color(ChatColor.GOLD)
                        .then(faction.getDisplayName())
                        .color(ChatColor.YELLOW)
                        .then(" (Matches faction name)")
                        .color(ChatColor.GREEN)
                        .command("/f show " + faction.getDisplayName() + " -f")
                        .send(displayedTo);
            }

            else {
                new
                        FancyMessage(i + ". ")
                        .color(ChatColor.GOLD)
                        .then(faction.getDisplayName()).color(ChatColor.YELLOW)
                        .then(" (Matches player name)").color(ChatColor.GREEN)
                        .command("/f show " + faction.getDisplayName() + " -p")
                        .send(displayedTo);
            }

            i++;
        }
    }

    public static void sendFactionInvite(Player displayedTo, String factionName, String inviter) {
        new FancyMessage(inviter)
                .color(ChatColor.BLUE)
                .then(" has invited you to join ")
                .color(ChatColor.YELLOW).then(factionName)
                .color(ChatColor.GOLD)
                .then(" Click this meesage to accept the invitation")
                .color(ChatColor.YELLOW)
                .command("/faction accept " + factionName)
                .send(displayedTo);
    }

}
