package com.universal.storage;

import java.io.File;

import com.universal.error.UniversalIOException;

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
 * This class implements the methods with empty code, this is useful to avoid new implementations 
 * with a lot of empty methods.
 */

public class UniversalStorageListenerAdapter implements UniversalStorageListener {
    /**
     * This method will be called just before storing process.
     */
    public void onStoreFile() {

    }

    /**
     * This method will be called just before creation process.
     */
    public void onCreateFolder() {
        
    }

    /**
     * This method will be called just before file removing process.
     */
    public void onRemoveFile() {
        
    }

    /**
     * This method will be called just before folder removing process.
     */
    public void onRemoveFolder() {
        
    }

    /**
     * This method will be called when an error occurs.
     */
    public void onError(UniversalIOException error) {
        
    }

    /**
     * This method will be called just after storing process.
     * 
     * @param data contains data about the new file.
     */
    public void onFileStored(UniversalStorageData data) {
        
    }

    /**
     * This method will be called just after creation process.
     * 
     * @param data contains data about the new folder.
     */
    public void onFolderCreated(UniversalStorageData data) {
        
    }

    /**
     * This method will be called just after file removing process.
     */
    public void onFileRemoved() {
        
    }

    /**
     * This method will be called just after folder removing process.
     */
    public void onFolderRemoved() {
        
    }
}