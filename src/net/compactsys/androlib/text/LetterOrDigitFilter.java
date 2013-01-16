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

package net.compactsys.androlib.text;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * This filter allow only letters or digits.
 * @see  Character#isLetterOrDigit(char)
 */
public class LetterOrDigitFilter implements InputFilter {

    public LetterOrDigitFilter() {
    }

    private boolean isAccepted(char ch) {
        return Character.isLetterOrDigit(ch);
    }

    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        for (int i = start; i < end; i++) {
            if (!isAccepted(source.charAt(i))) {
                return "";
            }
        }
        return null;
    }
}
