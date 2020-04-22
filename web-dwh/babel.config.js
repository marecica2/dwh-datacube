const presets = [
  ['@babel/preset-env', { 'forceAllTransforms': true }],
  '@babel/preset-react',
];

const plugins = [
    '@babel/plugin-proposal-class-properties',
];

module.exports = (api) => {
  api.cache(true);

  return {
    presets,
    plugins,
  };
};
