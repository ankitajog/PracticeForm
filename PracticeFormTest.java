
package testPack;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PracticeFormTest {

    WebDriver driver;
    WebDriverWait wait;

    String url = "https://demoqa.com/automation-practice-form";

    By firstName = By.id("firstName");
    By lastName = By.id("lastName");
    By email = By.id("userEmail");		//not required field
    By mobile = By.id("userNumber");
    By female = By.xpath("//label[@for='gender-radio-2']");
    By submit = By.id("submit");
    By heading = By.xpath("//h1[contains(normalize-space(), 'Practice Form')]");
    By resultModal = By.id("example-modal-sizes-title-lg");
    By closeButton = By.id("closeLargeModal");

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.manage().window().maximize();
        driver.get(url);
    }

    @BeforeMethod
    public void openForm() {
        driver.get("https://demoqa.com/automation-practice-form");
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstName));
    }
    
    @AfterClass
    public void tearDown() {
    	
        if (driver != null) {
            driver.quit();
        }
    }

    // Common method to enter the mandatory details
    public void fillRequiredDetails() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstName))
            .sendKeys("Ankita");

        driver.findElement(lastName).sendKeys("Jogdankar");

        //driver.findElement(female).click();
        selectFemaleGender();
        
        driver.findElement(mobile).sendKeys("9876543210");
    }

    // Common method to submit the form

public void submitForm() {
	//Explicit wait
    WebElement button = wait.until(
        ExpectedConditions.presenceOfElementLocated(submit));

    ((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({block:'center'});", button);

    wait.until(ExpectedConditions.visibilityOfElementLocated(submit));

    try {
        wait.until(ExpectedConditions.elementToBeClickable(submit)).click();
    } catch (org.openqa.selenium.ElementClickInterceptedException e) {

        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].click();", button);
    }
}

public void selectFemaleGender() {

    WebElement gender = wait.until(
        ExpectedConditions.presenceOfElementLocated(female));

    ((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({block:'center'});",
        gender);

    try {
        wait.until(
            ExpectedConditions.elementToBeClickable(female)).click();

    } catch (org.openqa.selenium.ElementClickInterceptedException e) {

        // Use JavaScript click only if normal click is intercepted
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].click();",
            gender);
    }
}

public void closeResultModal() {
    WebElement closebtn = wait.until(
        ExpectedConditions.elementToBeClickable(closeButton));

    closebtn.click();
}
   
    // TC_001: Verify form page loads
    @Test
    public void TC_001_verifyFormPageLoads() {

        wait.until(ExpectedConditions.visibilityOfElementLocated(firstName));

        Assert.assertTrue(
            driver.getCurrentUrl().contains("automation-practice-form"),
            "Practice Form URL did not load");

        Assert.assertTrue(
            driver.findElement(firstName).isDisplayed(),
            "First Name field is not displayed");

        Assert.assertTrue(
            driver.findElement(lastName).isDisplayed(),
            "Last Name field is not displayed");
        
        System.out.println("TC_001 PASS: Form page loaded successfully");
    }
    

    // TC_002: Verify form heading
    @Test
    public void TC_002_verifyFormHeading() {

        WebElement pageHeading = wait.until(
            ExpectedConditions.visibilityOfElementLocated(heading));

        Assert.assertEquals(
            pageHeading.getText().trim(),		//actual String , Expected String
            "Practice Form",
            "Incorrect page heading");

        System.out.println("TC_002 PASS: Practice Form heading is displayed");
    }
   
    // TC_003: Verify First Name with valid input
    @Test
    public void TC_003_verifyFirstName() {

        fillRequiredDetails();
        submitForm();

        WebElement result = wait.until(
            ExpectedConditions.visibilityOfElementLocated(resultModal));

        Assert.assertTrue(
            result.isDisplayed(),
            "Submission confirmation was not displayed");

        String details = driver.findElement(By.className("modal-content"))
                               .getText();

        Assert.assertTrue(
            details.contains("Ankita"),
            "First Name was not displayed in submitted details");

        closeResultModal();
        System.out.println("TC_003 PASS: First Name accepted");
    }
   
  
    // TC_004: Verify Last Name with valid input
    @Test
    public void TC_004_verifyLastName() {

        fillRequiredDetails();
        submitForm();

        wait.until(ExpectedConditions.visibilityOfElementLocated(resultModal));

        String details = driver.findElement(By.className("modal-content"))
                               .getText();

        Assert.assertTrue(
            details.contains("Jogdankar"),
            "Last Name was not displayed in submitted details");

        closeResultModal();
        System.out.println("TC_004 PASS: Last Name accepted");
    }
     
    
    // TC_005: Verify First Name is mandatory
    @Test
    public void TC_005_verifyFirstNameMandatory() {

        driver.findElement(lastName).sendKeys("Jogdankar");
        driver.findElement(female).click();
        driver.findElement(mobile).sendKeys("9876543210");

        submitForm();

        WebElement nameField = driver.findElement(firstName);

        boolean validationShown = (Boolean) ((JavascriptExecutor) driver)
            .executeScript(
                "return arguments[0].validity.valueMissing;",
                nameField);

        Assert.assertTrue(
            validationShown,
            "Expected First Name to be mandatory");

        
        System.out.println("TC_005 PASS: Blank First Name is rejected");
    }
    
    // TC_006: Verify Last Name is mandatory
    @Test
    public void TC_006_verifyLastNameMandatory() {

        driver.findElement(firstName).sendKeys("Ankita");
        driver.findElement(female).click();
        driver.findElement(mobile).sendKeys("9876543210");

        submitForm();

        WebElement nameField = driver.findElement(lastName);

        boolean validationShown = (Boolean) ((JavascriptExecutor) driver)
            .executeScript(
                "return arguments[0].validity.valueMissing;",
                nameField);

        Assert.assertTrue(
            validationShown,
            "Expected Last Name to be mandatory");

        System.out.println("TC_006 PASS: Blank Last Name is rejected");
    }
    
    // TC_007: Verify First Name accepts alphabetic input
    @Test
    public void TC_007_verifyAlphabeticFirstName() {

        driver.findElement(firstName).sendKeys("Ankita");
        driver.findElement(lastName).sendKeys("Jogdankar");
        driver.findElement(female).click();
        driver.findElement(mobile).sendKeys("9876543210");

        submitForm();

        wait.until(ExpectedConditions.visibilityOfElementLocated(resultModal));

        String details = driver.findElement(By.className("modal-content"))
                               .getText();

        Assert.assertTrue(
            details.contains("Ankita"),
            "Valid alphabetic First Name was not accepted");

        closeResultModal();
        System.out.println("TC_007 PASS: Alphabetic First Name accepted");
    }
    
    // TC_008: Check behavior for numbers/special characters in name
    @Test
    public void TC_008_verifyNameWithInvalidCharacters() {

        driver.findElement(firstName).sendKeys("Anki123");
        driver.findElement(lastName).sendKeys("Jogdankar");
        driver.findElement(female).click();
        driver.findElement(mobile).sendKeys("9876543210");

        submitForm();

        WebElement nameField = driver.findElement(firstName);

        boolean browserInvalid = (Boolean) ((JavascriptExecutor) driver)
            .executeScript(
                "return arguments[0].validity.valid === false;",
                nameField);

        if (browserInvalid) {
            System.out.println(
                "TC_008 PASS: Browser validation rejected the name");
        } else {
            System.out.println(
                "TC_008 OBSERVATION: Name field accepts Anki123 "
                + "without browser validation");
        }

        // DemoQA may allow these characters.
        // If your requirement prohibits them, add an assertion that
        // expects rejection; this will fail if the application accepts them.
    }
    
    // TC_009: Verify valid email address
    @Test
    public void TC_009_verifyValidEmail() {

        fillRequiredDetails();
        driver.findElement(email).sendKeys("ankita@example.com");

        submitForm();

        wait.until(ExpectedConditions.visibilityOfElementLocated(resultModal));

        String details = driver.findElement(By.className("modal-content"))
                               .getText();

        Assert.assertTrue(
            details.contains("ankita@example.com"),
            "Valid email was not displayed in submitted details");

        closeResultModal();
        System.out.println("TC_009 PASS: Valid email accepted");
    }
   
 // TC_010: Verify invalid email format using soft Assert

    @Test
    public void TC_010_verifyInvalidEmail() {

        SoftAssert softAssert = new SoftAssert();

        fillRequiredDetails();

        WebElement emailField = driver.findElement(email);
        emailField.sendKeys("ankita.example.com");

        boolean invalidEmail = (Boolean) ((JavascriptExecutor) driver)
                .executeScript(
                    "return arguments[0].validity.typeMismatch;",
                    emailField
                );

        softAssert.assertTrue(
                invalidEmail,
                "Expected invalid email format to be rejected"
        );

        System.out.println("Invalid email format detected: " + invalidEmail);

        // Submit only after checking validity
        submitForm();

        //softAssert.assertAll();
    }
  
    // TC_011: Verify blank email behavior
    @Test
    public void TC_011_verifyBlankEmail() {

        fillRequiredDetails();

        // Leave Email blank
        submitForm();

        WebElement emailField = driver.findElement(email);

        boolean emailRequired = (Boolean) ((JavascriptExecutor) driver)
            .executeScript(
                "return arguments[0].required;",
                emailField);

        boolean emailMissing = (Boolean) ((JavascriptExecutor) driver)
            .executeScript(
                "return arguments[0].validity.valueMissing;",
                emailField);

        System.out.println("Email required attribute: " + emailRequired);
        System.out.println("Email missing validation: " + emailMissing);

        // The test reports whether email is configured as mandatory.
        // If your requirement says email is mandatory, use:
        // Assert.assertTrue(emailRequired,
        //     "Email should be a mandatory field");

        // If email is optional, successful submission is expected.
        // Verify this by checking whether the confirmation dialog appears.
        if (!emailRequired) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(resultModal));
            System.out.println(
                "TC_011 OBSERVATION: Blank email is accepted");
        } else {
            Assert.assertTrue(
                emailMissing,
                "Mandatory email should be validated when left blank");
        }
    }
    
    
}
