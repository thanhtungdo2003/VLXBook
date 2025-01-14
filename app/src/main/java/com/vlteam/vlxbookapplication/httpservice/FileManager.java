package com.vlteam.vlxbookapplication.httpservice;

import android.content.Context;
import android.net.Uri;

import okhttp3.ResponseBody;

public interface FileManager {
    void upload(Uri uri, FileStorageType type);
    Uri download(Context context, String filename, FileStorageType type);

    String getFileName(Uri uri);
    java.io.File uriToFile(Uri uri);
    boolean saveFileToInternalStorage(Context context, ResponseBody body, String filename);
    public Uri getFileUri(Context context, String filename);
}
