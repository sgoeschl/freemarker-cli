#!/bin/sh
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Check that java is on the path

hash java 2>/dev/null || { echo >&2 "I require JDK but it's not installed.  Aborting."; exit 1; }
hash mvn 2>/dev/null || { echo >&2 "I require Maven but it's not installed.  Aborting."; exit 1; }

# Run all the samples being documented

mkdir target 2>/dev/null
mkdir target/out 2>/dev/null

FREEMARKER_CMD="target/appassembler/bin/freemarker-cli"
# FREEMARKER_CMD="java -jar target/freemarker-cli-2.0.0-BETA-4-SNAPSHOT-jar-with-dependencies.jar"

#############################################################################
# Info
#############################################################################

echo "templates/info.ftl"
$FREEMARKER_CMD -t templates/info.ftl README.md > target/out/info.txt || { echo >&2 "Test failed.  Aborting."; exit 1; }

#############################################################################
# Demo
#############################################################################

echo "templates/demo.ftl"
$FREEMARKER_CMD -t templates/demo.ftl README.md > target/out/demo.txt || { echo >&2 "Test failed.  Aborting."; exit 1; }

#############################################################################
# CSV
#############################################################################

echo "templates/csv/html/transform.ftl"
$FREEMARKER_CMD -t templates/csv/html/transform.ftl site/sample/csv/contract.csv > target/out/contract.html || { echo >&2 "Test failed.  Aborting."; exit 1; }

echo "templates/csv/md/transform.ftl"
$FREEMARKER_CMD -t templates/csv/md/transform.ftl site/sample/csv/contract.csv > target/out/contract.md || { echo >&2 "Test failed.  Aborting."; exit 1; }

echo "templates/csv/shell/curl.ftl"
$FREEMARKER_CMD -t ./templates/csv/shell/curl.ftl site/sample/csv/user.csv > target/out/curl.sh || { echo >&2 "Test failed.  Aborting."; exit 1; }

#############################################################################
# CSV To XML-FO & PDF
#############################################################################

echo "templates/csv/fo/transform.ftl"
$FREEMARKER_CMD -t templates/csv/fo/transform.ftl site/sample/csv/locker-test-users.csv > target/out/locker-test-users.fo || { echo >&2 "Test failed.  Aborting."; exit 1; }

if hash fop 2>/dev/null; then
	echo "fop -fo target/out/locker-test-users.fo target/out/locker-test-users.pdf"
    fop -fo target/out/locker-test-users.fo target/out/locker-test-users.pdf 2>/dev/null || { echo >&2 "Test failed.  Aborting."; exit 1; }
fi

echo "templates/csv/fo/transactions.ftl"
$FREEMARKER_CMD -t templates/csv/fo/transactions.ftl site/sample/csv/transactions.csv > target/out/transactions.fo || { echo >&2 "Test failed.  Aborting."; exit 1; }

if hash fop 2>/dev/null; then
	echo "fop -fo target/out/transactions.fo target/out/transactions-fo.pdf"
    fop -fo target/out/transactions.fo target/out/transactions-fo.pdf 2>/dev/null || { echo >&2 "Test failed.  Aborting."; exit 1; }
fi

#############################################################################
# CSV to HTML & PDF
#############################################################################

echo "templates/csv/html/transform.ftl"
$FREEMARKER_CMD -t templates/csv/html/transactions.ftl site/sample/csv/transactions.csv > target/out/transactions.html || { echo >&2 "Test failed.  Aborting."; exit 1; }

if hash wkhtmltopdf 2>/dev/null; then
	echo "wkhtmltopdf -O landscape target/out/transactions.html target/out/transactions-html.pdf"
    wkhtmltopdf -O landscape target/out/transactions.html target/out/transactions-html.pdf 2>/dev/null || { echo >&2 "Test failed.  Aborting."; exit 1; }
fi

#############################################################################
# Grok
#############################################################################

echo "templates/accesslog/combined-access.ftl"
$FREEMARKER_CMD -t templates/accesslog/combined-access.ftl site/sample/accesslog/combined-access.log > target/out/combined-access.log.txt || { echo >&2 "Test failed.  Aborting."; exit 1; }

#############################################################################
# Excel
#############################################################################

echo "templates/excel/html/transform.ftl"
$FREEMARKER_CMD -t templates/excel/html/transform.ftl site/sample/excel/test.xls > target/out/test.xls.html || { echo >&2 "Test failed.  Aborting."; exit 1; }
$FREEMARKER_CMD -t templates/excel/html/transform.ftl site/sample/excel/test.xlsx > target/out/test.xslx.html || { echo >&2 "Test failed.  Aborting."; exit 1; }
$FREEMARKER_CMD -t templates/excel/html/transform.ftl site/sample/excel/test-multiple-sheets.xlsx > target/out/test-multiple-sheets.xlsx.html || { echo >&2 "Test failed.  Aborting."; exit 1; }

echo "templates/excel/md/transform.ftl"
$FREEMARKER_CMD -t templates/excel/md/transform.ftl site/sample/excel/test-multiple-sheets.xlsx > target/out/test-multiple-sheets.xlsx.md || { echo >&2 "Test failed.  Aborting."; exit 1; }

echo "templates/excel/csv/transform.ftl"
$FREEMARKER_CMD -t templates/excel/csv/transform.ftl site/sample/excel/test-multiple-sheets.xlsx > target/out/test-multiple-sheets.xlsx.csv || { echo >&2 "Test failed.  Aborting."; exit 1; }

echo "templates/excel/csv/custom.ftl"
$FREEMARKER_CMD -t templates/excel/csv/custom.ftl -Dcsv.format=MYSQL site/sample/excel/test.xls > target/out/test-transform-xls.csv || { echo >&2 "Test failed.  Aborting."; exit 1; }

#############################################################################
# HTML
#############################################################################

echo "templates/html/csv/dependencies.ftl"
$FREEMARKER_CMD -t templates/html/csv/dependencies.ftl site/sample/html/dependencies.html > target/out/dependencies.csv || { echo >&2 "Test failed.  Aborting."; exit 1; }

#############################################################################
# JSON
#############################################################################

echo "templates/json/csv/swagger-endpoints.ftl"
$FREEMARKER_CMD -t templates/json/csv/swagger-endpoints.ftl site/sample/json/swagger-spec.json > target/out/swagger-spec.csv || { echo >&2 "Test failed.  Aborting."; exit 1; }

echo "templates/json/html/customer-user-products.ftl"
$FREEMARKER_CMD -t templates/json/html/customer-user-products.ftl site/sample/json/customer-user-products.json > target/out/customer-user-products.html || { echo >&2 "Test failed.  Aborting."; exit 1; }

if hash wkhtmltopdf 2>/dev/null; then
	echo "wkhtmltopdf -O landscape target/out/customer-user-products.html target/out/customer-user-products.pdf"
    wkhtmltopdf -O landscape target/out/customer-user-products.html target/out/customer-user-products.pdf 2>/dev/null || { echo >&2 "Test failed.  Aborting."; exit 1; }
fi

echo "templates/json/md/customer-user-products.ftl"
$FREEMARKER_CMD -t templates/json/md/customer-user-products.ftl  site/sample/json/customer-user-products.json > target/out/customer-user-products.md || { echo >&2 "Test failed.  Aborting."; exit 1; }

if hash curl 2>/dev/null; then
echo "templates/json/md/github-users.ftl"
$FREEMARKER_CMD -t templates/json/md/github-users.ftl site/sample/json/github-users.json > target/out/github-users-curl.md || { echo >&2 "Test failed.  Aborting."; exit 1; }
fi

#############################################################################
# Properties
#############################################################################

echo "templates/properties/csv/locker-test-users.ftl"
$FREEMARKER_CMD -t templates/properties/csv/locker-test-users.ftl site/sample/properties > target/out/locker-test-users.csv || { echo >&2 "Test failed.  Aborting."; exit 1; }

#############################################################################
# YAML
#############################################################################

echo "templates/yaml/txt/transform.ftl"
$FREEMARKER_CMD -t ./templates/yaml/txt/transform.ftl ./site/sample/yaml/customer.yaml > target/out/customer.txt || { echo >&2 "Test failed.  Aborting."; exit 1; }

#############################################################################
# XML
#############################################################################

echo "templates/xml/txt/recipients.ftl"
$FREEMARKER_CMD -t ./templates/xml/txt/recipients.ftl site/sample/xml/recipients.xml > target/out/recipients.txt || { echo >&2 "Test failed.  Aborting."; exit 1; }

echo "Created the following sample files in ./target/out"
ls -l ./target/out
