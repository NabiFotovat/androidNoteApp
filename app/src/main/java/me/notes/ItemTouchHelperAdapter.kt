package me.notes

import android.support.v7.widget.RecyclerView

/**
 * Interface to notify a [RecyclerView.Adapter] of moving and dismissal event from a [ ].

 * @author Paul Burke (ipaulpro)
 */
interface ItemTouchHelperAdapter {

    /**
     * Called when an item has been dragged far enough to trigger a move. This is called every time
     * an item is shifted, and not at the end of a "drop" event.

     * @see RecyclerView.getAdapterPositionFor
     * @see RecyclerView.ViewHolder.getAdapterPosition
     */
    fun onItemMove(fromPosition: Int, toPosition: Int)


    /**
     * Called when an item has been dismissed by a swipe.
     */
    fun onItemDismiss(position: Int)
}