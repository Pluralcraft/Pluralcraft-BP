/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.systems;

import me.tecc.pluralcraft.bp.util.IBP;

import java.util.logging.Level;

public class Information implements IBP {
    private String name;
    private Skin skin;
    private String pronouns;
    private transient IHasInformation ihi;

    public Information(IHasInformation ihi) {
        this.ihi = ihi;
    }

    public void setIhi(IHasInformation ihi) {
        this.ihi = ihi;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
        update();
    }

    public Skin getSkin() {
        return skin;
    }
    public void setSkin(Skin skin) {
        this.skin = skin;
        update();
    }

    private void update() {
        try {
            if (this.ihi != null) this.ihi.update(this);
        } catch (Throwable t) {
            logger().log(Level.WARNING, t, () -> "Error whilst updating information");
        }
    }

    public String getPronouns() {
        return pronouns;
    }

    public void setPronouns(String pronouns) {
        this.pronouns = pronouns;
    }
}
