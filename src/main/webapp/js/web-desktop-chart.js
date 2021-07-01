//var singleColor = ["#FF5252","#536DFE","#4CAF50","#F57C00", "#FF4081"]
var color = ["#4e73df", "#1cc88a", "#36b9cc", "#f6c23e", "#e74a3b", "#858796"];
var color2 = ["#5668E2", "#8A56E2", "#CF56E2", "#E256AE", "#E25668", "#E28956", "#E2CF56", "#AEE256", "#68E256", "#56E289", "#56E2CF", "#56AEE2"];

// Utilità - colorLuminance
function colorLuminance(hex, lum) {

  // validate hex string
  hex = String(hex).replace(/[^0-9a-f]/gi, '');
  if (hex.length < 6) {
    hex = hex[0] + hex[0] + hex[1] + hex[1] + hex[2] + hex[2];
  }
  lum = lum || 0;

  // convert to decimal and change luminosity
  var rgb = "#", c, i;
  for (i = 0; i < 3; i++) {
    c = parseInt(hex.substr(i * 2, 2), 16);
    c = Math.round(Math.min(Math.max(0, c + (c * lum)), 255)).toString(16);
    rgb += ("00" + c).substr(c.length);
  }

  return rgb;
}

// Utilità - numberFormatter
function numberFormatter(number, format) {
  decimals = format.decimals;
  dec_point = format.decimalSeparator;
  thousands_sep = format.thousandsSeparator;
  suffix = format.suffix;

  // * example: number_format(1234.56, 2, ',', ' ');
  // * return: '1 234,56'
  number = (number + '').replace(',', '').replace(' ', '');
  var n = !isFinite(+number) ? 0 : +number, prec = !isFinite(+decimals) ? 0 : Math.abs(decimals), sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep, dec = (typeof dec_point === 'undefined') ? '.' : dec_point, s = '', toFixedFix = function(n, prec) {
    var k = Math.pow(10, prec);
    return '' + Math.round(n * k) / k;
  };

  // Fix for IE parseFloat(0.55).toFixed(0) = 0;
  s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
  if (s[0].length > 3) {
    s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
  }
  if ((s[1] || '').length < prec) {
    s[1] = s[1] || '';
    s[1] += new Array(prec - s[1].length + 1).join('0');
  }
  return s.join(dec) + suffix;
}

function chart(chartId, chartData) {
  var ctx = document.getElementById(chartId);
  

  chartData.options.maintainAspectRatio = false;
  chartData.options.layout = {
    padding: {
      left: 10,
      right: 25,
      top: 10,
      bottom: 0
    }
  };

  chartData.options.tooltips = {
    backgroundColor: "rgb(255,255,255)",
    bodyFontColor: "#858796",
    titleMarginBottom: 10,
    titleFontColor: '#6e707e',
    titleFontSize: 14,
    borderColor: '#dddfeb',
    borderWidth: 1,
    xPadding: 15,
    yPadding: 15,
    displayColors: true,
    intersect: false,
    mode: 'index',
    caretPadding: 10,
    callbacks: {
      label: function(tooltipItem, chart) {
        var datasetIndex = tooltipItem.datasetIndex;
        var index = tooltipItem.index;

        if (chartData.type == "bar" || chartData.type == "line" || chartData.type == "radar") {
          var label = chart.datasets[datasetIndex].label + "";
        } else {
          var label = chart.labels[index] + "";          
        }
        var data = chart.datasets[datasetIndex].data[index];

        return " " + label + ': ' + numberFormatter(data, chartData.additionalInfo.numerFormat);
      }
    }
  }

  if (chartData.type == "bar" || chartData.type == "line") {
    chartData.options.scales = {
      xAxes: [{
        time: {
          unit: 'date'
        },
        gridLines: {
          display: false,
          drawBorder: false
        },
        ticks: {
          maxTicksLimit: 7
        }
      }],
      yAxes: [{
        ticks: {
          min: 0,
          maxTicksLimit: 5,
          padding: 10,
          // Include a dollar sign in the ticks
          callback: function(value, index, values) {
            return numberFormatter(value, chartData.additionalInfo.numerFormat);
          }
        },
        gridLines: {
          color: "rgb(234, 236, 244)",
          zeroLineColor: "rgb(234, 236, 244)",
          drawBorder: false,
          borderDash: [2],
          zeroLineBorderDash: [2]
        }
      }]
    };
  }

  new Chart(ctx, chartData);
}
