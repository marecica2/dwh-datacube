const configRootDir = __dirname;

module.exports = {
  cacheDirectory: '<rootDir>/node_modules/.cache/jest',
  verbose: true,
  notify: false,
  collectCoverage: true,
  setupFiles: [
    `${configRootDir}/eventSourceMock.js`,
    `${configRootDir}/setup.js`,
  ],
  roots: ['<rootDir>/src'],
  snapshotSerializers: ['enzyme-to-json/serializer'],
  testEnvironment: 'jsdom',
  transform: {
    '^.+\\.js?$': `${configRootDir}/jestWrapper.js`,
  },
  coverageReporters: ['json', 'text', 'lcovonly', 'clover'],
  moduleNameMapper: {
    '\\.(jpg|jpeg|png|gif|eot|otf|webp|svg|ttf|woff|woff2|mp4|webm|wav|mp3|m4a|aac|oga)$': `${configRootDir}/fileMock.js`,
    '\\.(css|less)$': `${configRootDir}/styleMock.js`,
  },
  collectCoverageFrom : [
    'src/**/*.{js,jsx}',
    '!build/**',
  ],
  transformIgnorePatterns: ['node_modules/(?!(@amcharts)/)'],
};
