/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.systems;

import com.comphenix.protocol.wrappers.WrappedSignedProperty;

public class Skin {
    private String texture;
    private String signature;

    public Skin(String texture, String signature) {
        this.texture = texture;
        this.signature = signature;
    }

    public WrappedSignedProperty toProperty() {
        return new WrappedSignedProperty("textures", texture, signature);
    }

    public static final Skin TEST = new Skin("ewogICJ0aW1lc3RhbXAiIDogMTYyODQyMzAxMTY5NiwKICAicHJvZmlsZUlkIiA6ICJmNWQwYjFhZTQxNmU0YTE5ODEyMTRmZGQzMWU3MzA1YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJDYXRjaFRoZVdhdmUxMCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82NzVkM2Q4ZTIzYTdmNTdlOTNkMjkxN2RkMThjNjYwZDg3NWJkYWIwNDIxZDkxYTAxOWM1M2JkZWFlYjU5N2FkIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=", "wjCWAT5bF9Iko5U/yzuSKymLNpB/tablcXUbZt8TJwL7PP3CurRd+uuMtojdK6/X0DcRbt8ObpFM9qsHafVCyoSCXiXrgvh7XR4RXByni8RrKxRUtUp+hW6/Q0BCKut19Ae/JNPACwI3hCnsNJDG+dHpCybJTD4aGQ3Vf/gO4cZBL2dowanllTjCfEIjWIUxnczlVatADqup1EJ5FiZAFkLb2esmcF8J36+AqEYLL0U10fuXV7lTzO07+L0bQey3ZXur81mSNJNhuRsPmf+UGOGqHKDyjaeeWW8ltNSsoHRnP0VWbpz0dnY6uuwXTiATE8XRtiAPCXbIqELTaxrmN+WDD0hNnSm6CMLKN80yEvQmJ98V1wZ9gvH0iXEUL5OwSRT3nnoQM//9Eve03uI6S7PEdTIgFwoRSvfXtFD+I5MOPOWVHDrEdURLwIZIVPfRc6ZC7WPEnQ9A5SzPfRfmXc/pF/KK45O1V1SFSExV6H0mNxjFq+ermVSZOrDjL9oJA1zoTH4gCYLE2CWGjjpPiIYtFbnUA9j+VwM3UAoILEmSoX55264T3GQKMlvMUDrb1sAcNIGRb0OkJqS/uQux7FpOguD+3ess1e1b2dtydsoXqGRJNQXPmSpgdEGG4TE3BT/PaUK0mNrcTBWTbJpEC77wJHnoZJtZwvf+BfmGzFs=");
}
