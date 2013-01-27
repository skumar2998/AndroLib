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


package net.compactsys.androlib.util;

import java.io.File;

public final class FileUtils {
    private FileUtils() {
    }

    /**
     * Retorna l'extensio d'un fitxer. Si el punt és en el primer caràcter no el
     * contempla com a extensió. (Ocults de linux)
     *
     * @param f Fitxer
     * @return Retorna l'extensio incloent el punt (ex. ".ext").
     */
    public static String getFileExtension(File f) {
        int dot = f.getName().lastIndexOf('.');
        if (dot > 0) {
            return f.getName().substring(dot);
        }
        return "";
    }

    public static File changeFileExtension(File f, String newfileExtension) {
        return new File(
                changeFileExtension(f.getAbsolutePath(), newfileExtension));
    }

    public static String changeFileExtension(String filename,
                                             String newfileExtension) {
        int dotPos = filename.lastIndexOf(".");
        String strFilename = filename.substring(0, dotPos);

        String strNewFileName = strFilename + "." + newfileExtension;
        return strNewFileName;
    }

    public static String getFilePath(File f) {
        String abspath = f.getAbsolutePath();
        return abspath.substring(0, abspath.lastIndexOf(File.separator));
    }
}
