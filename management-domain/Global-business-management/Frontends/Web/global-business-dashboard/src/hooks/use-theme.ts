import { useEffect } from 'react';
import { useDashboardStore } from '../stores/dashboard-store';

export function useTheme() {
  const { darkMode, setDarkMode } = useDashboardStore();

  useEffect(() => {
    const root = window.document.documentElement;
    root.classList.remove('light', 'dark');

    if (darkMode) {
      root.classList.add('dark');
    } else {
      root.classList.add('light');
    }
  }, [darkMode]);

  return { darkMode, setDarkMode, toggleTheme: () => setDarkMode(!darkMode) };
}

export function useSystemTheme() {
  useEffect(() => {
    const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');

    const handleChange = (e: MediaQueryListEvent) => {
      const { setDarkMode } = useDashboardStore.getState();
      if (useDashboardStore.getState().settings.theme === 'system') {
        setDarkMode(e.matches);
      }
    };

    mediaQuery.addEventListener('change', handleChange);
    return () => mediaQuery.removeEventListener('change', handleChange);
  }, []);
}
