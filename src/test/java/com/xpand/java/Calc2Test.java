package com.xpand.java;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;

public class Calc2Test {

    @BeforeSuite
    public void setUp() throws Exception {

    }

    @AfterSuite
    public void tearDown() throws Exception {
    }



    @Test
    public void MyCanDoStuff()
    {
        Assert.assertEquals(true, true);
    }

    @Test
    public void CanDoWeirdStuff()
    {
        Assert.assertEquals(true, false);
    }

}
