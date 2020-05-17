<%@page import="it.itdistribuzione.gilda.gen.form.sicurezza.RuoliForm"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="webDesktopTags" prefix="wd"%>
<%@taglib uri="formTags" prefix="form"%>
<%@taglib uri="groupTags" prefix="grp"%>

<wd:html>
<wd:head />

<wd:body>
 <wd:page>
  <wd:menu />
  <wd:content>
   <wd:checkMessage />

   <div class="card shadow">
    <div class="card-body">
     <grp:row>
      <form:lblControl name="<%=RuoliForm.Detail.CODRUOLO%>" />
      <form:lblControl name="<%=RuoliForm.Detail.DESCRIZIONEBREVE%>" />
     </grp:row>

     <grp:row>
      <form:lblControl name="<%=RuoliForm.Detail.DESCRIZIONELUNGA%>" controlWidth="10cols" />
     </grp:row>

     <grp:row>
      <form:lblControl name="<%=RuoliForm.Detail.POSIZIONE%>" />
      <form:lblControl name="<%=RuoliForm.Detail.FLAGVALIDO%>" />
     </grp:row>
    </div>

    <div class="card-footer">
     <form:editableMasterDetailBar name="<%=RuoliForm.Master.NAME%>" />
    </div>

    <div class="card-body">
     <form:tabSheet name="<%=RuoliForm.Tabs.NAME%>">

      <%-- Funzioni --%>
      <form:tab name="<%=RuoliForm.Tabs.FUNZIONI%>">
       <div class="card">
        <div class="card-body">
         <form:grid name="<%=RuoliForm.Funzioni.NAME%>" />
        </div>
        <div class="card-footer">
         <form:subMasterDetailBar name="<%=RuoliForm.Funzioni.NAME%>" />
        </div>
       </div>
      </form:tab>

      <%-- Menu --%>
      <form:tab name="<%=RuoliForm.Tabs.MENU%>">

       <div class="card">
        <div class="card-body">
         <form:grid name="<%=RuoliForm.Menu.NAME%>" />
        </div>
        <div class="card-footer">
         <form:subMasterDetailBar name="<%=RuoliForm.Menu.NAME%>" />
        </div>
       </div>
      </form:tab>

      <%-- Menu Utente --%>
      <form:tab name="<%=RuoliForm.Tabs.MENU_UTENTE%>">
       <div class="card">
        <div class="card-body">
         <form:grid name="<%=RuoliForm.MenuUtente.NAME%>" />
        </div>
        <div class="card-footer">
         <form:subMasterDetailBar name="<%=RuoliForm.MenuUtente.NAME%>" />
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