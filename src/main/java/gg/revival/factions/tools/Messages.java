package gg.revival.factions.tools;

import com.google.common.base.Joiner;
import gg.revival.factions.commands.CmdCategory;
import gg.revival.factions.commands.CommandManager;
import gg.revival.factions.commands.FCommand;
import gg.revival.factions.core.FactionManager;
import gg.revival.factions.file.FileManager;
import gg.revival.factions.obj.Faction;
import gg.revival.factions.obj.PlayerFaction;
import mkremins.fanciful.FancyMessage;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.*;

public class Messages {

    private static String getValue(String path) {
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

    public static String formattedChatChannel(String channel, String player, String message) {
        return getValue("chat-formatting.chat-channel")
                .replace("%channel%", channel)
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
        return getValue("errors.cant-pay-self");
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

    public static String badNumber() {
        return getValue("errors.bad-number");
    }

    public static String channelCreated(String channelName) {
        return getValue("chat-channels.channel-created")
                .replace("%channelname%", channelName);
    }

    public static String channelDeleted() {
        return getValue("chat-channels.channel-deleted");
    }

    public static String joinedChannel() {
        return getValue("chat-channels.joined-channel");
    }

    public static String joinedChannelOther(String player) {
        return getValue("chat-channels.joined-channel-other")
                .replace("%player%", player);
    }

    public static String joinedFactionChannel(String channel) {
        return getValue("chat-channels.joined-faction-channel")
                .replace("%channel%", channel);
    }

    public static String leftChannel() {
        return getValue("chat-channels.left-channel");
    }

    public static String leftChannelOther(String player) {
        return getValue("chat-channels.left-channel-other")
                .replace("%player%", player);
    }

    public static String kickedChannel() {
        return getValue("chat-channels.kicked-channel");
    }

    public static String kickedChannelOther(String player) {
        return getValue("chat-channels.kicked-channel-other")
                .replace("%player%", player);
    }

    public static String channelExists() {
        return getValue("chat-channels.channel-already-exists");
    }

    public static String alreadyInChannel() {
        return getValue("chat-channels.already-in-channel");
    }

    public static String badChannelName() {
        return getValue("chat-channels.bad-channel-name");
    }

    public static String notInChatChannel() {
        return getValue("chat-channels.not-in-channel");
    }

    public static String alreadySpeakingInChannel() {
        return getValue("chat-channels.already-speaking-in-channel");
    }

    public static String noAlliesInAllyChat() {
        return getValue("chat-channels.no-allies-in-ally-chat");
    }

    public static String playerNotInFaction() {
        return getValue("errors.player-not-in-faction");
    }

    public static String newLeader(String oldLeader, String newLeader) {
        return getValue("notifications.new-leader")
                .replace("%oldleader%", oldLeader)
                .replace("%newleader%", newLeader);
    }

    public static String newOfficer(String leader, String newOfficer) {
        return getValue("notifications.new-officer")
                .replace("%leader%", leader)
                .replace("%officer%", newOfficer);
    }

    public static String removedOfficer(String leader, String officer) {
        return getValue("notifications.removed-officer")
                .replace("%leader%", leader)
                .replace("%officer%", officer);
    }

    public static String notOfficer() {
        return getValue("errors.not-officer");
    }

    public static String alreadyOfficer() {
        return getValue("errors.already-officer");
    }

    public static String badTime() {
        return getValue("errors.bad-time");
    }

    public static String powerFrozenByStaff(String freezer) {
        return getValue("notifications.power-frozen-by-staff")
                .replace("%freezer%", freezer);
    }

    public static String powerFrozenByStaffOther(String faction) {
        return getValue("notifications.power-frozen-by-staff-other")
                .replace("%faction%", faction);
    }

    public static String dtrChanged(String changer, double newDtr) {
        return getValue("notifications.dtr-changed")
                .replace("%changer%", changer)
                .replace("%dtr%", String.valueOf(newDtr));
    }

    public static String dtrChangedOther(String faction, double newDtr) {
        return getValue("notifications.dtr-changed-other")
                .replace("%faction%", faction)
                .replace("%dtr%", String.valueOf(newDtr));
    }

    public static String factionDeposit(String player, String amt) {
        return getValue("notifications.faction-deposit")
                .replace("%player%", player)
                .replace("%amt%", amt);
    }

    public static String factionWithdrawl(String player, String amt) {
        return getValue("notifications.faction-withdrawl")
                .replace("%player%", player)
                .replace("%amt%", amt);
    }

    public static String alliesDisabled() {
        return getValue("errors.allies-disabled");
    }

    public static String selfMaxAllies() {
        return getValue("errors.ally-limit-self");
    }

    public static String otherMaxAllies() {
        return getValue("errors.ally-limit-other");
    }

    public static String allianceFormed(String factionOne, String factionTwo) {
        return getValue("broadcasts.alliance-formed")
                .replace("%factionOne%", factionOne)
                .replace("%factionTwo%", factionTwo);
    }

    public static String allianceBroken(String factionOne, String factionTwo) {
        return getValue("broadcasts.alliance-formed")
                .replace("%factionOne%", factionOne)
                .replace("%factionTwo%", factionTwo);
    }

    public static String allyRequest(String requestingFaction) {
        return getValue("notifications.ally-request")
                .replace("%faction%", requestingFaction);
    }

    public static String allyRequestSent(String requester, String faction) {
        return getValue("notifications.ally-request-sent")
                .replace("%requester%", requester)
                .replace("%faction%", faction);
    }

    public static String notAllied() {
        return getValue("errors.not-allied");
    }

    public static String allyRequestRevoked(String revoker, String faction) {
        return getValue("notifications.ally-request-revoked")
                .replace("%player%", revoker)
                .replace("%faction%", faction);
    }

    public static String allyRequestRevokedOther(String faction) {
        return getValue("notifications.ally-request-revoked-other")
                .replace("%faction%", faction);
    }

    public static String allyRequestPending() {
        return getValue("errors.ally-request-pending");
    }

    public static String homeNotSet() {
        return getValue("errors.home-not-set");
    }

    public static String cantAffordHomeTooHigh() {
        return getValue("errors.home-too-high-cant-afford");
    }

    public static String returnedHome() {
        return getValue("notifications.returned-home");
    }

    public static String homeWarpStarted(int dur) {
        return getValue("notifications.home-warp-started")
                .replace("%dur%", String.valueOf(dur));
    }

    public static String stuckWarpStarted(int dur) {
        return getValue("notifications.stuck-warp-started")
                .replace("%dur%", String.valueOf(dur));
    }

    public static String cantWarpHomeInsideClaim() {
        return getValue("errors.cant-home-from-current-claim");
    }

    public static String homeWarpCancelled() {
        return getValue("errors.home-warp-cancelled");
    }

    public static String stuckWarpCancelled() {
        return getValue("errors.stuck-warp-cancelled");
    }

    public static String notInsideClaim() {
        return getValue("errors.not-in-claim");
    }

    public static String logoutCancelled() {
        return getValue("errors.logout-cancelled");
    }

    public static String unstuck() {
        return getValue("notifications.unstuck");
    }

    public static String cantHomeWhileTagged() {
        return getValue("errors.cant-home-while-tagged");
    }

    public static String playerKicked(String kicker, String kicked) {
        return getValue("notifications.player-kicked")
                .replace("%kicker%", kicker)
                .replace("%kicked%", kicked);
    }

    public static String cantWhilePlayerIsTagged() {
        return getValue("errors.cant-while-player-is-tagged");
    }

    public static String cantKickLeader() {
        return getValue("errors.cant-kick-leader");
    }

    public static String playerKickedOther() {
        return getValue("notifications.player-kicked-other");
    }

    public static String cantWhileTagged() {
        return getValue("errors.cant-while-tagged");
    }

    public static String cantLeaveWhileLeader() {
        return getValue("errors.cant-leave-leader");
    }

    public static String playerLeft(String leaver) {
        return getValue("notifications.player-left")
                .replace("%leaver%", leaver);
    }

    public static String playerLeftOther() {
        return getValue("notifications.player-left-other");
    }

    public static String memberDeath(String deadMember) {
        return getValue("notifications.member-death")
                .replace("%player%", deadMember);
    }

    public static void factionInfo(PlayerFaction faction, Player displayedTo) {
        StringBuilder info = new StringBuilder();
        DecimalFormat balanceFormat = new DecimalFormat("#,##0.00");
        DecimalFormat dtrFormat = new DecimalFormat("0.0#");

        OfflinePlayerLookup.getManyOfflinePlayersByUUID(faction.getRoster(false), result -> {
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

            if (faction.getDtr().doubleValue() < 0.0)
                info.append(ChatColor.DARK_RED + "" + dtrFormat.format(faction.getDtr().doubleValue()));
            else if (faction.getDtr().doubleValue() < 1.0)
                info.append(ChatColor.RED + "" + dtrFormat.format(faction.getDtr().doubleValue()));
            else
                info.append(dtrFormat.format(faction.getDtr().doubleValue()));

            if (faction.isFrozen())
                info.append(ChatColor.GRAY + " (Frozen)" + "\n");
            else if (faction.getDtr().doubleValue() < 0.0)
                info.append(ChatColor.RED + " (Raid-able)" + "\n");
            else if (faction.getDtr().doubleValue() == faction.getMaxDTR())
                info.append(ChatColor.BLUE + " (Max)" + "\n");
            else
                info.append("\n");

            info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Balance" + ChatColor.WHITE + ": $" + balanceFormat.format(faction.getBalance()) + "\n");

            if (!faction.getAllies().isEmpty()) {
                List<String> allies = new ArrayList<>();

                for (UUID allyID : faction.getAllies()) {
                    String factionName = FactionManager.getFactionByUUID(allyID).getDisplayName();

                    allies.add(factionName);
                }

                info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Allies" + ChatColor.WHITE + ": " + Joiner.on(", ").join(allies) + "\n");
            }

            if (Bukkit.getPlayer(faction.getLeader()) != null && Bukkit.getPlayer(faction.getLeader()).isOnline()) {
                info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Leader" + ChatColor.WHITE + ": " + ChatColor.GREEN + "**" + result.get(faction.getLeader()) + "\n");
            } else {
                info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Leader" + ChatColor.WHITE + ": " + ChatColor.GRAY + "**" + result.get(faction.getLeader()) + "\n");
            }

            List<String> onlineOfficers = new ArrayList<>();
            List<String> onlineMembers = new ArrayList<>();
            List<String> offlineOfficers = new ArrayList<>();
            List<String> offlineMembers = new ArrayList<>();

            for (UUID officerID : faction.getOfficers()) {
                if (Bukkit.getPlayer(officerID) != null && Bukkit.getPlayer(officerID).isOnline()) {
                    onlineOfficers.add(ChatColor.GREEN + "*" + result.get(officerID));
                } else {
                    offlineOfficers.add(ChatColor.GRAY + "*" + result.get(officerID));
                }
            }

            for (UUID memberID : faction.getMembers()) {
                if (Bukkit.getPlayer(memberID) != null && Bukkit.getPlayer(memberID).isOnline()) {
                    onlineMembers.add(ChatColor.GREEN + result.get(memberID));
                } else {
                    offlineMembers.add(ChatColor.GRAY + result.get(memberID));
                }
            }

            if (onlineOfficers.isEmpty() && offlineOfficers.isEmpty()) {
                info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Officers" + ChatColor.WHITE + ": " + "N/A" + "\n");
            } else {
                info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Officers" + ChatColor.WHITE + ": " + Joiner.on(ChatColor.WHITE + ", ").join(onlineOfficers));

                if(!onlineOfficers.isEmpty() && !offlineOfficers.isEmpty())
                    info.append(" ");

                info.append(Joiner.on(ChatColor.WHITE + ", ").join(offlineOfficers) + "\n");
            }

            if (onlineMembers.isEmpty() && offlineMembers.isEmpty()) {
                info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Members" + ChatColor.WHITE + ": " + "N/A" + "\n");
            } else {
                info.append(ChatColor.YELLOW + " - " + ChatColor.GOLD + "Members" + ChatColor.WHITE + ": " + Joiner.on(ChatColor.WHITE + ", ").join(onlineMembers));

                if(!onlineMembers.isEmpty() && !offlineMembers.isEmpty())
                    info.append(" ");

                info.append(Joiner.on(ChatColor.WHITE + ", ").join(offlineMembers) + "\n");
            }

            if (faction.isFrozen()) {
                int seconds, minutes, hours;
                long dur = faction.getUnfreezeTime() - System.currentTimeMillis();

                seconds = (int) dur / 1000;
                minutes = seconds / 60;
                hours = minutes / 60;

                String unfreezeTime;

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

            displayedTo.sendMessage(info.toString());
        });
    }

    public static void sendList(Player player, int page) {
        Map<PlayerFaction, Integer> factionCounts = new HashMap<>();

        for (Faction factions : FactionManager.getActiveFactions()) {
            if (!(factions instanceof PlayerFaction)) continue;
            if (factionCounts.containsKey(factions)) continue;

            PlayerFaction playerFactions = (PlayerFaction) factions;

            factionCounts.put(playerFactions, playerFactions.getRoster(true).size());
        }

        Map<PlayerFaction, Integer> sortedFactionCounts = ToolBox.sortByValue(factionCounts);

        int startingPlace = page * 10;
        int finishingPlace = page + 10;
        int cursor = startingPlace;

        if (startingPlace > sortedFactionCounts.size()) {
            player.sendMessage(ChatColor.RED + "Invalid page number");
            return;
        }

        if (finishingPlace > sortedFactionCounts.size()) {
            finishingPlace = sortedFactionCounts.size();
        }

        player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH +
                "---------------" + ChatColor.GOLD + "" + ChatColor.BOLD +
                "[ " + ChatColor.YELLOW + "Faction List (Page #" + page + ") " + ChatColor.GOLD + "" + ChatColor.BOLD + "]"
                + ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH + "---------------");

        player.sendMessage("     " + "\n" + ChatColor.YELLOW + "Click a faction name to view more information" + ChatColor.RESET + "\n" + "     ");

        for (PlayerFaction factions : sortedFactionCounts.keySet()) {
            if(cursor >= finishingPlace)
                break;

            cursor++;

            new FancyMessage(cursor + ". ")
                    .color(ChatColor.YELLOW)
                    .then(factions.getDisplayName())
                    .color(ChatColor.BLUE)
                    .command("/f who " + factions.getDisplayName() + " -f")
                    .then(" - ")
                    .color(ChatColor.WHITE)
                    .then("[" + factions.getRoster(true).size() + "/" + factions.getRoster(false).size() + "]")
                    .color(ChatColor.YELLOW)
                    .then(" [" + factions.getDtr().doubleValue() + "/" + factions.getMaxDTR() + "DTR]")
                    .color(ChatColor.YELLOW)
                    .send(player);
        }

        player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH + "-------------------------------------------------");
    }

    public static void sendMultiFactionList(List<Faction> results, Player displayedTo, String query) {
        displayedTo.sendMessage(ChatColor.AQUA + "There were multiple results for your search request. Click one of the following:");

        int i = 1;
        for (Faction faction : results) {
            if (faction.getDisplayName().equalsIgnoreCase(query)) {
                new
                        FancyMessage(i + ". ")
                        .color(ChatColor.GOLD)
                        .then(faction.getDisplayName())
                        .color(ChatColor.YELLOW)
                        .then(" (Matches faction name)")
                        .color(ChatColor.GREEN)
                        .command("/f show " + faction.getDisplayName() + " -f")
                        .send(displayedTo);
            } else {
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

    public static void sendHelpPage(Player player, CmdCategory category) {
        if (category == null) {

            player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH +
                    "---------------" + ChatColor.GOLD + "" + ChatColor.BOLD +
                    "[ " + ChatColor.YELLOW + "Faction Help (" + "Home" + ") " + ChatColor.GOLD + "" + ChatColor.BOLD + "]"
                    + ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH + "---------------");

            player.sendMessage("     ");

            player.sendMessage(ChatColor.GOLD + "Choose a category that you wish to learn about:");

            new FancyMessage
                    ("Claim, ").color(ChatColor.YELLOW).command("/f help claim")
                    .then("Economy, ").color(ChatColor.YELLOW).command("/f help economy")
                    .then("Basics, ").color(ChatColor.YELLOW).command("/f help basics")
                    .then("Manage, ").color(ChatColor.YELLOW).command("/f help manage")
                    .then("Info").color(ChatColor.YELLOW).command("/f help info").send(player);

            player.sendMessage("     ");
            player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH + "-------------------------------------------------");

            return;
        }

        if (category.equals(CmdCategory.STAFF) && !player.hasPermission(Permissions.MOD) && !player.hasPermission(Permissions.ADMIN)) {
            player.sendMessage(Messages.noPermission());

            return;
        }

        player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH +
                "---------------" + ChatColor.GOLD + "" + ChatColor.BOLD +
                "[ " + ChatColor.YELLOW + "Faction Help (" + StringUtils.capitalize(category.toString().toLowerCase()) + ") " + ChatColor.GOLD + "" + ChatColor.BOLD + "]"
                + ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH + "---------------");

        player.sendMessage("     ");

        player.sendMessage(ChatColor.GOLD + "Hover over a command to view more information");

        player.sendMessage("     ");

        for (FCommand command : CommandManager.getFactionCommandsByCategory(category)) {
            List<String> info = new ArrayList<>();

            info.add("Description: " + command.getDescription());

            if (command.getAliases() != null && !command.getAliases().isEmpty()) {
                info.add("Aliases: " + Joiner.on(", ").join(command.getAliases()));
            }

            new FancyMessage(" - ")
                    .color(ChatColor.GOLD)
                    .then(command.getSyntax())
                    .tooltip(info)
                    .color(ChatColor.YELLOW).send(player);
        }

        player.sendMessage("     ");

        player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH + "-------------------------------------------------");
    }

}
