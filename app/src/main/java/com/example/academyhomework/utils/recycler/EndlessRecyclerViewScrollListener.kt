package com.example.academyhomework.utils.recycler

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 *   @author https://gist.github.com/nesquena/d09dc68ff07e845cc622
 */

abstract class EndlessRecyclerViewScrollListener(
    private val layoutManager: GridLayoutManager
) : RecyclerView.OnScrollListener() {

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private var visibleThreshold = 3 * layoutManager.spanCount

    // The current offset index of data you have loaded
    private var currentPage = 0

    // The total number of items in the dataset after the last load
    private var previousTotalItemCount = 0

    // True if we are still waiting for the last set of data to load.
    private var loading = true

    // Sets the starting page index
    private val startingPageIndex = 0


    fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    /**
     *  This happens many times a second during a scroll, so be wary of the code you place here.
     * We are given a few useful parameters to help us work out if we need to load some more data,
     * but first we check if we are waiting for the previous load to finish.
     *
     * @param dx The amount of horizontal scroll.
     * @param dy The amount of vertical scroll.
     */
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                var lastVisibleItemPosition = 0
                val totalItemCount = layoutManager.itemCount

                // get maximum element within the list
                if (layoutManager is GridLayoutManager) {
                    lastVisibleItemPosition =
                        (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                }
                // If the total item count is zero and the previous isn't, assume the
                // list is invalidated and should be reset back to initial state
                if (totalItemCount < previousTotalItemCount) {
                    currentPage = startingPageIndex
                    previousTotalItemCount = totalItemCount
                    if (totalItemCount == 0) {
                        loading = true
                    }
                }
                // If it’s still loading, we check to see if the dataset count has
                // changed, if so we conclude it has finished loading and update the current page
                // number and total item count.
                if (loading && (totalItemCount > previousTotalItemCount)) {
                    loading = false
                    previousTotalItemCount = totalItemCount;
                }
                // If it isn’t currently loading, we check to see if we have breached
                // the visibleThreshold and need to reload more data.
                // If we do need to reload some more data, we execute onLoadMore to fetch the data.
                // threshold should reflect how many total columns there are too
                if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
                    currentPage++
                    onLoadMore(currentPage, totalItemCount, recyclerView)
                    loading = true
                }



    }

    // Call this method whenever performing new searches
    fun resetState() {
        currentPage = startingPageIndex
        previousTotalItemCount = 0
        loading = true
    }

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?)

}