package com.jaemzware.board;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.jaemzware.seleniumcodebase.CodeBase;
import com.jaemzware.seleniumcodebase.EnvironmentType;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidParameterException;
import org.openqa.selenium.JavascriptExecutor;

/**
 * @author jaemzware@hotmail.com
 */
public class BoardScrub extends CodeBase {
    static final String propertiesFile = "src/test/java/com/jaemzware/board/selenium.properties";
    static Properties properties = new Properties();
    
    static final int timeout=10000;

    @Before
    public void BeforeTest() {
        try {// start the webdriver

            // properties file is in same directory as pom.xml
            properties.load(new FileInputStream(propertiesFile));

            // initialize verifification errors
            verificationErrors = new StringBuilder();
            
            // get input parameters HERE
            String getParameterResult = GetParameters();
            if(!getParameterResult.isEmpty()){
                System.out.println(getParameterResult);
                throw new InvalidParameterException();
            }

            //start driver
            StartDriver();
            
            //set implicit wait
            driver.manage().timeouts().implicitlyWait(defaultImplicitWait, TimeUnit.SECONDS);
        } 
        catch (InvalidParameterException ipex) {
            Assert.fail("INVALID PARAMETERS FOUND");
        }
        catch (FileNotFoundException fnfex){
            Assert.fail(propertiesFile+" NOT FOUND");
        }
        catch(IOException ioex){
            Assert.fail(propertiesFile+" IO EXCEPTION");
        }
        catch(Exception ex){
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void BuildPageOfFoundLinks() {
        try {

            // set implicit wait for this test
            driver.manage().timeouts().implicitlyWait(defaultImplicitWait, TimeUnit.SECONDS);

            // get base url
            String url;
            if (input != null) {
                url = input;
            } else {
                throw new Exception("URL NOT SPECIFIED (-Dinput)");
            }

            // MAKE SURE IT'S BEEN SPECIFIED
            if (url == null) {
                throw new Exception("URL NOT SPECIFIED NOR FOUND IN PROPERTIES FILE");
            }

            // get xpaths to search for

            // indicator that page of links has completely loaded
            final String linksLoadedIndicatorXpath = properties.getProperty(environment.toString()
                    + ".linksLoadedIndicatorXpath");

            // xpath of each link on page of links
            final String linkXpath = properties.getProperty(environment.toString() + ".linkXpath");

            // xpath of images to gather after following each link
            final String imageXpath = properties.getProperty(environment.toString() + ".imageXpath");

            // xpath of text to gather after following each link
            final String titleTextXpath = properties.getProperty(environment.toString() + ".titleTextXpath"); // NOT
                                                                                                              // REQUIRED
            final String bodyTextXpath = properties.getProperty(environment.toString() + ".bodyTextXpath"); // NOT
                                                                                                            // REQUIRED

            // CHECK FOR REQUIRED PARAMETERS
            if (linksLoadedIndicatorXpath == null) {
                throw new Exception("MISSING:" + environment.toString() + ".linksLoadedIndicatorXpath");
            }

            if (linkXpath == null) {
                throw new Exception("MISSING:" + environment.toString() + ".linkXpath");
            }

            if (imageXpath == null) {
                throw new Exception("MISSING:" + environment.toString() + ".imageXpath");
            }

            // NAVIGATE TO URL
            driverGetWithTime(url);

            // wait for login to complete
            (new WebDriverWait(driver, defaultImplicitWait)).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver d) {
                    return IsElementPresent(By.xpath(linksLoadedIndicatorXpath), timeout);
                }
            });

            // list for links
            List<String> urls = new ArrayList<String>();

            // make sure there are some links
            System.out.println("CHECKING FOR RESULTS");

            if (!IsElementPresent(By.xpath(linkXpath), timeout)) {
                throw new Exception("COULDN'T FIND ANY RESULTS");
            }

            // GET THE links
            System.out.println("FINDING RESULTS");

            List<WebElement> webElements = driver.findElements(By.xpath(linkXpath));

            // store off the hrefs
            System.out.println("SAVING RESULT LINKS");

            for (WebElement we : webElements) {
                urls.add(we.getAttribute("href"));
            }

            System.out.println("VISITING RESULT LINKS");
            List<String[]> results = new ArrayList<String[]>();

            // navigate to links and get images
            int maxVisits = (aNumber!=null)?Integer.parseInt(aNumber):0; //check if the max number was specified
            int visitCount = 0;
            for (String href : urls) {
                final String hrefToWaitFor = href;

                driverGetWithTime(href);

                // wait for navigate to href to complete
                (new WebDriverWait(driver, defaultImplicitWait)).until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(WebDriver d) {
                        // this waits for an element on the page. good for pages with a lot of elements to load, that
                        // could change the dom
                        return driver.getCurrentUrl().contains(hrefToWaitFor);

                    }
                });
                
                // check for the title text
                String titleText = "";
                if(titleTextXpath!=null){
                    if (IsElementPresent(By.xpath(titleTextXpath), timeout)) {
                        titleText = driver.findElement(By.xpath(titleTextXpath)).getText();
                        if (titleText == null) {
                            titleText = "WARNING: TITLETEXT AT XPATH:"+titleTextXpath+" GETTEXT IS NULL";
                        }
                        else if (titleText.isEmpty()){
                            titleText = "WARNING: TITLETEXT AT XPATH:"+titleTextXpath+" GETTEXT IS EMPTY";
                        }
                    }
                    else{
                        titleText = "WARNING: TITLETEXT AT XPATH:"+titleTextXpath+" WAS NOT FOUND";
                    }
                }
                else{
                    titleText = "WARNING: TITLETEXT NOT SPECIFIED";
                }
                

                // check for the body text
                String bodyText;
                if(bodyTextXpath!=null){
                    if (IsElementPresent(By.xpath(bodyTextXpath), timeout)) {
                        bodyText = driver.findElement(By.xpath(bodyTextXpath)).getText();
                        if (bodyText == null) {
                            bodyText = "WARNING: BODYTEXT AT XPATH:"+bodyTextXpath+" GETTEXT IS NULL";
                        }
                        else if(bodyText.isEmpty()){
                            bodyText = "WARNING: BODYTEXT AT XPATH:"+bodyTextXpath+" GETTEXT IS EMPTY";
                        }
                    }
                    else{
                        bodyText = "WARNING: BODYTEXT AT XPATH:"+bodyTextXpath+" WAS NOT FOUND";
                    }
                }
                else{
                    bodyText = "WARNING: BODYTEXT NOT SPECIFIED";
                }

                //if this is craigslist, get the contact info
                if(environment.equals(EnvironmentType.craigslist)){
                    
                    //find the contact button
                    String contactButtonXpath = properties.getProperty(environment.toString() + ".contactButtonXpath");
                    final String contactInfoXpath =  properties.getProperty(environment.toString() + ".contactInfoXpath");
                    if(IsElementPresent(By.xpath(contactButtonXpath))){
                        
                        //CLICK CONTACT BUTTON
                        ((JavascriptExecutor)driver).executeScript("$('section > button').click();");
                        
                        //wait for the contact form to appear
                        try
                        {
                            (new WebDriverWait(driver,10))
                            .until(new ExpectedCondition<Boolean>(){
                            @Override
                            public Boolean apply(WebDriver d) {
                                return IsElementPresent(By.xpath(contactInfoXpath));
                            }});
                        }
                        catch(Exception ex)
                        {
                            System.out.println("WARNING: TIMED OUT WAITING FOR CONTACT INFO AT :"+contactInfoXpath);
                        }
                        
                        //get the contact information
                        StringBuilder contactInfoString = new StringBuilder();
                        contactInfoString.append("<br />CONTACT:");
                        
                        String contactInfoStringPart;
                        List<WebElement> contactInfoUls = driver.findElements(By.xpath(contactInfoXpath));
                        for(WebElement we: contactInfoUls){
                            contactInfoStringPart = we.getText();
                            System.out.println(contactInfoStringPart);
                            contactInfoString.append("<br />").append(contactInfoStringPart);
                        }
                        
                        //append contact info to body
                        bodyText += contactInfoString;
                    }
                    else{
                        System.out.println("WARNING: COULD NOT FIND CRAIGSLIST CONTACT BUTTON AT:"+contactButtonXpath);
                    }
                }
                
                // check for images
                String imageSrc = "";
                if (IsElementPresent(By.xpath(imageXpath), timeout)) {
                    // add images to images list
                    List<WebElement> imageElements = driver.findElements(By.xpath(imageXpath));
                    for (WebElement i : imageElements) {
                        try {
                            imageSrc=i.getAttribute("src");
                            
                            //add result entry image
                            results.add(new String[] { href, imageSrc, titleText, bodyText });
                        } 
                        catch (Exception ex) {
                            System.out.println("WARNING: IMAGE WENT STALE");
                        }
                    }
                }
                else{
                    System.out.println("WARNING: IMAGE AT XPATH:"+imageXpath+" WAS NOT FOUND");
                }
                
                

                //add at least one result entry if no images were found
                if(imageSrc.isEmpty()){
                    results.add(new String[] { href, imageSrc, titleText, bodyText });
                }                
                
                // get a copy of the page
                RestRequest(href);
                
                //check the desired image count, and break if it's been reached
                if((maxVisits>0) && (++visitCount>maxVisits)){
                    break;
                }

            }

            WriteResultsToWebPage(results);

        } catch (Exception ex) {
            ScreenShot();
            System.out.println("SCRATCH EXCEPTION MESSAGE:"+ex.getMessage());
            CustomStackTrace("SCRATCH EXCEPTION TRACE", ex);
            Assert.fail(ex.getMessage());
        }
    }
    
    /**
     * Write results from a BoardScrub to a web page
     * @param results
     * @throws Exception 
     */
    private void WriteResultsToWebPage(List<String[]> results) throws Exception
    {
        // build web page
        String fileName = "Index-BoardScrub-" + getDateStamp() + ".htm";
        PrintWriter writer = new PrintWriter(fileName, "UTF-8");
        writer.println(HtmlReportHeader("BoardScrub"));

        String oldHref = new String();
        for (String[] entry : results) {
            if (!oldHref.equals(entry[0])) {
                oldHref = entry[0];
                writer.println("<a href='" + oldHref + "' target='_blank'>" + oldHref + "</a><br />");
                writer.println("<span>" + entry[2] + "</span><br />");
                writer.println("<span>" + entry[3] + "</span><br />");
            }
            writer.println("<center><img src='" + entry[1] + "' /></center><br />");
        }
        writer.println(HtmlReportFooter());

        writer.flush();
        writer.close();

        System.out.println("INDEX FILE WRITTEN:" + jenkinsReportPath + fileName);
    }

    @After
    public void AfterTest() {
        try {
            QuitDriver();
        } catch (Exception ex) {
            ScreenShot();
            this.CustomStackTrace("After test exception", ex);
            Assert.fail(ex.getMessage());
        }
    }

}
