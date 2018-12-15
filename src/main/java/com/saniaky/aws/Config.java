package com.saniaky.aws;

/**
 * @author Alexander Kohonovsky
 * @since 12/14/18
 */
@SuppressWarnings("WeakerAccess")
public class Config {

    private static Config config;
    public static final String JPG_TYPE = "jpg";
    public static final String JPG_MIME = "image/jpeg";
    public static final String PNG_TYPE = "png";
    public static final String PNG_MIME = "image/png";

    private String destBucket;
    private int maxWidth;
    private int maxHeight;

    private Config(String destBucket, int maxWidth, int maxHeight) {
        this.destBucket = destBucket;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    public static Config getInstance() {
        if (config == null) {
            config = new Config(System.getenv("DEST_BUCKET"), 100, 100);
        }
        return config;
    }

    public String getDestBucket() {
        return destBucket;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

}
