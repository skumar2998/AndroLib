/*
 * Copyright 2013 Ivan Masmitjà
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

package net.compactsys.androlib.apps.dialog;

import android.app.DialogFragment;
import android.os.Bundle;

abstract class InternalDialogFragment extends DialogFragment {

    private static class HackishSavedState {
        private static Bundle mBundle = new Bundle();

        public static Bundle getBundle(String key) {
            return mBundle.getBundle(key);
        }

        public static void putBundle(String key, Bundle bundle) {
            mBundle.putBundle(key, bundle);
        }
    }

    private String getInternalStateKey() {
        return "internalState:" + getTag();
    }

    protected void saveInternalState(Bundle bundle) {
        HackishSavedState.putBundle(getInternalStateKey(), bundle);
    }

    protected void resetInternalState() {
        HackishSavedState.putBundle(getInternalStateKey(), null);
    }

    protected Bundle getInternalState() {
        return HackishSavedState.getBundle(getInternalStateKey());
    }


}
