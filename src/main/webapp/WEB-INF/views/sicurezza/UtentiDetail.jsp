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
      <form:lblControl name="<%=UtentiForm.Detail.EMAIL%>" />
      <form:lblControl name="<%=UtentiForm.Detail.LOCALIZATION%>" />
     </grp:row>
     <grp:row>
      <form:lblControl name="<%=UtentiForm.Detail.FOTO%>" />
     </grp:row>
    </div>

    <div class="card-footer">
     <form:editableMasterDetailBar name="<%=UtentiForm.Master.NAME%>" />
    </div>

    <div class="card-body">
     <form:tabSheet name="<%=UtentiForm.Tabs.NAME%>">

      <%-- Profili --%>
      <form:tab name="<%=UtentiForm.Tabs.TAB_PROFILI%>">
       <div class="card">
        <div class="card-body">
         <form:grid name="<%=UtentiForm.Profili.NAME%>" />
        </div>
        <div class="card-footer p-1">
         <form:subMasterDetailBar name="<%=UtentiForm.Profili.NAME%>" />
        </div>
       </div>
      </form:tab>

      <%-- Utility --%>
      <form:tab name="<%=UtentiForm.Tabs.TAB_UTILITY%>">
       <div class="card">
        <div class="card-body">
         <grp:row>
          <form:lblControl name="<%=UtentiForm.Utility.PASSWORD%>" />
          <form:lblControl name="<%=UtentiForm.Utility.CONFERMA_PASSWORD%>" />
          <grp:cell>
           <form:control name="<%=UtentiForm.Utility.RESET_PASSWORD%>" />
          </grp:cell>
         </grp:row>
         <br />

         <div class="alert alert-info">
          <p class="mb-0 small">
           La password deve essere lunga tra <strong>8 e 16</strong> caratteri e contenere <strong>lettere</strong> (a-z, A-Z) o <strong>numeri</strong> (0-9).
          </p>
         </div>

        </div>
       </div>
      </form:tab>

     </form:tabSheet>
    </div>
   </div>

  </wd:content>
 </wd:page>
</wd:body>

</wd:html>
