# xray-testng-extensions

[![build workflow](https://github.com/Xray-App/xray-testng-extensions/actions/workflows/maven.yml/badge.svg)](https://github.com/Xray-App/xray-testng-extensions/actions/workflows/maven.yml)
![code coverage](
https://raw.githubusercontent.com/Xray-App/xray-testng-extensions/master/.github/badges/jacoco.svg)
[![license](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)
[![Gitter chat](https://badges.gitter.im/gitterHQ/gitter.png)](https://gitter.im/Xray-App/community)

This repo contains several enhancements to TestNG to allow you to take better advantage of [TestNG](https://testng.org/) whenever using it together with [Xray Test Management](https://getxray.app). This includes specific annotations and listener(s) that can then process their information.

This code is provided as-is; you're free to use it and modify it at your will (see license ahead).

This is a preliminary release so it is subject to changes, at any time.

## Overview

Results from automated scripts implemented as `@Test` methods can be tracked in test management tools to provide insights about quality aspects targeted by those scripts and their impacts.
Therefore, it's important to attach some relevant information during the execution of the tests, so it can be shared and analyzed later on in the test management tool (e.g. [Xray Test Management](https://getxray.app)).

The idea is to be able to produce a custom TestNG XML report containing additional information that Xray can take advantage of.
This way, testers can automate the test script and at the same time provide information such as the covered requirement, right from the test automation code. Additional information may be provided, usally through new annotations.

### Features

- link a test method to an existing Test issue or use auto-provisioning
- cover a "requirement" (i.e. an issue in Jira) from a test method
- specify additional fields for the auto-provisioned Test issues (e.g. summary, description, labels)
- attach screenshots or any other file as evidence to the Test Run, right from within the test method (PENDING Xray implementation)

### Main classes

The project consists of:

- **@XrayTest**, **@Requirement**: new, optional annotations to provide additional information whenever writing the automated test methods
- **XrayListener**: a listener for test events that can process the new annotations, and add them to the test results and attributes on the test results

Besides, there's also a work-in-progress reporter (you should not use this yet):

- **XrayReportListener**: an *optional* reporter, that extends the built-in XmlReporter and adds the capability of adding additional information (e.g., attachments) on the test results

## Installing

These extensions are available as an artifact available on (Maven) Central Repository, which is configured by default in your Maven instalation.

Add the following dependency to your pom.xml:

```xml
        <dependency>
          <groupId>app.getxray</groupId>
          <artifactId>xray-testng-extensions</artifactId>
          <version>0.1.0</version>
          <scope>test</scope>
        </dependency>
```

### Configuration

For the time being, there are no specific configurations to change the behavior of the provided listeners, as it is not needed.

However, to use this package you need to follow some steps as described ahead.
At high-level, you need to:

- enable the *XrayListener* listener
- configure TestNG's XMLReporter to include user-defined attributes on the report

### Enable the listener(s)

In order to generate the enhanced, customized TestNG XML report we need to register the **XrayListener** listener. This can be done in [several ways](https://testng.org/doc/documentation-main.html#testng-listeners):

- discovered automatically at runtime based by the ServiceLoader based on the contents of a file (e.g `src/test/resources/META-INF/services/org.testng.ITestNGListener`); this is probably the easiest one.

```bash
app.getxray.xray.testng.listeners.XrayListener
```

- on `the testng.xml` file

```xml
<suite>
 
  <listeners>
    <listener class-name="app.getxray.xray.testng.listeners.XrayListener" />
  </listeners>
 
...
```

- by annotating the test class

```java
@Listeners({ app.getxray.xray.testng.listeners.XrayListener.class })
public class MyTest {
  // ...
}
```

- programmaticaly
- from the command line

Registering the listener is mandatory.
In order to take advantage of the capabilities of this new listener, the new annotations can be used.

Note: there's another listener _XrayReportListener_ but you don't need to use it, as it is only required to further customize the TestNG XML report (e.g., for adding attachments) => this is not yet supported by Xray.
### Configure Maven to use TestNG XMLReporter and enable user-defined test attributes

For Maven, we can configure the surefire plugin and define a property `reporter` to enable the user-defined test attributes on the XML report, that will contain the values we provided using the annotations.

```xml
...
    <build>
        <plugins>
            <plugin>

                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version> 2.20.1</version>

                <configuration>
                    <testFailureIgnore>true</testFailureIgnore>

                    <suiteXmlFiles>
                      <suiteXmlFile>testng.xml</suiteXmlFile>
                    </suiteXmlFiles>

                    <properties>
                        <property>
                            <name>reporter</name>
                            <value>org.testng.reporters.XMLReporter:generateTestResultAttributes=true</value>
                        </property>
                    </properties>

                </configuration>
             </plugin>

        ...
        </plugins>
    </build>
```

### Configure Gradle to use TestNG XMLReporter and enable user-defined test attributes

For Gradle, we need to use a custom task, as the standard `test` task doesn't allow us to set attributes on the XMLReporter used by TestNG.

As an example, we could create a `testngTest` task that runs TestNG tests and enabled the intended behavior on the XMLReporter.

```gradle
task testngTest(type: JavaExec, dependsOn: [classes]) {
    group 'Verification'
    description 'Run TestNG tests'
    mainClass = 'org.testng.TestNG'
    args('testng.xml', '-reporter',
            'org.testng.reporters.XMLReporter:generateTestResultAttributes=true,generateGroupsAttribute=true'
    )
    classpath = sourceSets.test.runtimeClasspath
}
```

## How to use

### Using new annotations

Two new annotations (`@XrayTest`, `@Requirement`) can be used.
The annotations are optional and cannot be use more than once per test method.

#### @XrayTest

Test methods don't need to be annotated with `@XrayTest` unless you want to take advantage of the following enhancements.

You may use the **@XrayTest** annotation to:

- enforce mapping of result to specific, existing Test identified by issue key, using the **key** attribute
- enforce the summary on the corresponding Test issue, during auto-provisioning, using the **summary** attribute
- enforce the description on the corresponding Test issue, during auto-provisioning, using the **description** attribute

_Examples:_

1. enforce the given test and corresponding result to be reported against the existing Test issue having the key  CALC-1000 (i.e. create a Test Run in Xray for the Test with issue key CALC-1000)  

```java
    @Test
    @XrayTest(key = "CALC-1000")
    public void CanAddNumbers()
```

2. enforce the "Summary" and the "Description" fields on the corresponding Test issue. The test will be auto-provisioned in Xray, if it doesn't exist yet, based on the class name and method name implementing the test  

```java
    @Test
    @XrayTest(summary = "addition of two numbers", description = "tests the sum of two positive integers")
    public void CanAddNumbers()
```

3. set some label(s) on the corresponding Test issue; more than one label may specified, delimited by space. The test will be auto-provisioned in Xray, if it doesn't exist yet, based on the class name and method name implementing the test  

```java
    @Test
    @XrayTest(labels = "core addition")
    public void CanAddNumbers()
```

#### @Requirement

You may use the **Requirement** annotation, before your test methods in order to identify the covered requirement.

_Examples:_


1. use this test to cover a requirement/user story identified by the issue key CALC-1234 (i.e create a issue link "tests" between the Test issue and the identified requirement);  

```java
    @Test
    @Requirement("CALC-1234")
    public void CanAddNumbers()
```

### Using the test result within the test method

It's possible also to set attributes related to the test method, during the test execution lifecycle, through the ITestResult interface. This is more verbose than using the annotations approach though.


```java
import org.testng.Reporter;
...

public class CalcTest {

    @Test
    public void CanAddNumbers()
    {
        Assert.assertEquals(Calculator.Add(1, 1),2);
        Assert.assertEquals(Calculator.Add(-1, 1),0);

        ITestResult result = Reporter.getCurrentTestResult();
        result.setAttribute("requirement", "CALC-1234");   // Xray will try to create a link to this requirement issue
        result.setAttribute("test", "CALC-2");             // Xray will try to find this Test issue and report result against it
        result.setAttribute("labels", "core addition");    // Xray will add this(ese) label(s) to the associated Test issue
    }

}
```


## Embed attachments in TestNG's XML report (TestNG 7.1 onwards)

*Note: This feature is not yet supported by Xray. Please vote and track this [issue](https://jira.getxray.app/browse/XRAY-4557)*!


In order to add attachments to the result, first you need to use/enable the *XrayReportListener* listener, besides the *XrayListener*; you can do that, for example, by adding an entry/line to the `src/test/resources/META-INF/services/org.testng.ITestNGListener`

```bash
app.getxray.xray.testng.listeners.XrayListener
app.getxray.xray.testng.listeners.XrayReportListener
```

Then, in the test code, yoou can add File objects to an array that you need to set as an attribute named "attachments" on the ITestResult.

```java
    @Test
    public void CanAddNumbers()
    {
        ITestResult result = Reporter.getCurrentTestResult();
        File attachments[] = new File[] { new File("1.png"), new File("2.png") } ;
        result.setAttribute("attachments", attachments); 
        
        Assert.assertEquals(Calculator.Add(1, 1),2);
        Assert.assertEquals(Calculator.Add(-1, 1),0);
    }
```

The *XrayReportListener* will process the attribute `attachments` and will add them to the TestNG XML report, in a custom `<attachments>` XML section that Xray can process.

### 

```java
```

## Background

.

## TO DOs

- evaluate the merge of XrayListener and XrayReportListener, even though the later is not yet usable
- improve javadocs

## FAQ

1. I want to use the Xray annotations; do I need to use any listener at all?

Yes. You need to enable the *XrayListener* listener, so that it can process the information from the annotations and add them as attributes on the corresponding `<test-method>` element on the TestNG XML report.

2. I ran the tests but the TestNG XML report doesn't contain any information at all that I provided using the Xray related annotations.

First, make sure you enabled the *XrayListener* listener; second, you need to configure your build tool (e.g., Maven) so that TestNG's XMLReporter can include user-defined attributes at test level; for that, the `generateTestResultAttributes` should be set as true.

org.testng.reporters.XMLReporter:generateTestResultAttributes=true


## Contact

You may find me on [Twitter](https://twitter.com/darktelecom).
Any questions related with this code, please raise issues in this GitHub project. Feel free to contribute and submit PR's.
For Xray specific questions, please contact [Xray's support team](https://jira.getxray.app/servicedesk/customer/portal/2).
## References
- [Example using TestNG with Xray on Jira server/DC](https://docs.getxray.app/display/XRAY/Testing+using+TestNG+in+Java)
- [Example using TestNG with Xray on Jira Cloud](https://docs.getxray.app/display/XRAYCLOUD/Testing+using+TestNG+in+Java)
- [original discussion on customization of TestNG XML report](https://github.com/cbeust/testng/issues/2171)

## LICENSE

[MIT](LICENSE)
