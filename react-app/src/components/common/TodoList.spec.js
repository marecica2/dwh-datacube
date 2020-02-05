import React from 'react';
import { mount } from 'enzyme';
import { TodoList } from './TodoList';

describe('TodoList', () => {
  test('renders', () => {
    mount(<TodoList />);
  });

  test('initially displays 3 items, first of them done', () => {
    const wrapper = mount(<TodoList />);
    expect(wrapper.find('li')).toHaveLength(3);
    expect(
      wrapper.find('input[type=\'checkbox\']').map(el => el.getDOMNode().checked),
    ).toEqual([true, false, false]);
  });

  test('adds a new item', () => {
    const wrapper = mount(<TodoList />);
    wrapper.find('input[type=\'text\']').getDOMNode().value = 'New item';
    wrapper
      .find('button')
      .last()
      .simulate('click');
    expect(wrapper.find('li')).toHaveLength(4);
    expect(
      wrapper
        .find('li span')
        .last()
        .text(),
    ).toEqual('New item');
  });

  test('removes an item', () => {
    const wrapper = mount(<TodoList />);
    wrapper
      .find('li button')
      .first()
      .simulate('click');
    expect(wrapper.find('li')).toHaveLength(2);
    expect(wrapper.find('li span').map(el => el.text())).toEqual([
      'Prepare a demo',
      'Prepare presentation slides',
    ]);
  });
});