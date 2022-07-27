
package app.getxray.xray.testng.listeners;

import java.lang.reflect.Method;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import app.getxray.xray.testng.annotations.Requirement;
import app.getxray.xray.testng.annotations.XrayTest;

import static java.lang.System.out;
import static java.lang.System.err;

/**
 * The listener interface for receiving events related to execution of tests, and process Xray related annotations.
 * The listener can be automatically invoked when TestNG tests are run by using ServiceLoader mechanism.
 * You can also add this listener to a TestNG Test class by adding
 * <code>@Listeners({app.getxray.testng.XrayListener.class})</code>
 * before the test class
 *
 * @see XrayTest
 * @see Requirement
 */
public class XrayListener implements IInvokedMethodListener, ITestListener  {
    
    boolean testSuccess = true;
    
    
    /* (non-Javadoc)
     * @see org.testng.IInvokedMethodListener#beforeInvocation(org.testng.IInvokedMethod, org.testng.ITestResult)
     */
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if(method.isTestMethod() && annotationPresent(method, XrayTest.class) ) {
            testResult.setAttribute("summary", method.getTestMethod().getConstructorOrMethod().getMethod().getAnnotation(XrayTest.class).summary());
            testResult.setAttribute("description", method.getTestMethod().getConstructorOrMethod().getMethod().getAnnotation(XrayTest.class).description()); 
            testResult.setAttribute("test", method.getTestMethod().getConstructorOrMethod().getMethod().getAnnotation(XrayTest.class).key());
            testResult.setAttribute("labels", method.getTestMethod().getConstructorOrMethod().getMethod().getAnnotation(XrayTest.class).labels());
        }

        if(method.isTestMethod() && annotationPresent(method, Requirement.class) ) {
            testResult.setAttribute("requirement", method.getTestMethod().getConstructorOrMethod().getMethod().getAnnotation(Requirement.class).key());  
        }
    }

    
    private boolean annotationPresent(IInvokedMethod method, Class clazz) {
        boolean retVal = method.getTestMethod().getConstructorOrMethod().getMethod().isAnnotationPresent(clazz) ? true : false;
        return retVal;
    }

    
    /* (non-Javadoc)
     * @see org.testng.IInvokedMethodListener#afterInvocation(org.testng.IInvokedMethod, org.testng.ITestResult)
     */
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if(method.isTestMethod()) {
            if( !testSuccess ) {
                testResult.setStatus(ITestResult.FAILURE);
            }
        }
    }

    public void onTestStart(ITestResult result) {
        
    }

    public void onTestSuccess(ITestResult result) {
        
    }

    public void onTestFailure(ITestResult result) {
        
    }

    public void onTestSkipped(ITestResult result) {
        
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        
    }

    public void onStart(ITestContext context) {
        
    }

    public void onFinish(ITestContext context) {

    }
    

}
