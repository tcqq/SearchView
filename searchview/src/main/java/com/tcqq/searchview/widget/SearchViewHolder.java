/*
 * Copyright 2018 Martin Lapis, Perry Lance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tcqq.searchview.widget;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcqq.searchview.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


public class SearchViewHolder extends RecyclerView.ViewHolder {

    final ImageView icon_1;
    final ImageView icon_2;
    final TextView title;
    final TextView subtitle;

    SearchViewHolder(@NonNull View itemView, @Nullable final SearchAdapter.OnSearchItemClickListener listener) {
        super(itemView);
        icon_1 = itemView.findViewById(R.id.search_icon_1);
        icon_2 = itemView.findViewById(R.id.search_icon_2);
        title = itemView.findViewById(R.id.search_title);
        subtitle = itemView.findViewById(R.id.search_subtitle);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onSearchItemClick(
                            getLayoutPosition(),
                            title.getText(),
                            subtitle.getText());
                }
            }
        });
    }

}
