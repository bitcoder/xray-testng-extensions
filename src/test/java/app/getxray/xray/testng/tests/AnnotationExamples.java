package app.getxray.xray.testng.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import app.getxray.xray.testng.annotations.Requirement;
import app.getxray.xray.testng.annotations.XrayTest;

public class AnnotationExamples {
 
    @Test
    public void legacyTest() {
        Assert.assertEquals(true, true);
    }

    @Test
    @XrayTest(summary = "custom summary")
    public void annotatedWithTestSummary() {
    }
    
    @Test
    @XrayTest(description = "custom description")
    public void annotatedWithTestDescription() {
    }

    @Test
    @Requirement(key = "CALC-1234")
    public void annotatedWithRequirementKey() {
    }

    @Test
    @XrayTest(key = "CALC-2000")
    public void annotatedWithTestKey() {
    }

    @Test
    @XrayTest(labels = "core addition")
    public void annotatedWithLabels() {
    }

}
