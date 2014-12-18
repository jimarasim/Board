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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidParameterException;
import org.apache.commons.lang.StringUtils;

/**
 * @author jaemzware@hotmail.com
 */
public class BoardScrub extends CodeBase {
    private static final String propertiesFile = "src/test/java/com/jaemzware/board/selenium.properties";
    private static final Properties properties = new Properties();
    
    //url to navigate to 
    private static String url = null;
    // indicator that page of links has completely loaded
    private static String linksLoadedIndicatorXpath =null;
    // xpath of each link on page of links
    private static String linkXpath = null;
    // xpath of images to gather after following each link
    private static String imageXpath = null;
    // xpath of title text to gather after following each link
    private static String titleTextXpath = null;
    // xpath of text to gather after following each link
    private static String bodyTextXpath = null;
    // xpath of next link
    private static String nextLinkXpath = null;
    //num parameter name to use in url, for number of results to return (used for paging)
    private static String numResultsParm = null;
    //start parameter name to use in url, for nth result to get results from (used for paging)
    private static String startParm = null;
    //maximum number of visits
    private static int maxVisits=0;
    //target url to look for
    private static String targetUrl=null;
    //whether or not to show images
    private static Boolean showImages=true;
    
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
    
    @Test
    public void BuildPageOfFoundLinks() {
        try {

            // get command line parameters
            SetUrlCommandLineParameter(); 
            SetMaxVisitsCommandLineParameter();
            SetShowImagesCommandLineParameter();
            
            //get properties file information
            GetBuildPageOfFoundLinksRequiredProperties(); 
            
            //get all the links on the target url
            List<String> links = 
                    GetLinksOnPage(); 
            
            //get conent from the links
            List<String[]> contents = 
                    GetContentFromLinks(links); 

            //generate a page of the contents
            WriteContentsToWebPage(contents);

        } catch (Exception ex) {
            ScreenShot();
            System.out.println("BuildPageOfFoundLinks EXCEPTION MESSAGE:"+ex.getMessage());
            CustomStackTrace("BuildPageOfFoundLinks EXCEPTION TRACE", ex);
            Assert.fail(ex.getMessage());
        }
    }
    
    @Test
    public void BuildPageOfFoundLinksViaRest() {
        try {

            // get command line parameters
            SetUrlCommandLineParameter(); 
            SetMaxVisitsCommandLineParameter();
            SetTargetUrlCommandLineParameter();
            SetShowImagesCommandLineParameter();
            
            //get properties file information
            GetBuildPageOfFoundLinksRequiredProperties(); 
            
            //get all the links on the target url
            List<String> links = 
                    GetLinksOnPage(); 
            
            //get conent from the links
            List<String[]> contents = 
                    GetContentFromLinksViaRest(links); 

            //generate a page of the contents
            WriteContentsToWebPage(contents);

        } catch (Exception ex) {
            ScreenShot();
            System.out.println("BuildPageOfFoundLinksViaRest EXCEPTION MESSAGE:"+ex.getMessage());
            CustomStackTrace("BuildPageOfFoundLinksViaRest EXCEPTION TRACE", ex);
            Assert.fail(ex.getMessage());
        }
    }
    
    @Test 
    public void ResultPlace(){
        
        try{
            // set implicit wait for this test
            driver.manage().timeouts().implicitlyWait(defaultImplicitWait, TimeUnit.SECONDS);

            //GET REQUIRED COMMAND LINE PARMS
            SetUrlCommandLineParameter();
            SetMaxVisitsCommandLineParameter();
            SetTargetUrlCommandLineParameter();
            
            //get properties file paths
            GetResultPlaceRequiredProperties();

            //get each place where the target url shows up
            List<Integer>resultPlacesOfTarget = GetResultPlacesOfTarget();
            
            //report each place where the target url shows up
            ReportPlacesOfTarget(resultPlacesOfTarget);
            
        }
        catch(Exception ex){
            ScreenShot();
            System.out.println("EXCEPTION MESSAGE:"+ex.getMessage());
            CustomStackTrace("EXCEPTION TRACE", ex);
            Assert.fail(ex.getMessage());
        }
    }
    
    private void ReportPlacesOfTarget(List<Integer> resultPlacesOfTarget) throws Exception{
        //REPORT TEST RESULTS
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
    
    /**
     * This method sets url to the input command line parameter, and fails if not specified
     * @throws Exception 
     */
    private void SetUrlCommandLineParameter() throws Exception{
        // get base url
        if (input != null) {
            url = input;
        } else {
            throw new Exception("URL NOT SPECIFIED (-Dinput)");
        }

        // MAKE SURE IT'S BEEN SPECIFIED
        if (url == null) {
            throw new Exception("URL SPECIFIED WAS NULL (-Dinput)");
        }
    }
    
    /**
     * This method sets the maxvists to the aNumber command line parameter, or 0 if not specified
     * @throws Exception 
     */
    private void SetMaxVisitsCommandLineParameter() throws Exception{
        maxVisits = (aNumber!=null)?Integer.parseInt(aNumber):0; //check if the max number was specified
    }
    
    /**
     * This method sets the targetUrl to the aString command line parameter, and fails if not specified
     * @throws Exception 
     */
    private void SetTargetUrlCommandLineParameter() throws Exception{
        // get target result link string to look for
        if (aString != null) {
            targetUrl = aString;
        } else {
            throw new Exception("URL NOT SPECIFIED (-DaString)");
        }

        // MAKE SURE IT'S BEEN SPECIFIED
        if (targetUrl == null) {
            throw new Exception("TARGET NOT SPECIFIED NOR FOUND IN PROPERTIES FILE");
        }   
    }
    
    /**
     * This method sets the showImages according to the noImages command line parameter
     * @throws Exception 
     */
    private void SetShowImagesCommandLineParameter() throws Exception{
        showImages=StringUtils.isEmpty(System.getProperty("noImages"));
    }
    
    /**
     * this method gets required properties from the properties file for the BuildPageOfFoundLinks test
     */
    private void GetBuildPageOfFoundLinksRequiredProperties() throws Exception{
            // indicator that page of links has completely loaded
            linksLoadedIndicatorXpath = properties.getProperty(environment.toString()
                    + ".linksLoadedIndicatorXpath");

            // xpath of each link on page of links
            linkXpath = properties.getProperty(environment.toString() + ".linkXpath");

            // xpath of images to gather after following each link
            imageXpath = properties.getProperty(environment.toString() + ".imageXpath");

            // xpath of text to gather after following each link
            titleTextXpath = properties.getProperty(environment.toString() + ".titleTextXpath");
            
            bodyTextXpath = properties.getProperty(environment.toString() + ".bodyTextXpath"); 

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
            
            if (titleTextXpath == null) {
                throw new Exception("MISSING:" + environment.toString() + ".titleTextXpath");
            }

            if (bodyTextXpath == null) {
                throw new Exception("MISSING:" + environment.toString() + ".bodyTextXpath");
            }
            
            System.out.println("linksLoadedIndicatorXpath:"+linksLoadedIndicatorXpath+" linkXpath:"+linkXpath+" imageXpath:"+imageXpath+" titleTextXpath:"+titleTextXpath+" bodyTextXpath:"+bodyTextXpath);
    }
    
    /**
     * this method gets required properties from the properties file for the BuildPageOfFoundLinks test
     */
    private void GetResultPlaceRequiredProperties() throws Exception{
            //GET REQUIRED PROPERTIES FILE PARMS
            // indicator that page of links has completely loaded
            linksLoadedIndicatorXpath = properties.getProperty(environment.toString()+ ".linksLoadedIndicatorXpath");

            // xpath of each link on page of links
            linkXpath = properties.getProperty(environment.toString() + ".linkXpath");
            
            // xpath of next link
            nextLinkXpath = properties.getProperty(environment.toString() + ".nextLinkXpath");
            
            //num parameter name to use in url, for number of results to return (used for paging)
            numResultsParm = properties.getProperty(environment.toString() + ".numResultsParm");
            
            //start parameter name to use in url, for nth result to get results from (used for paging)
            startParm = properties.getProperty(environment.toString() + ".startParm");
            
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
            
            
            System.out.println("linksLoadedIndicatorXpath:"+linksLoadedIndicatorXpath+" linkXpath:"+linkXpath+" nextLinkXpath:"+nextLinkXpath+" numResultsParm:"+numResultsParm+" startParm:"+startParm);
    }
    
    /**
     * Write results from a BoardScrub to a web page
     * @param results
     * @throws Exception 
     */
    private void WriteContentsToWebPage(List<String[]> results) throws Exception
    {
        // build web page
        String fileName = "Index-BoardScrub-" + getDateStamp() + ".htm";
        PrintWriter writer = new PrintWriter(fileName, "UTF-8");
        writer.println(HtmlReportHeader("BoardScrub:<a href='"+input+"' target='_blank'>PAGE</a>"));

        String oldHref = new String();
        for (String[] entry : results) {
            if (!oldHref.equals(entry[0])) {
                oldHref = entry[0];
                writer.println("<hr size=10>");
                writer.println("<center>");
                writer.println("<h2><a href='" + oldHref + "' target='_blank'>LINK</a></h2>");
                writer.println("</center>");
                writer.println("<h3>TITLE:</h3><span>" + entry[2] + "</span><br />");
                writer.println("<h3>BODY:</h3><span>" + entry[3] + "</span><br />");
            }
            writer.println("<center><a href='"+oldHref+"' target='_blank'><img src='" + entry[1] + "' /></a></center><br />");
        }
        writer.println(HtmlReportFooter());

        writer.flush();
        writer.close();

        System.out.println("INDEX FILE WRITTEN:" + "http://50.251.226.90:8081/job/Board%20-%20BoardScrub/ws/" + fileName);
        System.out.println("INDEX FILE COPIED:" + jenkinsReportPath + fileName);
        System.out.println("INDEX FILE COPIED:" + jenkinsReportPathInternal + fileName);
    }

    
            
    /**
     * This method gets links on the target page
     * @return 
     */
    private List<String> GetLinksOnPage() throws Exception{
        // list for links
        List<String> urls = new ArrayList<>();
        
        driverGetWithTime(url);

        // wait for links to be loaded
        (new WebDriverWait(driver, defaultImplicitWait)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                return IsElementPresent(By.xpath(linksLoadedIndicatorXpath));
            }
        });

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
        
        return urls;
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
            System.out.println("HARDCODED SLEEP TO AVOID STALE REFERENCES:"+urlWithParms);
            Thread.sleep(quickWaitMilliSeconds);
     
    }
    
    /**
     * This method visits each url, and puts its content into a results list
     * @return
     * @throws Exception 
     */
    private List<String[]> GetContentFromLinks(List<String> links) throws Exception{
    

        System.out.println("VISITING RESULT LINKS");
        List<String[]> results = new ArrayList<>();

        // navigate to links and get images

        int visitCount = 0;
        for (String href : links) {
            try{
                driverGetWithTime(href);
                System.out.println("HARDCODED SLEEP FOR "+quickWaitMilliSeconds+"ms");
                Thread.sleep(quickWaitMilliSeconds);
            }
            catch(Exception ex){
                System.out.println("WARNING: PAGE TOOK LONG TO LOAD:"+href+", ... MOVING ON");
                continue;
            }

            // check for the title text
            String titleText="";

            if (IsElementPresent(By.xpath(titleTextXpath), quickWaitMilliSeconds)) {
                try{
                    titleText = driver.findElement(By.xpath(titleTextXpath)).getText();
                }
                catch(Exception ex){
                    System.out.println("WARNING: STALE ELEMENT REFERENCE WHILE GETTING IMAGES, TITLE, BODY FROM:"+href);
                    break;
                }

                if (titleText == null) {
                    System.out.println("WARNING: TITLETEXT AT XPATH:"+titleTextXpath+" GETTEXT IS NULL");
                }
                else if (titleText.isEmpty()){
                    System.out.println("WARNING: TITLETEXT AT XPATH:"+titleTextXpath+" GETTEXT IS EMPTY");
                }
            }
            else{
                System.out.println("WARNING: TITLETEXT AT XPATH:"+titleTextXpath+" WAS NOT FOUND AFTER:"+quickWaitMilliSeconds+"ms");
            }


            // check for the body text
            List<WebElement> allBodyTexts = new ArrayList<WebElement>();

            if (IsElementPresent(By.xpath(bodyTextXpath), quickWaitMilliSeconds)) {
                allBodyTexts = driver.findElements(By.xpath(bodyTextXpath));
            }
            else{
                System.out.println("WARNING: BODYTEXT AT XPATH:"+bodyTextXpath+" WAS NOT FOUND AFTER:"+quickWaitMilliSeconds+"ms");
            }


            //add all body text into one string
            StringBuilder bodyText=new StringBuilder();
            String tempString;
            for(WebElement we: allBodyTexts){
                try{
                    tempString = we.getText();
                    bodyText.append(tempString);
                }
                catch(Exception ex){
                    System.out.println("WARNING: allBodyTexts ELEMENTS WENT STALE WHILE TRYING TO GET TEXT FROM THEM");
                    break;
                }
            }

            // check for images
            String imageSrc = "";
            if(showImages){
                if (IsElementPresent(By.xpath(imageXpath), quickWaitMilliSeconds)) {
                    // add images to images list
                    List<WebElement> imageElements = driver.findElements(By.xpath(imageXpath));
                    for (WebElement i : imageElements) {
                        try {
                            imageSrc=i.getAttribute("src");

                            //add result entry image
                            results.add(new String[] { href, imageSrc, titleText, bodyText.toString().substring(0, 1000) });
                        } 
                        catch (Exception ex) {
                            System.out.println("WARNING: IMAGE WENT STALE");
                        }
                    }
                }
                else{
                    System.out.println("WARNING: IMAGE AT XPATH:"+imageXpath+" WAS NOT FOUND AFTER:"+quickWaitMilliSeconds+"ms");
                }
            }



            //add at least one result entry if no images were found
            if(imageSrc!=null && imageSrc.isEmpty()){
                results.add(new String[] { href, imageSrc, titleText, bodyText.toString() });
            }                

            //check the desired image count, and break if it's been reached
            if((maxVisits>0) && (++visitCount>maxVisits)){
                break;
            }

        }
        
       return results;
    }
    
    /**
     * This method visits each url, locally after getting it from a rest request, and puts its content into a results list
     * @return
     * @throws Exception 
     */
    private List<String[]> GetContentFromLinksViaRest(List<String> links) throws Exception{
    

        System.out.println("VISITING RESULT LINKS");
        List<String[]> results = new ArrayList<>();

        // navigate to links and get images

        int visitCount = 0;
        String rawHtml="";
        String rawHtmlLocalFile="";
        for (String href : links) {
            try{
                //do an http get of the page
                rawHtml = HttpGetReturnResponse(href);
                
                //write it to a file
                rawHtmlLocalFile = targetUrl + WriteHtmlContentToFile(rawHtml);
                
                
                driverGetWithTime(rawHtmlLocalFile);
                System.out.println("HARDCODED SLEEP FOR "+quickWaitMilliSeconds+"ms");
                Thread.sleep(quickWaitMilliSeconds);
            }
            catch(Exception ex){
                System.out.println("WARNING: EXCEPTION GETTING:"+rawHtmlLocalFile+", ... MOVING ON. EXCEPTION:"+ex.getMessage());
                this.CustomStackTrace(userid, ex);
                continue;
            }

            // check for the title text
            String titleText="";

            if (IsElementPresent(By.xpath(titleTextXpath), quickWaitMilliSeconds)) {
                try{
                    titleText = driver.findElement(By.xpath(titleTextXpath)).getText();
                }
                catch(Exception ex){
                    System.out.println("WARNING: STALE ELEMENT REFERENCE WHILE GETTING IMAGES, TITLE, BODY FROM:"+href);
                    break;
                }

                if (titleText == null) {
                    System.out.println("WARNING: TITLETEXT AT XPATH:"+titleTextXpath+" GETTEXT IS NULL");
                }
                else if (titleText.isEmpty()){
                    System.out.println("WARNING: TITLETEXT AT XPATH:"+titleTextXpath+" GETTEXT IS EMPTY");
                }
            }
            else{
                System.out.println("WARNING: TITLETEXT AT XPATH:"+titleTextXpath+" WAS NOT FOUND AFTER:"+quickWaitMilliSeconds+"ms");
            }


            // check for the body text
            List<WebElement> allBodyTexts = new ArrayList<WebElement>();

            if (IsElementPresent(By.xpath(bodyTextXpath), quickWaitMilliSeconds)) {
                allBodyTexts = driver.findElements(By.xpath(bodyTextXpath));
            }
            else{
                System.out.println("WARNING: BODYTEXT AT XPATH:"+bodyTextXpath+" WAS NOT FOUND AFTER:"+quickWaitMilliSeconds+"ms");
            }


            //add all body text into one string
            StringBuilder bodyText=new StringBuilder();
            String tempString;
            for(WebElement we: allBodyTexts){
                try{
                    tempString = we.getText();
                    bodyText.append(tempString);
                }
                catch(Exception ex){
                    System.out.println("WARNING: allBodyTexts ELEMENTS WENT STALE WHILE TRYING TO GET TEXT FROM THEM");
                    break;
                }
            }

            // check for images
            String imageSrc = "";
            if(showImages){
                if (IsElementPresent(By.xpath(imageXpath), quickWaitMilliSeconds)) {
                    // add images to images list
                    List<WebElement> imageElements = driver.findElements(By.xpath(imageXpath));
                    for (WebElement i : imageElements) {
                        try {
                            imageSrc=i.getAttribute("src");

                            //add result entry image
                            results.add(new String[] { href, imageSrc, titleText, bodyText.toString().substring(0, 1000) });
                        } 
                        catch (Exception ex) {
                            System.out.println("WARNING: IMAGE WENT STALE");
                        }
                    }
                }
                else{
                    System.out.println("WARNING: IMAGE AT XPATH:"+imageXpath+" WAS NOT FOUND AFTER:"+quickWaitMilliSeconds+"ms");
                }
            }



            //add at least one result entry if no images were found
            if(imageSrc!=null && imageSrc.isEmpty()){
                results.add(new String[] { href, imageSrc, titleText, bodyText.toString() });
            }                

            //check the desired image count, and break if it's been reached
            if((maxVisits>0) && (++visitCount>maxVisits)){
                break;
            }

        }
        
       return results;
    }
    
    private List<Integer> GetResultPlacesOfTarget() throws Exception{
        List<Integer>resultPlacesOfTarget = new ArrayList<>(); 
        int numResultsOnPage = 100;
        int numCurrentPageFirstResult=1;
        
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

        Boolean continueProcessing = true;

//PAGE THROUGH ALL RESULTS
        while(continueProcessing){

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

            //SET FIRST RESULT TO BE ON THE NEXT PAGE OF RESULTS
            numCurrentPageFirstResult += numResultsOnPage;

            if(maxVisits>0 && numCurrentPageFirstResult >= maxVisits){
                System.out.println("MAX VISITS REACHED numCurrentPageFirstResult:"+numCurrentPageFirstResult+" numResultsOnPage:"+numResultsOnPage+" maxVisits:"+maxVisits);

                //tell the loop to stop
                continueProcessing=false;
            }
            else if(!IsElementPresent(By.xpath(nextLinkXpath),quickWaitMilliSeconds)){
                System.out.println("LAST PAGE REACHED: "+driver.getCurrentUrl());

                //there is no next link, we're done, tell the while loop to stop
                continueProcessing=false;
            }
            else{

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
        
        return resultPlacesOfTarget;
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


//GRAVEYARD

// get a copy of the page DEBUGGING ONLY
//                RestRequest(href); 



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
                
