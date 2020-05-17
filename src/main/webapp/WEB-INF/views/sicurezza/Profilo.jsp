
<%@page import="it.itdistribuzione.gilda.gen.form.sicurezza.ProfiloForm"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="webDesktopTags" prefix="wd"%>
<%@ taglib uri="formTags" prefix="form"%>
<%@ taglib uri="groupTags" prefix="grp"%>

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
      <form:lblControl name="<%=ProfiloForm.Detail.IDUTENTE%>" />
      <form:lblControl name="<%=ProfiloForm.Detail.COGNOME%>" />
      <form:lblControl name="<%=ProfiloForm.Detail.NOME%>" />
     </grp:row>
     <grp:row>
      <form:lblControl name="<%=ProfiloForm.Detail.USERID%>" />
      <form:lblControl name="<%=ProfiloForm.Detail.PASSWORD%>" />
     </grp:row>
     <grp:row>
      <form:lblControl name="<%=ProfiloForm.Detail.EMAIL%>" />
      <form:lblControl name="<%=ProfiloForm.Detail.EMAILPASSWORD%>" />
      <form:lblControl name="<%=ProfiloForm.Detail.LOCALIZATION%>" />
     </grp:row>
    </div>

    <div class="card-footer">
     <form:editablePageBar />
    </div>

   </div>

  </wd:content>
 </wd:page>

</wd:body>

</wd:html>