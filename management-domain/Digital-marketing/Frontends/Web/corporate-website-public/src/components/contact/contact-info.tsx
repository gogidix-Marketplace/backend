'use client';

import { Mail, Phone, MapPin } from 'lucide-react';
import { Card } from '@/components/ui/card';

const contacts = [
  {
    type: 'Sales',
    description: 'Talk to our sales team about your needs',
    email: 'sales@gogidix.com',
    phone: '+1 (800) 123-4567',
  },
  {
    type: 'Support',
    description: 'Get help with your account or technical issues',
    email: 'support@gogidix.com',
    phone: '+1 (800) 987-6543',
  },
  {
    type: 'Partnerships',
    description: 'Explore partnership opportunities',
    email: 'partners@gogidix.com',
  },
  {
    type: 'Press Inquiries',
    description: 'Media and press related questions',
    email: 'press@gogidix.com',
  },
];

export function ContactInfo() {
  return (
    <div className="space-y-6">
      <Card className="p-6 bg-primary/5">
        <div className="flex items-center gap-3 mb-2">
          <MapPin className="h-5 w-5 text-primary" />
          <h3 className="font-semibold">Headquarters</h3>
        </div>
        <p className="text-sm text-muted-foreground">
          123 Market Street<br />
          San Francisco, CA 94105<br />
          United States
        </p>
      </Card>

      <div className="space-y-4">
        <h3 className="font-semibold">Contact Departments</h3>
        {contacts.map((contact) => (
          <Card key={contact.type} className="p-4">
            <h4 className="font-medium mb-1">{contact.type}</h4>
            <p className="text-sm text-muted-foreground mb-3">{contact.description}</p>
            <div className="space-y-2">
              <a
                href={`mailto:${contact.email}`}
                className="flex items-center gap-2 text-sm text-primary hover:underline"
              >
                <Mail className="h-4 w-4" />
                {contact.email}
              </a>
              {contact.phone && (
                <a
                  href={`tel:${contact.phone}`}
                  className="flex items-center gap-2 text-sm text-primary hover:underline"
                >
                  <Phone className="h-4 w-4" />
                  {contact.phone}
                </a>
              )}
            </div>
          </Card>
        ))}
      </div>

      <Card className="p-6 bg-gradient-to-br from-primary/10 to-accent/10">
        <h4 className="font-semibold mb-2">Office Hours</h4>
        <p className="text-sm text-muted-foreground">
          Monday - Friday: 9:00 AM - 6:00 PM PST<br />
          Saturday - Sunday: Closed
        </p>
      </Card>
    </div>
  );
}
