import React from 'react';
import { mount } from 'enzyme';
import ToolbarIconButton from './ToolbarIconButton';

describe('ToolbarIconButton', () => {
  it('Properly shows the ToolbarIconButton component', () => {
    const wrapper = mount(<ToolbarIconButton />);
    expect(wrapper).toMatchSnapshot();
  });
});
