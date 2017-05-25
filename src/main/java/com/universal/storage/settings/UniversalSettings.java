package com.universal.storage.settings;

import java.io.File;
import org.json.JSONObject;
import org.json.JSONArray;
import com.universal.error.UniversalStorageException;
import com.universal.storage.UniversalProvider;
import com.universal.util.FileUtil;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

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
 * This class represents the settings for a specific context.
 */
public class UniversalSettings {
    private String root;
    private String tmp;
    private String accessKey;
    private String secretKey;
    private String storageClass;
    private String s3region;
    private String googleDriveClientId;
    private String googleDriveClientSecret;
    private String dropboxAccessToken;
    private Map<String, String> tags;    
    private boolean encryption;
    private UniversalProvider provider;

    private final static List<String> STORAGE_CLASSES = new ArrayList<String>();

    static {
        STORAGE_CLASSES.add("REDUCED_REDUNDANCY");
        STORAGE_CLASSES.add("STANDARD_IA");
        STORAGE_CLASSES.add("STANDARD");
    }

    /**
     * This method loads the settings and sets the mandatory attributes, if one of them 
     * is missing a error will be thrown.
     * 
     * @param file for loading settings.
     */
    public UniversalSettings(File file) throws UniversalStorageException {
        try {
            JSONObject json = new JSONObject(FileUtil.readAsString(file));

            this.root = json.getString("root");
            if (this.root == null) {
                throw new Exception("Attribute root is missing within the settings file.");
            }
            
            this.tmp = json.getString("tmp");
            if (this.tmp == null) {
                throw new Exception("Attribute tmp is missing within the settings file.");
            }

            this.tmp = FileUtil.completeFileSeparator(this.tmp);
            //this.async = json.getBoolean("async"); 
            this.provider = retrieveProvider(json.getString("provider"));

            if (this.provider == UniversalProvider.FILE_SYSTEM) {
                this.root = FileUtil.completeFileSeparator(this.root);
            } else if (this.provider == UniversalProvider.DROPBOX) {
                JSONObject dropbox = json.getJSONObject("dropbox");

                this.dropboxAccessToken = dropbox.getString("access_token");
                if (this.dropboxAccessToken == null || "".equals(this.dropboxAccessToken.trim())) {
                    this.dropboxAccessToken = System.getenv("access_token");
                }

                if (this.dropboxAccessToken == null || "".equals(this.dropboxAccessToken.trim())) {
                    throw new IllegalStateException("access_token is null");
                }
            } else if (this.provider == UniversalProvider.GOOGLE_DRIVE) {
                JSONObject googleDrive = json.getJSONObject("google_drive");

                this.googleDriveClientSecret = googleDrive.getString("client_secret");
                if (this.googleDriveClientSecret == null || "".equals(this.googleDriveClientSecret.trim())) {
                    this.googleDriveClientSecret = System.getenv("client_secret");
                }

                if (this.googleDriveClientSecret == null || "".equals(this.googleDriveClientSecret.trim())) {
                    throw new IllegalStateException("client_secret is null");
                }

                this.googleDriveClientId = googleDrive.getString("client_id");
                if (this.googleDriveClientId == null || "".equals(this.googleDriveClientId.trim())) {
                    this.googleDriveClientId = System.getenv("client_id");
                }

                if (this.googleDriveClientId == null || "".equals(this.googleDriveClientId.trim())) {
                    throw new IllegalStateException("client_id is null");
                }
            } else if (this.provider == UniversalProvider.AWS_S3) {
                /**
                 * Load the s3 settings.
                 */
                this.tags = new HashMap<String, String>();

                JSONObject awsS3 = json.getJSONObject("aws_s3");

                this.encryption = awsS3.getBoolean("encryption");
                this.accessKey = awsS3.getString("access_key");
                if (this.accessKey == null || "".equals(this.accessKey.trim())) {
                    this.accessKey = System.getenv("access_key");
                }

                if (this.accessKey == null || "".equals(this.accessKey.trim())) {
                    throw new IllegalStateException("access_key is null");
                }

                this.secretKey = awsS3.getString("secret_key");
                if (this.secretKey == null || "".equals(this.secretKey.trim())) {
                    this.secretKey = System.getenv("secret_key");
                }
                
                if (this.secretKey == null || "".equals(this.secretKey.trim())) {
                    throw new IllegalStateException("secretKey is null");
                }

                this.s3region = awsS3.getString("s3_region");
                if (this.s3region == null || "".equals(this.s3region.trim())) {
                    throw new IllegalStateException("s3region is null");
                }

                this.storageClass = awsS3.getString("storage_class");
                if (this.storageClass == null || "".equals(this.storageClass.trim())) {
                    throw new IllegalStateException("storageClass is null");
                }

                validateStorageClass(this.storageClass);

                try {
                    JSONArray tags = awsS3.getJSONArray("tags");
                    for (int i = 0; i < tags.length(); i++) {
                        JSONObject tag = tags.getJSONObject(i);
                        this.tags.put(tag.getString("key"), tag.getString("value"));
                    }
                } catch (Exception ignore) {}
            }
        } catch (Exception e) {
            throw new UniversalStorageException(e.getMessage());
        }
    }

    /**
     * This method validates the passed storage class.
     * 
     * @param storageClass in context.
     */
    private void validateStorageClass(String storageClass) {
        if (!STORAGE_CLASSES.contains(storageClass)) {
            throw new IllegalStateException(storageClass + " is a unknown storage class.");
        }
    }

    /**
     * This method retrieves the provider according to the provided attribute from the settings file.
     * 
     * @param p attribute in context.
     */
    private UniversalProvider retrieveProvider(String p) {
        if ("file.system".equals(p)) {
            return UniversalProvider.FILE_SYSTEM;
        } else if ("aws.s3".equals(p)) {
            return UniversalProvider.AWS_S3;
        } else if ("google.drive".equals(p)) {
            return UniversalProvider.GOOGLE_DRIVE;
        } else if ("dropbox".equals(p)) {
            return UniversalProvider.DROPBOX;
        }

        return UniversalProvider.UNKNOWN;
    }

    /**
     * This method returns the configured root from these settings. 
     */
    public String getRoot() {
        return this.root;
    }

    /**
     * This method return the configured tmp folder from these settings.
     */
    public String getTmp() {
        return this.tmp;
    }

    /**
     * This method returns the configured provider from these settings.
     */
    public UniversalProvider getProvider() {
        return this.provider;
    }

    /**
     * Returns the current aws access key;
     */
    public String getAWSAccessKeyId() {
        return this.accessKey;
    }

    /**
     * Returns the current aws secret key;
     */
    public String getAWSSecretKey() {
        return this.secretKey;
    }

    /**
     * This mehotd returns the storage class from settings.
     */
    public String getStorageClass() {
        return this.storageClass;
    }

    /**
     * Returns true is this settings will activate encryption at rest or not.
     */
    public boolean getEncryption() {
        return this.encryption;
    }

    /**
     * Returns the current tags from settings.
     */
    public Map<String, String> getTags() {
        return this.tags;
    }

    /**
     * This method returns the current google drive client id.
     */
    public String getGoogleDriveClientId() {
        return this.googleDriveClientId;
    }

    /**
     * This method returns the current google drive client secret.
     */
    public String getGoogleDriveClientSecret() {
        return this.googleDriveClientSecret;
    }

    /**
     * This method returns the current dropbox access token.
     */
    public String getDropboxAccessToken() {
        return this.dropboxAccessToken;
    }

    /**
     * This method returns the current s3 region for this setting.
     */
    public String getS3Region() {
        return this.s3region;
    }
}