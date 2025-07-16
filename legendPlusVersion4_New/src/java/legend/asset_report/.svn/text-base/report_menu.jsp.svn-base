<jsp:include page="systemHeader.jsp" flush="true"/>

<html>

<head>
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Magma.net | Department Setup</title>

<link href="scripts/styles.css" type="text/css" rel="stylesheet">

<script>
	function exitPage(){
		window.location ='';
		return true;
	}
</script>
<script>
 function filterCriteria(form)
 {
  var result = "";
  if((form.region.value == "***")&&(form.branch.value == "***")&&(form.category.value == "***"))
  {
   result = "";
  }
  if((form.region.value != "***")&&(form.branch.value == "***")&&(form.category.value == "***"))
  {
   result = "WHERE region_id ='"+form.region.value+"'";
  }
  if((form.branch.value != "***")&&(form.region.value == "***")&&(form.category.value == "***"))
  {
   result = "WHERE branch_id ='"+form.branch.value+"'"; 
  }
  if((form.category.value != "***")&&(form.branch.value == "***")&&(form.region.value == "***"))
  {
   result = "WHERE category_id ='"+form.category.value+"'"; 
  }
  if((form.region.value != "***")&&(form.branch.value != "***")&&(form.category.value == "***"))
  {
   result = "WHERE region_id ='"+form.region.value+"' AND branch_id ='"+form.branch.value+"'"; 
  }
  if((form.region.value != "***")&&(form.category.value != "***")&&(form.branch.value == "***"))
  {
   result = "WHERE region_id ='"+form.region.value+"' AND category_id ='"+form.category.value+"'"; 
  }
  if((form.region.value == "***")&&(form.branch.value != "***")&&(form.category.value != "***"))
  {
   result = "WHERE branch_id ='"+form.branch.value+"' AND category_id ='"+form.category.value; 
  }
  if((form.region.value != "***")&&(form.category.value != "***")&&(form.branch.value != "***"))
  {
   result = "WHERE region_id ='"+form.region.value+"' AND category_id ='"+form.category.value+"' AND branch_id ='"+form.branch.value+"'"; 
  }
  return result;
  //alert(result)
  }
 
 function validateCriteria(form)
  {
  if(form.region.value == "")
  {
   alert("You have not selected a region.");
   form.region.focus();
   return;
  }
  if(form.branch.value == "")
  {
   alert("You have not selected a branch.");
   form.branch.focus();
   return;
  }
  if(form.category.value == "")
  {
   alert("You have not selected a category.");
   form.category.focus();
   return;
  }
  if(form.report_format.value == "applet")
  {
   alert("selected report format is under-construction.");
   form.report_format.focus();
   return;
  }
  //alert(filterCriteria(form))
  form.filt_crit.value = (filterCriteria(form)); 
  form.action = "jsp/processor.jsp?reportName="+form.report_name.value+"&reportFormat="+form.report_format.value+"&reportFilter="+form.filt_crit.value;
  form.submit();
 }
</script>

</head>
 <% asset.report.Utilities utilz = new asset.report.Utilities(); %>
<body>
<%
 String reportname = request.getParameter("reportName");
 System.out.println("report name : "+reportname);
%>
<form onSubmit="javascript: return validateCriteria(this)" name="report_filter"  method=post>
<table border="1" width="406" id="table1">
	<tr>
		<td>Filter Criteria &amp; Report format</td>
	</tr>
</table>
<p></p>
<table border="1" width="406" id="table2" height="191">
	<tr>
		<td width="132" height="39">Region</td>
		<td colspan="2">
    <select name="region">
    <option value="" selected>--SELECT A REGION--</option>
    <option value="***">ALL</option>
    <%=utilz.getResources("","SELECT region_id,region_name FROM am_ad_Region")%>
    </select>
    </td>
	</tr>
	<tr>
		<td width="132" height="34">Branch</td>
		<td colspan="2">
    <select name="branch">
    <option value="" selected>--SELECT A BRANCH--</option>
    <option value="***">ALL</option>
    <%=utilz.getResources("","SELECT branch_id,branch_name FROM am_ad_Branch")%>
    </select>
    </td>
  </tr>
	<tr>
	  <td height="37">Category</td>
	  <td colspan="2"><select name="category">
        <option value="" selected>--SELECT A CATEGORY--</option>
        <option value="***">ALL</option>
        <%=utilz.getResources("","SELECT category_id,category_name FROM am_ad_category")%>
      </select></td>
    </tr>
	<tr>
		<td width="132" height="37">Report format </td>
		<td colspan="2">
		<select name="report_format">
        <option value="pdf" selected>PDF OUTPUT</option>
        <option value="html">HTML OUTPUT</option>
        <option value="applet">APPLET VIEWER OUTPUT</option>
        </select>
        <input type="hidden" name="report_name" value="<%=reportname%>">
		<input type="hidden" name="filt_crit" >
        </td>
	 </tr>
	<tr>
	  <td align=right height="30">&nbsp;</td>
	  <td width="90" height="30" align=right><input class="action" type="button" value="Go..." onclick="javascript:validateCriteria(this.form)" name="reportfilter"></td>
	  <td width="162" align=right>&nbsp;</td>
	</tr>
</table>
<p>&nbsp;</p>
</form>

<p></p>

</body>

</html>