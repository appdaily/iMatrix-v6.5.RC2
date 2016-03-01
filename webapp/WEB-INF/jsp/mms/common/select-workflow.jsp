<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/mms-taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<head>
	<title><s:text name="wf.engine.delegate.selectionProcess"></s:text></title>
	<%@ include file="/common/mms-iframe-meta.jsp"%>
	<script type="text/javascript">
		function selectedOk(){
			var selectCount = $("input:checked[name='workflowId']").length;
			if(selectCount == 1){
				var obj = $("input:checked[name='workflowId']");
				parent.$('#_wfDefId').attr('value', $(obj).attr('value'));
				parent.$('#ids').remove();//ids文本框影响了流程的正常发起，所以要将它移除
				parent.afterSelectWorkflow();
				parent.$.colorbox.close();
			}else{
				show_message('message', iMatrixMessage["wf.engine.selectProcess"]);
			}
		}
		function show_message(id, msg, clazz){
			if(msg != ""){
				$("#"+id).html(msg);
				$("#"+id).attr('class', clazz);
			}
			$("#"+id).show("show");
			setTimeout('$("#'+id+'").hide("show");',3000);
		}
	</script>
</head>
<body  style="padding: 5px;" onload="getContentHeight();">
<div class="ui-layout-center">
	<div class="opt-body">
		<div class="opt-btn">
			<button onclick="selectedOk();" ><span><span><s:text name="menuManager.confirm"></s:text></span></span></button>
			<button onclick="parent.noWorkflowSelected();"><span><span><s:text name="cancel"/></span></span></button>
		</div>
		<div id="opt-content">
		<div id="message" style="display:none;"></div>
		<table class="form-table-border-left">
			<thead>
				<tr>
					<th style="width: 30px;"></th>
					<th><s:text name="wf.dictionary.processName"></s:text></th>
					<th><s:text name="wf.table.processVersion"/></th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="workflows">
				<tr>
					<td style="width: 30px;text-align: center;"> <input name="workflowId" type="radio" value="${processId}" /> </td>
					<td> ${name} </td>
					<td> ${version} </td>
				</tr>
				</s:iterator>
			</tbody>
		</table>
		</div>
	</div>
</div>	
</body>
</html>
