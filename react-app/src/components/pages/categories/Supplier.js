import React, {Component} from 'react';
import BarChart from '../../common/charts/BarChart';

const data = [{
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
  'country': 'France',
  'visits': 1114,
}, {
  'country': 'India',
  'visits': 984,
}, {
  'country': 'Spain',
  'visits': 711,
}, {
  'country': 'Netherlands',
  'visits': 665,
}, {
  'country': 'Russia',
  'visits': 580,
}, {
  'country': 'South Korea',
  'visits': 443,
}, {
  'country': 'Canada',
  'visits': 441,
}, {
  'country': 'Brazil',
  'visits': 395,
}];

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

class Supplier extends Component {
  async componentDidMount() {
  }

  render() {
    return (
        <div>
          <BarChart data={data} title="Spend per supplier"/>
          <BarChart data={data2} title="Spend per supplier per Category"/>
        </div>
    );
  }
}

export default Supplier;