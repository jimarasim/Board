#-Dtest="BoardScrubREST#BuildPageOfFoundLinksViaRest"                       - test to run
#-DlinkXpath="//span[@class='pl']/a"                                        - link on input page to follow
#-DlinksLoadedIndicatorXpath="//input[@id='query']"                         - element to wait for before finding links on the input page
#-DaNumber="5"                                                              - total number of links to gather data from
#-DaString="//p"                                                            - data to gather from the page links
#-Dinput="http://seattle.craigslist.org/search/sss?query'${1:-skateboard}"  - page input to this program; page to load and follow links from
#-Dbrowser="CHROME"                                                         - browser to open
#-Dnogrid                                                                   - do not use selenium grid. when this is not specified, you must specify -DaHubPort and -DaHubServer
#-Dreport="craigslistREST"                                                  - substring to put in the report name to identify this scrub
#-DwaitAfterPageLoadMilliSeconds="0"                                        - time in milliseconds to wait after the page has loaded

mvn -Dtest="BoardScrubREST#BuildPageOfFoundLinksViaRest"  -DlinkXpath="//a[contains(@href,'skateparks')]" -DlinksLoadedIndicatorXpath="//div[@id='footerLogo']" -DaNumber="0" -DaString="//div[@class='address']" -Dinput="http://grindline.com/find-a-park/" -Dbrowser="CHROME" -Dnogrid -Dreport="grindlineREST" -DwaitAfterPageLoadMilliSeconds="0" test
