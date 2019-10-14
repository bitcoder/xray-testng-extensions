package com.xpand.java;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import org.testng.Reporter;
import org.testng.reporters.XMLReporter;
import org.testng.ITestResult;
import com.xpand.annotations.Xray;
import java.io.File;
import java.nio.file.Files;

public class CalcTest {

    @BeforeSuite
    public void setUp() throws Exception {

    }

    @AfterSuite
    public void tearDown() throws Exception {
    }

/*
    public static String getFileAsBase64String(File file){
        byte[] fileContent = Files.readAllBytes(file.toPath());
        byte[] encoded = enc.encode(fileContent);
        String encodedStr = new String(encoded,"UTF-8");
    }
*/

    
    @DataProvider
    public Object[][] ValidDataProvider() {
        return new Object[][]{
            {  1, 2, 3 },
            {  2, 3, 4 },  // error or the data itself :)
            { -1, 1, 0 }
        };
    }


    @Test(dataProvider = "ValidDataProvider")
    @Xray(requirement = "CALC-1234", test = "CALC-1")
    public void CanAddNumbersFromGivenData(final int a, final int b, final int c)
    {
        Assert.assertEquals(Calculator.Add(a, b), c);
    }


	@Test
    @Xray(requirement = "CALC-1234", test = "CALC-2", labels = "core addition")
    public void CanAddNumbers()
    {
        Assert.assertEquals(Calculator.Add(1, 1),2);
        Assert.assertEquals(Calculator.Add(-1, 1),0);
        ITestResult result = Reporter.getCurrentTestResult();
        File attachments[] = new File[] { new File("/Users/smsf/1.png"), new File("/Users/smsf/2.png") } ;

        result.setAttribute("file", new File("/Users/smsf/1.png"));
        result.setAttribute("attachments", attachments);
        //result.addAttachment(new File("/Users/smsf/1.png"));
        //result.addAttachment(new File("/Users/smsf/2.png"));    
    }


    @Test(groups = { "functest", "operations" })
    @Xray(requirement = "CALC-1235", labels = "core")
    public void CanSubtract()
    {
        Assert.assertEquals(Calculator.Subtract(1, 1), 0);
        Assert.assertEquals(Calculator.Subtract(-1, -1), 0);
        Assert.assertEquals(Calculator.Subtract(100, 5), 95);
    }


    @Test(groups = { "operations" })
    @Xray(requirement = "CALC-1236")
    public void CanMultiplyX()
    {
        Assert.assertEquals(Calculator.Multiply(1, 1), 1);
        Assert.assertEquals(Calculator.Multiply(-1, -1), 1);
        Assert.assertEquals(Calculator.Multiply(100, 5), 500);
    }


    @Test
    @Xray(requirement = "CALC-1237")
    public void CanDivide()
    {
        Assert.assertEquals(Calculator.Divide(1, 1), 1);
        Assert.assertEquals(Calculator.Divide(-1, -1), 1);
        Assert.assertEquals(Calculator.Divide(100, 5), 20);
    }


    @Test
    public void CanDoStuff()
    {
        Assert.assertNotEquals(true, true);
    }


}
