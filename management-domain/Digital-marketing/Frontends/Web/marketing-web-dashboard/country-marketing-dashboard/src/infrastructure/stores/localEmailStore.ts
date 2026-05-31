// Local Email Store - Zustand
import { create } from 'zustand';
import { devtools } from 'zustand/middleware';
import { LocalEmailCampaign } from '../../domain/types';

const mockLocalEmails: LocalEmailCampaign[] = [
  {
    id: 'local-email-001',
    name: 'Weekly Newsletter - Week 8',
    subject: 'Your Weekly Update & Special Offers',
    preheader: 'See what\'s new this week...',
    fromName: 'Gogidix US',
    fromEmail: 'newsletters@gogidix.com',
    type: 'newsletter',
    status: 'sent',
    listId: 'list-001',
    listName: 'Subscribers',
    content: {
      html: '<html>...</html>',
      text: 'Plain text version...',
    },
    sentAt: new Date('2026-02-17T10:00:00'),
    metrics: {
      sent: 12500,
      delivered: 11875,
      opened: 3562,
      clicked: 890,
      bounced: 125,
      openRate: 30,
      clickRate: 25,
      bounceRate: 1,
    },
    country: 'US',
    createdBy: 'user-local-003',
    createdAt: new Date('2026-02-16T14:00:00'),
    updatedAt: new Date('2026-02-17T10:00:00'),
  },
  {
    id: 'local-email-002',
    name: 'Spring Sale Announcement',
    subject: '🌸 Spring Sale is Here! 50% Off Storewide',
    preheader: 'Limited time offer - shop now',
    fromName: 'Gogidix US',
    fromEmail: 'promo@gogidix.com',
    type: 'promotion',
    status: 'scheduled',
    listId: 'list-001',
    listName: 'Subscribers',
    content: {
      html: '<html>...</html>',
      text: 'Plain text version...',
    },
    scheduledFor: new Date('2026-02-25T10:00:00'),
    metrics: {
      sent: 0,
      delivered: 0,
      opened: 0,
      clicked: 0,
      bounced: 0,
      openRate: 0,
      clickRate: 0,
      bounceRate: 0,
    },
    country: 'US',
    createdBy: 'user-local-003',
    createdAt: new Date('2026-02-18T14:00:00'),
    updatedAt: new Date('2026-02-18T14:00:00'),
  },
  {
    id: 'local-email-003',
    name: 'Event Invitation - VIP Preview',
    subject: 'You\'re Invited: VIP Product Preview Event',
    preheader: 'Exclusive access to new products',
    fromName: 'Gogidix US',
    fromEmail: 'events@gogidix.com',
    type: 'event_invite',
    status: 'draft',
    listId: 'list-002',
    listName: 'VIP Customers',
    content: {
      html: '<html>...</html>',
      text: 'Plain text version...',
    },
    metrics: {
      sent: 0,
      delivered: 0,
      opened: 0,
      clicked: 0,
      bounced: 0,
      openRate: 0,
      clickRate: 0,
      bounceRate: 0,
    },
    country: 'US',
    createdBy: 'user-local-003',
    createdAt: new Date('2026-02-19T09:00:00'),
    updatedAt: new Date('2026-02-19T09:00:00'),
  },
];

interface LocalEmailState {
  emails: LocalEmailCampaign[];
  selectedEmail: LocalEmailCampaign | null;
  filters: {
    type?: string;
    status?: string;
  };
  isLoading: boolean;
  error: string | null;

  setEmails: (emails: LocalEmailCampaign[]) => void;
  setSelectedEmail: (email: LocalEmailCampaign | null) => void;
  setFilters: (filters: Partial<LocalEmailState['filters']>) => void;
  addEmail: (email: LocalEmailCampaign) => void;
  updateEmail: (id: string, updates: Partial<LocalEmailCampaign>) => void;
  deleteEmail: (id: string) => void;
  setLoading: (loading: boolean) => void;
  setError: (error: string | null) => void;
  getFilteredEmails: () => LocalEmailCampaign[];
}

export const useLocalEmailStore = create<LocalEmailState>()(
  devtools(
    (set, get) => ({
      emails: mockLocalEmails,
      selectedEmail: null,
      filters: {},
      isLoading: false,
      error: null,

      setEmails: (emails) => set({ emails }),
      setSelectedEmail: (email) => set({ selectedEmail: email }),
      setFilters: (newFilters) =>
        set((state) => ({
          filters: { ...state.filters, ...newFilters },
        })),
      addEmail: (email) =>
        set((state) => ({
          emails: [...state.emails, email],
        })),
      updateEmail: (id, updates) =>
        set((state) => ({
          emails: state.emails.map((e) =>
            e.id === id ? { ...e, ...updates } : e
          ),
          selectedEmail:
            state.selectedEmail?.id === id
              ? { ...state.selectedEmail, ...updates }
              : state.selectedEmail,
        })),
      deleteEmail: (id) =>
        set((state) => ({
          emails: state.emails.filter((e) => e.id !== id),
          selectedEmail:
            state.selectedEmail?.id === id ? null : state.selectedEmail,
        })),
      setLoading: (loading) => set({ isLoading: loading }),
      setError: (error) => set({ error }),

      getFilteredEmails: () => get().emails,
    }),
    { name: 'LocalEmailStore' }
  )
);
