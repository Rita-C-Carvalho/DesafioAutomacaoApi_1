package br.com.teste.accenture.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber-reports"},
        features = "src/test/resources/features",
        glue = {"br.com.teste.accenture.tests.demoqa.steps",
                "br.com.teste.accenture.steps",
                "br.com.teste.accenture.hooks"
        },
        //tags = "@DemoQA_User_Management_01"
        //tags = "@DemoQA_Token_Management_01"
        //tags = "@DemoQA_Authorization_01"
        //tags = "@DemoQA_Books_Management_01"
        //tags = "@DemoQA_Book_Rental_Multiple"
        //tags = " @DemoQA_User_Details_01"
        tags = "@DemoQA_Complete_Flow_01"
)
public class Runner {
}