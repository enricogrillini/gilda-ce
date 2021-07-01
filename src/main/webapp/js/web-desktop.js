/**
 * Project: gilda-ce Copyright (C) 2019-2020 Enrico Grillini
 * <p>
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * @author Enrico Grillini
 */


// AlertModal
class AlertModal {
  
  clear () {
    $('#alertModalDescription').text("");
  }
  
  addMessage(message) {
    var div;
    if (message.severity == "WARN") {
      div = $("<div/>").addClass("alert alert-dismissible alert-warning");
    } else if (message.severity == "ERROR") {
      div = $("<div/>").addClass("alert alert-dismissible alert-danger");
    } else {
      div = $("<div/>").addClass("alert alert-dismissible alert-info");
    }
    
    var p = $("<p/>").addClass("mb-0  font-weight-bold").text(message.description);
    div.append(p);
    
    $('#alertModalDescription').append(div);
  }

  showMessageList (messageList) {
    if (! messageList.empty) {
      this.clear ();
      
      $('#alertModalTitle').text("Attenzione!");

      for (var i in messageList.list) {
        this.addMessage(messageList.list[i])
      }      
      this.show();
    }
  }
  
  showSimpleMessage (title, message) {
    $('#alertModalTitle').text(title);
    $('#alertModalDescription').text(message);
    
    this.show()
  }
  
  show() {
    $('#alertModal').modal('show');
  }

}

class ConfirmModal {
  
  clear () {
    $('#confirmModalTitle').text("");
    $('#confirmModalDescription').text("");
  }
  
  // Visualizza le informazioni degli attribuiti [data-title, data-description]
  // dell'elemento passato
  showElementMessage (element) {
    element = $(element)
    var title = element.data('title')
    var description = element.data('description')

    $('#confirmModalTitle').text(title);
    $('#confirmModalDescription').text(description);

    $('#confirmModalSubmit').attr("related-element-id", element.attr("id"))
    $('#confirmModalSubmit').click(this.confirm)
    
    this.show();
  }
  
  show() {
    $('#confirmModal').modal('show');
  }

  confirm() { 
    var id = $('#confirmModalSubmit').attr("related-element-id")
    $("#" + id).click();
  }
  
}

// Inizializzazione pagina
$(function() {

  // Back button
  window.history.forward();

  // Topbar - Search
  $('#searchText').autocomplete({
    serviceUrl : "../api/webdesktop/search",
    width: "flex",    
    onSelect : function(suggestion) {
      if (suggestion.url.startsWith("javascript:")) {
        $("#searchText").val("");
        eval(suggestion.url.substring("javascript:".length))
      } else {
        window.location.replace(suggestion.url);
      }
    },
    beforeRender : function(container, suggestions) {
      container.children().each(function() {
        var jQueryDiv = $(this);
        var index=jQueryDiv.attr("data-index");
        var suggestion = suggestions[index];
        
        jQueryDiv.css({'min-height' : '40px', 'padding-bottom' : '3px'});

        // Immagine
        if (suggestion.imageUrl != null && suggestion.imageUrl != "") {
          jQueryDiv.prepend("<span style=\"width: 40px\" class=\"float-left\">" + suggestion.imageUrl + "</span>");  
        } 
        
        // Sottotitolo
        if (suggestion.subValue != null && suggestion.subValue != "") {
          jQueryDiv.append("<br>");
          jQueryDiv.append($("<small>").text(suggestion.subValue));
        }
      });
    }
  });

  // File
  $('input[type="file"]').change(function(e) {
    var fileName = e.target.files[0].name;
    $(this).siblings(".custom-file-label").html(fileName);
  });

  // autocomplete
  $('.autoComplete').each(function(index) {
    var jQueyName = "#" + this.id;
    var fullName = $(jQueyName).attr("fields") + "." + this.id;
    var url = $("#mainForm").attr("action") + "?navigationprefix___autocomplete___" + fullName;

    $(jQueyName).autocomplete({
      serviceUrl : url,
      onSelect : function(suggestion) {
        $(jQueyName).val(suggestion.value)
      },
      beforeRender : function(container, suggestions) {
        container.children().each(function() {
          var jQueryDiv = $(this);
          var index = jQueryDiv.attr("data-index");
          var suggestion = suggestions[index];

          if (!suggestion.valid) {
            jQueryDiv.css("font-style", "oblique")
            jQueryDiv.css("color", "#cccccc")
          }
        });
      }
    });
  });

  // Tooltip
  $('[data-toggle="tooltip"]').tooltip();

  // Table - Select row
  $('.table > tbody > tr > td').click(function() {
    if ($(this).hasClass("tableDetail")) {
      return;
    }

    var hiddenName = $(this).parent().attr('id');

    if (hiddenName != null) {
      $('<input>').attr({
        type : 'hidden',
        id : 'foo',
        name : hiddenName
      }).appendTo('#mainForm');

      $("#mainForm").submit();
    }

  });
  
  // Init AlertModal
  $('#alertModal').on('show.bs.modal', function(event) {
    var button = $(event.relatedTarget) // Button that triggered the modal
    var title = button.data('title')
    var description = button.data('description')

    $("#alertModalTitle").text(title);
    $("#alertModalDescription").text(description);
  })

  // Message
  if ($('#messageModal')) {
    $('#messageModal').modal('show');
  }
});

function openJobModal(groupName, jobName) {
  $('#jobHeader').text("Job " + groupName + "." + jobName);
  $('#jobHeader').attr("groupName", groupName);
  $('#jobHeader').attr("jobName", jobName);

  $('#jobStartBody').show();
  $('#jobStartFooter').show();
  $('#jobStartText').text("Vuoi avviare il Job " + groupName + "." + jobName + "?");

  $('#jobStatusBody').hide();
  $('#jobStatusFooter').hide();

  $('#jobModal').modal();
}

function startJob() {
  var groupName = $('#jobHeader').attr("groupName");
  var jobName = $('#jobHeader').attr("jobName");

  $.ajax("../api/webdesktop/startJob", {
    data : {
      groupName : groupName,
      jobName : jobName
    },
    type : 'POST',
    success : function(data) {
      jobMessage = data.jobMessage;

      $('#jobProgessBar').css("width", jobMessage.progress + "%")
      $('#jobMessageAlert').removeClass("alert-danger");
      $('#jobMessageAlert').addClass("alert-success");
      $('#jobStatus').text(jobMessage.status)
      $('#jobMessage').text(jobMessage.message)

      $('#jobStartBody').hide();
      $('#jobStartFooter').hide();

      $('#jobStatusBody').show();
      $('#jobStatusFooter').show();

      setTimeout(statusJob, 1000, jobMessage.executionId);
    }
  });
}

function statusJob(executionId) {
  $.ajax("../api/webdesktop/getJob", {
    data : {
      executionId : executionId
    },
    type : 'GET',
    success : function(data) {
      jobMessage = data.jobMessage;

      $('#jobProgessBar').css("width", jobMessage.progress + "%")
      $('#jobStatus').text(jobMessage.status)
      $('#jobMessage').text(jobMessage.message)
      $('#jobDetail').text(jobMessage.detail)

      $('#jobStartBody').hide();
      $('#jobStartFooter').hide();

      $('#jobStatusBody').show();
      $('#jobStatusFooter').show();
      
      $('#jobMessageAlert').removeClass("alert-danger");
      $('#jobMessageAlert').addClass("alert-success");
      
      if (jobMessage.status == "RUNNING") {
        setTimeout(statusJob, 3000, executionId);
      } else if (jobMessage.status == "ABORTED") {
        $('#jobMessageAlert').removeClass("alert-success");
        $('#jobMessageAlert').addClass("alert-danger");
      } else  {
        console.log("END")
      }
    }
  });
}

function buttonConfirm(button) {
  if (event) {
    event.preventDefault();
    new ConfirmModal().showElementMessage(button)
  }
}

function reloadPage(queryString) {
  var url = $('#mainForm').attr('action') + "?" + queryString;
  console.log (url);
  window.location.href = url;
}