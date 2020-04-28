package cn.tohsaka.factory.MCUpdater.utils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class OkHttpDownloader {

    private final String url, destFilename;
    private static final Logger LOGGER = Logger.getLogger(OkHttpDownloader.class.getName());

    public OkHttpDownloader(String url, String destFilename) {
        this.url = url;
        this.destFilename = destFilename;

    }

    public void download(Callback callback) {
        BufferedInputStream input = null;
        try (FileOutputStream fos = new FileOutputStream(destFilename)) {
            Request request = new Request.Builder().url(url).build();
            ResponseBody responseBody = new OkHttpClient().newCall(request).execute().body();
            long fileSize = responseBody.contentLength();
            input = new BufferedInputStream(responseBody.byteStream());
            byte[] buffer = new byte[10 * 1024 * 1024];
            int numberOfBytesRead;
            long totalNumberOfBytesRead = 0;
            while ((numberOfBytesRead = input.read(buffer)) != - 1) {
                fos.write(buffer, 0, numberOfBytesRead);
                totalNumberOfBytesRead += numberOfBytesRead;
                callback.onProgress(totalNumberOfBytesRead);
            }
            callback.onFinish();
        } catch (IOException ex) {
            callback.onError(ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public interface Callback {

        void onProgress(long progress);

        void onFinish();

        void onError(IOException ex);
    }
}
