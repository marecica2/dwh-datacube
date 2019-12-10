import React, { useEffect, useState } from 'react';
import BarChart from '../../common/charts/BarChart';
import chartApi from '../../../shared/api/chart.api';


const chartConfig = {
  SPEND_PER_SUPPLIER: {
    measures: ['sumCost'],
    dimensions: ['supplierName'],
    sorts: ['ascSumCost'],
  },
  SPEND_PER_SUPPLIER_PER_SERVICE_GROUP: {
    measures: ['sumCost'],
    dimensions: ['supplierName'],
    sorts: ['ascSumCost'],
    supplierName: 'Ups,Fedex',
  },
};

const ApiChart = ({ title, x, y, config }) => {
  const [data, setData] = useState([]);

  useEffect(() => {
    const api = async () => {
      const result = await chartApi().getChart(config);
      setData(result);
    };
    api();
  }, [setData]);

  return (
    <BarChart data={data} x={x} y={y} title={title}/>
  );
};

export default () => {
  return (
    <div>
      <ApiChart
        title="Spend per Supplier"
        config={chartConfig.SPEND_PER_SUPPLIER}
        x="supplierName"
        y="sumCost"
      />
      <ApiChart
        title="Spend per Supplier per Service Group"
        config={chartConfig.SPEND_PER_SUPPLIER_PER_SERVICE_GROUP}
        x="supplierName"
        y="sumCost"
      />
    </div>
  )
}