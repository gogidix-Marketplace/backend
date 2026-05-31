module.exports = {
  parserOptions: {
    ecmaVersion: 2022,
    sourceType: 'module',
  },
  extends: ['eslint:recommended'],
  root: true,
  env: { node: true, jest: true, es6: true },
  ignorePatterns: ['.eslintrc.js', 'node_modules/', 'coverage/'],
  rules: {
    'no-unused-vars': 'warn',
    'no-console': 'off',
  },
};
