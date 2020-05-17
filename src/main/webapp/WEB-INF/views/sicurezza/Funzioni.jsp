<%@page import="it.itdistribuzione.gilda.gen.form.sicurezza.FunzioniForm"%>
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
   
    <!-- Tabella -->
    <div class="card-body">
     <form:grid name="<%=FunzioniForm.Anagrafica.NAME%>" />
    </div>

    <!-- Toolbar -->
    <div class="card-footer">
     <form:editableGridBar name="<%=FunzioniForm.Anagrafica.NAME%>">
      <form:control name="<%=FunzioniForm.Utility.NORMALIZZA%>" />
      <form:control name="<%=FunzioniForm.Utility.CLONA%>" />
     </form:editableGridBar>
    </div>
   </div>
  </wd:content>
 </wd:page>
</wd:body>

</wd:html>