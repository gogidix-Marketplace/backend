#!/usr/bin/env node

/**
 * Gogidix Management Domain - Start All Dashboards
 *
 * This script starts all dashboards in parallel on their assigned ports.
 */

const { spawn } = require('child_process')
const path = require('path')

// Base path to Management-domain
const BASE_PATH = path.join(__dirname, '../../..')

// Dashboard configurations
const dashboards = [
  { name: 'CEO Dashboard', path: 'Executive-domain/Frontends/Web/ceo-web-dashboard', port: 3000 },
  { name: 'Executive Portal', path: 'Executive-domain/Frontends/Web/executive-web-portal', port: 3001 },
  { name: 'Finance Dashboard', path: 'Finance-department/Frontends/Web/finance-web-dashboard', port: 3002 },
  { name: 'HR Dashboard', path: 'Human-resource/Frontends/Web/hr-web-dashboard', port: 3003 },
  { name: 'Sales Dashboard', path: 'Sales-department/Frontends/Web/sales-web-dashboard', port: 3004 },
  { name: 'Support Dashboard', path: 'Customer-support/Frontends/Web/support-web-dashboard', port: 3005 },
  { name: 'Admin Dashboard', path: 'System-administrator/Frontends/Web/admin-web-dashboard', port: 3006 },
  { name: 'GBM Portal', path: 'Global-business-management/Frontends/Web/gbm-web-portal', port: 3007 },
  { name: 'Monitoring Dashboard', path: 'Foundation-Services-Monitoring/Frontends/Web/monitoring-web-dashboard', port: 3008 },
]

// Check if a port is in use
async function isPortInUse(port) {
  const net = require('net')
  return new Promise((resolve) => {
    const server = net.createServer()
    server.once('error', () => resolve(true))
    server.once('listening', () => {
      server.close()
      resolve(false)
    })
    server.listen(port, '127.0.0.1')
  })
}

// Start a single dashboard
async function startDashboard(config) {
  const fullPath = path.join(BASE_PATH, config.path)

  console.log(`\n🚀 Starting ${config.name}...`)
  console.log(`   Path: ${config.path}`)
  console.log(`   Port: ${config.port}`)

  // Check if port is already in use
  const portInUse = await isPortInUse(config.port)
  if (portInUse) {
    console.log(`   ⚠️  Port ${config.port} already in use - skipping`)
    return null
  }

  // Check if package.json exists
  const fs = require('fs')
  const packageJsonPath = path.join(fullPath, 'package.json')
  if (!fs.existsSync(packageJsonPath)) {
    console.log(`   ❌ No package.json found - skipping`)
    return null
  }

  // Check if node_modules exists
  const nodeModulesPath = path.join(fullPath, 'node_modules')
  if (!fs.existsSync(nodeModulesPath)) {
    console.log(`   ⚠️  Dependencies not installed - run install first`)
    return null
  }

  const child = spawn('npm', ['run', 'dev'], {
    cwd: fullPath,
    shell: true,
    stdio: 'pipe',
    env: { ...process.env, PORT: config.port },
  })

  child.stdout.on('data', (data) => {
    // Only print important messages
    const output = data.toString()
    if (output.includes('ready') || output.includes('listening') || output.includes('started')) {
      console.log(`   ✅ ${config.name} is ready on port ${config.port}`)
    }
  })

  child.stderr.on('data', (data) => {
    const output = data.toString()
    if (!output.includes('postcss') && !output.includes('vite:')) {
      console.error(`   ${config.name}: ${output.trim().substring(0, 100)}`)
    }
  })

  child.on('error', (err) => {
    console.error(`   ❌ Failed to start ${config.name}: ${err.message}`)
  })

  // Give it some time to start
  await new Promise(resolve => setTimeout(resolve, 2000))

  return child
}

// Main execution
async function main() {
  console.log('╔═══════════════════════════════════════════════════════════════════╗')
  console.log('║   Gogidix Management Domain - Starting All Dashboards             ║')
  console.log('╚═══════════════════════════════════════════════════════════════════╝')

  const children = []

  for (const dashboard of dashboards) {
    const child = await startDashboard(dashboard)
    if (child) {
      children.push({ ...dashboard, child })
    }
  }

  console.log('\n═══════════════════════════════════════════════════════════════════')
  console.log(`\n✅ Started ${children.length} out of ${dashboards.length} dashboards`)
  console.log('\n📊 Dashboard Launcher: http://localhost:2999')
  console.log('\nPress Ctrl+C to stop all dashboards\n')

  // Handle graceful shutdown
  process.on('SIGINT', () => {
    console.log('\n\n🛑 Stopping all dashboards...')
    children.forEach(({ child, name }) => {
      child.kill('SIGTERM')
      console.log(`   Stopped ${name}`)
    })
    process.exit(0)
  })

  // Keep the process alive
  await new Promise(() => {})
}

main().catch(console.error)
