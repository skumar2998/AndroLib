
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

import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayUtils {
    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static <T> T[] concatAll(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }


    /**
     * Thread safe clone for backward compatibility as {@link SparseBooleanArray#clone()}
     * is supported first in 4.x APIs
     *
     * @param original object to clone
     * @return a clone
     */
    public static SparseBooleanArray clone(final SparseBooleanArray original) {
        if (original == null)
            return null;

        final SparseBooleanArray clone = new SparseBooleanArray();

        synchronized (original) {
            final int size = original.size();
            for (int i = 0; i < size; i++) {
                clone.put(i, original.get(i));
            }
        }

        return clone;
    }

    public static int[] toArray(final SparseBooleanArray original) {
        if (original == null)
            return null;

        final int orgSize = original.size();
        if (orgSize == 0)
            return null;

        int[] bigArray = new int[orgSize];
        int bigArrayIndex = 0;
        synchronized (original) {
            for (int i = 0; i < orgSize; i++) {
                if (original.valueAt(i)) {
                    bigArray[bigArrayIndex++] = original.keyAt(i);
                }
            }
        }
        return Arrays.copyOf(bigArray, bigArrayIndex);
    }
}