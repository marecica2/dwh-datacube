import React from 'react';
import am4Theme from '@amcharts/amcharts4/themes/animated';
import * as am4core from '@amcharts/amcharts4/core';
import * as am4charts from '@amcharts/amcharts4/charts';
import {Typography} from '@material-ui/core';

am4core.useTheme(am4Theme);

class BarChart extends React.Component {
  constructor(props) {
    super(props);
    this.chartRef = React.createRef();
  }

  componentDidMount() {
    const { data } = this.props;
    const chart = am4core.create(this.chartRef.current, am4charts.XYChart);
    chart.data = data;

    // Create axes
    const categoryAxis = chart.xAxes.push(new am4charts.CategoryAxis());
    categoryAxis.dataFields.category = 'country';
    categoryAxis.renderer.grid.template.location = 0;
    categoryAxis.renderer.minGridDistance = 30;
    categoryAxis.renderer.labels.template.adapter.add('dy', (dy, target) => {
      // eslint-disable-next-line no-bitwise,no-self-compare
      if (target.dataItem && target.dataItem.index & 2 === 2) {
        return dy + 25;
      }
      return dy;
    });
    chart.yAxes.push(new am4charts.ValueAxis());

    // Create series
    const series = chart.series.push(new am4charts.ColumnSeries());
    series.dataFields.valueY = 'visits';
    series.dataFields.categoryX = 'country';
    series.name = 'Visits';
    series.columns.template.tooltipText = '{categoryX}: [bold]{valueY}[/]';
    series.columns.template.fillOpacity = .8;

    const columnTemplate = series.columns.template;
    columnTemplate.strokeWidth = 2;
    columnTemplate.strokeOpacity = 1;
    this.chart = chart;
  }

  componentWillUnmount() {
    if (this.chart) {
      this.chart.dispose();
    }
  }

  render() {
    return (
      <div>
        <Typography variant="h6">{this.props.title}</Typography>
        <div ref={this.chartRef} style={{width: '100%', height: '500px'}} />
        <br />
        <br />
      </div>
    );
  }
}

export default BarChart;