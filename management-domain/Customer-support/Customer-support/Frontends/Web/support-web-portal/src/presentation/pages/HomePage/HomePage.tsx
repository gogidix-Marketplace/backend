import { useEffect } from 'react';

export function HomePage() {
  useEffect(() => {
    document.title = 'Support Web Portal - Gogidix';
  }, []);

  return (
    <div className="page-container">
      <h1>Support Web Portal</h1>
      <p>Welcome to the Gogidix Management Portal</p>
      <div className="cards">
        <div className="card">
          <h2>Dashboard</h2>
          <p>View your analytics and metrics</p>
        </div>
        <div className="card">
          <h2>Reports</h2>
          <p>Generate and view reports</p>
        </div>
        <div className="card">
          <h2>Settings</h2>
          <p>Configure your preferences</p>
        </div>
      </div>
    </div>
  );
}
