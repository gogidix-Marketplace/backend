/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        executive: {
          blue: '#0D47A1',
          'blue-dark': '#0A3D8C',
          'blue-light': '#1565C0',
          gold: '#FFA000',
          'gold-light': '#FFB300',
          slate: '#1E293B',
        },
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
      },
    },
  },
  plugins: [],
}
