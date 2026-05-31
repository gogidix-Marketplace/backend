#!/usr/bin/env node

/**
 * Gogidix Management Domain - Check Dashboard Status
 *
 * This script checks the status of all dashboards.
 */

const http = require('http')

// Dashboard configurations
const dashboards = [
  { name: 'CEO Dashboard', id: 'ceo-dashboard', port: 3000 },
  { name: 'Executive Portal', id: 'executive-portal', port: 3001 },
  { name: 'Finance Dashboard', id: 'finance-dashboard', port: 3002 },
  { name: 'HR Dashboard', id: 'hr-dashboard', port: 3003 },
  { name: 'Sales Dashboard', id: 'sales-dashboard', port: 3004 },
  { name: 'Support Dashboard', id: 'support-dashboard', port: 3005 },
  { name: 'Admin Dashboard', id: 'admin-dashboard', port: 3006 },
  { name: 'GBM Portal', id: 'gbm-portal', port: 3007 },
  { name: 'Monitoring Dashboard', id: 'monitoring-dashboard', port: 3008 },
]

// Check if a port is responding
async function checkPort(port) {
  return new Promise((resolve) => {
    const req = http.get(`http://localhost:${port}`, (res) => {
      resolve(true)
    })
    req.on('error', () => resolve(false))
    req.setTimeout(1000, () => {
      req.destroy()
      resolve(false)
    })
  })
}

// Main execution
async function main() {
  console.log('╔═══════════════════════════════════════════════════════════════════╗')
  console.log('║   Gogidix Management Domain - Dashboard Status                    ║')
  console.log('╚═══════════════════════════════════════════════════════════════════╝\n')

  let running = 0

  for (const dashboard of dashboards) {
    const isRunning = await checkPort(dashboard.port)
    const status = isRunning ? '🟢 RUNNING' : '🔴 STOPPED'
    const url = `http://localhost:${dashboard.port}`

    console.log(`${status}  ${dashboard.name.padEnd(25)} ${url}`)

    if (isRunning) running++
  }

  console.log('\n' + '─'.repeat(70))
  console.log(`\nTotal: ${running}/${dashboards.length} dashboards running`)
  console.log(`Launcher: http://localhost:2999\n`)
}

main().catch(console.error)
