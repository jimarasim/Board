mvn -DnoScroll -Dtest="BoardScrub#BuildPageOfFoundLinks" -DnumResultsRESTParm="num" -DstartRESTParm="start" -Dreport="tnaboard20151213b" -Dinput="https://www.tnaboard.com/forumdisplay.php?98-WA-Provider-Ads&s=&pp=30&prefixid=wa_statewide&field_dvb_service_type=Escort" -DnextLinkXpath="//a[@rel='next']/img[@alt='Next' and @title='Next']" -DaString="tnaboard.com" -DlinksLoadedIndicatorXpath="//span[@class='forumtitle']" -DlinkXpath="//a[@class='title']" -DimageXpath="//*[contains(@id,'post_message_')]/blockquote//img" -DtitleTextXpath="//span[@class='threadtitle']/a" -DbodyTextXpath="//div[@class='postrow']" -Dbrowser="CHROME" -DaHubPort="4723" -DaHubServer="localhost" -Dnogrid -DaNumber="300" -DwaitAfterPageLoadMilliSeconds="0" -Dnoscroll test
