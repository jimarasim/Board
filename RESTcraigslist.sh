mvn -Dtest="BoardScrub#BuildPageOfFoundLinksViaRest"  -DlinkXpath="//span[@class='pl']/a" -DlinksLoadedIndicatorXpath="//input[@id='query']" -DaNumber="5" -DaString="//p" -Dinput="http://seattle.craigslist.org/search/sss?query'${1:-space+heater}" -Dbrowser="CHROME" -Dnogrid -Dreport="craigslist" -DwaitAfterPageLoadMilliSeconds="0" test
