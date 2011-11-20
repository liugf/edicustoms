<#macro tcs attribute value='' >
<#if value?? && !empty(value)>
				<tcs:${attribute}>${value}</tcs:${attribute}>
<#else>
				<tcs:${attribute} xsi:nil="true" />
</#if>
</#macro>