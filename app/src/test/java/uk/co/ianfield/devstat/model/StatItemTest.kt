package uk.co.ianfield.devstat.model

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import org.mockito.Mockito.`when` as whenMock

/**
 * Created by Ian on 20/11/2015.
 */
@RunWith(JUnit4::class)
class StatItemTest {
    @Mock lateinit var statItem: StatItem

    @Before
    fun setUp() {
        initMocks(this)
    }

    @Test
    @Throws(Exception::class)
    fun testToString() {
        whenMock(statItem.title).thenReturn("a")
        whenMock(statItem.info).thenReturn("b")
        assertThat(statItem.title).isEqualTo("a")

        // Can't mock toString()
        statItem = StatItem()
        statItem.title = "a"
        statItem.info = "b"
        assertThat(statItem.toString()).isEqualTo("a: b")
    }
}
