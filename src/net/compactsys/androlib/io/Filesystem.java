
/*
 * Copyright 2012 Ivan Masmitjà
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.compactsys.androlib.io;

import android.os.Environment;

import java.io.*;

public class Filesystem {
    private final static int COPY_BUFFER_SIZE = 1024;

    /**
     * Copies an existing file to a new file.
     *
     * @param sourceFileName The file to copy.
     * @param destFileName   The name of the destination file. This cannot be a directory or an existing file.
     * @return Bytes transferred
     * @throws IOException
     */
    public static int Copy(String sourceFileName, String destFileName) throws IOException {
        InputStream myInput = new FileInputStream(sourceFileName);
        OutputStream myOutput = new FileOutputStream(destFileName);

        int totalBytes = Copy(myInput, myOutput);

        myOutput.close();
        myInput.close();

        return totalBytes;
    }

    /**
     * Transfer bytes from the istream to the ostream
     *
     * @param istream Input stream
     * @param ostream Output stream
     * @return Bytes transferred
     * @throws IOException
     */
    public static int Copy(InputStream istream, OutputStream ostream)
            throws IOException {

        byte[] buffer = new byte[COPY_BUFFER_SIZE];
        int length;
        int totalBytes = 0;
        while ((length = istream.read(buffer)) > 0) {
            ostream.write(buffer, 0, length);
            totalBytes += length;
        }

        ostream.flush();
        return totalBytes;
    }

    public static String getSDCardPath() {
        File f = Environment.getExternalStorageDirectory();
        return f.getAbsolutePath();
    }

    /**
     * Check if SDCARD is available.
     *
     * @return True if SDCARD is available.
     */
    public static boolean isSDCardAvailable() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            return true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            return true;
        } else {
            // Something else is wrong. It may be one of many other states, but
            // all we need to know is we can neither read nor write
            return false;
        }
    }

    /**
     * Check if SDCARD is available and writable
     *
     * @return True if SDCARD is available and writable
     */
    public static boolean isSDCardWritable() {
        // Check SD Storage
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            return true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            return false;
        } else {
            // Something else is wrong. It may be one of many other states, but
            // all we need to know is we can neither read nor write
            return false;
        }
    }

    /**
     * Gets a list of matching files in the directory.
     *
     * @param directory        Directory
     * @param fileRegExpFilter Regular expression
     * @return List of matching files
     */
    public static File[] getFiles(File directory, final String fileRegExpFilter) {
        FileFilter filter = new FileFilter() {
            public boolean accept(File file) {
                return (file.isFile() && file.getName().matches(fileRegExpFilter));
            }
        };

        if ((directory != null) && (directory.exists()))
            return directory.listFiles(filter);
        else
            return null;
    }
}
