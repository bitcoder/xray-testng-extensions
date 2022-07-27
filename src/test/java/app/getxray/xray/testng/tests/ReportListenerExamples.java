package app.getxray.xray.testng.tests;

import java.io.File;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import app.getxray.xray.testng.listeners.XrayReportListener;

@Listeners(XrayReportListener.class)
public class ReportListenerExamples {
    
    @Test
    public void addAttachements()
    {
        ITestResult result = Reporter.getCurrentTestResult();
        String myFile = "src/test/java/app/getxray/xray/testng/tests/xray.png";
        File attachments[] = new File[] { new File(myFile) } ;
        result.setAttribute("attachments", attachments); 
    }
}
