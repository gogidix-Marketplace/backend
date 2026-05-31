import { expect, afterEach } from 'vitest'
import { cleanup } from '@testing-library/react'
import '@testing-library/jest-dom'

// Cleanup after each test
afterEach(() => {
  cleanup()
})

// Mock crypto.randomUUID for tests
if (!global.crypto) {
  global.crypto = {} as Crypto
}
if (!global.crypto.randomUUID) {
  global.crypto.randomUUID = () => 'test-uuid-' + Math.random().toString(36).substr(2, 9)
}
