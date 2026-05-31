module.exports = {
  parserOptions: {
    ecmaVersion: 2022,
    sourceType: 'module',
  },
  extends: ['eslint:recommended'],
  root: true,
  env: { node: true, jest: true, es6: true },
  ignorePatterns: ['.eslintrc.js', 'node_modules/', 'coverage/', 'src/__tests__/'],
  rules: {
    'no-unused-vars': 'off',
    'no-console': 'off',
    'no-undef': 'off',
    'no-case-declarations': 'off',
    'no-unreachable': 'off',
    'no-prototype-builtins': 'off',
  },
};
