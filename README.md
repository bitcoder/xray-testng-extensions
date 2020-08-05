# xray-testng-extensions

This repo contains several extensions, implemented as listeners, that allow you to take better advantage of [TestNG](https://testng.org/) whenever using it together with [Xray Test Management](https://getxray.app).
This code is provided as-is; you're free to use it and modify it at your will.

## Installing

Configure the maven repository in your pom.xml:

```
        <repository>
                <id>xpandit</id>
                <name>xpand-releases</name>
                <url>http://maven.xpand-it.com/artifactory/releases</url>
                <releases>
                        <enabled>true</enabled>
                </releases>
        </repository>
```        

Add the following dependency to your pom.xml:

```
        <dependency>
          <groupId>com.xpandit.xray</groupId>
          <artifactId>xray-testng-extensions</artifactId>
          <version>1.1.0</version>
          <scope>test</scope>
        </dependency>
```

## New annotations

You may use the 
Xray(...) annotation, before your test methods in order to identify:

- covered requirement (by issue key), using the **requirement** attribute
- enforce mapping of result to specific Test (by issue key), using the **test** attribute
- labels to add do Test entity in Xray, using the **labels** attribute; labels must be delimited by space

_Examples:_

1. create a "tests" link to requirement/user story identified by CALC-1234;  

```java
    @Test
    @Xray(requirement = "CALC-1234")
    public void CanAddNumbers()
```

2. creating a "tests" link to requirement/user story identified by CALC-1234; associate the results with Test identified by key CALC-2; add labels "core" and "addition".



```java
    @Test
    @Xray(requirement = "CALC-1234", test = "CALC-2", labels = "core addition")
    public void CanAddNumbers()
```




## Embed attachments in TestNG's XML report (TestNG 7.1 onwards)

*Note: please switch to testng_v71 branch first; this feature requires TestNG >= 7.1 and a compatible version of Xray*!


In order to add attachments to the result, you need to add File objects to an array that you need to set as an attribute named "attachments" on the ITestResult.


    @Test
    public void CanAddNumbers()
    {
        ITestResult result = Reporter.getCurrentTestResult();
        File attachments[] = new File[] { new File("1.png"), new File("2.png") } ;
        result.setAttribute("attachments", attachments); 
        
        Assert.assertEquals(Calculator.Add(1, 1),2);
        Assert.assertEquals(Calculator.Add(-1, 1),0);
    }

## References
- [Example using TestNG with Xray on Jira server/DC](https://docs.getxray.app/display/XRAY/Testing+using+TestNG+in+Java)
- [Example using TestNG with Xray on Jira Cloud](https://docs.getxray.app/display/XRAYCLOUD/Testing+using+TestNG+in+Java)
- [original discussion on customization of TestNG XML report](https://github.com/cbeust/testng/issues/2171)



## Contact

You may find me on [Twitter](https://twitter.com/darktelecom).
Any questions related with this code, please raise here issues. Feel free to contribute and submit PR's.
For Xray specific questions, please contact [Xray's support team](https://jira.xpand-it.com/servicedesk/customer/portal/2).

## LICENSE

[MIT](LICENSE)
