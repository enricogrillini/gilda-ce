<%@page import="it.itdistribuzione.gilda.gen.Constant"%>
<%@page import="it.itdistribuzione.gilda.gen.form.HomePubblicaForm"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="it.eg.sloth.framework.common.casting.DataTypes"%>
<%@page import="it.eg.sloth.framework.common.casting.Casting"%>
<%@taglib uri="webDesktopTags" prefix="wd"%>
<%@taglib uri="formTags" prefix="form"%>
<%@taglib uri="groupTags" prefix="grp"%>

<wd:html>

<wd:head />

<body id="page-top" class="bg-gradient-primary">
 <form action="/sec/login" method="post">
  <wd:checkMessage />

  <div class="container">

   <!-- Outer Row -->
   <div class="row justify-content-center">

    <div class="col-xl-10 col-lg-12 col-md-9">

     <div class="card o-hidden border-0 shadow-lg my-5">
      <div class="card-body p-0">
       <!-- Nested Row within Card Body -->
       <div class="row">
        <div class="col-lg-6 d-none d-lg-block bg-login-image"></div>
        <div class="col-lg-6">
         <div class="p-5">

          <div class="text-center">
           <br>
           <h1 class="h4 text-gray-900 mb-4">Welcome Back!</h1>
           <br>
          </div>

          <div class="form-group">
           <input type="text" class="form-control form-control-user" id="userid" name="username" placeholder="Userid">
          </div>
          <div class="form-group">
           <input type="password" class="form-control form-control-user" id="password" name="password" placeholder="Password">
          </div>
          <br>
          <button type="submit" name="navigationprefix___button___login" class="btn btn-primary btn-user btn-block">Login</button>
          <br>
          <hr>
          <div class="text-center">
           <p>
            <small><a href="https://github.com/enricogrillini/gilda-ce" target="_blank" rel="noopener noreferrer" data-toggle="tooltip" data-placement="bottom" data-original-title="Project on GitHub">Project: Gilda</a> - Version <%=Constant.VERSION%></small>
           </p>
          </div>
          <div class="text-center">
           <p>
            <small>Update <%=Constant.DATE%></small>
           </p>
          </div>
          <div class="text-center">
           <p>
            <small><a href="https://github.com/enricogrillini/gilda-ce/blob/master/LICENSE" target="_blank" rel="noopener noreferrer" data-toggle="tooltip" data-placement="bottom" data-original-title="Licence">Copyright (C) 2019-2020 Enrico Grillini</a></small>
           </p>
          </div>          
         </div>
        </div>
       </div>
      </div>
     </div>
    </div>
   </div>
  </div>
 </form>

 <script src="../vendor/jquery/jquery.min.js"></script>
 <script src="../vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
 <script src="../vendor/jquery-easing/jquery.easing.min.js"></script>
 <script src="../vendor/jquery-autocomplete/jquery.autocomplete.js"></script>
 <script src="../js/sb-admin-2.min.js"></script>
 <script src="../js/web-desktop.js"></script>
</body>

</wd:html>