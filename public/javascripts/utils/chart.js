function preprocessData(dataInput, user) {
  let dataOutput = [];
  let label = [];
  let dataExport = [];
  dataInput.forEach((element) => {
    if(element.user_fk == 1){}
    dataOutput.push(element.score);
    label.push('Game #'+element.id);
  });
  dataExport[0] = dataOutput;
  dataExport[1] = label;
  return dataExport;
}


function createChart(dataInput){
  let chartContainer = '<div class="container" id="chartContainer"></div>';
  let chartCanvas = '<canvas id="myChart" style="width: 100%; height: 300px;"></canvas>';
  $('#app').append(chartContainer);
  $('#chartContainer').append(chartCanvas);
  let ctx = $("#myChart");
  let myChart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: dataInput[1],
      datasets: [{
        lineTension: 0,
        data: dataInput[0],
        //backgroundColor: colors
      }]
    },
    options: {
      legend: {
        display: false
      },
      tooltips: {
        callbacks: {
          label: function(tooltipItem) {
            return tooltipItem.yLabel;
          }
        }
      }
    }
  });
}
