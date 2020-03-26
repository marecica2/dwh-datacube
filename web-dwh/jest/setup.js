require('babel-polyfill');
require('raf/polyfill');

const Enzyme = require('enzyme');
const Adapter = require('enzyme-adapter-react-16');

const { shallow, render, mount } = Enzyme;
Enzyme.configure({ adapter: new Adapter() });

// Make Enzyme functions available in all test files without importing
global.shallow = shallow;
global.render = render;
global.mount = mount;
// need it for @amcharts4
global.SVGPathElement = Object;

// Fail tests on any warning
console.error = (message) => {
  console.warn(message);
  // throw new Error(message);
};

