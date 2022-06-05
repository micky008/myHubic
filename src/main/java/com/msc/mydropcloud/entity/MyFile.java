package com.msc.mydropcloud.entity;

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
}
