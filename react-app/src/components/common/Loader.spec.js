import React from 'react';
import { shallow } from 'enzyme';
import Loader from './Loader';

function setup(props = {}) {
  const wrapper = shallow(<Loader />);
  return { wrapper, props };
}

describe('Loader', () => {
  test('should display graphics', () => {
    const { wrapper } = setup();
    expect(wrapper.find('img').exists()).toBe(true);
  });

  test('should be wrapped in Box', () => {
    const props = {
      variant: 'wrapped',
    };

    const { wrapper } = setup(props);
    expect(wrapper.find('img').exists()).toBe(true);
  });
});