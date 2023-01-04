package org.example.stepDefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import managers.FileReaderManager;
import org.testng.asserts.SoftAssert;
import pages.P01_register;
import pages.P02_home;
import testDataTypes.Customer;

import java.text.SimpleDateFormat;

import static org.example.stepDefs.Hooks.driver;

public class D01_registerStepDef {
    private final P01_register register = new P01_register(driver);
    private final P02_home home = new P02_home(driver);

    public D01_registerStepDef() {
    }

    @Given("user go to register page")
    public void user_go_to_register_page() {
        home.clickRegisterLink();
    }

    /////////////
    @When("he enters {string} personal details on register page")
    public void he_enters_personal_details_on_register_page(String customerName) {
        Customer customer = FileReaderManager.getInstance().getJsonReader().getCustomerByName(customerName);
        register.fill_PersonalDetails(customer);
    }
    /////////////

    @When("user select gender type")
    public void userSelectGenderType() {
        register.clickGenderMale();
    }

    @And("user enter first name {string} and last name {string}")
    public void userEnterFirstNameAndLastName(String arg0, String arg1) {
        register.enterFirstName(arg0);
        register.enterLastName(arg1);

    }

    @And("user enter date of birth")
    public void userEnterDateOfBirth() {
        // Day
        register.selectDayOfBerth(12);
        // Month
        register.selectMonthOfBerth("January");
        // Year
        register.selectYearOfBerth("1995");
    }

    @And("user enter email field")
    public void userEnterEmailField() {
        String email = new SimpleDateFormat("MMddHHmmss").format(new java.util.Date()) + "@test.com";
        register.interEmail(email);
    }

    @And("user fills Password fields {string} {string}")
    public void userFillsPasswordFields(String arg0, String arg1) {
        register.interPassword(arg0);
        register.interConfirmPassword(arg1);
    }

    @And("user clicks on register button")
    public void userClicksOnRegisterButton() {
        register.clickRegisterButton();
    }

    @Then("success message is displayed")
    public void successMessageIsDisplayed() {
        String expected = "Your registration completed";
        String actual = register.getSuccessMassage();
        SoftAssert soft = new SoftAssert();
//        first assertion
        soft.assertEquals(actual.contains(expected), true);
//        second assertion
        soft.assertEquals(driver.getCurrentUrl(),
                "https://demo.nopcommerce.com/registerresult/1?returnUrl=/");
        soft.assertAll();
    }

}
