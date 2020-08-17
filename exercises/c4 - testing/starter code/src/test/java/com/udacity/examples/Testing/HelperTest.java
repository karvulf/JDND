package com.udacity.examples.Testing;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;

public class HelperTest {

    @Test
    public void verify_getCount() {
        List<String> empNames = Arrays.asList("kaka", "wurst");
        long actual = Helper.getCount(empNames);
        assertEquals(2, actual);
    }

    @Test
    public void verify_getStats() {
        List<Integer> yrsOfExperience = Arrays.asList(13, 4, 15, 6, 17, 8, 19, 1, 2, 3);

        IntSummaryStatistics stats = Helper.getStats(yrsOfExperience);

        assertEquals(19, stats.getMax());
    }

    @Test
    public void verify_getMerged() {
        List<String> empNames = Arrays.asList("kaka", "", "wurst");
        String actual = Helper.getMergedList(empNames);
        assertEquals("kaka, wurst", actual);
    }
}
