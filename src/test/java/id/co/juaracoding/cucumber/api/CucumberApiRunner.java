package id.co.juaracoding.cucumber.api;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features/api",
        glue = "id.co.juaracoding.cucumber.api",
        plugin = {
                "pretty",
                "html:target/cucumber-report/api-report.html",
                "json:target/cucumber-report/api-report.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true
)
public class CucumberApiRunner extends AbstractTestNGCucumberTests {
}
