package com.example.academyhomework.entities

data class Movie(
    val id: Int,
    val pgAge: Int? = null,
    val title: String,
    val genres: List<Genre>,
    val reviewCount: Int? = null,
    val isLiked: Boolean? = null,
    val rating: Double,
    val imageUrl: String,
    val detailImageUrl: String? = null,
    val storyLine: String? = null,
    val releaseDate:String,
    val popularity:Double

){
    /**
     * Returns a hash code value for the object.  The general contract of `hashCode` is:
     *
     * * Whenever it is invoked on the same object more than once, the `hashCode` method must consistently return the same integer, provided no information used in `equals` comparisons on the object is modified.
     * * If two objects are equal according to the `equals()` method, then calling the `hashCode` method on each of the two objects must produce the same integer result.
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }
}
