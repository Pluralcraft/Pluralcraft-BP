/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.messages;

import me.tecc.pluralcraft.bp.systems.Member;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;

import java.util.List;

public class Components {
    public static final ChatColor NORMAL = ChatColor.AQUA;
    public static final ChatColor ERROR = ChatColor.RED;
    public static final ChatColor HIGHLIGHT_1 = ChatColor.YELLOW;
    public static final ChatColor HIGHLIGHT_2 = ChatColor.WHITE;

    public static TextComponent throwable(Throwable t) {
        TextComponent component = new TextComponent();
        component.setText(t.getClass().getSimpleName());
        component.setColor(HIGHLIGHT_1);
        HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(t.getMessage()));
        component.setHoverEvent(hover);
        return component;
    }
    public static TextComponent coloured(String text, ChatColor color) {
        TextComponent component = new TextComponent(text);
        component.setColor(color);
        return component;
    }

    public static Text text(BaseComponent... components) {
        return new Text(components);
    }

    public static TextComponent member(Member member) {
        ComponentBuilder builder = new ComponentBuilder();
        builder
                .append(member.getInformation().getName()).color(HIGHLIGHT_1)
                .append(" (").color(NORMAL)
                .append(member.getID().toString()).color(HIGHLIGHT_2)
                .append(")").color(NORMAL);
        return new TextComponent(builder.create());
    }
}
