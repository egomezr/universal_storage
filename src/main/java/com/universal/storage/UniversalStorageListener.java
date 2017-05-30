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
 * This interface declares the methods for working with listener.
 */

public interface UniversalStorageListener {
    /**
     * This method will be called when the storeFile method is called.
     */
    void onStoreFile();

    /**
     * This method will be called when the createFolder method is called.
     */
    void onCreateFolder();

    /**
     * This method will be called when the removeFile method is called.
     */
    void onRemoveFile();

    /**
     * This method will be called when the removeFolder method is called.
     */
    void onRemoveFolder();

    /**
     * This method will be called when a error occurs.
     * 
     * @param error in context.
     */
    void onError(UniversalIOException error);

    /**
     * This method will be called when the storeFile method has finished.
     * 
     * @param data useful information about the stored file.
     */
    void onFileStored(UniversalStorageData data);

    /**
     * This method will be called when the createFolder method has finished.
     * 
     * @param data useful information about the created folder.
     */
    void onFolderCreated(UniversalStorageData data);

    /**
     * This method will be called when the removeFile has finished.
     */
    void onFileRemoved();

    /**
     * This method will be called when the removeFolder has finished.
     */
    void onFolderRemoved();
} 