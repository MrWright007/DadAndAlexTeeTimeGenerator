package com.alextest;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SeleniumApp {
    public static void main(String[] args) throws InterruptedException, ParseException {


        //This script will always run on Sunday morning at 12:01am

        final String dayOfWeekToPlay = "Tuesday";
        final String timeOfDay = "Morning";
        final String preferredTime = "7:30:00";
        final String username = "alexwright923@gmail.com";
        final String password = "Hazard30";

        //Setting system properties of ChromeDriver
        System.setProperty("webdriver.chrome.driver", "/Users/alexwright/bin/chromedriver/chromedriver");



        //Creating an object of ChromeDriver
        WebDriver driver = new ChromeDriver();


        try {

            driver.manage().window().maximize();

            //Specifying pageLoadTimeout and Implicit wait
            driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

            //launching the specified URL
            driver.get("https://foreupsoftware.com/index.php/booking/20022/3242#/teetimes");

            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"schedule_select\"]")));

            //Drop-Down Menu for Course
            Select dropdown = new Select(driver.findElement(By.xpath("//*[@id=\"schedule_select\"]")));
            dropdown.selectByVisibleText("Blue");

            // Click Select
            WebElement publicButton = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div/button[1]"));
            publicButton.click();
            Thread.sleep(1000);

            //Sometimes webpage freezes up, Try a double-click.
            try {
                publicButton.click();
            } catch (Exception e) {
                System.out.println("No need for double-click");
            }

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            LocalDateTime now = LocalDateTime.now();
            String todaysDate = dtf.format(now) + "";
            System.out.println("Today's Date is " + dtf.format(now));

            //Add the days to today's date to get to Friday (FOR TESTING WE WILL DO 7 DAYS SO WE CAN GET A LIST
            LocalDateTime oneWeekFromToday = now.plusDays(7);
            String teeTimeWantedDate = dtf.format(oneWeekFromToday) + "";
            System.out.println("Desired Date is " + teeTimeWantedDate);

            //Create the desired Tee Time Variable as String


            String desiredDateTimeString = teeTimeWantedDate + " " + preferredTime;
            System.out.println("String for preferred Date time is: " + desiredDateTimeString);


            //Create Variable of desired Tee Time as LocalDateTime variable
            Date desiredTime = new SimpleDateFormat("HH:mm:ss").parse(preferredTime);
            int hour = desiredTime.getHours();
            int minutes = desiredTime.getMinutes();
            int seconds = desiredTime.getSeconds();
            LocalDate ld = oneWeekFromToday.toLocalDate();
            LocalTime lt = LocalTime.of(hour, minutes, seconds);  // 12:34:56
            LocalDateTime desiredDateTime = LocalDateTime.of(ld, lt);
            System.out.println("Preferred Date time is " + desiredDateTime);

            //Enter the desired Date into the field

            WebElement dateField = driver.findElement(By.id("date-field"));
            dateField.click();
            //TODO REMEMBER THAT THIS IS MAC SPECIFIC
            String ctrla = Keys.chord(Keys.COMMAND, "a");
            dateField.sendKeys(ctrla);
            dateField.sendKeys(Keys.BACK_SPACE);
            Thread.sleep(500);
            dateField.sendKeys(teeTimeWantedDate);
            dateField.sendKeys(Keys.RETURN);
            Thread.sleep(4000);

            //Get the List of resulting times and select the first one
            WebElement times = driver.findElement(By.id("times"));
            List<WebElement> timeList = times.findElements(By.className("start"));
            Map<WebElement, Double> map = new HashMap<WebElement, Double>();

            //Test Treemap
            WebElement teeTimeSelection = null;
            String teeTimeSelectionString = null;
            long selectedDiffTime = 365000;


            System.out.println("Beginning the loop process...");
            for (int i = 0; i < timeList.size(); i++) {
                //TODO add each key and element into a Map
                System.out.println("LOOP COUNT: " + i + 1);
                WebElement thisElement = timeList.get(i);
                String teeTimeString = thisElement.getText().trim();
                System.out.println(teeTimeString);
                String time = teeTimeString.replace("pm", "PM").replace("am", "AM"); // 10:00PM
                LocalTime tt = LocalTime.parse(time,
                        DateTimeFormatter.ofPattern("h:mma", Locale.US));
                LocalDateTime teeTime = LocalDateTime.of(ld, tt);
                //compare to desiredDateTime
                System.out.println("Desired Date Time is: " + desiredDateTime + " and Tee Time is: " + teeTime);


                //TODO Figure out this calculation
                long diff = Math.abs(ChronoUnit.MINUTES.between(desiredDateTime, teeTime));

                System.out.println("Tee Time: " + teeTimeString + " and Difference in Minutes is: " + diff + " minutes");

                //TODO IF Statement - If Selected Diff Time is null OR isGreaterThan CurrentDiffTime then update TeeTimeSelection AND Selected Diff Time

                if (diff <= selectedDiffTime) {
                    teeTimeSelection = thisElement;
                    selectedDiffTime = diff;
                    teeTimeSelectionString = teeTimeString;
                    System.out.println("NEW BEST TEE TIME FOUND: " + teeTimeSelectionString);

                }
            }

            System.out.println("Loop finalized. Best Tee Time selection is: " + teeTimeSelectionString);


            //Click Selected Tee Time
            teeTimeSelection.click();

            try {
                teeTimeSelection.click();
            } catch (Exception e) {
                System.out.println("Second Click not needed");
            }
            try {
                teeTimeSelection.click();
            } catch (Exception e) {
                System.out.println("Third Click not needed");
            }
            try {
                teeTimeSelection.click();
            } catch (Exception e) {
                System.out.println("Fourth Click not needed");
            }

            //Find and Assign the "2" PLayer Button Option
            //TODO This could be modified to find 2 3 or 4 players if we want to
            WebElement modalElements = driver.findElement(By.id("modal"));
            List<WebElement> modalLinks = modalElements.findElements(By.tagName("a"));
            WebElement playerSelection = null;
            for (WebElement w : modalLinks) {
                System.out.println(w + w.getText() + "");
                String wText = w.getText() + "";
                if (wText.equals("2")) {
                    playerSelection = w;
                }
            }


            //Press 2 Button
            playerSelection.click();

            /*
            JavascriptExecutor jse = (JavascriptExecutor)driver;
            jse.executeScript("arguments[0].click()", buttonBox);
            jse.executeScript("arguments[0].click()", buttonBox);
            jse.executeScript("arguments[0].click()", buttonBox);
             */

            //Press Book Time

            WebElement modalFooter = driver.findElement(By.className("modal-footer"));
            List<WebElement> footerButtons = driver.findElements(By.tagName("button"));
            WebElement bookTimeButton = null;
            for (WebElement w : footerButtons) {
                String wText = w.getText() + "";
                if (wText.equals("Book Time")) {
                    bookTimeButton = w;
                }
            }

            bookTimeButton.click();


            //TODO ONLY FOR PUBLIC BOOKING Enter Username and Password then hit enter OR Log in

            //Enter Username

            WebElement emailEntry = driver.findElement(By.id("login_email"));
            emailEntry.click();
            emailEntry.sendKeys(username);
            //Enter Password
            WebElement passwordEntry = driver.findElement(By.id("login_password"));
            passwordEntry.click();
            passwordEntry.sendKeys(password);
            passwordEntry.sendKeys(Keys.RETURN);

            // WAIT
            Thread.sleep(3000);

            //Click GUEST button for Player 2 (This could be updated to select a Player 2 if they are already a Member also


            List<WebElement> modalButtons = modalElements.findElements(By.tagName("button"));
            WebElement guestButtonSelection = null;
            WebElement continueButtonSelection = null;
            for (WebElement w : modalButtons) {
                System.out.println(w + w.getText() + "");
                String wText = w.getText() + "";
                if (wText.equals("Guest")) {
                    guestButtonSelection = w;
                }
                if (wText.equals("Continue")) {
                    continueButtonSelection = w;
                }

            }

            guestButtonSelection.click();
            guestButtonSelection.click();
            Thread.sleep(1000);
            continueButtonSelection.click();
            Thread.sleep(5000);

            System.out.println("Guest Process Complete");

            //TODO IF you need to select a credit card, this would be the time to do it

            WebElement modalElements2 = driver.findElement(By.id("select-credit-card"));
            List<WebElement> modalButtons2 = modalElements2.findElements(By.tagName("button"));
            WebElement finalBookTimeButton = null;
            for (WebElement w : modalButtons2) {
                System.out.println(w + w.getText() + "");
                String wText = w.getText() + "";
                if (wText.equals("Book Time")) {
                    finalBookTimeButton = w;
                }
            }
            finalBookTimeButton.click();


            Thread.sleep(10000);



            // quit driver
            driver.quit();
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e);
            driver.quit();
        }
    }
}



