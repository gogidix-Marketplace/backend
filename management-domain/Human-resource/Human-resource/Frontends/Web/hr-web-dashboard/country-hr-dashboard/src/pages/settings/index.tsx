'use client';

import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useForm } from 'react-hook-form';
import { useAuth } from '../../hooks/useAuth';
import { ProtectedRoute } from '../../components/common/ProtectedRoute';
import { DashboardLayout } from '../../components/layout/DashboardLayout';
import { hrApi, User } from '../../api/hrApi';

const SettingsPage: React.FC = () => {
  const { user, logout } = useAuth();
  const queryClient = useQueryClient();
  const [activeTab, setActiveTab] = useState<'profile' | 'notifications' | 'security' | 'audit'>('profile');
  const [saveMessage, setSaveMessage] = useState<{ type: 'success' | 'error'; text: string } | null>(null);

  const { data: profile, isLoading } = useQuery({
    queryKey: ['profile'],
    queryFn: () => hrApi.getProfile(),
  });

  const updateProfileMutation = useMutation({
    mutationFn: (data: Partial<User>) => hrApi.updateProfile(data),
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

  const { register: registerProfile, handleSubmit: handleSubmitProfile } = useForm<User>({
    defaultValues: profile || user,
  });

  const onSubmitProfile = (data: User) => {
    updateProfileMutation.mutate(data);
  };

  return (
    <ProtectedRoute>
      <DashboardLayout>
        <div className="space-y-6">
          {/* Header */}
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Settings</h1>
            <p className="text-gray-600">Manage your account and preferences</p>
          </div>

          <div className="bg-white rounded-lg shadow">
            {/* Tabs */}
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
              {saveMessage && (
                <div className={`mb-6 p-4 rounded-lg ${
                  saveMessage.type === 'success' ? 'bg-green-50 text-green-800' : 'bg-red-50 text-red-800'
                }`}>
                  {saveMessage.text}
                </div>
              )}

              {activeTab === 'profile' && (
                <div className="max-w-2xl">
                  <h3 className="text-lg font-semibold text-gray-900 mb-6">Profile Information</h3>

                  {isLoading ? (
                    <div className="flex items-center justify-center h-64">
                      <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
                    </div>
                  ) : (
                    <form onSubmit={handleSubmitProfile(onSubmitProfile)} className="space-y-6">
                      {/* Avatar */}
                      <div className="flex items-center gap-6">
                        <div className="h-20 w-20 rounded-full bg-blue-100 flex items-center justify-center">
                          <span className="text-2xl font-semibold text-blue-600">
                            {user?.firstName?.[0]}{user?.lastName?.[0]}
                          </span>
                        </div>
                        <div>
                          <button type="button" className="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">
                            Change Photo
                          </button>
                          <p className="text-xs text-gray-500 mt-1">JPG, PNG or GIF. Max 2MB.</p>
                        </div>
                      </div>

                      {/* Name */}
                      <div className="grid grid-cols-2 gap-4">
                        <div>
                          <label className="block text-sm font-medium text-gray-700 mb-2">
                            First Name
                          </label>
                          <input
                            type="text"
                            {...registerProfile('firstName')}
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                            defaultValue={profile?.firstName || user?.firstName}
                          />
                        </div>
                        <div>
                          <label className="block text-sm font-medium text-gray-700 mb-2">
                            Last Name
                          </label>
                          <input
                            type="text"
                            {...registerProfile('lastName')}
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                            defaultValue={profile?.lastName || user?.lastName}
                          />
                        </div>
                      </div>

                      {/* Email */}
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                          Email Address
                        </label>
                        <input
                          type="email"
                          {...registerProfile('email')}
                          className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 bg-gray-50"
                          defaultValue={profile?.email || user?.email}
                          disabled
                        />
                        <p className="text-xs text-gray-500 mt-1">Contact HR to change your email.</p>
                      </div>

                      {/* Role */}
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                          Role
                        </label>
                        <input
                          type="text"
                          value={profile?.role || user?.role || ''}
                          className="w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-50"
                          disabled
                        />
                      </div>

                      {/* Permissions */}
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                          Permissions
                        </label>
                        <div className="flex flex-wrap gap-2">
                          {(profile?.permissions || user?.permissions || []).map((permission) => (
                            <span
                              key={permission}
                              className="px-3 py-1 bg-blue-100 text-blue-800 rounded-full text-sm"
                            >
                              {permission}
                            </span>
                          ))}
                        </div>
                      </div>

                      {/* Actions */}
                      <div className="flex justify-end gap-4">
                        <button type="submit" className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
                          Save Changes
                        </button>
                      </div>
                    </form>
                  )}
                </div>
              )}

              {activeTab === 'notifications' && (
                <div className="max-w-2xl">
                  <h3 className="text-lg font-semibold text-gray-900 mb-6">Notification Preferences</h3>

                  <div className="space-y-6">
                    {[
                      {
                        category: 'Recruitment',
                        items: [
                          { name: 'New job applications', description: 'Get notified when new applications are submitted' },
                          { name: 'Interview reminders', description: 'Reminders for scheduled interviews' },
                          { name: 'Offer status changes', description: 'When offers are accepted or declined' },
                        ],
                      },
                      {
                        category: 'Performance',
                        items: [
                          { name: 'Review deadlines', description: 'Upcoming performance review deadlines' },
                          { name: 'Training completions', description: 'When team members complete training' },
                        ],
                      },
                      {
                        category: 'Compliance',
                        items: [
                          { name: 'Compliance alerts', description: 'Critical compliance issues and expirations' },
                          { name: 'Policy updates', description: 'When HR policies are updated' },
                        ],
                      },
                      {
                        category: 'System',
                        items: [
                          { name: 'Weekly summary', description: 'Weekly digest of HR activities' },
                          { name: 'Report ready', description: 'When scheduled reports are ready' },
                        ],
                      },
                    ].map((section) => (
                      <div key={section.category}>
                        <h4 className="font-medium text-gray-900 mb-3">{section.category}</h4>
                        <div className="space-y-3">
                          {section.items.map((item) => (
                            <div key={item.name} className="flex items-center justify-between py-3 border-b border-gray-100 last:border-0">
                              <div>
                                <p className="text-sm font-medium text-gray-700">{item.name}</p>
                                <p className="text-xs text-gray-500">{item.description}</p>
                              </div>
                              <label className="relative inline-flex items-center cursor-pointer">
                                <input type="checkbox" className="sr-only peer" defaultChecked={item.name !== 'Weekly summary'} />
                                <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
                              </label>
                            </div>
                          ))}
                        </div>
                      </div>
                    ))}

                    <div className="flex justify-end">
                      <button className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
                        Save Preferences
                      </button>
                    </div>
                  </div>
                </div>
              )}

              {activeTab === 'security' && (
                <div className="max-w-2xl">
                  <h3 className="text-lg font-semibold text-gray-900 mb-6">Security Settings</h3>

                  <div className="space-y-6">
                    {/* Change Password */}
                    <div className="bg-gray-50 rounded-lg p-6">
                      <h4 className="font-medium text-gray-900 mb-4">Change Password</h4>
                      <form className="space-y-4">
                        <div>
                          <label className="block text-sm font-medium text-gray-700 mb-2">
                            Current Password
                          </label>
                          <input
                            type="password"
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                          />
                        </div>
                        <div>
                          <label className="block text-sm font-medium text-gray-700 mb-2">
                            New Password
                          </label>
                          <input
                            type="password"
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                          />
                        </div>
                        <div>
                          <label className="block text-sm font-medium text-gray-700 mb-2">
                            Confirm New Password
                          </label>
                          <input
                            type="password"
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                          />
                        </div>
                        <button type="submit" className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
                          Update Password
                        </button>
                      </form>
                    </div>

                    {/* Two-Factor Authentication */}
                    <div className="bg-gray-50 rounded-lg p-6">
                      <div className="flex items-center justify-between">
                        <div>
                          <h4 className="font-medium text-gray-900">Two-Factor Authentication</h4>
                          <p className="text-sm text-gray-600 mt-1">Add an extra layer of security to your account</p>
                        </div>
                        <button className="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-100">
                          Enable
                        </button>
                      </div>
                    </div>

                    {/* Active Sessions */}
                    <div className="bg-gray-50 rounded-lg p-6">
                      <h4 className="font-medium text-gray-900 mb-4">Active Sessions</h4>
                      <div className="space-y-3">
                        {[
                          { device: 'Chrome on Windows', location: 'San Francisco, US', current: true },
                          { device: 'Safari on iPhone', location: 'San Francisco, US', current: false },
                        ].map((session, index) => (
                          <div key={index} className="flex items-center justify-between py-2">
                            <div>
                              <p className="text-sm font-medium text-gray-700">
                                {session.device}
                                {session.current && (
                                  <span className="ml-2 px-2 py-0.5 bg-blue-100 text-blue-800 text-xs rounded-full">
                                    Current
                                  </span>
                                )}
                              </p>
                              <p className="text-xs text-gray-500">{session.location}</p>
                            </div>
                            {!session.current && (
                              <button className="text-sm text-red-600 hover:text-red-700">
                                Revoke
                              </button>
                            )}
                          </div>
                        ))}
                      </div>
                    </div>
                  </div>
                </div>
              )}

              {activeTab === 'audit' && (
                <div>
                  <h3 className="text-lg font-semibold text-gray-900 mb-6">Audit Logs</h3>

                  <div className="overflow-x-auto">
                    <table className="min-w-full divide-y divide-gray-200">
                      <thead className="bg-gray-50">
                        <tr>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                            Timestamp
                          </th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                            User
                          </th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                            Action
                          </th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                            Resource
                          </th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                            IP Address
                          </th>
                        </tr>
                      </thead>
                      <tbody className="bg-white divide-y divide-gray-200">
                        {[
                          { timestamp: '2024-03-15 14:32:00', user: 'john.doe@gogidix.com', action: 'LOGIN', resource: '-', ip: '192.168.1.100' },
                          { timestamp: '2024-03-15 14:30:00', user: 'john.doe@gogidix.com', action: 'VIEW', resource: '/dashboard', ip: '192.168.1.100' },
                          { timestamp: '2024-03-15 14:25:00', user: 'jane.smith@gogidix.com', action: 'UPDATE', resource: 'Employee Profile', ip: '10.0.0.45' },
                          { timestamp: '2024-03-15 14:20:00', user: 'admin@gogidix.com', action: 'EXPORT', resource: 'Payroll Report', ip: '172.16.0.1' },
                          { timestamp: '2024-03-15 14:15:00', user: 'john.doe@gogidix.com', action: 'LOGOUT', resource: '-', ip: '192.168.1.100' },
                        ].map((log, index) => (
                          <tr key={index} className="hover:bg-gray-50">
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                              {log.timestamp}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                              {log.user}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <span className={`px-2 py-1 text-xs font-medium rounded-full ${
                                log.action === 'LOGIN' || log.action === 'LOGOUT' ? 'bg-blue-100 text-blue-800' :
                                log.action === 'UPDATE' ? 'bg-yellow-100 text-yellow-800' :
                                log.action === 'EXPORT' ? 'bg-purple-100 text-purple-800' :
                                'bg-gray-100 text-gray-800'
                              }`}>
                                {log.action}
                              </span>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                              {log.resource}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                              {log.ip}
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
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
