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

package net.compactsys.androlib.widget;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Array adapter used to keep selected position in ListView
 */
public class SelectedArrayAdapter<E> extends ArrayAdapter<E> {

    // init value for not-selected
    private int selectedPos = -1;

    public SelectedArrayAdapter(Context context, int textViewResourceId, List<E> objects) {
        super(context, textViewResourceId, objects);
    }

    public void setSelectedPosition(int pos) {
        selectedPos = pos;
        // inform the view of this change
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPos;
    }
}
