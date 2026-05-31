import { renderHook, act } from '@testing-library/react';
import { useUIStore } from '../use-ui-store';

// Mock zustand persistence
jest.useFakeTimers();

describe('useUIStore', () => {
  beforeEach(() => {
    // Reset store before each test
    const { result } = renderHook(() => useUIStore());
    act(() => {
      result.current.setSidebarOpen(false);
      result.current.setSearchOpen(false);
      result.current.setMobileMenuOpen(false);
    });
  });

  it('initializes with default values', () => {
    const { result } = renderHook(() => useUIStore());
    expect(result.current.sidebarOpen).toBe(false);
    expect(result.current.searchOpen).toBe(false);
    expect(result.current.mobileMenuOpen).toBe(false);
    expect(result.current.cookieConsent).toBe(null);
    expect(result.current.locale).toBe('en');
    expect(result.current.theme).toBe('system');
  });

  it('toggles sidebar state', () => {
    const { result } = renderHook(() => useUIStore());

    act(() => {
      result.current.toggleSidebar();
    });
    expect(result.current.sidebarOpen).toBe(true);

    act(() => {
      result.current.toggleSidebar();
    });
    expect(result.current.sidebarOpen).toBe(false);
  });

  it('toggles search state', () => {
    const { result } = renderHook(() => useUIStore());

    act(() => {
      result.current.toggleSearch();
    });
    expect(result.current.searchOpen).toBe(true);
  });

  it('toggles mobile menu state', () => {
    const { result } = renderHook(() => useUIStore());

    act(() => {
      result.current.toggleMobileMenu();
    });
    expect(result.current.mobileMenuOpen).toBe(true);
  });

  it('sets cookie consent', () => {
    const { result } = renderHook(() => useUIStore());

    act(() => {
      result.current.setCookieConsent('accepted');
    });
    expect(result.current.cookieConsent).toBe('accepted');
  });

  it('sets locale', () => {
    const { result } = renderHook(() => useUIStore());

    act(() => {
      result.current.setLocale('fr');
    });
    expect(result.current.locale).toBe('fr');
  });

  it('sets theme', () => {
    const { result } = renderHook(() => useUIStore());

    act(() => {
      result.current.setTheme('dark');
    });
    expect(result.current.theme).toBe('dark');
  });

  it('sets region', () => {
    const { result } = renderHook(() => useUIStore());

    act(() => {
      result.current.setRegion('eu');
    });
    expect(result.current.region).toBe('eu');
  });
});
