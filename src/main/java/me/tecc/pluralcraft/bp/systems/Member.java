/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.systems;

import me.tecc.pluralcraft.bp.storage.Storage;

import java.io.File;
import java.util.UUID;

public class Member implements IHasInformation {
    private Information information;
    private final UUID systemID;
    private final UUID id;

    Member(PcSystem system, String name) {
        this.systemID = system.getID();
        this.id = UUID.randomUUID();
        this.information = new Information(null);
        this.information.setName(name);
        this.information.setIhi(this);
    }

    public Information getInformation() {
        this.information.setIhi(this);
        return this.information;
    }

    public PcSystem getSystem() {
        return SystemManager.getSystem(getSystemID());
    }
    public UUID getSystemID() {
        return this.systemID;
    }

    public UUID getID() {
        return this.id;
    }
    public File getNBTFile() {
        return Storage.getMemberFile(getSystemID(), getID(), "dat");
    }

    @Override
    public void update(Information information) {
        this.information = information;
        PcSystem system = getSystem();
        if (system != null) system.update(this);
    }
}
