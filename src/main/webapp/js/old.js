var statoElaborazioneInterval = null;
var urlStatoElaborazioneJson = "./StatoElaborazioneJson.json";
var urlGetIdElaborazioneJson = "./GetIdElaborazioneJson.json";
var urlNotificationCenterJson = "./NotificationCenterJson.json";
var urlSearchJson = "../api/webdesktop/search";

/////////////
// Utility //
/////////////
function wdSubmit (formName, pageName, buttonName) {
 var url = pageName + "?navigationprefix___button___" + buttonName + "=" + buttonName;
 
 $("#" + formName).attr("action", url);
 $("#" + formName).submit();
}

function showMessageBox(messageList) {
 var message = "";

 $.each(messageList, function(index, value) {
  message += index == 0 ? "" : "\n";
  message += value.description;
 });

 alert(message);
}


/////////////////
// Interfaccia //
/////////////////
function notifAppClose() {
 var type = "applicationClose";
 var applicationName = this.id.substring("close".length);

 $.getJSON(urlNotificationCenterJson, {
  type : type,
  applicationName : applicationName,
  nocache : new Date().getTime()
 }, function(data) {
  if (checkJsonCall(data)) {
   notificationReload();
  }
 }).error(function() {
  $(window.location).attr('href', 'HomePubblicaPage.html');
 });
}

function notifAppRun() {
 var arr = this.id.split("#");

 var type = "applicationDo";
 var applicationName = arr[0];
 var operation = arr[1];

 $.getJSON(urlNotificationCenterJson, {
  type : type,
  applicationName : applicationName,
  operation : operation,
  nocache : new Date().getTime()
 }, function(data) {
  if (checkJsonCall(data)) {
   notificationReload();
  }
 }).error(function() {
  $(window.location).attr('href', 'HomePubblicaPage.html');
 });
}

function notifMessageRun() {
 var arr = this.id.split("#");

 var type = "messageDo";
 var applicationName = arr[0];
 var operation = arr[1];
 var messageId = arr[2];

 $.getJSON(urlNotificationCenterJson, {
  type : type,
  applicationName : applicationName,
  operation : operation,
  messageId : messageId,
  nocache : new Date().getTime()
 }, function(data) {
  if (checkJsonCall(data)) {
   notificationReload();
  }
 }).error(function() {
  $(window.location).attr('href', 'HomePubblicaPage.html');
 });
}

function notifMessageClose() {
 var arr = this.id.split("#");

 var type = "messageClose";
 var applicationName = arr[0];
 var messageId = arr[2];

 $.getJSON(urlNotificationCenterJson, {
  type : type,
  applicationName : applicationName,
  messageId : messageId,
  nocache : new Date().getTime()
 }, function(data) {
  if (checkJsonCall(data)) {
   notificationReload();
  }
 }).error(function() {
  $(window.location).attr('href', 'HomePubblicaPage.html');
 });
}

// Applicazione
function notifDrawApp(application) {
 var appDiv = $("<div />", {
  "class" : "notif"
 });
 appDiv.append(notifDrawAppBar(application));

 if (application.message != null) {
  for ( var i = 0; i < application.message.length; i++) {
   appDiv.append(notifDrawMessage(application, application.message[i]));
  }
 }
 if (application.subTitle1 != null || application.subTitle2 != null || application.description != null) {
  appDiv.append(notifDrawFooter(application));
 }

 return appDiv;
}

// Applicazione - Barra
function notifDrawAppBar(application) {
 var appDiv = $("<div />", {
  "class" : "notifBar"
 });

 if (application.url != null) {
  appDiv.append($("<a />", {
   href : application.url,
   text : application.title,
   "class" : "title"
  }));
 } else {
  appDiv.append($("<span />", {
   text : application.title,
   "class" : "title"
  }));
 }

 if (application.closeable) {
  appDiv.append($("<span />", {
   id : "close" + application.name,
   text : " ",
   "class" : "close",
   click : notifAppClose,
   title : "Chiudi"
  }));
 }

 for ( var i = 0; i < application.applicationFunction.length; i++) {
  appDiv.append($("<span />", {
   id : application.name + "#" + application.applicationFunction[i].name,
   text : " ",
   "class" : application.applicationFunction[i].className,
   click : notifAppRun,
   title : application.applicationFunction[i].title
  }));
 }

 return appDiv;
}

// Applicazione - Footer
function notifDrawFooter(application) {
 var appDiv = $("<div />", {
  "class" : "notifApp"
 });

 // Titolo
 if (application.subTitle1 != null) {
  var titleDiv = $("<div />", {
   text : application.subTitle1,
   "class" : "subTitle1"
  });
  if (application.subTitle2) {
   titleDiv.append($("<div />", {
    text : application.subTitle2,
    "class" : "subTitle2"
   }));
  }
  appDiv.append(titleDiv);
 }

 // Descrizione
 if (application.description != null) {
  appDiv.append($("<div />", {
   text : "description",
   "class" : "description"
  }));
 }

 return appDiv;
}

// Applicazione - Messaggio
function notifDrawMessage(application, message) {
 var appDiv = $("<div />", {
  id : application.id,
  "class" : "notifMess"
 });

 // Titolo
 var titleDiv = $("<div />", {
  "class" : "title"
 });
 if (message.url) {
  titleDiv.append($("<a />", {
   href : message.url,
   text : message.title
  }));
 } else {
  titleDiv.append($("<span />", {
   text : message.title
  }));
 }

 appDiv.append(titleDiv);

 // Descrizione
 var divDescription = $("<div />", {
  "class" : "description"
 });
 if (message.url) {
  divDescription.append($("<a />", {
   href : message.url,
   text : message.description
  }));
 } else {
  divDescription.append($("<span />", {
   text : message.description
  }));
 }

 appDiv.append(divDescription);

 // Sotto titolo
 if (message.subTitle) {
  var subTitleDiv = $("<div />", {
   text : message.subTitle,
   "class" : "subTitle"
  });
  if (message.messaggeFunction != null) {
   for ( var i = 0; i < message.messaggeFunction.length; i++) {
    appDiv.append(notifDrawMessageFunction(application, message, message.messaggeFunction[i]));
   }
  }

  if (message.closeable) {
   subTitleDiv.append($("<span />", {
    id : application.name + "#close#" + message.id,
    text : " ",
    "class" : "close",
    click : notifMessageClose,
    title : "Chiudi"
   }));
  }

  appDiv.append(subTitleDiv);
 }

 return appDiv;
}

// Applicazione - Messaggio
function notifDrawMessageFunction(application, message, messageFunction) {
 return $("<span />", {
  id : application.name + "#" + messageFunction.name + "#" + message.id,
  text : " ",
  "class" : messageFunction.className,
  click : notifMessageRun,
  title : messageFunction.title
 });
}

function notificationReload() {
 $("#notificationCenter").html("");
 $.getJSON(urlNotificationCenterJson, {
  type : "getNotificationCenter",
  nocache : new Date().getTime()
 }, function(data) {
  if (checkJsonCall(data)) {
   for ( var i = 0; i < data.notificationCenter.application.length; i++) {
    if (data.notificationCenter.application[i].open) {
     $("#notificationCenter").append(notifDrawApp(data.notificationCenter.application[i]));
    }
   }
  }
 }).error(function() {
  $(window.location).attr('href', 'HomePubblicaPage.html');
 });
}

function expandDetail(idRow) {
 $('#' + idRow + "Detail").show();
 $('#' + idRow + "Expand").hide();
 $('#' + idRow + "Collapse").show();
}

function collapseDetail(idRow) {
 $('#' + idRow + "Detail").hide();
 $('#' + idRow + "Expand").show();
 $('#' + idRow + "Collapse").hide();
}

function checkJsonCall(data) {
 if (data.redirectToHome) {
  $(window.location).attr('href', 'HomePubblicaPage.html');
  return false;
 } else if (!data.messageList.clear) {
  var message = "";
  $.each(data.messageList, function(index, value) {
   message += index == 0 ? "" : "\n";
   message += value.description;
  });
  alert(message);

  return false;
 }

 return true;
}

function showSubMenuOnClick() {
 var top = $(this).position().top + 3;
 var left = 70;
 var dialogId = $(this).attr("id") + "voce";

 if ($("#" + dialogId).length > 0) {
  $("#" + dialogId).dialog({
   height : 350,
   width : 650,
   modal : true,
   closeOnEscape : true,
   close : function(ev, ui) {
    $("body").css("overflow", "auto");
   },
   position : [ left, top ]
  });

  $("body").css("overflow", "hidden");
  $(".ui-dialog-titlebar").show();
 }
}

function statoElaborazioneJson(idlogelaborazione) {
 $.getJSON(urlStatoElaborazioneJson, {
  idlogelaborazione : idlogelaborazione,
  nocache : new Date().getTime()
 }, function(data) {
  $("#progressDialog").dialog({
   title : data.elaborazione
  });
  $("#progressMessage").html(data.messaggio);
  $("#progressBar").progressbar({
   value : data.percentuale
  });
 });
}

function getIdElaborazione() {
 var idlogelaborazione = null;

 $.ajax({
  type : 'GET',
  url : urlGetIdElaborazioneJson,
  dataType : 'json',
  success : function(data) {
   idlogelaborazione = data.idlogelaborazione;
  },
  data : {
   nocache : new Date().getTime(),
  },
  async : false
 });
 
 return idlogelaborazione;
}

function lanciaElabOnclick(url) {
 $.getJSON(urlGetIdElaborazioneJson, {
  nocache : new Date().getTime()
 }, function(data) {
  var idlogelaborazione = data.idlogelaborazione;

  $(".ui-dialog-content").dialog("close");

  // Apro la finestra
  $("#progressDialog").dialog({
   modal : true,
   title : "Elaborazione in corso",
   closeOnEscape : true,
   close : function() {
    clearInterval(statoElaborazioneInterval);
   }
  });

  $("#progressMessage").html("");
  $("#progressBar").progressbar({
   value : 0
  });

  // Imposto il refresh dell'avanzamento
  statoElaborazioneInterval = setInterval(function() {
   statoElaborazioneJson(idlogelaborazione);
  }, 2000);

  // Lancio l'elaborazione
  $.getJSON(url, {
   idlogelaborazione : idlogelaborazione
  }, function(data) {
   clearInterval(statoElaborazioneInterval);

   $("#progressMessage").html("Operazione terminata");
   statoElaborazioneJson(idlogelaborazione);
  });

 });
}

// Apertura/chiusura centro notifiche
function notificationSwitchOnClick() {
 $(".contentSmall").switchClass("contentSmall", "contentLarge");
 $(".contentLarge").switchClass("contentLarge", "contentSmall");

 $(".notificationLarge").switchClass("notificationLarge", "notificationSmall");
 $(".notificationSmall").switchClass("notificationSmall", "notificationLarge");

 if ($('#notificationCenter').is(':visible')) {
  $("#notificationButton").switchClass("notificationHide", "notificationShow");
  $.getJSON(urlNotificationCenterJson, {
   type : "close",
   nocache : new Date().getTime()
  }, null);
 } else {
  $("#notificationButton").switchClass("notificationShow", "notificationHide");
  $.getJSON(urlNotificationCenterJson, {
   type : "open",
   nocache : new Date().getTime()
  }, function(data) {
   notificationReload();
  });
 }
}

// Inizializza la ricerca generale
function initSearch() {
 if ($("#searchLabel").length == 0) {
  return;
 }
 
 var uiItem = $("#searchLabel").autocomplete({
  minLength : 2,
  source : function(request, response) {
   $.getJSON(urlSearchJson, {
    term : request.term,
    nocache : new Date().getTime()
   }, function(data) {
    response(data.items);
   });
  },
  focus : function(event, ui) {
   $("#searchLabel").val(ui.item.title);
   return false;
  },
  select : function(event, ui) {
   $("#searchLabel").val(ui.item.title);
   $("#searchValue").val(ui.item.url);

   if (ui.item.url.indexOf("javascript:") >= 0) {
    eval(ui.item.url.substr("javascript:".length, ui.item.url.length - "javascript:".length));
   } else {
    $("#search").submit();
   }

   return false;
  }
 });

//private String title - label;
//private String subTitle - desc;
//private String imageUrl - icon;
//private String url - value;
//private SearchRelevance itemRelevance;

 
 // Personalizzo il rendering
 uiItem.data("ui-autocomplete")._renderItem = function(ul, item) {
  var html = "<a><div style=\"float:left; margin-right:5px; width:60px; text-align:center; font-size:2em; color: #57abff;\">";
  html += (item.imageUrl != null && item.imageUrl != "") ?  item.imageUrl : "&nbsp;";
  html += "</div><b>" + item.title + "</b>";
  html += (item.subTitle != null && item.subTitle != "") ? "<br>" + item.subTitle : "";
  html += "<div style=\"clear:left\"></div></a>";

  return $("<li></li>").data("item.autocomplete", item).append(html).appendTo(ul);
 };
}

function showMenuUser() {
 $("#menuUser").show();
}

// Inizializzazioni
$(function() {
 // Prevent back button
 window.history.forward();
 
 // Inizializzazione funzionalità Desktop
 initSearch(); // Ricerca

 // Menu Utente
 $("#menuUser").menu().hide();
 $("#menuUser").mouseleave(function() {
  $("#menuUser").hide();
 });

 // Menu
 $(".menuDiv").click(showSubMenuOnClick);

 // Varie
 $(".date").datepicker();

 $(".month").datepicker({
  changeMonth : true,
  changeYear : true,
  showButtonPanel : true,
  dateFormat : 'mm/yy',
  onClose : function(dateText, inst) {
   var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
   var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
   $(this).datepicker('setDate', new Date(year, month, 1));
  },
  beforeShow : function(input, inst) {
   if ((datestr = $(this).val()).length == 7) {
    year = datestr.substring(3, 7);
    month = datestr.substring(0, 2) * 1 - 1;
    $(this).datepicker('option', 'defaultDate', new Date(year, month, 1));
    $(this).datepicker('setDate', new Date(year, month, 1));
   } else {
    $(this).datepicker('setDate', new Date());
   }
  }
 });

 $(".month").focus(function() {
  $(".ui-datepicker-calendar").hide();
  $("#ui-datepicker-div").position({
   my : "center top",
   at : "center bottom",
   of : $(this)
  });
 });

 // Utility
 $(".simpleButton").button({ text : true });
 $(".play").button({ icons : { primary : "ui-icon-play" }, text : true });
 $(".stop").button({ icons : { primary : "ui-icon-stop" }, text : true });
 $(".work").button({ icons : { primary : "ui-icon-wrench" }, text : true });
 $(".mail").button({ icons : { primary : "ui-icon-mail-closed" }, text : true });
 $(".lock").button({ icons : { primary : "ui-icon-locked" }, text : true });
 $(".cart").button({ icons : { primary : "ui-icon-cart" }, text : true });
 $(".cartIco").button({ icons : { primary : "ui-icon-cart" }, text : false });
 $(".back").button({ icons : { primary : "ui-icon-triangle-1-w" }, text : true });

 // Login
 $(".login").button({ icons : { primary : "ui-icon-locked" }, text : true });

 // Toolbar
 $(".load").button({ icons : { primary : "ui-icon-search" }, text : true });
 $(".reset").button({ icons : { primary : "ui-icon-circle-close" }, text : true });
 $(".detail").button({ icons : { primary : "ui-icon-circle-arrow-e" }, text : false });
 $(".refresh").button({ icons : { primary : "ui-icon-arrowrefresh-1-e" }, text : true });

 $(".firstRow").button({ icons : { primary : "ui-icon-seek-first" }, text : false });
 $(".prevPage").button({ icons : { primary : "ui-icon-seek-prev" }, text : false });
 $(".prev").button({ icons : { primary : "ui-icon-triangle-1-w" }, text : false });
 $(".next").button({ icons : { primary : "ui-icon-triangle-1-e" }, text : false });
 $(".nextPage").button({ icons : { primary : "ui-icon-seek-next" }, text : false });
 $(".lastRow").button({ icons : { primary : "ui-icon-seek-end" }, text : false });
 $(".elenco").button({ icons : { primary : "ui-icon-circle-arrow-w" }, text : true });
 $(".excel").button({ icons : { primary : "ui-icon-document" }, text : true });
 $(".excelIco").button({ icons : { primary : "ui-icon-document" }, text : false });

 $(".deleteNoText").button({ icons : { primary : "ui-icon-minusthick" }, text : false });
 $(".updateNoText").button({ icons : { primary : "ui-icon-pencil" }, text : false });
 $(".checkNoText").button({ icons : { primary : "ui-icon-check" }, text : false });
 $(".uncheckNoText").button({ icons : { primary : "ui-icon-closethick" }, text : false });

 $(".insert").button({ icons : { primary : "ui-icon-plusthick" }, text : true });
 $(".delete").button({ icons : { primary : "ui-icon-minusthick" }, text : true });
 $(".update").button({ icons : { primary : "ui-icon-pencil" }, text : true });
 $(".commit").button({ icons : { primary : "ui-icon-disk" }, text : true });
 $(".rollback").button({ icons : { primary : "ui-icon-arrowreturnthick-1-s" }, text : true });

 $(".collapsed").button({ icons : { primary : "ui-icon-triangle-1-e" }, text : false });
 $(".expanded").button({ icons : { primary : "ui-icon-triangle-1-se" }, text : false });

 // Confirm message
 $(".confirm").button().click(function(event, ui) {
  var confirmMessage = $(this).attr("confirmMessage");

  if (!confirm(confirmMessage)) {
   event.preventDefault();
  }
  });

 // Tab
 $('.tabs').tabs({
  activate : function(event, ui) {
   var tabId = '#tab' + ui.newPanel.attr("id");
   $(tabId).trigger('click');
  }
 });

 $('.tabSelected').each(function(index) {
  $("[href='#" + this.id + "']").trigger("click");
 });

 // MessageBox
 $("#messageBox").dialog({
  autoOpen : true,
  hide : "explode",
  height : 400,
  width : 800,
  modal : true,
  buttons : {
   "Chiudi" : function() {
    $(this).dialog("close");
   }
  }
 });

 // Autocomplete
 $('.autoComplete').each(function(index) {
  var fullName = $("#" + this.id).attr("fields") + "." + this.id;
  var url = $("#mainForm").attr("action") + "?navigationprefix___autocomplete___" + fullName;

  
  $("#" + this.id).autocomplete({ minLength : 2, source : function(request, response) {
   $.getJSON(url, { term : request.term, nocache : new Date().getTime() }, function(data) {
    response(data);
   });
  } });
  
  $("#" + this.id).data("ui-autocomplete")._renderItem = function(ul, item) {
   var html = "";

   if (item.valid) {
    html = "<a>" + item.value + "</a>";
   } else {
    html = "<a><i style=\"color:gray\">" + item.value + "<i></a>";
   }
   
   return $("<li></li>").data("item.autocomplete", item).append(html).appendTo(ul);
  };
 });



 // MultipleAutoComplete
 function split(val) {
  return val.split(/,\s*/);
 }
 function extractLast(term) {
  return split(term).pop();
 }

 $('.multipleAutoComplete').each(function(index) {
  var fullName = $("#" + this.id).attr("fields") + "." + this.id;
  var url = $("#mainForm").attr("action") + "?navigationprefix___autocomplete___" + fullName;

  $("#" + this.id).bind("keydown", function(event) {
   if (event.keyCode === $.ui.keyCode.TAB && $(this).data("ui-autocomplete").menu.active) {
    event.preventDefault();
   }
  }).autocomplete({
   minLength : 2,
   source : function(request, response) {
    $.getJSON(url, {
     term : extractLast(request.term),
     nocache : new Date().getTime()
    }, function(data) {
     response(data);
    });
   },
   focus : function() {
    // prevent value inserted on focus
    return false;
   },
   select : function(event, ui) {
    var terms = split(this.value);
    // remove the current input
    terms.pop();
    // add the selected item
    terms.push(ui.item.value);
    // add placeholder to get the comma-and-space at the end
    terms.push("");
    this.value = terms.join(", ");
    return false;
   }
  });

  $("#" + this.id).data("ui-autocomplete")._renderItem = function(ul, item) {
   var html = "";

   if (item.valid) {
    html = "<a>" + item.value + "</a>";
   } else {
    html = "<a><i style=\"color:gray\">" + item.value + "<i></a>";
   }

   return $("<li></li>").data("item.autocomplete", item).append(html).appendTo(ul);
  };
 });

 // Grafici
 //$.jqplot.sprintf.thousandsSeparator = '.';
 //$.jqplot.sprintf.decimalMark = ',';

 // Notification center
 if ($('#notificationCenter') && $('#notificationCenter').is(':visible')) {
  notificationReload();
 }
});
