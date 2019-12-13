import React, { useContext, useEffect, useState } from 'react';
import BarChart from '../../common/charts/BarChart';
import chartApi from '../../../shared/api/chart.api';
import Filters from '../../common/filters/Filters';
import { AppContext } from '../../context/AppContext';


const chartConfig = {
  SPEND_PER_SUPPLIER: {
    measures: ['sumCost'],
    dimensions: ['supplierName'],
    sorts: ['ascSumCost'],
  },
  SPEND_PER_SUPPLIER_PER_SERVICE_GROUP: {
    measures: ['sumCost'],
    dimensions: ['supplierName', 'serviceType'],
    sorts: ['ascSumCost'],
  },
};


const arraysToCommaString = (filters) => {
  if(filters != null) {
    return Object.entries(filters).reduce((acc, [k, v]) => {
      const newAcc = { ...acc };
      if(v.length > 0)
        newAcc[k] = v.join(',');
      return newAcc;
    }, {});
  }
  return null;
};

const ApiChart = ({ title, x, y, config }) => {
  const [data, setData] = useState([]);
  const { state } = useContext(AppContext);

  useEffect(() => {
    const api = async () => {
      const result = await chartApi().getChart({ ...config, ...arraysToCommaString(state.filters) });
      setData(result);
    };
    api();
  }, [setData, config, state.filters]);

  return (
    <BarChart data={data} x={x} y={y} title={title}/>
  );
};

export default () => {
  return (
    <div>
      <Filters />
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