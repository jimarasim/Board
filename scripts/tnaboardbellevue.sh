#!/usr/bin/env bash
mvn -Dtest=BoardScrub#BuildPageOfFoundLinks -Dbrowser=CHROME -Denvironment=tnaboard -Dinput="https://www.tnaboard.com/forumdisplay.php?98-WA-Provider-Ads&s=&pp=30&prefixid=seattle&field_dvb_service_type=Escort" -DaNumber=100 -Dnogrid -Dreport=seatna -DwaitAfterPageLoadMilliSeconds=0 test
