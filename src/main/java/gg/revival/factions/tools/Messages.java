package gg.revival.factions.tools;

import gg.revival.factions.file.FileManager;
import org.bukkit.ChatColor;

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

    public static String noSubclaimAccess() { return getValue("errors.no-subclaim-access"); }

    public static String subclaimDeleted() { return getValue("notifications.subclaim-deleted"); }

    public static String subclaimDeletedFaction() { return getValue("notifications.subclaim-deleted-faction"); }

    public static String subclaimTooClose() { return getValue("errors.subclaim-too-close"); }

    public static String leaderRequired() { return getValue("errors.leader-required"); }

    public static String officerRequired() { return getValue("errors.officer-required"); }

    public static String unraidableRequired() { return getValue("errors.unraidable-required"); }

    public static String unfrozenRequired() { return getValue("errors.unfrozen-required"); }

    public static String notInFaction() { return getValue("errors.not-in-faction"); }

}
