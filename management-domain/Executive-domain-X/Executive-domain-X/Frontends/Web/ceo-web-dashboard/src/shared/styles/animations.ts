// Animation timing constants
export const ANIMATION_DURATION = {
  fast: 150,
  normal: 200,
  slow: 300,
  slower: 400,
} as const

// Easing functions
export const EASING = {
  ease: 'cubic-bezier(0.4, 0, 0.2, 1)',
  easeOut: 'cubic-bezier(0, 0, 0.2, 1)',
  easeIn: 'cubic-bezier(0.4, 0, 1, 1)',
  bounce: 'cubic-bezier(0.68, -0.55, 0.265, 1.55)',
} as const

// Card entrance animation variants
export const cardVariants = {
  hidden: {
    opacity: 0,
    y: 20,
    scale: 0.96,
  },
  visible: {
    opacity: 1,
    y: 0,
    scale: 1,
    transition: {
      duration: ANIMATION_DURATION.slower / 1000,
      ease: EASING.easeOut,
    },
  },
}

// Stagger children animation
export const staggerContainer = {
  hidden: { opacity: 0 },
  visible: {
    opacity: 1,
    transition: {
      staggerChildren: 0.1,
      delayChildren: 0.1,
    },
  },
}

export const staggerItem = {
  hidden: { opacity: 0, y: 10 },
  visible: {
    opacity: 1,
    y: 0,
    transition: {
      duration: ANIMATION_DURATION.normal / 1000,
      ease: EASING.easeOut,
    },
  },
}

// Input focus animation
export const inputFocusVariants = {
  unfocused: {
    borderColor: '#E2E8F0',
    backgroundColor: '#F8FAFC',
  },
  focused: {
    borderColor: '#3B82F6',
    backgroundColor: '#FFFFFF',
    boxShadow: '0 0 0 3px rgba(59, 130, 246, 0.1)',
    transition: {
      duration: ANIMATION_DURATION.normal / 1000,
      ease: EASING.ease,
    },
  },
}

// Button variants
export const buttonVariants = {
  idle: {
    scale: 1,
    boxShadow: '0 1px 2px rgba(0, 0, 0, 0.05)',
  },
  hover: {
    scale: 1.01,
    y: -2,
    boxShadow: '0 4px 12px rgba(0, 0, 0, 0.15)',
    transition: {
      duration: ANIMATION_DURATION.normal / 1000,
      ease: EASING.ease,
    },
  },
  tap: {
    scale: 0.98,
    transition: {
      duration: ANIMATION_DURATION.fast / 1000,
      ease: EASING.easeIn,
    },
  },
}

// Checkmark animation for success state
export const checkmarkPathVariants = {
  hidden: {
    pathLength: 0,
    opacity: 0,
  },
  visible: {
    pathLength: 1,
    opacity: 1,
    transition: {
      pathLength: { duration: 0.5, ease: 'easeInOut' },
      opacity: { duration: 0.1 },
    },
  },
}

// Shake animation for errors
export const shakeVariants = {
  hidden: { x: 0 },
  visible: {
    x: [0, -10, 10, -10, 10, 0],
    transition: {
      duration: 0.4,
    },
  },
}
