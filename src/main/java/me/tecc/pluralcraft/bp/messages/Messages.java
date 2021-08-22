/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.messages;

import me.tecc.pluralcraft.bp.PluralcraftBP;
import me.tecc.pluralcraft.bp.systems.Information;
import me.tecc.pluralcraft.bp.systems.Member;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;

public class Messages {
    public static TextComponent pluginInfo() {
        ComponentBuilder builder = new ComponentBuilder();

        builder
                .append("This server is running ").color(Components.NORMAL)
                .append("Pluralcraft BP").color(Components.HIGHLIGHT_1)
                .append(" version ").color(Components.NORMAL)
                .append(PluralcraftBP.get().getDescription().getVersion()).color(Components.HIGHLIGHT_2)
                .append("\n").reset()
                .append("By ").color(Components.NORMAL)
                .append(new TextComponent(new ComponentBuilder()
                        .append("tecc")
                        .color(ChatColor.of("#5006c7"))
                        .event(new HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                Components.text(Components.coloured("Go to her website", ChatColor.of("#8337ff")))
                        ))
                        .event(new ClickEvent(
                                ClickEvent.Action.OPEN_URL,
                                "https://tecc.me/"
                        ))
                        .create()))
                .append(", on behalf of ").color(Components.NORMAL)
                .append(new TextComponent(new ComponentBuilder()
                        .append("hotdogninja")
                        .color(ChatColor.of("#faa31b"))
                        .create()));
        return new TextComponent(builder.create());
    }
    public static TextComponent invalid(String what, String input) {
        ComponentBuilder builder = new ComponentBuilder();
        builder
                .append(input).color(Components.HIGHLIGHT_1)
                .append(" is not a valid " + what + "!").color(Components.ERROR);
        return new TextComponent(builder.create());
    }
    public static TextComponent errorWhilst(String doing) {
        return errorWhilst(doing, null);
    }
    public static TextComponent errorWhilst(String doing, Throwable t) {
        ComponentBuilder builder = new ComponentBuilder();
        builder
                .append("An error occurred whilst " + doing).color(Components.ERROR);
        if (t != null) {
            builder
                    .append(": ").color(Components.ERROR)
                    .append(Components.throwable(t));
        }
        return new TextComponent(builder.create());
    }

    public static TextComponent addMember(String name) {
        ComponentBuilder builder = new ComponentBuilder();
        builder
                .append("A ").color(Components.NORMAL)
                .append("member").color(Components.HIGHLIGHT_1)
                .append(" named ").color(Components.NORMAL)
                .append(name).color(Components.HIGHLIGHT_2)
                .append(" has been added.");
        return new TextComponent(builder.create());
    }

    public static TextComponent createSystem(String who) {
        ComponentBuilder builder = new ComponentBuilder();
        builder
                .append("A ").color(Components.NORMAL)
                .append("system").color(Components.HIGHLIGHT_1)
                .append(" has been created for ").color(Components.NORMAL)
                .append(who).color(Components.HIGHLIGHT_1);

        return new TextComponent(builder.create());
    }

    public static TextComponent notEnoughArguments(String what, int amount) {
        ComponentBuilder builder = new ComponentBuilder();
        builder
                .append("Not enough arguments! This " + what + " requires at least ").color(Components.ERROR)
                .append(String.valueOf(amount)).color(Components.HIGHLIGHT_1)
                .append(" argument(s).");
        return new TextComponent(builder.create());
    }

    public static TextComponent mustBe(String source, String target) {
        ComponentBuilder builder = new ComponentBuilder();
        builder
                .append("You need to be a ").color(Components.ERROR)
                .append(source).color(Components.HIGHLIGHT_1)
                .append(" to execute this " + target).color(Components.ERROR);
        return new TextComponent(builder.create());
    }

    public static TextComponent notAMember(String input) {
        ComponentBuilder builder = new ComponentBuilder();
        builder
                .append(input).color(Components.HIGHLIGHT_1)
                .append(" is not a member of your system!").color(Components.ERROR);
        return new TextComponent(builder.create());
    }

    public static TextComponent information(Information information) {
        ComponentBuilder builder = new ComponentBuilder();

        Object[] values = new Object[]{
                "Name", new TextComponent(information.getName()),
                "Pronouns", new TextComponent(information.getPronouns())
        };
        boolean newline = false;
        for (int i = 0; i < values.length; i++) {
            String name = (String) values[i];
            TextComponent value = (TextComponent) values[++i];
            if (value.getText() == null) value.setText("[UNSPECIFIED]");
            if (value.getColor() == null) value.setColor(Components.HIGHLIGHT_2);
            if (newline) builder.append("\n").reset();
            else newline = true;
            builder
                    .append(name).color(Components.NORMAL)
                    .append(" ").reset()
                    .append(value).color(Components.HIGHLIGHT_2);
        }
        return new TextComponent(builder.create());
    }
    public static TextComponent member(Member member) {
        ComponentBuilder builder = new ComponentBuilder();
        builder
                .append("Member ").color(Components.NORMAL)
                .append(member.getID().toString()).color(Components.HIGHLIGHT_1)
                .append("\n").reset()
                .append(information(member.getInformation()));
        return new TextComponent(builder.create());
    }

    public static TextComponent noSelection(String what) {
        ComponentBuilder builder = new ComponentBuilder();
        builder
                .append("You don't have any " + what + " selected!").color(Components.ERROR);
        return new TextComponent(builder.create());
    }

    public static TextComponent memberList(Iterable<Member> members) {
        ComponentBuilder builder = new ComponentBuilder();

        boolean addNewline = false;
        for (Member member : members) {
            if (addNewline) builder.append("\n").reset();
            TextComponent component = Components.member(member);
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to select")));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pluralcraft-bp member select " + member.getID().toString()));
            builder.append(component);
            addNewline = true;
        }
        return new TextComponent(builder.create());
    }

    public static TextComponent set(String name, BaseComponent value) {
        ComponentBuilder builder = new ComponentBuilder();
        builder
                .append("Set property ").color(Components.NORMAL)
                .append(name).color(Components.HIGHLIGHT_1)
                .append(" to ").color(Components.NORMAL)
                .append(value).color(Components.HIGHLIGHT_2);
        return new TextComponent(builder.create());
    }

    public static TextComponent selected(String type, TextComponent value) {
        ComponentBuilder builder = new ComponentBuilder();
        builder
                .append("Selected " + type + " ").color(Components.NORMAL)
                .append(value).color(Components.HIGHLIGHT_1);
        return new TextComponent(builder.create());
    }

    public static TextComponent switched() {
        ComponentBuilder builder = new ComponentBuilder();
        builder
                .append("Switched fronter.").color(Components.NORMAL);
        return new TextComponent(builder.create());
    }

    public static TextComponent alreadyFronter(Member nextMember) {
        ComponentBuilder builder = new ComponentBuilder();
        builder
                .append(nextMember.getInformation().getName()).color(Components.HIGHLIGHT_1)
                .append(" is already the fronter!").color(Components.NORMAL);
        return new TextComponent(builder.create());
    }
}
