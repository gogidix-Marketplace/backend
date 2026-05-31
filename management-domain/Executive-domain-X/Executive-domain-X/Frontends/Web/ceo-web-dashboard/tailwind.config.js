/** @type {import('tailwindcss').Config} */
export default {
  darkMode: 'class',
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        border: 'hsl(var(--border))',
        input: 'hsl(var(--input))',
        ring: 'hsl(var(--ring))',
        background: 'hsl(var(--background))',
        foreground: 'hsl(var(--foreground))',
        primary: { DEFAULT: 'hsl(var(--primary))', foreground: 'hsl(var(--primary-foreground))' },
        secondary: { DEFAULT: 'hsl(var(--secondary))', foreground: 'hsl(var(--secondary-foreground))' },
        destructive: { DEFAULT: 'hsl(var(--destructive))', foreground: 'hsl(var(--destructive-foreground))' },
        muted: { DEFAULT: 'hsl(var(--muted))', foreground: 'hsl(var(--muted-foreground))' },
        accent: { DEFAULT: 'hsl(var(--accent))', foreground: 'hsl(var(--accent-foreground))' },
        popover: { DEFAULT: 'hsl(var(--popover))', foreground: 'hsl(var(--popover-foreground))' },
        card: { DEFAULT: 'hsl(var(--card))', foreground: 'hsl(var(--card-foreground))' },
        executive: { 50: '#E3F2FD', 100: '#BBDEFB', 500: '#2196F3', 700: '#1976D2', 900: '#0D47A1', 950: '#0A1929' },
        role: { ceo: '#FFA000', cfo: '#FF6B00', coo: '#00BCD4', cto: '#7C4DFF' },
        dept: { marketing: '#E91E63', support: '#00BCD4', business: '#4CAF50', hr: '#FF9800', sales: '#2196F3', sysadmin: '#9C27B0', finance: '#FF5722', foundation: '#607D8B' },
        bu: { courier: '#0EA5E9', ecommerce: '#8B5CF6', warehousing: '#F59E0B', airfreight: '#06B6D4', ocean: '#1E40AF', haulage: '#DC2626', procurement: '#059669', admin: '#475569' },
      },
      borderRadius: { lg: 'var(--radius)', md: 'calc(var(--radius) - 2px)', sm: 'calc(var(--radius) - 4px)' },
      keyframes: { 'fade-in-up': { from: { opacity: 0, transform: 'translateY(20px)' }, to: { opacity: 1, transform: 'translateY(0)' } }, shimmer: { '0%': { backgroundPosition: '-200% 0' }, '100%': { backgroundPosition: '200% 0' } } },
      animation: { 'fade-in-up': 'fade-in-up 0.6s ease-out forwards', shimmer: 'shimmer 2s infinite' },
    },
  },
  plugins: [],
}
