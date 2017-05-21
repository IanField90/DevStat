package uk.co.ianfield.devstat.model

/**
 * Created by Ian Field on 20/02/2014.
 */
class StatItem {
    var title: String? = null
    var info: String? = null

    override fun toString(): String {
        return String.format("%s: %s", title, info)
    }
}
