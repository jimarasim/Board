#!/usr/bin/env bash
# -DnoScroll - dont scroll the page after its loaded
# -Dreport - name given to the html report. format will be index{$REPORT}.htm
# -Dinput - initial page to load
# -DbodyTextXpath - xpath of element to get from every web page that represents something in the body of the web page
# -DnextLinkXpath - xpath of element that navigates to the next page where there are multiple pages of results. it's expected this element will not be available on the last page.
# -DlinkXpath - xpath of links to follow from the initial page specified by -Dinput
# -DimageXpath - xpath of images to gather on each page of each link matching -DlinkXpath
# -DtitleTextXpath - xpath to get the title of each page visited
# -Dnogrid - dont use selenium grid
# -DaNumber - maximum number of links to visit
# -DwaitAfterPageLoadMilliSeconds - how long to pause after each page is loaded or scrolled
# -Dtest - class name and junit test to run {class#@Test}
mvn -DnoScroll -Dreport="brownpapertickets" -Dinput="http://brownpapertickets.com" -DbodyTextXpath="//p|//span" -DnextLinkXpath="//void" -DlinksLoadedIndicatorXpath="//a[contains(@class,'footerlang')]" -DlinkXpath="//a" -DimageXpath="//img" -DtitleTextXpath="//h1" -Dbrowser="CHROME" -Dnogrid -DaNumber="5" -DwaitAfterPageLoadMilliSeconds="0" -Dtest="BoardScrub#BuildPageOfFoundLinks" test