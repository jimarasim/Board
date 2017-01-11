#!/usr/bin/env bash
#[SOURCE OF TRUTH FOR BoardScrub#BuildPageOfFoundLinks GRID craigslist]
#LEVEL LAND REAL ESTATE NOT AUBURN
mvn -Dbrowser="FIREFOX" -Dinput="http://seattle.craigslist.org/search/rea?query=-auburn+level&sort=pricedsc&search_distance=20&postal=98310&min_price=50000&max_price=110000&availabilityMode=0" -Dreport="gridcraigslisthousesbremerton" -DnoScreenShots -DnoScroll -DlinksLoadedIndicatorXpath="//a[contains(text(),'help')]" -DlinkXpath="//a[contains(@class,'result-title')]" -DnextLinkXpath="//a[contains(@class,'button next')]" -DbodyTextXpath="//section[@id='postingbody']" -DimageXpath="//div[contains(@class,'slide first')]/img" -DtitleTextXpath="//h2[@class='postingtitle']" -DaHubPort="4444" -DaHubServer="localhost" -Dtest="BoardScrub#BuildPageOfFoundLinks" test
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