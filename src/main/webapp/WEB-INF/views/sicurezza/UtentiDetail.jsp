<%@page import="it.itdistribuzione.gilda.gen.form.sicurezza.UtentiForm"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="webDesktopTags" prefix="wd"%>
<%@taglib uri="formTags" prefix="form"%>
<%@taglib uri="groupTags" prefix="grp"%>

<wd:html>
<wd:head />

<wd:body>
 <wd:page>
  <wd:menu />
  <wd:content multipart="true">
   <wd:checkMessage />

   <div class="card shadow">
    <div class="card-body">
     <grp:row>
      <form:lblControl name="<%=UtentiForm.Detail.IDUTENTE%>" />
      <form:lblControl name="<%=UtentiForm.Detail.COGNOME%>" />
      <form:lblControl name="<%=UtentiForm.Detail.NOME%>" />
     </grp:row>
     <grp:row>
      <form:lblControl name="<%=UtentiForm.Detail.USERID%>" />
      <form:lblControl name="<%=UtentiForm.Detail.PASSWORD%>" />
     </grp:row>
     <grp:row>
      <form:lblControl name="<%=UtentiForm.Detail.EMAIL%>" />
      <form:lblControl name="<%=UtentiForm.Detail.EMAILPASSWORD%>" />
      <form:lblControl name="<%=UtentiForm.Detail.LOCALIZATION%>" />
     </grp:row>
     <grp:row>
      <form:lblControl name="<%=UtentiForm.Detail.FOTO %>" />
     </grp:row>     
    </div>

    <div class="card-footer">
     <form:editableMasterDetailBar name="<%=UtentiForm.Master.NAME%>" />
    </div>

    <div class="card-body">
     <form:tabSheet name="<%=UtentiForm.Tabs.NAME%>">
      <form:tab name="<%=UtentiForm.Tabs.TAB_PROFILI%>">
       <grp:row>
        <form:grid name="<%=UtentiForm.Profili.NAME%>" />
        <form:subMasterDetailBar name="<%=UtentiForm.Profili.NAME%>" />
       </grp:row>
      </form:tab>
     </form:tabSheet>
    </div>
   </div>

  </wd:content>
 </wd:page>
</wd:body>

</wd:html>
