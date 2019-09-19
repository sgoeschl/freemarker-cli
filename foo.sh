#!/bin/sh

MY_BASE_URL=${MY_BASE_URL:=https://postman-echo.com}
echo "MY_BASE_URL = ${MY_BASE_URL}" 
 
echo "Executing 4 requests - starting at `date`"
echo "status,time,user"
curl --write-out '%{http_code},%{time_total},0401126' --insecure --silent --show-error --output /dev/null -H "Authorization: Bearer 0401126-0000-0000-0000-0123456789012" "${MY_BASE_URL}/get?__=0401126"; echo
curl --write-out '%{http_code},%{time_total},0401133' --insecure --silent --show-error --output /dev/null -H "Authorization: Bearer 0401133-0000-0000-0000-0123456789012" "${MY_BASE_URL}/get?__=0401133"; echo
curl --write-out '%{http_code},%{time_total},0401173' --insecure --silent --show-error --output /dev/null -H "Authorization: Bearer 0401173-0000-0000-0000-0123456789012" "${MY_BASE_URL}/get?__=0401173"; echo
curl --write-out '%{http_code},%{time_total},0401234' --insecure --silent --show-error --output /dev/null -H "Authorization: Bearer 0401234-0000-0000-0000-0123456789012" "${MY_BASE_URL}oooo/get?__=0401234"; echo
echo "Finished at `date`"
