<#ftl output_format="XML" >
<#assign name = documents[0].name>
<#assign content = documents[0].content>
<#assign cvsFormat = CSVFormat.DEFAULT.withHeader()>
<#assign csvParser = CSVParser.parse(content, cvsFormat)>
<#assign csvHeaders = csvParser.getHeaderMap()?keys>
<#assign csvRecords = csvParser.records>
<#--------------------------------------------------------------------------->
<?xml version="1.0" encoding="UTF-8"?>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <fo:layout-master-set>
        <fo:simple-page-master master-name="first"
                               page-height="21cm"
                               page-width="29.7cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="1cm"
                               margin-right="1cm">
            <fo:region-body margin-top="1cm"/>
            <fo:region-before extent="1cm"/>
            <fo:region-after extent="1cm"/>
        </fo:simple-page-master>
    </fo:layout-master-set>
    <fo:page-sequence master-reference="first">
        <fo:flow flow-name="xsl-region-body">
            <fo:table table-layout="fixed" width="100%" border-collapse="separate">
                <@writeTableHeader headers=csvHeaders/>
                <@writeTableBody columns=csvRecords/>
            </fo:table>
        </fo:flow>
    </fo:page-sequence>
</fo:root>

<#--------------------------------------------------------------------------->
<#macro writeTableHeader headers>
    <fo:table-header>
        <fo:table-row>
            <#list headers as header>
                <fo:table-cell>
                    <fo:block font-weight="bold">${header}</fo:block>
                </fo:table-cell>
            </#list>
        </fo:table-row>
    </fo:table-header>
</#macro>

<#--------------------------------------------------------------------------->
<#macro writeTableBody columns>
    <fo:table-body>
        <#list columns as column>
            <fo:table-row>
                <#list column.iterator() as field>
                    <fo:table-cell>
                        <fo:block>${field}</fo:block>
                    </fo:table-cell>
                </#list>
            </fo:table-row>
        </#list>
    </fo:table-body>
</#macro>
