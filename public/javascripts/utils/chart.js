function preprocessData(dataInput, user) {
    let dataOutput = [];
    let label = [];
    let tooltipInfo = [];
    let dataExport = [];
    let counter = 1;
    dataInput.forEach((element) => {
        if(element.user_fk == user){
            tooltipInfo.push(element.timestamp);
            dataOutput.push(element.score);
            label.push('Game #'+counter);
            counter++;
        }
    });
    dataExport[0] = dataOutput;
    dataExport[1] = label;
    dataExport[2] = tooltipInfo;
    console.log(dataExport);
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
                borderColor: 'rgba(80, 150, 200, 1)',
                backgroundColor: 'rgba(80, 150, 200, 0.2)'
            }],
        },
        options: {
            legend: {
                display: false
            },
            tooltips: {
                callbacks: {
                    label: function(tooltipItem) {
                        return tooltipItem.yLabel + ' km';
                    }
                }
            }
        }
    });
}
