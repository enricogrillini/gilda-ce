<%@page import="it.itdistribuzione.gilda.gen.form.HomePrivataForm"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="webDesktopTags" prefix="wd"%>
<%@ taglib uri="groupTags" prefix="grp"%>
<%@ taglib uri="formTags" prefix="form"%>

<wd:html>
<wd:head />

<wd:body>
 <wd:page>
  <wd:menu />

  <wd:content>
   <wd:checkMessage />

   <div class="row">
    <div class="col-6 mb-4">
     <form:simpleCard>
      <p>
       <form:text name="<%=HomePrivataForm.Aforismi.AFORISMA%>" />
      </p>
      <footer class="blockquote-footer">
       <cite title="Autore"><form:text name="<%=HomePrivataForm.Aforismi.AUTORE%>" /></cite>
      </footer>
     </form:simpleCard>
    </div>
   </div>

   <div class="row">
    <!-- Line Chart -->
    <div class="col-6">
     <div class="card shadow mb-4">
      <div class="card-body">
       <form:chartCanvas name="<%=HomePrivataForm.MonitorDataLine.NAME%>" />
      </div>
     </div>
    </div>

    <!-- Bar Chart -->
    <div class="col-6">
     <div class="card shadow mb-4">
      <div class="card-body">
       <form:chartCanvas name="<%=HomePrivataForm.MonitorDataBar.NAME%>" />
      </div>
     </div>
    </div>

   </div>

  </wd:content>
 </wd:page>
 <form:chartScript name="<%=HomePrivataForm.MonitorDataLine.NAME%>" />
 <form:chartScript name="<%=HomePrivataForm.MonitorDataBar.NAME%>" />
</wd:body>

</wd:html>
