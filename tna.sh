mvn -Dtest=BoardScrub#BuildPageOfFoundLinks
-Dbrowser=CHROME
-Dinput="https://www.tnaboard.com/forumdisplay.php?98-WA-Provider-Ads&sort=views&order=desc"
-DaHubPort=4723
-DaHubServer=localhost
-Dnogrid
-DlinksLoadedIndicatorXpath="\/\/span[@class='forumtitle'\ and\ contains(text(),'Provider\ Ads')]"
-DlinkXpath="\/\/a[contains(@class,'title')]"
-DimageXpath="\/\/*[contains(@id,'post_message_')]\/blockquote\/\/img"
-DtitleTextXpath="\/\/span[@class='threadtitle']\/a"
-DbodyTextXpath="\/\/div[@class='postrow']"
-DnextLinkXpath="\/\/a[@rel='next']\/img[@alt='Next'\ and\ @title='Next']"
test

# TO PASS IN AN XPATH COMMAND LINE PARAMETER
#   "//PATH" PUT QUOTES AROUND XPATH "//IMG"
#   / ESCAPE FORWARD SLASHES WITH BACK SLASHES \/
#   _ ESCAPE SPACES WITH BACK SLASHES \_
#   ' DONT ESCAPE SINGLE QUOTES '
#   @ DONT ESCAPE AT CHARACTER @
#   = DONT ESCAPE EQUAL CHARACTER =
#   [ DONT ESCAPE SQUARE BRACKET
#   ( DONT ESCAPE OPEN PARENTHESES
#   ) DONT ESCAPE CLOSE PARENTHESES



# -DnextLinkXpath = "\/\/a[@rel='next']\/img[@alt='Next'\ and\ @title='Next']"



# -DaHubServer:localhost
# -DaHubPort:4723
# -Dbrowser:CHROME
# -DappiumApp:null
# -DappiumUdid:null
# -DappiumIosTargetVersion:null
# -DappiumIosDeviceName:null
# -Denvironment:null
# -DwaitAfterPageLoadMilliSeconds:0
# -Duserid:null
# -Dpassword:null
# -Dinput:https://www.tnaboard.com/forumdisplay.php?98# -WA# -Provider# -Ads&sort=views&order=desc
# -DaNumber:null
# -DaString:null
# -Dreport:null
# -DnoImages:null
# -Dlogging:null
# -DnoScroll:null
# -DnoScreenshots:null
# -DlinksLoadedIndicatorXpath:null
# -DlinkXpath:null
# -DimageXpath:null
# -DtitleTextXpath:null
# -DbodyTextXpath:null
# -DnextLinkXpath:null
