package com.msc.mydropcloud.entity;

import com.msc.mydropcloud.Link;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author Michael
 */
public class MyFile {

    public String endpoint;
    public UUID uuid;
    public boolean isDir = false;
    public UUID parent;
    public String hash;

    public MyFile() {
    }

    public MyFile(Link.MyFile lMyFile) {
        this.endpoint = lMyFile.getEndpoint();
        this.uuid = UUID.fromString(lMyFile.getUuid());
        this.isDir = lMyFile.getIsDir();
        this.parent = UUID.fromString(lMyFile.getParent());
        this.hash = lMyFile.getHash();
    }

    @Override
    public String toString() {
        return uuid.toString() + " " + endpoint;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.uuid);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MyFile other = (MyFile) obj;
        return Objects.equals(this.uuid, other.uuid);
    }


}
