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

    <!-- Tabella -->
    <div class="card-body">
     <form:grid name="<%=RuoliForm.Master.NAME%>" />
    </div>

    <!-- Toolbar -->
    <div class="card-footer">
     <form:editableMasterDetailBar name="<%=RuoliForm.Master.NAME%>" />
    </div>
   </div>
  </wd:content>
 </wd:page>
</wd:body>

</wd:html>