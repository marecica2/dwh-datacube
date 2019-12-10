import React, { useEffect, useState, useRef } from 'react';
import am4Theme from '@amcharts/amcharts4/themes/animated';
import * as am4core from '@amcharts/amcharts4/core';
import * as am4charts from '@amcharts/amcharts4/charts';
import {Typography} from '@material-ui/core';

am4core.useTheme(am4Theme);

export default ({ title, data, x, y}) => {
  const chartRef = useRef(null);
  let chart = null;

  useEffect(() => {
    chart = am4core.create(chartRef.current, am4charts.XYChart);
    chart.data = data;

    // Create axes
    const categoryAxis = chart.xAxes.push(new am4charts.CategoryAxis());
    categoryAxis.dataFields.category = x;
    categoryAxis.renderer.grid.template.location = 0;
    categoryAxis.renderer.minGridDistance = 30;
    chart.yAxes.push(new am4charts.ValueAxis());

    // Create series
    const series = chart.series.push(new am4charts.ColumnSeries());
    series.dataFields.valueY = y;
    series.dataFields.categoryX = x;
    series.name = 'Sum Cost';
    series.columns.template.tooltipText = '{categoryX}: [bold]{valueY}[/]';
    series.columns.template.fillOpacity = .8;

    const columnTemplate = series.columns.template;
    columnTemplate.strokeWidth = 2;
    columnTemplate.strokeOpacity = 1;

    return () => {
      if (chart) {
        chart.dispose();
      }
    }
  }, [data]);

  return (
    <div>
      <Typography variant="h6">{title}</Typography>
      <div ref={chartRef} style={{width: '100%', height: '500px'}} />
      <br />
      <br />
    </div>
  )
}
