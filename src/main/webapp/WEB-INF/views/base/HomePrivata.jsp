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

  </wd:content>
 </wd:page>
</wd:body>

</wd:html>
