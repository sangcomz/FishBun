package com.sangcomz.fishbun.util;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

import java.io.File;

/**
 * Created by sangcomz on 16/01/2017.
 */

public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

    private MediaScannerConnection mMs;
    private File mFile;
    private ScanListener scanListener;

    public SingleMediaScanner(Context context, File f) {
        init(context, f);
    }

    public SingleMediaScanner(Context context, File f, ScanListener scanListener) {
        init(context, f);
        this.scanListener = scanListener;
    }

    private void init(Context context, File f) {
        mFile = f;
        mMs = new MediaScannerConnection(context, this);
        mMs.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        mMs.scanFile(mFile.getAbsolutePath(), null);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        if (scanListener != null) scanListener.onScanCompleted();
        mMs.disconnect();
    }
}