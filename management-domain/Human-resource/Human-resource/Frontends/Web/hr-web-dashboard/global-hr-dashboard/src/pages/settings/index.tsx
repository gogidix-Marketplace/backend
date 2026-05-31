'use client';

import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useForm } from 'react-hook-form';
import { useAuth } from '../../hooks/useAuth';
import { ProtectedRoute } from '../../components/common/ProtectedRoute';
import { DashboardLayout } from '../../components/layout/DashboardLayout';
import { globalHrApi, GlobalUser } from '../../api/globalHrApi';

const SettingsPage: React.FC = () => {
  const { user, logout } = useAuth();
  const queryClient = useQueryClient();
  const [activeTab, setActiveTab] = useState<'profile' | 'notifications' | 'security' | 'audit'>('profile');
  const [saveMessage, setSaveMessage] = useState<{ type: 'success' | 'error'; text: string } | null>(null);

  const { data: profile, isLoading } = useQuery({
    queryKey: ['profile'],
    queryFn: () => globalHrApi.getProfile(),
  });

  const updateProfileMutation = useMutation({
    mutationFn: (data: Partial<GlobalUser>) => globalHrApi.updateProfile(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['profile'] });
      setSaveMessage({ type: 'success', text: 'Profile updated successfully!' });
      setTimeout(() => setSaveMessage(null), 3000);
    },
    onError: () => {
      setSaveMessage({ type: 'error', text: 'Failed to update profile.' });
      setTimeout(() => setSaveMessage(null), 3000);
    },
  });

  const { register: registerProfile, handleSubmit: handleSubmitProfile } = useForm<GlobalUser>({
    defaultValues: profile || user,
  });

  const onSubmitProfile = (data: GlobalUser) => {
    updateProfileMutation.mutate(data);
  };

  return (
    <ProtectedRoute>
      <DashboardLayout>
        <div className="space-y-6">
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Settings</h1>
            <p className="text-gray-600">Manage your account and preferences</p>
          </div>

          <div className="bg-white rounded-lg shadow">
            <div className="border-b border-gray-200">
              <nav className="flex -mb-px">
                {[
                  { id: 'profile', label: 'Profile' },
                  { id: 'notifications', label: 'Notifications' },
                  { id: 'security', label: 'Security' },
                  { id: 'audit', label: 'Audit Logs' },
                ].map((tab) => (
                  <button
                    key={tab.id}
                    onClick={() => setActiveTab(tab.id as any)}
                    className={`px-6 py-4 text-sm font-medium border-b-2 transition-colors ${
                      activeTab === tab.id
                        ? 'border-blue-500 text-blue-600'
                        : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                    }`}
                  >
                    {tab.label}
                  </button>
                ))}
              </nav>
            </div>

            <div className="p-6">
              {activeTab === 'profile' && (
                <div className="max-w-2xl">
                  <h3 className="text-lg font-semibold text-gray-900 mb-6">Profile Information</h3>
                  <form onSubmit={handleSubmitProfile(onSubmitProfile)} className="space-y-6">
                    <div className="flex items-center gap-6">
                      <div className="h-20 w-20 rounded-full bg-blue-100 flex items-center justify-center">
                        <span className="text-2xl font-semibold text-blue-600">A</span>
                      </div>
                    </div>
                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">First Name</label>
                        <input type="text" {...registerProfile('firstName')} className="w-full px-4 py-2 border border-gray-300 rounded-lg" />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">Last Name</label>
                        <input type="text" {...registerProfile('lastName')} className="w-full px-4 py-2 border border-gray-300 rounded-lg" />
                      </div>
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">Email</label>
                      <input type="email" {...registerProfile('email')} className="w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-50" disabled />
                    </div>
                    <button type="submit" className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">Save Changes</button>
                  </form>
                </div>
              )}
            </div>
          </div>
        </div>
      </DashboardLayout>
    </ProtectedRoute>
  );
};

export default SettingsPage;
