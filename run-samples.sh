#!/bin/sh

# Run all the samples being documented 

mkdir target
mkdir target/out

echo "templates/demo.ftl"
groovy freemarker-cli.groovy -t templates/demo.ftl target/out/demo.txt

echo "templates/csv/html/transform.ftl"
groovy freemarker-cli.groovy -t templates/csv/html/transform.ftl site/sample/csv/contract.csv > target/out/contract.html

echo "templates/csv/md/transform.ftl"
groovy freemarker-cli.groovy -t templates/csv/md/transform.ftl site/sample/csv/contract.csv > target/out/contract.md

echo "templates/excel/html/test.ftl"
groovy freemarker-cli.groovy -t templates/excel/html/test.ftl site/sample/excel/test.xls > target/out/test.xls.html
groovy freemarker-cli.groovy -t templates/excel/html/test.ftl site/sample/excel/test.xlsx > target/out/test.xslx.html

echo "templates/json/csv/swagger-endpoints.ftl"
groovy freemarker-cli.groovy -t templates/json/csv/swagger-endpoints.ftl site/sample/json/swagger-spec.json > target/out/swagger-spec.csv

echo "templates/json/html/customer-user-products.ftl"
groovy freemarker-cli.groovy -t templates/json/html/customer-user-products.ftl site/sample/json/customer-user-products.json > target/out/customer-user-products.html

echo "templates/json/md/customer-user-products.ftl"
groovy freemarker-cli.groovy -t templates/json/md/customer-user-products.ftl  site/sample/json/customer-user-products.json > target/out/customer-user-products.md

echo "templates/json/md/github-users.ftl"
groovy freemarker-cli.groovy -t templates/json/md/github-users.ftl site/sample/json/github-users.json > target/out/github-users.md

echo "templates/properties/csv/locker-test-users.ftl"
groovy freemarker-cli.groovy -i **/*.properties -t templates/properties/csv/locker-test-users.ftl site/sample/properties > target/out/locker-test-users.csv

echo "templates/xml/txt/recipients.ftl"
groovy freemarker-cli.groovy -t ./templates/xml/txt/recipients.ftl site/sample/xml/recipients.xml > target/out/recipients.txt

echo "templates/csv/fo/transform.ftl"
groovy freemarker-cli.groovy -t templates/csv/fo/transform.ftl site/sample/csv/locker-test-users.csv > target/out/locker-test-users.fo