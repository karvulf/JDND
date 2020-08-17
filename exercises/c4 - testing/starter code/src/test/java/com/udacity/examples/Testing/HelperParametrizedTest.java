package com.udacity.examples.Testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class HelperParametrizedTest {

    private String input;
    private String output;

    public HelperParametrizedTest(String input, String output) {
        super();
        this.input = input;
        this.output = output;
    }

    @Parameters
    public static Collection initData() {
        String empNames[][] = {{"kaka", "wurst"},{"kaka", "pipi"}};
        return Arrays.asList(empNames);
    }

    @Test
    public void verify_input_name_is_not_the_same_as_the_output_name() {
        assertNotEquals(input, output);
    }
}
