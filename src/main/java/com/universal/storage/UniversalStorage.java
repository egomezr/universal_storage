package com.universal.storage;

import com.universal.error.UniversalIOException;
import com.universal.storage.settings.UniversalSettings;
import com.universal.storage.UniversalProvider;
import java.io.InputStream;
import java.io.File;
import java.util.Map;
import java.util.HashMap;

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
 * This class declares and implements the available methods to manage files.
 */
public abstract class UniversalStorage {
    protected UniversalSettings settings;
    private static final Object lock = new Object();
    private static final Map<String, UniversalStorage> STORAGES = new HashMap<String, UniversalStorage>();
    /**
     * This constructor will be implemented by the extended classes.
     * 
     * @param settings for this new storage instance.
     */
    public UniversalStorage(UniversalSettings settings) {
        this.settings = settings;
    }
    /**
     * This method stores a file within the storage provider according to the current settings.
     * The method will replace the file if already exists within the root.
     * 
     * @param file to be stored within the storage.
     * @throws UniversalIOException when a specific IO error occurs.
     */
    void storeFile(File file) throws UniversalIOException {
        storeFile(file, null);
    }

    /**
     * This method stores a file within the storage provider according to the current settings.
     * The method will replace the file if already exists within the root.
     * 
     * @param file to be stored within the storage.
     * @param path is the path for this new file within the root.
     * @throws UniversalIOException when a specific IO error occurs.
     */
    abstract void storeFile(File file, String path) throws UniversalIOException;

    /**
     * This method stores a file according to the provided path within the storage provider according to the current settings.
     * 
     * @param path pointing to the file which will be stored within the storage.
     * @throws UniversalIOException when a specific IO error occurs.
     */
    abstract void storeFile(String path) throws UniversalIOException;

    /**
     * This method stores a file according to the provided path within the storage provider according to the current settings.
     * 
     * @param path pointing to the file which will be stored within the storage.
     * @param targetPath is the path within the storage.
     * 
     * @throws UniversalIOException when a specific IO error occurs.
     */
    abstract void storeFile(String path, String targetOath) throws UniversalIOException;

    /**
     * This method removes a file from the storage.  This method will use the path parameter 
     * to localte the file and remove it from the storage.
     * 
     * @param path is the file's path.  
     * @throws UniversalIOException when a specific IO error occurs.
     */
    abstract void removeFile(String path) throws UniversalIOException;

    /**
     * This method creates a new folder within the storage. 
     * 
     * @param path is the folder's path.
     * @throws UniversalIOException when a specific IO error occurs.
     */
    abstract void createFolder(String path) throws UniversalIOException;

    /**
     * This method removes the folder located on that path.
     * 
     * @param path of the folder.
     */
    abstract void removeFolder(String path) throws UniversalIOException;

    /**
     * This method retrieves a file from the storage.
     * The method will retrieve the file according to the passed path.  A file will be stored within the settings' tmp folder.
     * 
     * @param path in context.
     * @returns a file pointing to the retrieved file.
     */
    abstract File retrieveFile(String path) throws UniversalIOException;

    /**
     * This method retrieves a file from the storage as InputStream.
     * The method will retrieve the file according to the passed path.  
     * A file will be stored within the settings' tmp folder.
     * 
     * @param path in context.
     * @returns an InputStream pointing to the retrieved file.
     */
    abstract InputStream retrieveFileAsStream(String path) throws UniversalIOException;

    /**
     * This method cleans the context of this storage.  This method doesn't remove any file from the storage.
     * The method will clean the tmp folder to release disk usage.
     */
    abstract void clean() throws UniversalIOException ;

    public static class Impl {
        /**
         * This method returns a UniversalStorage according to the provider.
         * This method will loads the settings according to the current context.
         * 
         * Loading settings from context, the following order will be used:
         * 1. Throughout jvm parameter (universal.storage.settings).
         * 2. Throughout environment variable (univeral_storage_settings).
         */
        public static UniversalStorage getInstance() {
            return getInstance(null);
        }

        /**
         * This method returns a UniversalStorage according to the provider.
         * This method will loads the settings according to the current context if the custom settings is null.
         * 
         * For loading settings from context, the following order will be used:
         * 1. Throughout jvm parameter (universal.storage.settings).
         * 2. Throughout environment variable (univeral_storage_settings).
         * 
         * @param s is a custom settings.
         */
        public static UniversalStorage getInstance(UniversalSettings s) {
            File file = null;
            if (s == null) {
                /*
                * We need to load the current settings according to the following options:
                * 1. Throughout jvm parameter (universal.storage.settings).
                * 2. Throughout environment variable (univeral_storage_settings).
                * 
                * The discovery for these settings will be executed following the order above.
                */
                String settingFile = System.getProperties().getProperty("universal.storage.settings");
                if (settingFile == null) {
                    settingFile = System.getenv().get("universal_storage_settings");
                }

                if (settingFile == null) {
                    throw new IllegalStateException("It was not found the parameter to read settings.  Try to check the parameters (universal.storage.settings or universal_storage_settings).");
                }

                file = new File(settingFile);
                if (!file.exists() || file.isDirectory()) {
                    throw new IllegalStateException("The setting file '" + file.getName() + "' either doesn't exist or is a folder.");
                }
            }

            try {
                UniversalSettings settings = s == null ? new UniversalSettings(file) : s;
                return retrieveUniversalStorage(settings);
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalStateException(e.getMessage());
            }
        }
    }

    /**
     * This method retrieves an UniversalStorage according to the provider within current settings.
     * 
     * @param settings in context.
     */
    private static UniversalStorage retrieveUniversalStorage(UniversalSettings settings) throws Exception {
        synchronized (lock) {
            if (settings.getProvider() == UniversalProvider.FILE_SYSTEM) {
                registerUniversalStorage("com.universal.storage.UniversalFileStorage", settings);
                return STORAGES.get("com.universal.storage.UniversalFileStorage");            
            } else if (settings.getProvider() == UniversalProvider.AWS_S3) {
                registerUniversalStorage("com.universal.storage.UniversalS3Storage", settings);
                return STORAGES.get("com.universal.storage.UniversalS3Storage");
            } else if (settings.getProvider() == UniversalProvider.GOOGLE_DRIVE) {
                registerUniversalStorage("com.universal.storage.UniversalGoogleDriveStorage", settings);
                return STORAGES.get("com.universal.storage.UniversalGoogleDriveStorage");
            } else if (settings.getProvider() == UniversalProvider.DROPBOX) {
                registerUniversalStorage("com.universal.storage.UniversalDropboxStorage", settings);
                return STORAGES.get("com.universal.storage.UniversalDropboxStorage");
            }

            return null;
        }
    }

    /**
     * This method verifies if exist an instance in STORAGES.
     * 
     * @param name of a universal storage.  
     */
    public static void registerUniversalStorage(String name, UniversalSettings settings) throws Exception {
        //if (STORAGES.get(name) == null) {
            STORAGES.put(name, (UniversalStorage) Class.forName(name).getConstructor(UniversalSettings.class).
                        newInstance(settings));
        //}
    }

    protected void validateRoot(UniversalSettings settings) throws UniversalIOException {
        if (settings.getProvider() == UniversalProvider.FILE_SYSTEM) {
            /**
             * Validates if this root is a folder
             */
            File rootFolder = new File(settings.getRoot());
            if (!rootFolder.isDirectory()) {
                throw new UniversalIOException(settings.getRoot() + " is not a folder.");
            }
        }
    }
}