package uk.co.ianfield.devstat.model

/**
 * Created by Ian Field on 20/02/2014.
 */
open class StatItem {
    lateinit var title: String
    lateinit var info: String

    override fun toString(): String {
        return String.format("%s: %s", title, info)
    }
}
