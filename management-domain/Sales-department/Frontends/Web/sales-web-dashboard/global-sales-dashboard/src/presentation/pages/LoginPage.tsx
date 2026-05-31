// Login Page
// Authentication page for HQ Sales Dashboard

import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuthStore, useUIStore } from '@infrastructure';
import { AuthLayout } from '../components/layouts/AuthLayout';
import { Button } from '../components/common/Button';
import type { LoginRequest } from '@domain';

export function LoginPage() {
  const navigate = useNavigate();
  const { login, isLoading, error } = useAuthStore();
  const { addNotification } = useUIStore();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const credentials: LoginRequest = { email, password };
      await login(credentials);
      addNotification({
        type: 'success',
        message: 'Welcome back! You have successfully logged in.',
      });
      navigate('/');
    } catch (err) {
      // Error is handled by the store
    }
  };

  return (
    <AuthLayout
      title="Sign In"
      subtitle="Enter your credentials to access the HQ Sales Dashboard"
    >
      <form onSubmit={handleSubmit} className="login-form">
        {error && (
          <div className="form-error">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <circle cx="12" cy="12" r="10" />
              <line x1="12" y1="8" x2="12" y2="12" />
              <line x1="12" y1="16" x2="12.01" y2="16" />
            </svg>
            <span>{error}</span>
          </div>
        )}

        <div className="form-group">
          <label htmlFor="email">Email Address</label>
          <input
            id="email"
            type="email"
            placeholder="john.doe@company.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            autoFocus
          />
        </div>

        <div className="form-group">
          <label htmlFor="password">Password</label>
          <input
            id="password"
            type="password"
            placeholder="Enter your password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>

        <div className="form-actions">
          <a href="/forgot-password" className="forgot-link">
            Forgot password?
          </a>
        </div>

        <Button
          type="submit"
          variant="primary"
          size="lg"
          fullWidth
          loading={isLoading}
        >
          Sign In
        </Button>

        <div className="form-footer">
          <p className="form-footer-text">
            Don't have an account?{' '}
            <a href="/register">Contact your administrator</a>
          </p>
        </div>
      </form>
    </AuthLayout>
  );
}
