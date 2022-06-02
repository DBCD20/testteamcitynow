package com.jetbrains.teamcity.anewtodolist.integration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SlowWebServiceTest {

    @Test
    public void slowTest() throws InterruptedException {
        Thread.sleep(5000);
        assertEquals(1,1);
    }
}
