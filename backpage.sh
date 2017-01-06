#!/usr/bin/env bash
mvn -DnoScroll -Dreport="backpage20151213" -Dinput="http://seattle.backpage.com/FemaleEscorts/" -DbodyTextXpath="//div[@class='postingBody']" -DnextLinkXpath="//a[contains(@class,'pagination next']" -DlinksLoadedIndicatorXpath="//input[@id='searchButton']" -DlinkXpath="//div[contains(@class,'cat')]/a[contains(@href,'http://seattle.backpage.com/FemaleEscorts/')]" -DimageXpath="//img[contains(@src,'.backpage.com')]" -DtitleTextXpath="//a[@class='h1link']/h1" -Dbrowser="CHROME" -DaHubPort="4723" -DaHubServer="localhost" -Dnogrid -Dnoscroll -DaNumber="50" -DwaitAfterPageLoadMilliSeconds="0" -Dtest="BoardScrub#BuildPageOfFoundLinks" test

