<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/setting-taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<head>
	<%@ include file="/common/setting-iframe-meta.jsp"%>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"></script>
	
	
	<title>微米网配置</title>
	<script type="text/javascript">
	$(document).ready(function(){
		validateSave();
	});
	
	function validateSave(){
		$("#smsGatewaySettingFrom").validate({
			submitHandler: function() {
				ajaxSubmit("smsGatewaySettingFrom",webRoot+"/sms/sms-gateway-setting-saveConfig.htm", "smsGatewaySettingZone",saveCallback);
			},
			rules: {
				weimiId: "required",
				weimiPw: "required",
				smsSign: "required",
				maxTime: "required"
			},
			messages: {
				weimiId: iMatrixMessage["menuManager.required"],
				weimiPw: iMatrixMessage["menuManager.required"],
				smsSign: iMatrixMessage["menuManager.required"],
				maxTime: iMatrixMessage["menuManager.required"]
			}
		});
	}
	
	function saveCallback(){
		showMsg();
		validateSave();
	}
	//保存	
	function save(){
		 $("#smsGatewaySettingFrom").submit();

	}
	
	</script>
</head>
<body onload="">
<div class="ui-layout-center">
<div class="opt-body">
	<div class="opt-btn">
		<a class="btn" href="#" onclick="save();"><span><span><s:text name="menuManager.save"></s:text></span></span></a>
		<a class="btn" href="#" onclick="window.parent.$.colorbox.close();"><span><span><s:text name="interfaceManager.close"></s:text></span></span></a>
	</div>
	<div id="opt-content" >
		<aa:zone name="smsGatewaySettingZone">
			<div id="message" style="display: none;"><s:actionmessage theme="mytheme" /></div>
			
			<form action="" name="smsGatewaySettingFrom" id="smsGatewaySettingFrom" method="post">
				
				<input  type="hidden" name="id" id="id" value="${id}"/>
				<input  type="hidden" name="flag" id="flag" value="weimi"/>
				<input type="hidden" name="gatewayName" id="gatewayName" value="${gatewayName}"/>
				<table class="form-table-without-border">
					<tr>
						<td class="content-title"><s:text name="messagePlatform.smsCatUid"></s:text>：</td>
						<td> <input name="weimiId" id="weimiId" maxlength="30" value="${weimiId }"/> <span class="required">*</span></td>
						<td class="content-title" style="width: 130px;"><s:text name="messagePlatform.smsCatPassword"></s:text>：</td>
						<td> <input name="weimiPw" id="weimiPw" maxlength="100" value="${weimiPw }"/> <span class="required">*</span></td>
					</tr>
					
					<tr>
						<td class="content-title" style="width: 130px;"><s:text name="messagePlatform.smsSign"></s:text>：</td>
						<td> <input name="smsSign" id="smsSign" maxlength="30" value="${smsSign }"/> <span class="required">*</span></td>
						<td class="content-title"><s:text name="messagePlatform.maxSendNumber"></s:text>：</td>
						<td> <input name="maxTime" id="maxTime" maxlength="30" value="${maxTime }"/> <span class="required">*</span></td>
					</tr>
					
					<tr>
						<td class="content-title" style="width: 130px;"><s:text name="messagePlatform.shoufaSet"></s:text>：</td>
						<td> 
							<select name="sendReceiveStatus" id="sendReceiveStatus" >
							
								<s:iterator value="sendReceiveStatusList" var="var">
									<option value="<s:property value= '#var'/>" <s:if test="#var == smsGatewaySetting.sendReceiveStatus">selected="selected"</s:if>  >
										<s:if test="#var == @com.norteksoft.bs.sms.base.enumeration.SendReceiveStatus@ONLYSEND"><s:text name="messagePlatform.sendOnly"></s:text></s:if>
										<s:elseif test="#var == @com.norteksoft.bs.sms.base.enumeration.SendReceiveStatus@OBLYRECEIVE"><s:text name="messagePlatform.receiveOnly"></s:text></s:elseif>
										<s:elseif test="#var == @com.norteksoft.bs.sms.base.enumeration.SendReceiveStatus@SENDANDRECEIVE"><s:text name="messagePlatform.sendAndReceive"></s:text></s:elseif>
									</option>
								</s:iterator>
							
							</select>
						</td>
					</tr>
					
					
					
						
				</table>
			</form>
		</aa:zone>
	</div>
</div>
</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script src="${resourcesCtx}/widgets/timepicker/timepicker_<%=com.norteksoft.product.util.ContextUtils.getCurrentLanguage()%>.js" type="text/javascript"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
</html>