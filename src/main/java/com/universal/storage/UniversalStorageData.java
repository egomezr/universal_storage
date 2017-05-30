package com.universal.storage;

/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Dynamicloud
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * This class represents the returned data from a specific provider after either 
 * storing a new file or creation of a new folder.
 */

public class UniversalStorageData {
    private String name;
    private String remoteLink;
    private String remoteId;
    private String remotePath;

    /**
     * This constructor instantiates a new UniversalStorageDate with the passwed attributes.
     * 
     * @param name for this new UniversalStorageDate
     * @param remoteLink for this new UniversalStorageDate
     * @param remoteId for this new UniversalStorageDate
     * @param remotePath for this new UniversalStorageDate
     */
    public UniversalStorageData(String name, String remoteLink, String remoteId, String remotePath) {
        this.name = name;
        this.remoteLink = remoteLink;
        this.remoteId = remoteId;
        this.remotePath = remotePath;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getRemoteLink() {
        return this.remoteLink;
    }
    public void setRemoteLink(String remoteLink) {
        this.remoteLink = remoteLink;
    }

    public String getRemoteId() {
        return this.remoteId;
    }
    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public String getRemotePath() {
        return this.remotePath;
    }
    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public String toString() {
        return new StringBuilder(100)
                .append(this.name)
                .append("\n")
                .append(this.remotePath)
                .append("\n")
                .append(this.remoteId)
                .append("\n")
                .append(this.remoteLink)
                .toString();
    }
}