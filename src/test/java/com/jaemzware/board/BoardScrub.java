package com.jaemzware.board;

import com.jaemzware.seleniumcodebase.AutomationCodeBase;
import java.io.FileInputStream;
import java.io.PrintWriter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebElement;

/**
 *@author jaemzware@hotmail.com
 * 
 */
public class BoardScrub extends AutomationCodeBase
{
    static final String propertiesFile = "src/test/java/com/jaemzware/board/selenium.properties";
    static Properties properties = new Properties();
    
    @Before
    public void BeforeTest()
    {
        try{//start the webdriver
            
            //properties file is in same directory as pom.xml
            properties.load(new FileInputStream(propertiesFile));
            
            //get input parameters HERE
            GetParameters();
            
            StartDriver();
        }
        catch(Exception ex)
        {
            Assert.fail("BEFORE TESTS EXCEPTION:"+ex.getMessage());
        }
    }
    
    @Test
    public void BuildPageOfFoundLinks()
    {
        try{
            
            //set implicit wait for this test
            driver.manage().timeouts().implicitlyWait(defaultImplicitWait, TimeUnit.SECONDS);
            
            //get base url
            String url;
            if(input!=null){
                url=input;
            }
            else{
                url= properties.getProperty(environment.toString()+".url");
            }
            
            //MAKE SURE IT'S BEEN SPECIFIED
            if(url==null){
                throw new Exception("URL NOT SPECIFIED NOR FOUND IN PROPERTIES FILE");
            }
            
            //get xpaths to search for
            
            //indicator that page of links has completely loaded
            final String linksLoadedIndicatorXpath = properties.getProperty(environment.toString()+".linksLoadedIndicatorXpath");
            
            //xpath of each link on page of links
            final String linkXpath = properties.getProperty(environment.toString()+".linkXpath");
            
            //xpath of images to gather after following each link
            final String imageXpath = properties.getProperty(environment.toString()+".imageXpath");
            
            //xpath of text to gather after following each link
            final String textXpath = properties.getProperty(environment.toString()+".textXpath"); //NOT REQUIRED
            
            
            //CHECK FOR REQUIRED PARAMETERS
            if(linksLoadedIndicatorXpath==null){
                throw new Exception("MISSING:"+environment.toString()+".linksLoadedIndicatorXpath");
            }
            
            if(linkXpath==null){
                throw new Exception("MISSING:"+environment.toString()+".linkXpath");
            }
            
            if(imageXpath==null){
                throw new Exception("MISSING:"+environment.toString()+".imageXpath");
            }
            
            //NAVIGATE TO URL
            driverGetWithTime(url);
            
            
            //wait for login to complete
            (new WebDriverWait(driver,defaultImplicitWait))
                    .until(new ExpectedCondition<Boolean>(){
                    @Override
                    public Boolean apply(WebDriver d) {
                        return IsElementPresent(By.xpath(linksLoadedIndicatorXpath),1000);
                    }});
            
            //list for links
            List<String> urls = new ArrayList<String>();
            
            //make sure there are some links
            System.out.println("CHECKING FOR LINKS");
            
            if(!IsElementPresent(By.xpath(linkXpath),1000)){
                throw new Exception("COULDN'T FIND ANY LINKS");
            }
            
            //GET THE links
            System.out.println("FINDING LINKS");
            
            List<WebElement> webElements = driver.findElements(By.xpath(linkXpath));
            
            //store off the hrefs
            System.out.println("SAVING LINKS");
            
            for(WebElement we:webElements){
                urls.add(we.getAttribute("href"));
            }
            
            System.out.println("VISITING LINKS");
            List<String[]> images = new ArrayList<String[]>();
            
            //navigate to links and get images
            for(String href:urls)
            {
                final String hrefToWaitFor = href;
                
                driverGetWithTime(href);
                
                //wait for navigate to href to complete
                (new WebDriverWait(driver,defaultImplicitWait))
                        .until(new ExpectedCondition<Boolean>(){
                        @Override
                        public Boolean apply(WebDriver d) {
                            //this waits for an element on the page.  good for pages with a lot of elements to load, that could change the dom
                            return driver.getCurrentUrl().contains(hrefToWaitFor);
                            
                        }});
                
                
                //check for the text
                String optionalText="";
                if(textXpath!=null && IsElementPresent(By.xpath(textXpath),1000))
                {
                    optionalText = driver.findElement(By.xpath(textXpath)).getText();
                    if(optionalText==null){
                        optionalText="";
                    }
                }
                
                //check for images
                if(IsElementPresent(By.xpath(imageXpath),1000)){
                    //add images to images list
                    List<WebElement> imageElements = driver.findElements(By.xpath(imageXpath));
                    for(WebElement i:imageElements){
                        try{
                            images.add(new String[]{href,i.getAttribute("src"),optionalText});
                        }
                        catch(Exception ex){
                            System.out.println("WARNING: IMAGE WENT STALE");
                            break;
                        }   
                    }
                }
                
                
            }
            
            //build web page
            String fileName = "index"+getDateStamp()+".htm";
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            writer.println("<html><head><title>boardscrub</title></head><body><h1>boardscrub</h1>");
            
            
            String oldHref = new String();
            for(String[] entry:images)
            {
                if(!oldHref.equals(entry[0]))
                {
                    oldHref = entry[0];
                    writer.println("<a href='"+oldHref+"' target='_blank'>"+oldHref+"</a><br />");
                }
                writer.println("<span>"+entry[2]+"</span><br />");
                writer.println("<img src='"+entry[1]+"' /><br />");
            }
            writer.println("</body></html>");
            
            writer.flush();
            writer.close();
            
            System.out.println("INDEX FILE WRITTEN:"+fileName);
        }
        catch(Exception ex)
        {
            ScreenShot();
            CustomStackTrace("Scratch exception",ex);
            Assert.fail(ex.getMessage());
        }
    }
    
    
    @After
    public void AfterTest()
    {
        try
        {
            QuitDriver();
        }
        catch(Exception ex)
        {
            ScreenShot();
            this.CustomStackTrace("After test exception",ex);
            Assert.fail(ex.getMessage());
        }
    }
    
}
