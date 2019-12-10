import React, { useEffect, useState } from 'react';
import BarChart from '../../common/charts/BarChart';
import chartApi from '../../../shared/api/chart.api';

const data2 = [{
  'country': 'USA',
  'visits': 2025,
}, {
  'country': 'China',
  'visits': 1882,
}, {
  'country': 'Japan',
  'visits': 1809,
}, {
  'country': 'Germany',
  'visits': 1322,
}, {
  'country': 'UK',
  'visits': 1122,
}, {
  'country': 'Brazil',
  'visits': 395,
}];

export default () => {
  const [data, setData] = useState([]);

  useEffect(() => {
    const api = async () => {
      const result = await chartApi().getChart({
        measures: ['sumCost'],
        dimensions: ['supplierName'],
        sorts: ['ascSumCost'],
        supplierName: 'Ups,Fedex',
      });
      setData(result);
    };
    api();
  }, [setData]);

  return (
    <div>
      <BarChart data={data} x="supplierName" y="sumCost" title="Spend per supplier"/>
      <BarChart data={data2} x="country" y="visits" title="Visits per Country"/>
    </div>
  );
}