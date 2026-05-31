'use client';

import { useEffect, useState } from 'react';
import { leadApi } from '@/lib/api';
import { formatDistanceToNow } from 'date-fns';

interface Lead {
  id: string;
  leadNumber: string;
  firstName: string;
  lastName: string;
  email: string;
  company: string;
  status: string;
  leadScore: string;
  estimatedValue: number;
  createdAt: string;
}

interface LeadsTableProps {
  limit?: number;
}

export default function LeadsTable({ limit = 10 }: LeadsTableProps) {
  const [leads, setLeads] = useState<Lead[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadLeads();
  }, []);

  const loadLeads = async () => {
    try {
      const data = await leadApi.getAll(0, limit);
      setLeads(data.leads || []);
    } catch (error) {
      console.error('Failed to load leads:', error);
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status: string) => {
    const colors: Record<string, string> = {
      NEW: 'badge badge-info',
      CONTACTED: 'badge badge-warning',
      QUALIFIED: 'badge badge-primary',
      PROPOSAL: 'badge badge-primary',
      NEGOTIATION: 'badge badge-warning',
      WON: 'badge badge-success',
      LOST: 'badge badge-danger',
      UNRESPONSIVE: 'badge badge-danger',
    };
    return colors[status] || 'badge badge-info';
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center py-8">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  return (
    <div className="overflow-x-auto">
      <table className="table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Company</th>
            <th>Status</th>
            <th>Score</th>
            <th>Value</th>
            <th>Created</th>
          </tr>
        </thead>
        <tbody>
          {leads.length === 0 ? (
            <tr>
              <td colSpan={6} className="text-center text-gray-500 py-8">
                No leads found
              </td>
            </tr>
          ) : (
            leads.map((lead) => (
              <tr key={lead.id}>
                <td>
                  <div>
                    <p className="font-medium text-gray-900">
                      {lead.firstName} {lead.lastName}
                    </p>
                    <p className="text-xs text-gray-500">{lead.email}</p>
                  </div>
                </td>
                <td className="text-gray-900">{lead.company || '-'}</td>
                <td>
                  <span className={getStatusColor(lead.status)}>
                    {lead.status.replace('_', ' ')}
                  </span>
                </td>
                <td>
                  <span className={`inline-flex items-center px-2 py-1 rounded-full text-xs font-medium ${
                    lead.leadScore === 'A' ? 'bg-success-100 text-success-800' :
                    lead.leadScore === 'B' ? 'bg-primary-100 text-primary-800' :
                    lead.leadScore === 'C' ? 'bg-warning-100 text-warning-800' :
                    'bg-danger-100 text-danger-800'
                  }`}>
                    Score: {lead.leadScore}
                  </span>
                </td>
                <td className="text-gray-900">
                  {lead.estimatedValue ? `$${lead.estimatedValue.toLocaleString()}` : '-'}
                </td>
                <td className="text-gray-500 text-sm">
                  {formatDistanceToNow(new Date(lead.createdAt), { addSuffix: true })}
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}
