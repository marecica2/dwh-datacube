const presets = [
  ['@babel/preset-env', { 'forceAllTransforms': true }],
  '@babel/preset-react',
];

const plugins = [
];

module.exports = (api) => {
  api.cache(true);

  return {
    presets,
    plugins,
  };
};
