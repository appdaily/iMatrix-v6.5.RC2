<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/mms-taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<head>
	<title><s:text name="mms.formManager"/></title>
	<%@ include file="/common/mms-iframe-meta.jsp"%>
	
	<link href="${imatrixCtx}/widgets/formeditor/themes/default/default.css" rel="stylesheet" type="text/css" />
	<script src="${imatrixCtx}/widgets/formeditor/kindeditor.js" type="text/javascript"></script>
	<script src="${imatrixCtx}/widgets/formeditor/lang/zh_CN.js" type="text/javascript"></script>
	<script src="${imatrixCtx}/widgets/formeditor/formeditor.js" type="text/javascript"></script>
	<script src="${mmsCtx}/js/formControl.js" type="text/javascript"></script>
	<link href="${imatrixCtx}/widgets/formeditor/formeditor.css" rel="stylesheet" type="text/css" />
	
	<script type="text/javascript">
	function choiceControl(){
		if($("#tableColumnId").get(0).selectedIndex==0){
			$("#name").attr("value","");
		}
		ajaxAnyWhereSubmit("textForm", "", "controlContent");
	}
	
	function generateHtml(){
		var dataType = $("#dataType").val();
		var controlType=$("#controlType").attr("value") ;
		var isDateOrTime = false;
		if(dataType=="TIME"||dataType=="DATE"){
			isDateOrTime = true;
			if(controlType!="TEXT"){
				alert("日期或时间类型时只能用文本框!");
				return;
			}
		}
		var classStyle="";
		var styleContent="";
		var event="";
		if($("#classId").attr("value")!=""){
			classStyle=" class='"+$("#classId").attr("value")+"'";
		}
		if($("#styleId").attr("value")!=""){
			styleContent=" style='"+$("#styleId").attr("value")+"'";
		}
		if($("#eventId").attr("value")!=""){
			event=" onclick='"+$("#eventId").attr("value")+"'";
		}
		var html;
		if(isDateOrTime){
			html = "<input  pluginType=\"TEXT\" id=\""+$("#controlId").attr("value")+"\"" 
			        +" name=\""+ $("#name").attr("value")+"\"" 
			        +" title=\""+$("#title").attr("value")+"\""
			        +" dataType=\""+$("#dataType").attr("value")+"\""
			        +" format=\""+$("#format").attr("value")+"\""
					+" request=\""+$('#request').attr('checked')+"\""
					+classStyle
					+styleContent
					+" readOnly=\"readOnly\" value=\""+$("#controlValue").attr("value")+"\"";
		}else{
		   html ="<input  pluginType='TEXT'  type='"+controlType
				+"' id='"+$("#controlId").attr("value")
				+"' signatureVisible='"+$("#signatureVisible").attr('checked')
				+"' name='"+$("#name").attr("value") 
				+"' title='"+$("#title").attr("value")
				+"' dataType='"+$("#dataType").attr("value")
				+"' request='"+$('#request').attr('checked')
				+"' readOlny='"+$('#readOlny').attr('checked')
				+"' format='"+$("#format").attr("value")
				+"' formatType='"+($('#formatTypeenum').attr('checked')?"enum":"null")
				+"' formatTip='"+$('select#format option:selected').html()
				+"' value='"+$("#controlValue").attr("value")
				+"' maxlength='"+$("#maxLength").attr("value")+"'"
				+classStyle
				+styleContent
				+event;
		}
		if("${standard}"=="true"){
			if($("#tableColumnId").get(0).selectedIndex==0){
				alert("请选择对应字段");
				return;
			}else{
				html = html
					+" dbName='"+$("#dbName").attr("value")+"'";
			}
		}
		html=html+"/>";
		
		parent.html(html);
	}
	function controlTypeChange(value){
		if(value=='HIDDEN'){
			$("#validateArea").hide();
		}else{
			$("#validateArea").show();
		}
		if(value=='RADIO'||value=='CHECKBOX'){
			$(".formatTr").hide();
		}else{
			$(".formatTr").show();
		}

	}
	function formatTypeChange(){
		if($('#formatTypeenum').attr('checked') || $('#formatTypeenum').attr('checked')=='true'){
			$('#format').removeAttr('disabled');
		}else{
			$('#format').attr('disabled',"disabled");
		}
	}
	//更换数据类型时
	function changeDataType(){
		var dataType = $("#dataType").val();
		var value = $("#controlValue").val();
			var valParent = $("#controlValue").parent();
		if(dataType=="TIME"){
			$("#controlValue").remove();
			$("#controlType").removeAttr("disabled");
			$(valParent).append('<input name="formControl.controlValue" id="controlValue" readonly="readonly" value=""></input>');
				$("#controlValue").datetimepicker({
				  "dateFormat":'yy-mm-dd',
				   changeMonth:true,
				   changeYear:true,
				   showSecond: false,
					showMillisec: false,
					"timeFormat": 'hh:mm'
			   });
		}else if(dataType=="DATE"){
			$("#controlValue").remove();
			$("#controlType").removeAttr("disabled");
			$(valParent).append('<input name="formControl.controlValue" id="controlValue" readonly="readonly" value=""></input>');
			$("#controlValue").datepicker({
				  "dateFormat":'yy-mm-dd',
				  changeMonth:true,
				  changeYear:true,
				  showButtonPanel:"true"
			   });
		}else if(dataType=="BOOLEAN"){
			$("#controlValue").remove();
			if("mysql"=="${dataBase}"){
				$(valParent).append('<select name="formControl.controlValue" id="controlValue"><option  value=true>是</option><option  value=false>否</option></select>');
			}else {
				$(valParent).append('<select name="formControl.controlValue" id="controlValue"><option  value=1>是</option><option  value=0>否</option></select>');
			}
			
			$("#controlType").attr("value","RADIO");
			$("#controlType").attr("disabled","disabled");
		}else{
			$("#controlValue").remove();
			$("#controlType").removeAttr("disabled");
			$(valParent).append('<input name="formControl.controlValue" id="controlValue" value=""></input>');
		}
		defautMaxlengthSet();
	}
	function validateBoolean(obj){
		var v=obj.value;
		if($.trim(v)!='0'&&$.trim(v)!='1'){
			$(obj).attr("value","");
		}
	}
	</script>
	<style type="text/css">
	.form-table-without-border td input{
		width:200px;
	}
	</style>
</head>
<body onload="getContentHeight();">
<div class="ui-layout-center">
<div class="opt-body">
	<div class="opt-btn">
		<button class="btn" onclick="$('#textForm').submit();"><span><span><s:text name="menuManager.confirm"></s:text></span></span></button>
		<button class="btn" onclick='parent.$.colorbox.close();'><span><span ><s:text name="cancel"></s:text></span></span></button>
	</div>
	<div id="opt-content">
		<aa:zone name="controlContent">
			<form name="textForm" id="textForm" action="${mmsCtx }/form/form-view-text.htm">
				<s:hidden name="id"></s:hidden>
				<s:hidden id="code" name="code"></s:hidden>
				<s:hidden id="version" name="version"></s:hidden>
				<s:hidden id="standard" name="standard"></s:hidden>
				<s:hidden id="occasion" name="occasion" value="changeSource"></s:hidden>
				
				<fieldset style="border: #f0f0ee solid 1px; padding: 3px;">
					<legend style="margin: 0 10px; color: #0046d5;">&nbsp;<s:text name="formManager.fieldDescribe"></s:text>&nbsp;</legend>
					<table class="form-table-without-border">
					<tbody>
						<tr>
							<td class="content-title"><s:text name="formManager.type"></s:text>：</td>
							<td>
							 <s:if test="standard">
								<s:select theme="simple" id="controlType" onchange="controlTypeChange(this.value)" list="#{'TEXT':getText('formManager.textbox'),'PASSWORD':getText('formManager.passwordbox'),'HIDDEN':getText('formManager.hiddenbox')}" 
								 name="formControl.controlType"></s:select>
							 </s:if><s:else>
							 	<s:if test='formControl.dataType.code=="布尔型"'>
									<select name="formControl.controlType" disabled="disabled" id="controlType" onchange="controlTypeChange(this.value)" >
										<option <s:if test="formControl.controlType.code=='文本框'">selected="selected"</s:if> value="TEXT"><s:text name="formManager.textbox"></s:text></option>
										<option <s:if test="formControl.controlType.code=='密码框'">selected="selected"</s:if> value="PASSWORD"><s:text name="formManager.passwordbox"></s:text></option>
										<option <s:if test="formControl.controlType.code=='隐藏域'">selected="selected"</s:if> value="HIDDEN"><s:text name="formManager.hiddenbox"></s:text></option>
										<!-- 
										<option <s:if test="formControl.controlType.code=='单选框'">selected="selected"</s:if> value="RADIO">单选框</option>
										<option <s:if test="formControl.controlType.code=='复选框'">selected="selected"</s:if> value="CHECKBOX">复选框</option>
										 -->
									</select>
								</s:if><s:else>
									<s:select theme="simple" id="controlType" onchange="controlTypeChange(this.value)" list="#{'TEXT':getText('formManager.textbox'),'PASSWORD':getText('formManager.passwordbox'),'HIDDEN':getText('formManager.hiddenbox')}" 
									 name="formControl.controlType"></s:select>
								</s:else>
							 </s:else>
							</td>
							<td>
							</td>	
						</tr>	
						<s:if test="standard">
							<tr>
								<td class="content-title"><s:text name="formManager.fieldRelative"></s:text>：</td>
								<td>
								<s:hidden id="dataType" name="tableColumn.dataType.enumName"></s:hidden>
									<s:select onchange="choiceControl();" id="tableColumnId" name="tableColumnId" list="columns" theme="simple" listKey="id" listValue="alias" headerKey="0" headerValue="请选择"></s:select>
								</td>
								<td></td>	
							</tr>
							<tr>
								<td class="content-title"><s:text name="formManager.fieldName"></s:text>：</td>
								<td>
									<s:if test="tableColumnId==null||tableColumnId==0">
										<input id="name" name="formControl.name" class="{required:true,messages: {required:'<s:text name="required"></s:text>'}}" value="${formControl.name }"/><span class="required">*</span>
									</s:if>
									<s:else>
										<input id="name" name="formControl.name" class="{required:true,messages: {required:'<s:text name="required"></s:text>'}}" value="${formControl.name }" readonly="readonly"/><span class="required">*</span>
									</s:else>
									<s:hidden  theme="simple" name="formControl.dbName" id="dbName"/>
									<s:hidden  theme="simple" name="formControl.dataType" id="dataType"/>
								</td>
								<td>
									<span id="nameTip"></span>
								</td>	
							</tr>
						</s:if>
						<s:else>
							<tr>
								<td class="content-title"><s:text name="formManager.fieldName"></s:text>：</td>
								<td>
								<input id="name" name="formControl.name" class="{required:true,messages: {required:'<s:text name="required"></s:text>'}}" value="${formControl.name }" onblur="fieldNameOk(this);" maxlength="27"/><span class="required">*</span>
								</td>
								<td>
									<span id="nameTip"></span>
								</td>	
							</tr>
							<tr>	
								<td class="content-title"><s:text name="formManager.fieldType"></s:text>：</td>	
								<td>
									<s:select theme="simple" id="dataType" list="#{'TEXT':getText('formManager.text'),'DATE':getText('formManager.date'),'TIME':getText('formManager.time'),'INTEGER':getText('formManager.integer'),'LONG':getText('formManager.long'),'DOUBLE':getText('formManager.double'),'FLOAT':getText('formManager.float'),'CLOB':getText('formManager.longText')}" 
							 name="formControl.dataType" onchange="changeDataType();"></s:select>
								</td>
							</tr>
						</s:else>
						<tr>
							<td class="content-title"><s:text name="formManager.fieldAlias"></s:text>：</td>
							<td>
							<input id="title" name="formControl.title" class="{required:true,messages: {required:'<s:text name="required"></s:text>'}}" value="${formControl.title }"/><span class="required">*</span>
							<input id="controlId" type="hidden" name="formControl.controlId" class="" value="${formControl.controlId }"/>
							</td>
							<td><span id="titleTip"></span></td>	
						</tr>
						<tr>
							<td class="content-title"><s:text name="formManager.signature"></s:text>：</td>
							<td>
								<input id="signatureVisible" type="checkbox"  name="formControl.signatureVisible"<s:if test="formControl.signatureVisible">checked="checked"</s:if>>  <s:text name="common.yes"></s:text>
							</td>
							<td></td>		
						</tr>	
						<tr>
						<s:if test="standard">
							<s:if test="tableColumn.dataType.enumName=='BOOLEAN'">
								<td><s:text name="formManager.checkedValue"></s:text>:
								</td><td>
								<s:select theme="simple" id="controlValue" name="formControl.controlValue"
											list="#{'true':getText('common.yes'),'false':getText('common.no')}"></s:select></td>
							</s:if><s:else>
								<td class="content-title"><s:text name="formManager.defaultValue"></s:text>：</td>
								<td>
								<s:if test="tableColumn.dataType.enumName=='TIME'">
									<script type="text/javascript">
									$(function(){
										$("#controlValue").datetimepicker({
										  "dateFormat":'yy-mm-dd',
										   changeMonth:true,
										   changeYear:true,
										   showSecond: false,
											showMillisec: false,
											"timeFormat": 'hh:mm'
									   });
									});
									</script>
									<input name="formControl.controlValue" id="controlValue" readonly="readonly" value="${formControl.controlValue }"></input>
								</s:if>
								<s:elseif test="tableColumn.dataType.enumName=='DATE'">
								<script type="text/javascript">
									$(function(){
										$("#controlValue").datepicker({
										  "dateFormat":'yy-mm-dd',
										  changeMonth:true,
										  changeYear:true,
										  showButtonPanel:"true"
									   });
									});
									</script>
									<input name="formControl.controlValue" id="controlValue" readonly="readonly" value="${formControl.controlValue }"></input>
								</s:elseif>
								<s:else>
									<s:textfield theme="simple" id="controlValue" name="formControl.controlValue" ></s:textfield>
								</s:else>	
								</td>
								<td><span id="controlValueTip"></span></td>	
							</s:else>
						</s:if><s:else>
							<td class="content-title"><s:text name="formManager.defaultValue"></s:text>：</td>
							<td>
							<s:if test="formControl.dataType.enumName=='TIME'">
								<script type="text/javascript">
								$(function(){
									$("#controlValue").datetimepicker({
									  "dateFormat":'yy-mm-dd',
									   changeMonth:true,
									   changeYear:true,
									   showSecond: false,
										showMillisec: false,
										"timeFormat": 'hh:mm'
								   });
								});
								</script>
								<input name="formControl.controlValue" id="controlValue" readonly="readonly" value="${formControl.controlValue }"></input>
							</s:if>
							<s:elseif test="formControl.dataType.enumName=='DATE'">
							<script type="text/javascript">
								$(function(){
									$("#controlValue").datepicker({
									  "dateFormat":'yy-mm-dd',
									  changeMonth:true,
									  changeYear:true,
									  showButtonPanel:"true"
								   });
								});
								</script>
								<input name="formControl.controlValue" id="controlValue" readonly="readonly" value="${formControl.controlValue }"></input>
							</s:elseif>
							<s:elseif test='formControl.dataType.code=="布尔型"'>
								<s:if test="dataBase=='mysql'">
									<s:select theme="simple" id="controlValue" name="formControl.controlValue"
											list="#{'true':getText('common.yes'),'false':getText('common.no')}"></s:select>
								</s:if>
								<s:else>
									<s:select theme="simple" id="controlValue" name="formControl.controlValue"
											list="#{'1':getText('common.yes'),'0':getText('common.no')}"></s:select>
								</s:else>
							</s:elseif>
							<s:else>
								<input name="formControl.controlValue" id="controlValue" value="${formControl.controlValue }"></input>
							</s:else>	
							</td>
							<td><span id="controlValueTip"></span></td>	
						</s:else>
						</tr>
						<tr>
							<td class="content-title"><s:text name="formManager.styleClass"></s:text>：</td>
							<td>
								<s:textfield theme="simple" id="classId" name="formControl.classStyle"></s:textfield>
								<br/><s:text name="formManager.attention"></s:text>
							</td>
							<td></td>	
						</tr>
						<tr>
							<td class="content-title"><s:text name="formManager.inlineStyle"></s:text>：</td>
							<td>
								<s:textfield theme="simple" id="styleId" name="formControl.styleContent"></s:textfield>
								<br/><s:text name="formManager.example"></s:text>：color: red;
							</td>
							<td></td>	
						</tr>	
						<tr>
							<td class="content-title"><s:text name="formManager.controlEvent"></s:text>：</td>
							<td>
								<s:textfield theme="simple" id="eventId" name="formControl.clickEvent" ></s:textfield>
							</td>
							<td></td>	
						</tr>	
					</tbody>
				</table>
				</fieldset>
				<fieldset style="border: #f0f0ee solid 1px; padding: 3px;margin: 8px 0;display:none;" id="validateArea">
					<legend style="margin: 0 10px; color: #0046d5;">&nbsp;<s:text name="formManager.fieldValidate"></s:text>&nbsp;</legend>
					<input id="maxLengthExist" value="${formControl.maxLength }" type="hidden"/>
					<table class="form-table-without-border">
						<tbody>
							<s:if test="standard">
								<tr>
									<td class="content-title"><s:text name="formManager.maxLenth"></s:text>：</td>
									<td>
										<s:if test="tableColumnId==null||tableColumnId==0">
											<s:textfield theme="simple" id="maxLength" name="formControl.maxLength" onkeyup="value=this.value.replace(/[^0-9]/,'');"></s:textfield>
										</s:if><s:else>
											<s:textfield theme="simple" id="maxLength" name="formControl.maxLength" onkeyup="value=this.value.replace(/[^0-9]/,'');" readonly="true"></s:textfield>
										</s:else>
									</td>
								</tr>
							</s:if><s:else>
								<tr>
									<td class="content-title"><s:text name="formManager.maxLenth"></s:text>：</td>
									<td>
										<s:textfield theme="simple" id="maxLength" name="formControl.maxLength" onkeyup="value=this.value.replace(/[^0-9]/,'');"></s:textfield>
									</td>
								</tr>
							</s:else>
							<tr>
								<td class="content-title"><s:text name="formManager.isRequired"></s:text>：</td>
								<td>
									<input id="request" type="checkbox" name="formControl.request"<s:if test="formControl.request">checked="checked"</s:if>>  <s:text name="common.yes"></s:text>
								</td>
								<td></td>	
							</tr>
							
							<tr class="formatTr" style="display: none;">
								<td class="content-title"><s:text name="formManager.formatValidate"></s:text>：</td>
								<td>
									<s:radio onclick="formatTypeChange();" theme="simple" id="formatType" name="formControl.formatType" list="#{'null':getText('formManager.noValidate'),'enum':getText('formManager.validate')}"></s:radio>
								</td>
								<td></td>	
							</tr>
							<tr class="formatTr" style="display:none;">
								<td class="content-title"><s:text name="formManager.formatType"></s:text>：</td>
								<td>
									<s:select theme="simple" id="format" name="formControl.format"
										list="#{'number':getText('formManager.digit'),'email':getText('formManager.emailAddress')}"></s:select>
								</td>
								<td></td>	
							</tr>
							<tr style="display:none;">
								<td class="content-title"><s:text name="formManager.formatInstructio"></s:text>：</td>
								<td>
									<s:textfield  theme="simple" id="formatTip" name="formControl.formatTip"></s:textfield>
								</td>
								<td></td>	
							</tr>
							<tr>
								<td class="content-title"><s:text name="formManager.isRead"></s:text>：</td>
								<td>
									<input id="readOlny" type="checkbox" name="formControl.readOlny" value="true" <s:if test="formControl.readOlny">checked="checked"</s:if>>  <s:text name="common.yes"></s:text>
								</td>
								<td></td>
							</tr>		
						</tbody>
					</table>
				</fieldset>
				
			</form>
				<script type="text/javascript">
				function validateText(){
					$("#textForm").validate({
						submitHandler: function() {
							generateHtml();
						}
					});
				}
				$(function(){
					validateText();
					formatTypeChange();
				});
				</script>
				<s:if test="formControl.controlType.enumName!='RADIO'&&formControl.controlType.enumName!='CHECKBOX'">
					<script type="text/javascript">
					$(function(){$(".formatTr").show();});
					</script>
				</s:if>
				<s:if test="formControl.controlType.enumName!='HIDDEN'">
					<script type="text/javascript">
					$(function(){
						$("#validateArea").show();
					});
					</script>
				</s:if>
		</aa:zone>
	</div>
</div>
</div>
</body>
<script src="${resourcesCtx}/widgets/timepicker/timepicker_<%=com.norteksoft.product.util.ContextUtils.getCurrentLanguage()%>.js" type="text/javascript"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"></script>
</html>
