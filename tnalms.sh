mvn -Dtest=BoardScrub#BuildPageOfFoundLinks -Dbrowser=CHROME -Dinput="https://www.tnaboard.com/forumdisplay.php?100-WA-LMA-amp-LMS" -DaHubPort=4723 -DaHubServer=localhost -Denvironment=tnaboard -Dnogrid -DaNumber=25 test