#!/usr/bin/env bash
#[SOURCE OF TRUTH FOR BoardScrub#BuildPageOfFoundLinks NO GRID craigslist]
#SEATTLE SOFTWARE JOBS
#mvn -DthrottleDownWaitTimeMilliSeconds=0 -Dbrowser=FIREFOX -Dinput="http://seattle.craigslist.org/search/sof" -Dreport="jobs" -DnoScreenShots -DnoScroll -DlinksLoadedIndicatorXpath="//a[contains(text(),'help')]" -DlinkXpath="//a[contains(@class,'result-title')]" -DnextLinkXpath="//a[contains(@class,'button next')]" -DbodyTextXpath="//section[@id='postingbody']" -DimageXpath="//div[contains(@class,'slide first')]/img" -DtitleTextXpath="//h2[@class='postingtitle']" -Dnogrid -Dtest="BoardScrub#BuildPageOfFoundLinks" test
#BAINBRIDGE HOMES
#mvn -DthrottleDownWaitTimeMilliSeconds=0 -Dbrowser=CHROME  -Dreport="bainbridgerealestate" -Dinput="http://seattle.craigslist.org/search/rea?search_distance=12&postal=98110&min_price=10000&max_price=77000&availabilityMode=0" -DnoScreenShots -DnoScroll -DlinksLoadedIndicatorXpath="//a[contains(text(),'help')]" -DlinkXpath="//a[contains(@class,'result-title')]" -DnextLinkXpath="//a[contains(@class,'button next')]" -DbodyTextXpath="//section[@id='postingbody']" -DimageXpath="//div[contains(@class,'slide first')]/img" -DtitleTextXpath="//h2[@class='postingtitle']" -Dnogrid -Dtest="BoardScrub#BuildPageOfFoundLinks" test
#PORT ORCHARD HOMES
#mvn -DwaitAfterPageLoadMilliSeconds=3000 -Dbrowser=CHROME -Dreport="portorchardrealestate" -Dinput="http://seattle.craigslist.org/search/rea?search_distance=15&postal=98366&min_price=20000&max_price=77000&availabilityMode=0" -DnoScreenShots -DnoScroll -DlinksLoadedIndicatorXpath="//a[contains(text(),'help')]" -DlinkXpath="//a[contains(@class,'result-title')]" -DnextLinkXpath="//a[contains(@class,'button next')]" -DbodyTextXpath="//section[@id='postingbody']" -DimageXpath="//div[contains(@class,'slide first')]/img" -DtitleTextXpath="//h2[@class='postingtitle']" -Dnogrid -Dtest="BoardScrub#BuildPageOfFoundLinks" test
#POULSBO HOMES
mvn -DwaitAfterPageLoadMilliSeconds=3000 -Dbrowser=CHROME -Dreport="poulsborealestate" -Dinput="http://seattle.craigslist.org/search/rea?search_distance=25&postal=98370&min_price=20000&max_price=55000&availabilityMode=0" -DnoScreenShots -DnoScroll -DlinksLoadedIndicatorXpath="//a[contains(text(),'help')]" -DlinkXpath="//a[contains(@class,'result-title')]" -DnextLinkXpath="//a[contains(@class,'button next')]" -DbodyTextXpath="//section[@id='postingbody']" -DimageXpath="//div[contains(@class,'slide first')]/img" -DtitleTextXpath="//h2[@class='postingtitle']" -Dnogrid -Dtest="BoardScrub#BuildPageOfFoundLinks" test
#BOISE HOMES
#mvn -DthrottleDownWaitTimeMilliSeconds=0 -Dbrowser=CHROME -Dinput="http://boise.craigslist.org/search/rea?search_distance=20&postal=83701&min_price=10000&max_price=70000&availabilityMode=0" -Dreport="boiserealestate" -DnoScreenShots -DnoScroll -DlinksLoadedIndicatorXpath="//a[contains(text(),'help')]" -DlinkXpath="//a[contains(@class,'result-title')]" -DnextLinkXpath="//a[contains(@class,'button next')]" -DbodyTextXpath="//section[@id='postingbody']" -DimageXpath="//div[contains(@class,'slide first')]/img" -DtitleTextXpath="//h2[@class='postingtitle']" -Dnogrid -Dtest="BoardScrub#BuildPageOfFoundLinks" test
#=====================================
#COMMAND LINE SWITCHES FOR BoardScrub#BuildPageOfFoundLinks
#-DbodyTextXpath="//section[@id='postingbody']" xpath for the body text of the board post to grab
#-DimageXpath="//div[contains(@class,'slide first')]/img" xpath for images to grab from the board post
#-DlinksLoadedIndicatorXpath="//a[contains(text(),'help')]"
#-DlinkXpath="//a[contains(@class,'result-title')]" xpath for links to follow from the board query
#-DnextLinkXpath="//a[contains(@class,'button next')]" xpath for the link that goes to the next page of board query results
#-DtitleTextXpath="//h2[@class='postingtitle']" xpath for the title of the board post to grab
#=====================================
#CODEBASE HUB SWITCHES
#-DaHubServer="localhost"; //where to look for selenium grid. server name only. default behavior is to look for one.
#-DaHubPort="4444"; //port to use for selenium grid, if looking for one.
#-Dnogrid=null; //dont use selenium grid. default behavior is to look for grid on aHubPort aHubServer
#=====================================
#CODEBASE AND TEST TIME SWITCHES
#-DdefaultImplicitWaitSeconds=15; //implicit wait time for finding elements on a page, where the page being loaded is a factor (selenium)
#-DhardCodedSleepMilliSeconds=5000; //facebookcrawlalllinks is the only test that uses this
#-DthrottleDownWaitTimeMilliSeconds=500; //how long to wait when throttling down to look for elements after a page is known to be loaded (boardscrub craigslist codebase iselementpresent default)
#-DwaitAfterPageLoadMilliSeconds=0; //how long to wait for thread.sleep OR after a page loads from a link click (see protected String driverGetWithTime)
#-DwaitForPageChangeMilliSeconds=60000; //how long to wait for a page to change from an old url to a new one (see protected void WaitForPageChange)
#=====================================
#OTHER TEST SWITCHES
#-DaNumber=0; //verifylogos and boardscrub use it to signify how many links to visit. =<0 == visit all
#-DaString=null; //verifylogos uses it to signify what element to look for as its "logo"
#-Dinput=null; //verifylogos and boardscrub use it specify the first page to load
#-DnoImages=null; //boardscrub uses this to omit images found in its report.
#-Dpassword=null; //usage depends on test; virtually no known usage
#-Dreport=null; //verifylogos and boardscrub use this to name their reports: index{report}.htm
#-Duserid=null;//usage depends on test; virtually no known usage
#=====================================
#OTHER CODEBASE SWITCHES
#-Dlogging=null; //turn on logging to report browser errors, currently used only with CHROME
#-DnoScreenShots=null; //codebase drivergetwithtime uses this to decide whether to save screenshots
#-DnoScroll=null; //codebase ScrollPage() will refrain from scrolling if this is specified
#=====================================
#BROWSER ENUMERATIONS
#-Dbrowser
#    //GRID: PLATFORM AND VERSION ARE ONLY USED WHEN USING GRID OR A CHROME EMULATION
#    //NON-GRID: FOR NON-GRID, JUST SPECIFYING CHROME, FIREFOX, OR SAFARI WORKS ON MAC
#    //FIREFOX: ONLY WORKS WITH GECKODRIVER NOW
#    CHROME("chrome","",Platform.WINDOWS),  //REQUIRES CHROMEDRIVER
#    CHROMELINUX("chrome","",Platform.LINUX),
#    CHROMELINUX32("chrome","",Platform.LINUX),
#    CHROMEMAC ("chrome","",Platform.MAC),
#    FIREFOX("firefox","",Platform.WINDOWS), //REQUIRES GECKODRIVER
#    FIREFOXLINUX("firefox","",Platform.LINUX),
#    FIREFOXMAC("firefox","",Platform.MAC),
#    SAFARI("safari","",Platform.MAC),  //REQUIRES SAFARI DRIVER
#    IE8("InternetExplorer","8",Platform.WINDOWS), //REQUIRES IEDRIVERSERVER
#    IE9("InternetExplorer","9",Platform.WINDOWS),
#    IE10("InternetExplorer","10",Platform.WINDOWS),
#    IE11("InternetExplorer","11",Platform.WINDOWS),
#    //CHROME EMULATIONS
#    CHROMENEXUS5("Google","Nexus 5", Platform.MAC),
#    CHROMENEXUS6P("Google","Nexus 6P",Platform.MAC),
#    CHROMEIPHONE5("Apple","iPhone 5",Platform.MAC),
#    CHROMEIPHONE6("Apple","iPhone 6",Platform.MAC),
#    CHROMEIPHONE6PLUS("Apple","iPhone 6 Plus",Platform.MAC),
#    CHROMEIPAD("Apple","iPad",Platform.MAC),
#    //APPIUM MUST BE RUNNING AS THE SELENIUM GRID FOR THESE
#    APPIUMSAFARISIMULATOR("","",Platform.MAC),
#    APPIUMAPPSIMULATOR("","",Platform.MAC),
#    APPIUMAPPDEVICE("","",Platform.MAC),
#    APPIUMSAFARIDEVICE("","",Platform.MAC);
