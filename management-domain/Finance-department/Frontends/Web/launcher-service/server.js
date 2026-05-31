import express from 'express';
import { spawn } from 'child_process';
import cors from 'cors';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const app = express();
const PORT = 2999;

app.use(cors());
app.use(express.json());

// Store running processes
const runningProcesses = new Map();

// Dashboard configurations
const BASE_PATH = path.resolve(__dirname, '../..');
const dashboards = [
  { id: 'ceo-dashboard', name: 'CEO Dashboard', path: '../../../Executive-domain/Frontends/Web/ceo-web-dashboard', port: 3010 },
  { id: 'executive-dashboard', name: 'Executive Hub', path: '../../../Executive-domain/Frontends/Web/executive-web-dashboard', port: 3011 },
  { id: 'finance-hub', name: 'Finance Hub', path: '../../finance-web-dashboard', port: 3020 },
  { id: 'accountant-dashboard', name: 'Accountant', path: '../../finance-web-dashboard/accountant-dashboard', port: 3021 },
  { id: 'cfo-dashboard', name: 'CFO Dashboard', path: '../../finance-web-dashboard/cfo-dashboard', port: 3022 },
  { id: 'reports-dashboard', name: 'Reports', path: '../../finance-web-dashboard/reports-dashboard', port: 3023 },
  { id: 'finance-portal', name: 'Finance Portal', path: '../../finance-web-portal', port: 3024 },
  { id: 'hr-dashboard', name: 'HR Dashboard', path: '../../../Human-resource/Frontends/Web/hr-web-dashboard', port: 3030 },
  { id: 'hr-portal', name: 'HR Portal', path: '../../../Human-resource/Frontends/Web/hr-web-portal', port: 3031 },
  { id: 'sales-dashboard', name: 'Sales Dashboard', path: '../../../Sales-department/Frontends/Web/sales-web-dashboard', port: 3040 },
  { id: 'sales-portal', name: 'Sales Portal', path: '../../../Sales-department/Frontends/Web/sales-web-portal', port: 3041 },
  { id: 'global-marketing', name: 'Global Marketing', path: '../../../Digital-marketing/Frontends/Web/marketing-web-dashboard/global-marketing-dashboard', port: 3050 },
  { id: 'country-marketing', name: 'Country Marketing', path: '../../../Digital-marketing/Frontends/Web/marketing-web-dashboard/country-marketing-dashboard', port: 3051 },
  { id: 'marketing-portal', name: 'Marketing Portal', path: '../../../Digital-marketing/Frontends/Web/marketing-web-portal', port: 3052 },
  { id: 'corporate-admin', name: 'Corporate Admin', path: '../../../Digital-marketing/Frontends/Web/corporate-website-admin', port: 3053 },
  { id: 'support-dashboard', name: 'Support Dashboard', path: '../../../Customer-support/Frontends/Web/support-web-dashboard', port: 3060 },
  { id: 'support-portal', name: 'Support Portal', path: '../../../Customer-support/Frontends/Web/support-web-portal', port: 3061 }
];

// Helper function to start a dashboard
function startDashboard(dashboard) {
  if (runningProcesses.has(dashboard.id)) {
    return { success: false, message: 'Already running' };
  }

  const dashboardPath = path.resolve(BASE_PATH, dashboard.path);

  console.log(`Starting ${dashboard.name} from ${dashboardPath}`);

  const child = spawn('npm', ['run', 'dev'], {
    cwd: dashboardPath,
    shell: true,
    detached: false,
    stdio: 'ignore'
  });

  child.on('error', (err) => {
    console.error(`Failed to start ${dashboard.name}:`, err);
    runningProcesses.delete(dashboard.id);
  });

  child.on('exit', (code) => {
    console.log(`${dashboard.name} exited with code ${code}`);
    runningProcesses.delete(dashboard.id);
  });

  runningProcesses.set(dashboard.id, { process: child, dashboard });

  return { success: true, message: `Starting ${dashboard.name}...` };
}

// Helper function to stop a dashboard
function stopDashboard(dashboardId) {
  const procData = runningProcesses.get(dashboardId);
  if (!procData) {
    return { success: false, message: 'Not running' };
  }

  try {
    process.kill(-procData.process.pid, 'SIGTERM');
    runningProcesses.delete(dashboardId);
    return { success: true, message: 'Stopped' };
  } catch (err) {
    return { success: false, message: 'Failed to stop' };
  }
}

// API Routes
app.get('/api/status', (req, res) => {
  const status = dashboards.map(d => ({
    id: d.id,
    name: d.name,
    port: d.port,
    running: runningProcesses.has(d.id)
  }));
  res.json({ dashboards: status });
});

app.post('/api/start/:id', (req, res) => {
  const dashboard = dashboards.find(d => d.id === req.params.id);
  if (!dashboard) {
    return res.status(404).json({ success: false, message: 'Dashboard not found' });
  }

  const result = startDashboard(dashboard);
  res.json(result);
});

app.post('/api/start-all', async (req, res) => {
  const results = [];

  for (const dashboard of dashboards) {
    const result = startDashboard(dashboard);
    results.push({ id: dashboard.id, name: dashboard.name, ...result });
    // Small delay between starts
    await new Promise(resolve => setTimeout(resolve, 500));
  }

  res.json({ success: true, results });
});

app.post('/api/stop/:id', (req, res) => {
  const result = stopDashboard(req.params.id);
  res.json(result);
});

app.post('/api/stop-all', (req, res) => {
  const ids = Array.from(runningProcesses.keys());
  const results = ids.map(id => stopDashboard(id));

  res.json({ success: true, stopped: ids.length });
});

app.get('/api/health', (req, res) => {
  res.json({
    status: 'ok',
    service: 'Gogidix Launcher Service',
    running: runningProcesses.size,
    total: dashboards.length
  });
});

// Start server
app.listen(PORT, () => {
  console.log(`
╔════════════════════════════════════════════════════════╗
║   Gogidix Launcher Service                             ║
║   Running on http://localhost:${PORT}                      ║
║                                                        ║
║   This service manages all dashboard startup/shutdown  ║
╚════════════════════════════════════════════════════════╝
  `);
});

// Cleanup on exit
process.on('SIGINT', () => {
  console.log('\\nStopping all dashboards...');
  runningProcesses.forEach((procData) => {
    try {
      process.kill(-procData.process.pid, 'SIGTERM');
    } catch (err) {
      // Ignore
    }
  });
  process.exit(0);
});
