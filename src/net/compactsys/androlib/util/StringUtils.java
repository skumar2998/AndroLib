
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

public class StringUtils {

    /**
     * Removes similar characters specified from the beginning of a string.
     * @param str Original string
     * @param ch Character to remove
     * @return The string that remains after all occurrences of ch parameter are removed
     *         from the start of the current string.
     */
    public static String removeFirstChars(String str, char ch)
    {
        int start = 0;
        while ((start < str.length()) && (str.charAt(start) == ch))
            start++;

        if (start < str.length())
            return str.substring(start);
        else
            return "";
    }
}
