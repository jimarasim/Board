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
    static final int sleepForGoogleResultsPage = 3000;
    
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
            StartDriver("../SeleniumCodeBase/SeleniumGrid/");
            
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

    /**
     * This method waits for the google search page to change, when paging through results
     * @param oldValue - old value of what should be at resultStatsTextXpath
     * @param urlWithParms - informational only just used to print out to console, what page is being loaded
     * @throws Exception 
     */
    private void WaitForGoogleResultsPageChange(String oldUrl, String urlWithParms) throws Exception{
            final String waitTillUrlIsNot=oldUrl; //string to wait for to change when the page is loaded
            
            // wait for links to be loaded by waiting for the resultStatsText to change
            (new WebDriverWait(driver, defaultImplicitWait)).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver d) {
                    return !driver.getCurrentUrl().equals(waitTillUrlIsNot);
                }
            });
            
            //hardcoded wait (i hate these) to avoid stale element references later.
            Thread.sleep(sleepForGoogleResultsPage);
     
    }
    
    @Test 
    public void ResultPlace(){
        
        int numResultsOnPage = 100;
        int numCurrentPageFirstResult=1;
        int maxVisits = (aNumber!=null||!aNumber.isEmpty())?Integer.parseInt(aNumber):0;
        
        try{
            // set implicit wait for this test
            driver.manage().timeouts().implicitlyWait(defaultImplicitWait, TimeUnit.SECONDS);

//GET REQUIRED COMMAND LINE PARMS
            // get base url
            String url;
            if (input != null) {
                url = input;
            } else {
                throw new Exception("URL NOT SPECIFIED (-Dinput)");
            }

            // MAKE SURE IT'S BEEN SPECIFIED
            if (url == null) {
                throw new Exception("URL SPECIFIED WAS NULL (-Dinput)");
            }
            
            // get target result link string to look for
            String targetUrl;
            if (aString != null) {
                targetUrl = aString;
            } else {
                throw new Exception("URL NOT SPECIFIED (-DaString)");
            }

            // MAKE SURE IT'S BEEN SPECIFIED
            if (targetUrl == null) {
                throw new Exception("TARGET NOT SPECIFIED NOR FOUND IN PROPERTIES FILE");
            }

//GET REQUIRED PROPERTIES FILE PARMS
            // indicator that page of links has completely loaded
            final String linksLoadedIndicatorXpath = properties.getProperty(environment.toString()+ ".linksLoadedIndicatorXpath");

            // xpath of each link on page of links
            final String linkXpath = properties.getProperty(environment.toString() + ".linkXpath");
            
            // xpath of next link
            final String nextLinkXpath = properties.getProperty(environment.toString() + ".nextLinkXpath");
            
            //num parameter name to use in url, for number of results to return (used for paging)
            final String numResultsParm = properties.getProperty(environment.toString() + ".numResultsParm");
            
            //start parameter name to use in url, for nth result to get results from (used for paging)
            final String startParm = properties.getProperty(environment.toString() + ".startParm");
            
            if (linksLoadedIndicatorXpath == null) {
                throw new Exception("MISSING:" + environment.toString() + ".linksLoadedIndicatorXpath");
            }

            if (linkXpath == null) {
                throw new Exception("MISSING:" + environment.toString() + ".linkXpath");
            }
            
            if (nextLinkXpath == null) {
                throw new Exception("MISSING:" + environment.toString() + ".nextLinkXpath");
            }

            if (numResultsParm == null) {
                throw new Exception("MISSING:" + environment.toString() + ".numResultsParm");
            }
            
            if (startParm == null) {
                throw new Exception("MISSING:" + environment.toString() + ".startParm");
            }
            
            List<Integer>resultPlacesOfTarget = new ArrayList<>(); 
            
            //ADD NUM AND START PARMS TO SEARCH STRING (GOOGLE SPECIFIC)
            String urlWithParms = url + 
                    "&"+
                    numResultsParm+
                    "="+
                    Integer.toString(numResultsOnPage);
            
            // NAVIGATE TO URL
            
            String oldUrl=driver.getCurrentUrl();
            driverGetWithTime(urlWithParms);
            WaitForGoogleResultsPageChange(oldUrl,urlWithParms);
            
 
                
            while(IsElementPresent(By.xpath(nextLinkXpath),quickWaitMilliSeconds)){
                
                System.out.println("IS THERE A NEXT LINK ON THIS PAGE:"+driver.getCurrentUrl()+" ?"+IsElementPresent(By.xpath(nextLinkXpath),5000));
                
// make sure there are some links
                System.out.println("CHECKING FOR RESULTS");

                if (!IsElementPresent(By.xpath(linkXpath), quickWaitMilliSeconds)) {
                    throw new Exception("COULDN'T FIND ANY RESULTS");
                }

// GET THE LINKS
                System.out.println("FINDING RESULTS");

                List<WebElement> webElements = driver.findElements(By.xpath(linkXpath));

                System.out.println("RESULT COUNT:"+webElements.size());

                // store off the hrefs
                System.out.println("REVIEWING LINKS");

//CHECK IF TARGET LINK IS IN THE LINKS
                String linkHref;
                String linkText;
                for(int i=0;i<webElements.size();i++){

                    linkHref=webElements.get(i).getAttribute("href");
                    linkText=webElements.get(i).getText();

                    if(linkHref.contains(targetUrl)){
                        //report the link position with the first result offset
                        resultPlacesOfTarget.add(numCurrentPageFirstResult + i);
                    }

                    System.out.println((i+numCurrentPageFirstResult)+":\t"+linkHref);
                    System.out.println((i+numCurrentPageFirstResult)+":\t"+linkText);
                }
                
//CHECK IF WE WANT TO KEEP GOING (DEPENDING ON HOW MANY PAGES TO VISIT)
                if(numCurrentPageFirstResult/numResultsOnPage >= maxVisits){
                    System.out.println("MAX VISITS REACHED numCurrentPageFirstResult:"+numCurrentPageFirstResult+" numResultsOnPage:"+numResultsOnPage+" maxVisits:"+maxVisits);
                    break;
                }
                else{
                    //SET FIRST RESULT TO BE ON THE NEXT PAGE OF RESULTS
                    numCurrentPageFirstResult += numResultsOnPage+1;
                    
                    System.out.println("numCurrentPageFirstResult:"+numCurrentPageFirstResult+" numResultsOnPage:"+numResultsOnPage+" maxVisits:"+maxVisits);
                    
                    //CONSTRUCT THE NEW URL
                    urlWithParms = url + 
                    "&"+
                    numResultsParm+
                    "="+
                    Integer.toString(numResultsOnPage)+
                    "&"+
                    startParm+
                    "="+
                    Integer.toString(numCurrentPageFirstResult);
                    
                    //GO TO THE NEXT PAGE
                    //get results text string, so we can tell when its changed in WaitForGoogleResultsPageChange
                    oldUrl=driver.getCurrentUrl();
                    
                    // NAVIGATE TO URL
                    driverGetWithTime(urlWithParms);

                    //WAIT FOR NEW RESULTS PAGE TO LOAD
                    WaitForGoogleResultsPageChange(oldUrl,urlWithParms);
                }
                
            }
            
            
            
            
//REPORT TEST RESULTS
//            System.out.println("NUMBER OF LINKS FOUND:"+urls.size());
            if(resultPlacesOfTarget.size()<1){
                throw new Exception("TARGET:"+targetUrl+" NOT FOUND IN RESULTS");
            }
            else{
                System.out.println("TARGET:"+targetUrl+" FOUND AT PLACE (0-based):");
                for(Integer place:resultPlacesOfTarget){
                    System.out.println("PLACE:"+place);

                }
            }
        }
        catch(Exception ex){
            ScreenShot();
            System.out.println("EXCEPTION MESSAGE:"+ex.getMessage());
            CustomStackTrace("EXCEPTION TRACE", ex);
            Assert.fail(ex.getMessage());
        }
    }
    
    @Test
    public void BuildPageOfFoundLinks() {
        try {

            // set implicit wait for this test
            driver.manage().timeouts().implicitlyWait(defaultImplicitWait, TimeUnit.SECONDS);

//GET REQUIRED COMMAND LINE PARMS
            // get base url
            String url;
            if (input != null) {
                url = input;
            } else {
                throw new Exception("URL NOT SPECIFIED (-Dinput)");
            }

            // MAKE SURE IT'S BEEN SPECIFIED
            if (url == null) {
                throw new Exception("URL SPECIFIED WAS NULL (-Dinput)");
            }
            
//GET REQUIRED PROPERTIES FILE PARMS
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

            // wait for links to be loaded
            (new WebDriverWait(driver, defaultImplicitWait)).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver d) {
                    return IsElementPresent(By.xpath(linksLoadedIndicatorXpath));
                }
            });

            // list for links
            List<String> urls = new ArrayList<String>();

            // make sure there are some links
            System.out.println("CHECKING FOR RESULTS");

            if (!IsElementPresent(By.xpath(linkXpath), quickWaitMilliSeconds)) {
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
                final String targetHref = href;

                driverGetWithTime(href);

                // check for the title text
                String titleText;
                if(titleTextXpath!=null){
                    if (IsElementPresent(By.xpath(titleTextXpath), quickWaitMilliSeconds)) {
                        try{
                            titleText = driver.findElement(By.xpath(titleTextXpath)).getText();
                        }
                        catch(Exception ex){
                            System.out.println("WARNING: STALE ELEMENT REFERENCE WHILE GETTING IMAGES, TITLE, BODY FROM:"+href);
                            break;
                        }
                        
                        if (titleText == null) {
                            titleText = "WARNING: TITLETEXT AT XPATH:"+titleTextXpath+" GETTEXT IS NULL";
                        }
                        else if (titleText.isEmpty()){
                            titleText = "WARNING: TITLETEXT AT XPATH:"+titleTextXpath+" GETTEXT IS EMPTY";
                        }
                    }
                    else{
                        titleText = "WARNING: TITLETEXT AT XPATH:"+titleTextXpath+" WAS NOT FOUND AFTER:"+quickWaitMilliSeconds+"ms";
                    }
                }
                else{
                    titleText = "WARNING: TITLETEXT NOT SPECIFIED";
                }
                

                // check for the body text
                String bodyText;
                if(bodyTextXpath!=null){
                    if (IsElementPresent(By.xpath(bodyTextXpath), quickWaitMilliSeconds)) {
                        bodyText = driver.findElement(By.xpath(bodyTextXpath)).getText();
                        if (bodyText == null) {
                            bodyText = "WARNING: BODYTEXT AT XPATH:"+bodyTextXpath+" GETTEXT IS NULL";
                        }
                        else if(bodyText.isEmpty()){
                            bodyText = "WARNING: BODYTEXT AT XPATH:"+bodyTextXpath+" GETTEXT IS EMPTY";
                        }
                    }
                    else{
                        bodyText = "WARNING: BODYTEXT AT XPATH:"+bodyTextXpath+" WAS NOT FOUND AFTER:"+quickWaitMilliSeconds+"ms";
                    }
                }
                else{
                    bodyText = "WARNING: BODYTEXT NOT SPECIFIED";
                }

                //if this is craigslist, get the contact info
//                if(environment.equals(EnvironmentType.craigslist)){
//                    
//                    //find the contact button
//                    String contactButtonXpath = properties.getProperty(environment.toString() + ".contactButtonXpath");
//                    final String contactInfoAnchorXpaths =  properties.getProperty(environment.toString() + ".contactInfoAnchorXpaths");
//                    final String contactInfoLiXpaths =  properties.getProperty(environment.toString() + ".contactInfoLiXpaths");
//                    if(IsElementPresent(By.xpath(contactButtonXpath),quickWaitMilliSeconds)){
//                        
//                        //JQUERY APPROACH
////                      String contactButtonJQueryExcecute = properties.getProperty(environment.toString() + ".contactButtonJQueryExcecute");
////                      ((JavascriptExecutor)driver).executeScript(contactButtonJQueryExcecute);
//                        
//                        driver.findElement(By.xpath(contactButtonXpath)).click();
//                        
//                        //ACTIONS APPROACH
////                      WebElement contactButton = driver.findElement(By.xpath(contactButtonXpath));
////                      Actions builder = new Actions(driver);
////                      builder.moveToElement(contactButton).click(contactButton).build().perform();
//                        
//                        //get the contact information anchors
//                        StringBuilder contactInfoString = new StringBuilder();
//                        contactInfoString.append("<br />CONTACT INFORMATION:<br />");
//                        
//                        //WRITE OUT CONTACT INFORMAITON LIST ITEMS WITH TEXT ONLY
//                        String contactInfoLiText;
//                        List<WebElement> contactInfoLis = driver.findElements(By.xpath(contactInfoLiXpaths));
//                        for(WebElement weLi: contactInfoLis){
//                            contactInfoLiText = weLi.getText();
//                            contactInfoString.append(contactInfoLiText).append("<br />");
//                        }
//                        
//
//                        //WRITE OUT CONTACT INFORMAITON ANCHORS WITH TEXT AND HREFS
//                        String contactInfoAnchorHref, contactInfoAnchorText;
//                        List<WebElement> contactInfoAnchors = driver.findElements(By.xpath(contactInfoAnchorXpaths));
//                        for(WebElement weA: contactInfoAnchors){
//                            contactInfoAnchorText = weA.getText();
//                            contactInfoAnchorHref = weA.getAttribute("href");
//                            contactInfoString.append("<a href='");
//                            contactInfoString.append(contactInfoAnchorHref);
//                            contactInfoString.append("' target='_blank'>");
//                            contactInfoString.append(contactInfoAnchorHref);
//                            contactInfoString.append("</a><br />");
//                        }
//
//                        //append contact info to body
//                        bodyText += contactInfoString;
//                        
//
//                    }
//                    else{
//                        System.out.println("WARNING: COULD NOT FIND CRAIGSLIST CONTACT BUTTON AT:"+contactButtonXpath+" AFTER:"+quickWaitMilliSeconds+"ms");
//                    }
//                }
                
                // check for images
                String imageSrc = "";
                if (IsElementPresent(By.xpath(imageXpath), quickWaitMilliSeconds)) {
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
                    System.out.println("WARNING: IMAGE AT XPATH:"+imageXpath+" WAS NOT FOUND AFTER:"+quickWaitMilliSeconds+"ms");
                }
                
                

                //add at least one result entry if no images were found
                if(imageSrc.isEmpty()){
                    results.add(new String[] { href, imageSrc, titleText, bodyText });
                }                
                
                // get a copy of the page DEBUGGING ONLY
//                RestRequest(href); 
                
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
        writer.println(HtmlReportHeader("BoardScrub:<a href='"+input+"' target='_blank'>"+input+"</a>"));

        String oldHref = new String();
        for (String[] entry : results) {
            if (!oldHref.equals(entry[0])) {
                oldHref = entry[0];
                writer.println("<hr size=10>");
                writer.println("<center>");
                writer.println("<h2><u>FROM RESULT:</u></h2>");
                writer.println("<h2><a href='" + oldHref + "' target='_blank'>" + oldHref + "</a></h2>");
                writer.println("</center>");
                writer.println("<h3>TITLE:</h3><span>" + entry[2] + "</span><br />");
                writer.println("<h3>BODY:</h3><span>" + entry[3] + "</span><br />");
            }
            writer.println("<h3>IMAGES:</h3><center><img src='" + entry[1] + "' /></center><br />");
        }
        writer.println(HtmlReportFooter());

        writer.flush();
        writer.close();

        System.out.println("INDEX FILE WRITTEN:" + fileName);
//        System.out.println("INDEX FILE COPIED (IF RUN FROM JENKINS):" + jenkinsReportPath + fileName);
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
