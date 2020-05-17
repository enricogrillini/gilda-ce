$(function() {

  // Back button
  window.history.forward();

  // Topbar - Search
  $('#searchText').autocomplete({
    serviceUrl : "../api/webdesktop/search",
    onSelect : function(suggestion) {
      $("#searchValue").val(suggestion.value)
      $("#searchData").val(suggestion.url)
      $("#searchForm").submit();
    },
    beforeRender : function(container, suggestions) {
      container.children().each(function() {
        var jQueryDiv = $(this);
        var index=jQueryDiv.attr("data-index");
        var suggestion = suggestions[index];

        // Immagine
        
        if (suggestion.imageUrl != null && suggestion.imageUrl != "") {
          jQueryDiv.prepend("<span style=\"width: 40px\" class=\"float-left\">" + suggestion.imageUrl + "</span>");  
        } else {
          jQueryDiv.prepend("<span style=\"width: 40px\" class=\"float-left\">&nbsp;</span>");            
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
    $('.custom-file-label').html(fileName);
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
      }
    });
  });

  // Tooltip
  $('[data-toggle="tooltip"]').tooltip();

  // Table - Select row
  $('.table > tbody > tr').click(function() {
    var hiddenName = $(this).attr('id');

    if (hiddenName != null) {
      $('<input>').attr({
        type : 'hidden',
        id : 'foo',
        name : hiddenName
      }).appendTo('#mainForm');

      $("#mainForm").submit();
    }

  });

  // Message
  if ($('#messageModal')) {
    $('#messageModal').modal('show');
  }
});