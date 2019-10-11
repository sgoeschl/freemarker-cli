<#ftl output_format="plainText" >
Manual Test
---------------------------------------------------------------------------
<#assign smallNumber = 3.1415927>
<#assign largeNumber = 99999.99>

Small Number :  ${smallNumber}
Large Number :  ${largeNumber}
Date         :  ${.now?date}
Time         :  ${.now?time}
