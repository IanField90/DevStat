package uk.co.ianfield.devstat.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Ian on 20/11/2015.
 */
@RunWith(JUnit4.class)
public class StatItemTest {

    @Test
    public void testToString() {
        StatItem item = mock(StatItem.class);
        when(item.getTitle()).thenReturn("a");
        when(item.getInfo()).thenReturn("b");
        assertThat(item.getTitle()).isEqualTo("a");

        // Can't mock toString()
        item = new StatItem();
        item.setTitle("a");
        item.setInfo("b");
        assertThat(item.toString()).isEqualTo("a: b");
    }
}
