package de.mycrocast.dtalisty.adapter;

import android.view.View;

public interface RecyclerClickListener{

    void onEntryClick(View view, int entryIndex);

    void onDeleteClick(View view, int entryIndex);

    void onEditClick(View view, int entryIndex);

}
