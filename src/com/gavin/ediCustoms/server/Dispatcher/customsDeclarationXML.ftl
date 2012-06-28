<#include "tcs.ftl" />
<?xml version="1.0" encoding="UTF-8"?>
<TCS101Message>
  <MessageHead>
    <MessageType>001</MessageType>
    <MessageId>${messageId!""}</MessageId>
    <MessageTime>${messageTime!""}</MessageTime>
    <SenderId>${enterprise.jichengtongId!""}</SenderId>
    <SenderAddress>${enterprise.jichengtongId!""}@${enterprise.quyujiedian}</SenderAddress>
    <ReceiverId>T99999999999</ReceiverId>
    <ReceiverAddress>T99999999999@TCS10001</ReceiverAddress>
  </MessageHead>
  <MessageBody>
    <tcs:TcsFlow201 schemaLocation="http://www.chinaport.gov.cn/tcs/v2 TcsWorkFlow.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:tcs="http://www.chinaport.gov.cn/tcs/v2">
      <tcs:TcsUser>
      	<@tcs attribute="UserId" value=userId />
      	<@tcs attribute="UserPrivateKey" value=userPrivateKey />
      </tcs:TcsUser>
      <tcs:TcsLink>
        <tcs:TcsLinkMan>
          <@tcs attribute="Name" value=enterprise.contact />
          <tcs:Department></tcs:Department>
          <tcs:Duty xsi:nil="true" />
          <@tcs attribute="Telephone" value=enterprise.telephone />
          <tcs:Mobile xsi:nil="true" />
          <@tcs attribute="Fax" value=enterprise.fax />
          <@tcs attribute="Address" value=enterprise.address />
          <tcs:ZipCode>1</tcs:ZipCode>
          <tcs:Email xsi:nil="true" />
          <tcs:ImCode>1</tcs:ImCode>
          <tcs:ImType>1</tcs:ImType>
        </tcs:TcsLinkMan>
      </tcs:TcsLink>
      <tcs:TcsFlow>
        <@tcs attribute="MessageId" value=messageId />
        <@tcs attribute="BpNo" value=bpNo />
        <tcs:ActionList>
          <tcs:ActionId>a01</tcs:ActionId>
        </tcs:ActionList>
        <@tcs attribute="TaskId" value=taskId />
        <tcs:TaskNote>黄埔-001</tcs:TaskNote>
        <tcs:CorpTaskId>
        </tcs:CorpTaskId>
        <tcs:TaskControl>
        </tcs:TaskControl>
      </tcs:TcsFlow>
      <tcs:TcsData>
        <tcs:DeclarationDocument xsi:schemaLocation="http://www.chinaport.gov.cn/tcs/v2" xmlns:tcs="http://www.chinaport.gov.cn/tcs/v2">
          <@tcs attribute="TcsDocumentNo" value=tcsDocumentNo />
          <tcs:DataCooperationType>001</tcs:DataCooperationType>
          <tcs:EntryInformation>
            <tcs:EntryIdentityInformationList>
              <tcs:EntryIdentityInformation>
                <tcs:EntryIdentity>001</tcs:EntryIdentity>
                <@tcs attribute="CorporationCustomsCode" value=runEnterprise.tradeCode />
                <@tcs attribute="CorporationName" value=runEnterprise.registeName />
              </tcs:EntryIdentityInformation>
              <tcs:EntryIdentityInformation>
                <tcs:EntryIdentity>002</tcs:EntryIdentity>
                <@tcs attribute="CorporationCustomsCode" value=enterprise.tradeCode />
                <@tcs attribute="CorporationName" value=enterprise.registeName />
              </tcs:EntryIdentityInformation>
              <tcs:EntryIdentityInformation>
                <tcs:EntryIdentity>003</tcs:EntryIdentity>
                <@tcs attribute="CorporationCustomsCode" value=declareEnterprise.tradeCode />
                <@tcs attribute="CorporationName" value=declareEnterprise.registeName />
              </tcs:EntryIdentityInformation>
              <tcs:EntryIdentityInformation>
                <tcs:EntryIdentity>004</tcs:EntryIdentity>
                <@tcs attribute="CorporationCustomsCode" value="744483308" />
                <@tcs attribute="CorporationName" value="东莞市新泽报关咨询服务有限公司" />
              </tcs:EntryIdentityInformation>
            </tcs:EntryIdentityInformationList>
            <tcs:LogisticsLocationInformationList>
              <tcs:LogisticsLocationInformation>
                <tcs:LogisticsLocationType>004</tcs:LogisticsLocationType>
                <@tcs attribute="LogisticsLocationCode" value=customsDeclarationHead.destinationPort />
              </tcs:LogisticsLocationInformation>
              <tcs:LogisticsLocationInformation>
                <tcs:LogisticsLocationType>012</tcs:LogisticsLocationType>
                <@tcs attribute="LogisticsLocationCode" value=customsDeclarationHead.tradeCountry />
              </tcs:LogisticsLocationInformation>
            </tcs:LogisticsLocationInformationList>
            <tcs:EportLocationInformationList>
              <tcs:EportLocationInformation>
                <tcs:EportLocationTypeCode>001</tcs:EportLocationTypeCode>
                <@tcs attribute="EportLocationCode" value=loadPortCustoms />
              </tcs:EportLocationInformation>
              <tcs:EportLocationInformation>
                <tcs:EportLocationTypeCode>003</tcs:EportLocationTypeCode>
                <@tcs attribute="EportLocationCode" value=customsDeclarationHead.iePort />
              </tcs:EportLocationInformation>
            </tcs:EportLocationInformationList>
            <tcs:BaseInformation>
              <tcs:PreentryNo xsi:nil="true" />
              <tcs:EntryType>001</tcs:EntryType>
              <tcs:EntryTransitType>003</tcs:EntryTransitType>
              <@tcs attribute="DeclareProperty" value=customsDeclarationHead.declareProperty />
              <tcs:TaxCorporationType>002</tcs:TaxCorporationType>
              <@tcs attribute="DestinationCode" value=customsDeclarationHead.ownerDistrict />
              <tcs:WarehouseNo xsi:nil="true" />
              <@tcs attribute="CYNo" value=customsDeclarationHead.loadPort />
              <@tcs attribute="ContractNo" value=customsDeclarationHead.contractNo />
              <#if !isNomalTrade>
              <@tcs attribute="EnrolNo" value=manualNo />
              <#else>
              <tcs:EnrolNo xsi:nil="true" />
              </#if>
              
              <@tcs attribute="ApprovalNo" value=customsDeclarationHead.certificationCode />
              <tcs:LicenseNo xsi:nil="true" />
              <tcs:RelativeEntryNo xsi:nil="true" />
              <tcs:RelativeEnrolNo xsi:nil="true" />
              <@tcs attribute="MeansOfTransportName" value=customsDeclarationHead.transportTool />
              <tcs:MeansOfTransportId xsi:nil="true" />
              <@tcs attribute="BillOfLadingNo" value=customsDeclarationHead.billNo />
              <@tcs attribute="PackingType" value=customsDeclarationHead.wrapType />
              <@tcs attribute="MeansOfTransportMode" value=customsDeclarationHead.transportMode />
              <@tcs attribute="TransactionMode" value=customsDeclarationHead.dealMode />
              <@tcs attribute="TradeMode" value=customsDeclarationHead.tradeMode />
              <@tcs attribute="CutMode" value=customsDeclarationHead.taxKind />
              <#if customsDeclarationHead.isExport >
              <tcs:ImportExportFlag>E</tcs:ImportExportFlag>
              <#else>
              <tcs:ImportExportFlag>I</tcs:ImportExportFlag>
              </#if>
              
              <@tcs attribute="Packages" value=customsDeclarationHead.packNo />
              <@tcs attribute="NetWeight" value=customsDeclarationHead.netWeight?string("0.####") />
              <@tcs attribute="GrossWeight" value=customsDeclarationHead.grossWeight?string("0.####") />
              <tcs:FreightMark xsi:nil="true" />
              <@tcs attribute="FreightRate" value=customsDeclarationHead.feeRate />
              <tcs:FreightCurrency xsi:nil="true" />
              <tcs:InsuranceMark xsi:nil="true" />
              <@tcs attribute="InsuranceRate" value=customsDeclarationHead.insurRate />
              <tcs:InsuranceCurrency xsi:nil="true" />
              <tcs:ExtrasMark xsi:nil="true" />
              <@tcs attribute="ExtrasRate" value=customsDeclarationHead.otherRate />
              <tcs:ExtrasCurrency xsi:nil="true" />
              <@tcs attribute="PayMode" value=customsDeclarationHead.payWay />
              <tcs:SaleDomesticRatio xsi:nil="true" />
              <@tcs attribute="ImportExportDate" value=(customsDeclarationHead.importExportDate?string("yyyy-MM-dd"))!"" />
              <@tcs attribute="TransportDate" value=transportDate?string("yyyy-MM-dd") />
              <@tcs attribute="Note" value=note />
              <tcs:DeclarantID xsi:nil="true" />
              <tcs:DeclarantTelephone xsi:nil="true" />
              <tcs:EntrydeclarantNo xsi:nil="true" />
              <@tcs attribute="ICCardNo" value=enterprise.icCardNo />
              <@tcs attribute="Name" value=customsDeclarationHead.declarant />
            </tcs:BaseInformation>
            <tcs:GoodsInformationList>
            <#list customsDeclarationGoods as customsDeclarationGood>	
              <tcs:GoodsInformation>
                <@tcs attribute="No" value=customsDeclarationGood.no />
                <@tcs attribute="HsCode" value=customsDeclarationGood.codeTS />
                <tcs:MaterialNo xsi:nil="true" />
                <@tcs attribute="GoodsName" value=customsDeclarationGood.name />
                <tcs:EnglishName xsi:nil="true" />
                <@tcs attribute="Model" value=customsDeclarationGood.shenbaoguifan />
                <#if !isNomalTrade>
                <@tcs attribute="EnrolNo" value=customsDeclarationGood.noInContract />
                <#else>
	            <tcs:EnrolNo xsi:nil="true" />
	            </#if>
                <@tcs attribute="Quantity" value=customsDeclarationGood.quantity?string("0.###") />                
                <@tcs attribute="QuantityUnit" value=customsDeclarationGood.declareUnit />
                <tcs:UnitPrice>#{customsDeclarationGood.declarePrice!"";m4M4}</tcs:UnitPrice>
                <tcs:TotalPrice>#{customsDeclarationGood.totalPrice!"";m2M2}</tcs:TotalPrice>
                <@tcs attribute="Currency" value=customsDeclarationGood.currency />
                <@tcs attribute="FirstQuantity" value=customsDeclarationGood.quantity1?string("0.###") />
                <@tcs attribute="FirstUnit" value=customsDeclarationGood.unit1 />
                <#if customsDeclarationGood.quantity2!=0 >
                <@tcs attribute="SecondQuantity" value=customsDeclarationGood.quantity2?string("0.###") />
				<@tcs attribute="SecondUnit" value=customsDeclarationGood.unit2 />
                <#else>
                <tcs:SecondQuantity xsi:nil="true" />
                <tcs:SecondUnit xsi:nil="true" />
                </#if>
                <@tcs attribute="OriginCode" value=customsDeclarationGood.originCountry />
                <@tcs attribute="Use" value=customsDeclarationHead.useage />
                <@tcs attribute="DutyMode" value=customsDeclarationGood.taxMode />
                <tcs:ProcessingCharges xsi:nil="true" />
                <@tcs attribute="GoodsVersion" value=customsDeclarationGood.goodVersion />
                <tcs:ClassificationMark xsi:nil="true" />
                <tcs:Note xsi:nil="true" />
              </tcs:GoodsInformation>
            </#list>
            </tcs:GoodsInformationList>            
            <#if (containers?size>0)>
            <tcs:EntryContainerInformationList>
           	<#list containers as container>
              <tcs:EntryContainerInformation>
	              <@tcs attribute="ContainerNo" value=container.containerNo />
	              <@tcs attribute="ContainerSize" value=container.size />
	              <@tcs attribute="ContainerWeight" value=container.weight?string("0.####") />
              </tcs:EntryContainerInformation>
            </#list>
            </tcs:EntryContainerInformationList> 
            </#if>
            <#if (attachments?size>0)>
            <tcs:DocumentAttachedInformationList>
           	<#list attachments?keys as key>
              <tcs:DocumentAttachedInformation>
	              <@tcs attribute="DocumentAttachedCode" value=key />
	              <@tcs attribute="DocumentAttachedNo" value=attachments[key] />
              </tcs:DocumentAttachedInformation>
            </#list>
            </tcs:DocumentAttachedInformationList> 
            </#if>        
          </tcs:EntryInformation>
          
          
          <tcs:TransitInformation>
          	<tcs:TransitBaseInformation>
          		<@tcs attribute="WaybillNo" value=customsDeclarationHead.waybillNo />
          		<@tcs attribute="MeansOfTransportMode" value=customsDeclarationHead.localMeansOfTransportMode />
          		<@tcs attribute="MeansOfTransportCode" value=customsDeclarationHead.localMeansOfTransportCode />
          		<@tcs attribute="MeansOfTransportName" value=customsDeclarationHead.localMeansOfTransportName />
          		<@tcs attribute="MeansOfTransportId" value=customsDeclarationHead.localMeansOfTransportId />
          		<tcs:CorporationName>${(customsDeclarationHead.corporationName?trim)!enterprise.registeName}</tcs:CorporationName>
          		<tcs:OrganizationCode>${(customsDeclarationHead.organizationCode?trim)!enterprise.ownerCode}</tcs:OrganizationCode>
          	</tcs:TransitBaseInformation>
          	<tcs:BillOfLadingInformation>
          		<@tcs attribute="BillOfLadingNo" value=customsDeclarationHead.billOfLadingNo />
          		<@tcs attribute="MeansOfTransportCode" value=customsDeclarationHead.meansOfTransportCode />
          		<@tcs attribute="MeansOfTransportName" value=customsDeclarationHead.meansOfTransportName />
          		<@tcs attribute="MeansOfTransportId" value=customsDeclarationHead.meansOfTransportId />
          	</tcs:BillOfLadingInformation>
          	<tcs:TransitContainerInformationList>
          		<#list containers as container>	
	            <tcs:TransitContainerInformation>
	            	<@tcs attribute="ContainerNo" value=container.containerNo />
	            	<@tcs attribute="No" value=container_index+1 />
	            	<@tcs attribute="ContainerSize" value=container.size />
	            	<@tcs attribute="MeansOfTransportName" value=customsDeclarationHead.localMeansOfTransportName />
                  	<@tcs attribute="MeansOfTransportWeight" value=container.bracketWeight?string("0.####") />
            	</tcs:TransitContainerInformation>
            	</#list>
            </tcs:TransitContainerInformationList>
          </tcs:TransitInformation>
          
          
          <tcs:SignInformation>
            <@tcs attribute="IccardNo" value=enterprise.icCardNo />
            <@tcs attribute="Name" value=customsDeclarationHead.declarant />
            <@tcs attribute="OrganizationCode" value=enterprise.ownerCode />
            <tcs:SignInformation>A001</tcs:SignInformation>
            <@tcs attribute="SignDate" value=SignDate />
            <@tcs attribute="CertificateNo" value=enterprise.certificateNo />
          </tcs:SignInformation>
        </tcs:DeclarationDocument>
      </tcs:TcsData>
    </tcs:TcsFlow201>
  </MessageBody>
</TCS101Message>


