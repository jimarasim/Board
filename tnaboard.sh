mvn -Dtest="BoardScrub#BuildPageOfFoundLinks" -DnumResultsRESTParm="num" -DstartRESTParm="start" -DnextLinkXpath="//a[@rel='next']/img[@alt='Next' and @title='Next']" -D="input//p" -DaString="//p" -DlinksLoadedIndicatorXpath="//span[@class='forumtitle']" -DlinkXpath="//a[@class='title']" -DimageXpath="//*[contains(@id,'post_message_')]/blockquote//img" -DtitleTextXpath="//span[@class='threadtitle']/a" -DbodyTextXpath="//div[@class='postrow']" -Dbrowser="CHROME" -Dinput="https://www.tnaboard.com/forumdisplay.php?100-WA-LMA-amp-LMS" -DaHubPort="4723" -DaHubServer="localhost" -Dnogrid -DaNumber="2" test
