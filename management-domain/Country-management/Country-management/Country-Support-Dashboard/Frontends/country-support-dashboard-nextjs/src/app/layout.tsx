import type { Metadata } from 'next';
import './globals.css';

export const metadata: Metadata = {
  title: 'Country Support Dashboard',
  description: 'Country-level customer support dashboard for Gogidix ecosystem',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  );
}
