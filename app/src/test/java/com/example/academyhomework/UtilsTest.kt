package com.example.academyhomework

import com.example.academyhomework.utils.MovieDiffHelper
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UtilsTest {
    private val networkMock = NetworkMock()
    @Test
    fun `MovieDiffHelper test stale`() {


        val list1 = networkMock.loadMovies(0..10)
        val list2 = networkMock.loadMovies(0..15)

        val diff = MovieDiffHelper.getDiff(list1,list2)

        assertEquals(
            MovieDiffHelper.Relevance.StaleData(
                newListIndies = listOf(11, 12, 13, 14, 15)),
            diff)
    }

    @Test
    fun `MovieDiffHelper test fresh`() {

        val list1 = networkMock.loadMovies(0..10)
        val list2 = networkMock.loadMovies(0..10)

        val diff = MovieDiffHelper.getDiff(list1,list2)

        assertEquals(
            MovieDiffHelper.Relevance.FreshData,
            diff)
    }
}