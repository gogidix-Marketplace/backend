import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import { Card, CardContent } from '@shared/components/ui/card'
import { Switch } from '@shared/components/ui/switch'
import { Badge } from '@shared/components/ui/badge'
import { ChevronRight, ChevronLeft, Check, User, Shield, Bell, LayoutDashboard, CheckCircle2, Sparkles, Building2, Clock } from 'lucide-react'
import { cn } from '@shared/utils/cn'

/**
 * Production-grade Onboarding Page for New Executives
 * Premium design with executive blue/gold theme and smooth animations
 */

type Step = 'welcome' | 'profile' | 'security' | 'notifications' | 'tour' | 'complete'

interface ProfileData {
  firstName: string
  lastName: string
  displayName: string
  timezone: string
  language: string
}

interface SecurityData {
  enable2FA: boolean
  passwordConfirmed: boolean
  backupCodesSaved: boolean
}

interface NotificationData {
  emailAlerts: boolean
  pushAlerts: boolean
  smsAlerts: boolean
  criticalOnly: boolean
  quietHours: boolean
  quietStart: string
  quietEnd: string
}

const TIMEZONES = [
  { value: 'UTC', label: 'UTC (Universal)' },
  { value: 'America/New_York', label: 'Eastern Time (ET)' },
  { value: 'America/Chicago', label: 'Central Time (CT)' },
  { value: 'America/Denver', label: 'Mountain Time (MT)' },
  { value: 'America/Los_Angeles', label: 'Pacific Time (PT)' },
  { value: 'Europe/London', label: 'London (GMT/BST)' },
  { value: 'Africa/Lagos', label: 'West Africa Time (WAT)' },
  { value: 'Africa/Nairobi', label: 'East Africa Time (EAT)' },
  { value: 'Asia/Dubai', label: 'Gulf Standard Time (GST)' },
]

const LANGUAGES = [
  { code: 'en', name: 'English', flag: '🇬🇧' },
  { code: 'fr', name: 'Français', flag: '🇫🇷' },
  { code: 'es', name: 'Español', flag: '🇪🇸' },
  { code: 'pt', name: 'Português', flag: '🇵🇹' },
]

const TOUR_FEATURES = [
  {
    id: 'overview',
    title: 'Executive Overview',
    description: 'View your strategic health score and key performance indicators with real-time updates.',
    icon: '📊',
    gradient: 'from-blue-500 to-cyan-500',
  },
  {
    id: 'approvals',
    title: 'Pending Approvals',
    description: 'Review and approve budget requests, hiring decisions, and strategic initiatives.',
    icon: '✅',
    gradient: 'from-green-500 to-emerald-500',
  },
  {
    id: 'analytics',
    title: 'Advanced Analytics',
    description: 'Deep dive into cross-domain performance with AI-powered insights and forecasting.',
    icon: '📈',
    gradient: 'from-purple-500 to-pink-500',
  },
  {
    id: 'reports',
    title: 'Executive Reports',
    description: 'Access comprehensive reports for board meetings and stakeholder updates.',
    icon: '📄',
    gradient: 'from-amber-500 to-orange-500',
  },
]

export default function OnboardingPage() {
  const navigate = useNavigate()
  const [currentStep, setCurrentStep] = useState<Step>('welcome')

  const [profile, setProfile] = useState<ProfileData>({
    firstName: '',
    lastName: '',
    displayName: '',
    timezone: 'America/New_York',
    language: 'en',
  })

  const [security, setSecurity] = useState<SecurityData>({
    enable2FA: true,
    passwordConfirmed: false,
    backupCodesSaved: false,
  })

  const [notifications, setNotifications] = useState<NotificationData>({
    emailAlerts: true,
    pushAlerts: true,
    smsAlerts: false,
    criticalOnly: false,
    quietHours: true,
    quietStart: '22:00',
    quietEnd: '08:00',
  })

  const [tourFeatureIndex, setTourFeatureIndex] = useState(0)
  const [focusedField, setFocusedField] = useState<string | null>(null)

  const steps: Step[] = ['welcome', 'profile', 'security', 'notifications', 'tour', 'complete']
  const currentStepIndex = steps.indexOf(currentStep)

  const handleNext = async () => {
    if (currentStep === 'complete') {
      navigate('/')
      return
    }

    // Validation
    if (currentStep === 'profile') {
      if (!profile.firstName || !profile.lastName) {
        return
      }
    }

    if (currentStep === 'security' && !security.passwordConfirmed) {
      return
    }

    if (currentStep === 'tour' && tourFeatureIndex < TOUR_FEATURES.length - 1) {
      setTourFeatureIndex(tourFeatureIndex + 1)
      return
    }

    setCurrentStep(steps[currentStepIndex + 1])
  }

  const handleBack = () => {
    if (currentStep === 'tour' && tourFeatureIndex > 0) {
      setTourFeatureIndex(tourFeatureIndex - 1)
      return
    }
    if (currentStepIndex > 0) {
      setCurrentStep(steps[currentStepIndex - 1])
    }
  }

  const handleSkip = () => {
    setCurrentStep('complete')
  }

  const renderStep = () => {
    switch (currentStep) {
      case 'welcome':
        return <WelcomeStep onNext={handleNext} />
      case 'profile':
        return (
          <ProfileStep
            data={profile}
            onChange={setProfile}
            onNext={handleNext}
            onBack={handleBack}
            onSkip={handleSkip}
            focusedField={focusedField}
            setFocusedField={setFocusedField}
          />
        )
      case 'security':
        return (
          <SecurityStep
            data={security}
            onChange={setSecurity}
            onNext={handleNext}
            onBack={handleBack}
            onSkip={handleSkip}
          />
        )
      case 'notifications':
        return (
          <NotificationsStep
            data={notifications}
            onChange={setNotifications}
            onNext={handleNext}
            onBack={handleBack}
            onSkip={handleSkip}
          />
        )
      case 'tour':
        return (
          <TourStep
            featureIndex={tourFeatureIndex}
            onNext={handleNext}
            onBack={handleBack}
            onSkip={handleSkip}
          />
        )
      case 'complete':
        return <CompleteStep onNext={handleNext} />
      default:
        return null
    }
  }

  return (
    <div className="executive-bg min-h-screen flex items-center justify-center p-4 py-8">
      {/* Decorative Elements */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-20 left-10 w-64 h-64 bg-[#FFA000] opacity-5 rounded-full blur-3xl animate-pulse" />
        <div className="absolute bottom-20 right-10 w-96 h-96 bg-white opacity-5 rounded-full blur-3xl animate-pulse" />
      </div>

      <div className="w-full max-w-2xl relative z-10">
        <Card className="glass-card border-0 shadow-2xl overflow-hidden">
          {/* Progress Bar */}
          {currentStep !== 'welcome' && currentStep !== 'complete' && (
            <div className="border-b border-blue-400/20 px-8 py-5 bg-[#0D47A1]/5">
              <div className="flex items-center justify-between mb-3">
                <span className="text-sm font-medium text-white">
                  Step {currentStepIndex} of {steps.length - 2}
                </span>
                <Badge className="bg-[#FFA000] text-white border-0">
                  {Math.round((currentStepIndex / (steps.length - 2)) * 100)}%
                </Badge>
              </div>
              <div className="h-2 w-full overflow-hidden rounded-full bg-blue-900/50">
                <div
                  className="h-full bg-gradient-to-r from-[#FFA000] to-[#FF8F00] transition-all duration-500 ease-out"
                  style={{ width: `${(currentStepIndex / (steps.length - 2)) * 100}%` }}
                />
              </div>
              {/* Step indicators */}
              <div className="flex justify-between mt-4">
                {steps.slice(1, -1).map((step, i) => {
                  const stepNum = i + 1
                  const isPast = stepNum < currentStepIndex
                  const isCurrent = stepNum === currentStepIndex
                  return (
                    <div key={step} className="flex flex-col items-center gap-1">
                      <div
                        className={cn(
                          'w-8 h-8 rounded-full flex items-center justify-center text-xs font-bold transition-all duration-300',
                          isPast && 'bg-[#FFA000] text-white',
                          isCurrent && 'bg-white text-[#0D47A1] ring-2 ring-[#FFA000]',
                          !isPast && !isCurrent && 'bg-blue-900/50 text-blue-300'
                        )}
                      >
                        {isPast ? <Check className="h-4 w-4" /> : stepNum}
                      </div>
                    </div>
                  )
                })}
              </div>
            </div>
          )}

          <CardContent className="p-8 min-h-[500px]">{renderStep()}</CardContent>
        </Card>
      </div>
    </div>
  )
}

// Step Components with premium design

function WelcomeStep({ onNext }: { onNext: () => void }) {
  return (
    <div className="space-y-8 text-center py-8 animate-fade-in-up">
      {/* Logo */}
      <div className="flex justify-center">
        <div className="relative">
          <div className="absolute inset-0 bg-[#FFA000] opacity-20 blur-xl rounded-full" />
          <div className="relative flex h-24 w-24 items-center justify-center rounded-2xl bg-gradient-to-br from-[#FFA000] to-[#FF8F00] shadow-lg">
            <Building2 className="h-12 w-12 text-white" />
          </div>
          <div className="absolute -bottom-2 -right-2 flex h-8 w-8 items-center justify-center rounded-full bg-green-500 border-2 border-white">
            <Sparkles className="h-4 w-4 text-white" />
          </div>
        </div>
      </div>

      {/* Title */}
      <div>
        <h1 className="text-4xl font-bold text-white mb-3">Welcome, Executive</h1>
        <p className="text-blue-100 text-lg">
          Let's configure your executive dashboard in just a few steps
        </p>
      </div>

      {/* Feature Preview */}
      <div className="grid grid-cols-3 gap-4 py-6">
        {[
          { icon: '👤', title: 'Profile', desc: 'Your executive profile' },
          { icon: '🔒', title: 'Security', desc: '2FA & protection' },
          { icon: '🔔', title: 'Alerts', desc: 'Smart notifications' },
        ].map((feature, i) => (
          <div
            key={i}
            className="p-4 rounded-xl bg-blue-500/10 border border-blue-400/20 animate-fade-in-up"
            style={{ animationDelay: `${0.1 + i * 0.1}s` }}
          >
            <div className="text-3xl mb-2">{feature.icon}</div>
            <p className="text-sm font-medium text-white">{feature.title}</p>
            <p className="text-xs text-blue-200">{feature.desc}</p>
          </div>
        ))}
      </div>

      {/* CTA Button */}
      <Button
        size="lg"
        className="h-14 px-8 bg-gradient-to-r from-[#FFA000] to-[#FF8F00] hover:from-[#FF8F00] hover:to-[#FF6F00] text-white font-medium shadow-lg hover:shadow-xl transition-all duration-300"
        onClick={onNext}
      >
        <span className="flex items-center justify-center gap-2 text-lg">
          Get Started <ChevronRight className="h-5 w-5" />
        </span>
      </Button>
    </div>
  )
}

function ProfileStep({
  data,
  onChange,
  onNext,
  onBack,
  onSkip,
  focusedField,
  setFocusedField,
}: {
  data: ProfileData
  onChange: (data: ProfileData) => void
  onNext: () => void
  onBack: () => void
  onSkip: () => void
  focusedField: string | null
  setFocusedField: (val: string | null) => void
}) {
  const isComplete = data.firstName && data.lastName

  return (
    <div className="space-y-6 animate-fade-in-up">
      {/* Header */}
      <div className="text-center mb-8">
        <div className="inline-flex items-center justify-center w-16 h-16 rounded-full bg-gradient-to-br from-[#FFA000] to-[#FF8F00] mb-4">
          <User className="h-8 w-8 text-white" />
        </div>
        <h2 className="text-2xl font-bold text-white mb-2">Profile Setup</h2>
        <p className="text-blue-200">Tell us about yourself</p>
      </div>

      {/* Form */}
      <div className="space-y-5">
        <div className="grid grid-cols-2 gap-5">
          <div className="space-y-2">
            <Label htmlFor="firstName" className="text-blue-100 text-sm font-medium">
              First Name <span className="text-[#FFA000]">*</span>
            </Label>
            <Input
              id="firstName"
              placeholder="John"
              value={data.firstName}
              onChange={(e) => onChange({ ...data, firstName: e.target.value })}
              onFocus={() => setFocusedField('firstName')}
              onBlur={() => setFocusedField(null)}
              className="executive-input h-11 text-slate-900"
              required
            />
          </div>
          <div className="space-y-2">
            <Label htmlFor="lastName" className="text-blue-100 text-sm font-medium">
              Last Name <span className="text-[#FFA000]">*</span>
            </Label>
            <Input
              id="lastName"
              placeholder="Mitchell"
              value={data.lastName}
              onChange={(e) => onChange({ ...data, lastName: e.target.value })}
              onFocus={() => setFocusedField('lastName')}
              onBlur={() => setFocusedField(null)}
              className="executive-input h-11 text-slate-900"
              required
            />
          </div>
        </div>

        <div className="space-y-2">
          <Label htmlFor="displayName" className="text-blue-100 text-sm font-medium">
            Display Name
          </Label>
          <Input
            id="displayName"
            placeholder="John Mitchell - CEO"
            value={data.displayName}
            onChange={(e) => onChange({ ...data, displayName: e.target.value })}
            onFocus={() => setFocusedField('displayName')}
            onBlur={() => setFocusedField(null)}
            className="executive-input h-11 text-slate-900"
          />
          <p className="text-xs text-blue-200">How your name appears in the dashboard</p>
        </div>

        <div className="grid grid-cols-2 gap-5">
          <div className="space-y-2">
            <Label htmlFor="timezone" className="text-blue-100 text-sm font-medium">
              Timezone
            </Label>
            <select
              id="timezone"
              className="executive-input h-11 w-full text-slate-900"
              value={data.timezone}
              onChange={(e) => onChange({ ...data, timezone: e.target.value })}
            >
              {TIMEZONES.map((tz) => (
                <option key={tz.value} value={tz.value}>
                  {tz.label}
                </option>
              ))}
            </select>
          </div>
          <div className="space-y-2">
            <Label htmlFor="language" className="text-blue-100 text-sm font-medium">
              Language
            </Label>
            <select
              id="language"
              className="executive-input h-11 w-full text-slate-900"
              value={data.language}
              onChange={(e) => onChange({ ...data, language: e.target.value })}
            >
              {LANGUAGES.map((lang) => (
                <option key={lang.code} value={lang.code}>
                  {lang.flag} {lang.name}
                </option>
              ))}
            </select>
          </div>
        </div>
      </div>

      {/* Navigation */}
      <div className="flex justify-between pt-4 border-t border-blue-400/20">
        <Button
          variant="outline"
          className="border-blue-300/30 text-blue-100 hover:bg-blue-500/10"
          onClick={onBack}
        >
          <ChevronLeft className="mr-2 h-4 w-4" /> Back
        </Button>
        <div className="flex gap-3">
          <Button
            variant="ghost"
            className="text-blue-200 hover:text-white hover:bg-blue-500/10"
            onClick={onSkip}
          >
            Skip
          </Button>
          <Button
            className="bg-gradient-to-r from-[#FFA000] to-[#FF8F00] hover:from-[#FF8F00] hover:to-[#FF6F00] text-white shadow-lg"
            onClick={onNext}
            disabled={!isComplete}
          >
            Next <ChevronRight className="ml-2 h-4 w-4" />
          </Button>
        </div>
      </div>
    </div>
  )
}

function SecurityStep({
  data,
  onChange,
  onNext,
  onBack,
  onSkip,
}: {
  data: SecurityData
  onChange: (data: SecurityData) => void
  onNext: () => void
  onBack: () => void
  onSkip: () => void
}) {
  const isComplete = data.passwordConfirmed && (!data.enable2FA || data.backupCodesSaved)

  return (
    <div className="space-y-6 animate-fade-in-up">
      {/* Header */}
      <div className="text-center mb-8">
        <div className="inline-flex items-center justify-center w-16 h-16 rounded-full bg-gradient-to-br from-green-500 to-emerald-600 mb-4">
          <Shield className="h-8 w-8 text-white" />
        </div>
        <h2 className="text-2xl font-bold text-white mb-2">Security Setup</h2>
        <p className="text-blue-200">Protect your account with enhanced security</p>
      </div>

      {/* 2FA Toggle */}
      <div className="p-5 rounded-xl bg-blue-500/10 border border-blue-400/20">
        <div className="flex items-center justify-between">
          <div>
            <h3 className="text-white font-medium mb-1">Two-Factor Authentication</h3>
            <p className="text-sm text-blue-200">
              Add an extra layer of security with authenticator app
            </p>
          </div>
          <Switch
            checked={data.enable2FA}
            onCheckedChange={(checked) => onChange({ ...data, enable2FA: !!checked })}
            className="data-[state=checked]:bg-[#FFA000]"
          />
        </div>
      </div>

      {/* 2FA Details */}
      {data.enable2FA && (
        <div className="p-5 rounded-xl bg-slate-900/50 border border-slate-700 space-y-4 animate-fade-in-up">
          <div className="flex items-center gap-2 text-green-400">
            <CheckCircle2 className="h-5 w-5" />
            <span className="font-medium">Authenticator app ready</span>
          </div>

          {/* Backup Codes */}
          {!data.backupCodesSaved && (
            <div className="p-4 rounded-lg bg-amber-500/10 border border-amber-500/30">
              <p className="text-sm text-amber-200 mb-3">
                ⚠️ Save these backup codes securely
              </p>
              <div className="grid grid-cols-4 gap-2 mb-3">
                {['AB12-CD34', 'EF56-GH78', 'IJ90-KL12', 'MN34-OP56'].map((code, i) => (
                  <code
                    key={i}
                    className="block bg-slate-800 text-amber-300 text-xs p-2 text-center font-mono rounded border border-amber-500/20"
                  >
                    {code}
                  </code>
                ))}
              </div>
              <Button
                variant="outline"
                size="sm"
                className="w-full bg-amber-500/10 border-amber-500/30 text-amber-200 hover:bg-amber-500/20"
                onClick={() => onChange({ ...data, backupCodesSaved: true })}
              >
                I've Saved These Codes
              </Button>
            </div>
          )}
        </div>
      )}

      {/* Password Confirmation */}
      <div className="p-5 rounded-xl bg-blue-500/10 border border-blue-400/20">
        <div className="flex items-center justify-between mb-3">
          <div>
            <h3 className="text-white font-medium">Confirm Password</h3>
            <p className="text-sm text-blue-200">Verify to complete setup</p>
          </div>
          {data.passwordConfirmed && (
            <Badge className="bg-green-500 text-white border-0 gap-1">
              <CheckCircle2 className="h-3 w-3" /> Verified
            </Badge>
          )}
        </div>

        {!data.passwordConfirmed && (
          <div className="space-y-3">
            <Input
              type="password"
              placeholder="Enter your password"
              className="executive-input h-11 text-slate-900"
            />
            <Button
              variant="outline"
              size="sm"
              className="w-full bg-blue-500/10 border-blue-400/30 text-blue-100 hover:bg-blue-500/20"
              onClick={() => onChange({ ...data, passwordConfirmed: true })}
            >
              Confirm Password
            </Button>
          </div>
        )}
      </div>

      {/* Navigation */}
      <div className="flex justify-between pt-4 border-t border-blue-400/20">
        <Button
          variant="outline"
          className="border-blue-300/30 text-blue-100 hover:bg-blue-500/10"
          onClick={onBack}
        >
          <ChevronLeft className="mr-2 h-4 w-4" /> Back
        </Button>
        <div className="flex gap-3">
          <Button
            variant="ghost"
            className="text-blue-200 hover:text-white hover:bg-blue-500/10"
            onClick={onSkip}
          >
            Skip
          </Button>
          <Button
            className="bg-gradient-to-r from-[#FFA000] to-[#FF8F00] hover:from-[#FF8F00] hover:to-[#FF6F00] text-white shadow-lg"
            onClick={onNext}
            disabled={!isComplete}
          >
            Next <ChevronRight className="ml-2 h-4 w-4" />
          </Button>
        </div>
      </div>
    </div>
  )
}

function NotificationsStep({
  data,
  onChange,
  onNext,
  onBack,
  onSkip,
}: {
  data: NotificationData
  onChange: (data: NotificationData) => void
  onNext: () => void
  onBack: () => void
  onSkip: () => void
}) {
  return (
    <div className="space-y-6 animate-fade-in-up">
      {/* Header */}
      <div className="text-center mb-8">
        <div className="inline-flex items-center justify-center w-16 h-16 rounded-full bg-gradient-to-br from-purple-500 to-pink-500 mb-4">
          <Bell className="h-8 w-8 text-white" />
        </div>
        <h2 className="text-2xl font-bold text-white mb-2">Notification Preferences</h2>
        <p className="text-blue-200">Choose how you want to receive alerts</p>
      </div>

      <div className="space-y-4">
        {/* Alert Channels */}
        <div className="space-y-3">
          <h3 className="text-sm font-medium text-blue-100 uppercase tracking-wider">Alert Channels</h3>

          {[
            { key: 'emailAlerts', icon: '📧', label: 'Email Notifications', desc: 'Receive alerts via email' },
            { key: 'pushAlerts', icon: '🔔', label: 'Push Notifications', desc: 'Browser push for urgent alerts' },
            { key: 'smsAlerts', icon: '📱', label: 'SMS Alerts', desc: 'Critical alerts via text' },
          ].map((item) => (
            <div
              key={item.key}
              className="flex items-center justify-between p-4 rounded-xl bg-blue-500/10 border border-blue-400/20 hover:border-[#FFA000]/50 transition-colors"
            >
              <div className="flex items-center gap-3">
                <span className="text-2xl">{item.icon}</span>
                <div>
                  <p className="text-white font-medium">{item.label}</p>
                  <p className="text-sm text-blue-200">{item.desc}</p>
                </div>
              </div>
              <Switch
                checked={data[item.key as keyof NotificationData]}
                onCheckedChange={(checked) => onChange({ ...data, [item.key]: !!checked })}
                className="data-[state=checked]:bg-[#FFA000]"
              />
            </div>
          ))}
        </div>

        {/* Quiet Hours */}
        <div className="p-5 rounded-xl bg-blue-500/10 border border-blue-400/20">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h3 className="text-white font-medium flex items-center gap-2">
                <Clock className="h-5 w-5 text-[#FFA000]" />
                Quiet Hours
              </h3>
              <p className="text-sm text-blue-200">Suppress notifications during specific hours</p>
            </div>
            <Switch
              checked={data.quietHours}
              onCheckedChange={(checked) => onChange({ ...data, quietHours: !!checked })}
              className="data-[state=checked]:bg-[#FFA000]"
            />
          </div>

          {data.quietHours && (
            <div className="grid grid-cols-2 gap-4 animate-fade-in-up">
              <div className="space-y-2">
                <Label className="text-blue-100 text-sm">Start Time</Label>
                <Input
                  type="time"
                  value={data.quietStart}
                  onChange={(e) => onChange({ ...data, quietStart: e.target.value })}
                  className="executive-input h-11 text-slate-900"
                />
              </div>
              <div className="space-y-2">
                <Label className="text-blue-100 text-sm">End Time</Label>
                <Input
                  type="time"
                  value={data.quietEnd}
                  onChange={(e) => onChange({ ...data, quietEnd: e.target.value })}
                  className="executive-input h-11 text-slate-900"
                />
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Navigation */}
      <div className="flex justify-between pt-4 border-t border-blue-400/20">
        <Button
          variant="outline"
          className="border-blue-300/30 text-blue-100 hover:bg-blue-500/10"
          onClick={onBack}
        >
          <ChevronLeft className="mr-2 h-4 w-4" /> Back
        </Button>
        <div className="flex gap-3">
          <Button
            variant="ghost"
            className="text-blue-200 hover:text-white hover:bg-blue-500/10"
            onClick={onSkip}
          >
            Skip
          </Button>
          <Button
            className="bg-gradient-to-r from-[#FFA000] to-[#FF8F00] hover:from-[#FF8F00] hover:to-[#FF6F00] text-white shadow-lg"
            onClick={onNext}
          >
            Next <ChevronRight className="ml-2 h-4 w-4" />
          </Button>
        </div>
      </div>
    </div>
  )
}

function TourStep({
  featureIndex,
  onNext,
  onBack,
  onSkip,
}: {
  featureIndex: number
  onNext: () => void
  onBack: () => void
  onSkip: () => void
}) {
  const feature = TOUR_FEATURES[featureIndex]
  const isLast = featureIndex === TOUR_FEATURES.length - 1

  return (
    <div className="space-y-8 animate-fade-in-up">
      {/* Header */}
      <div className="flex items-center justify-between">
        <Badge className="bg-[#FFA000] text-white border-0 px-3 py-1">
          Feature {featureIndex + 1} of {TOUR_FEATURES.length}
        </Badge>
        <Button
          variant="ghost"
          size="sm"
          className="text-blue-200 hover:text-white hover:bg-blue-500/10"
          onClick={onSkip}
        >
          Skip Tour
        </Button>
      </div>

      {/* Feature Display */}
      <div className="text-center py-12">
        <div
          className={cn(
            'inline-flex items-center justify-center w-32 h-32 rounded-3xl bg-gradient-to-br mb-6 shadow-2xl animate-fade-in-scale',
            feature.gradient
          )}
        >
          <span className="text-6xl">{feature.icon}</span>
        </div>
        <h2 className="text-3xl font-bold text-white mb-4">{feature.title}</h2>
        <p className="text-lg text-blue-200 max-w-md mx-auto">{feature.description}</p>
      </div>

      {/* Progress Dots */}
      <div className="flex justify-center gap-3">
        {TOUR_FEATURES.map((_, i) => (
          <div
            key={i}
            className={cn(
              'h-3 rounded-full transition-all duration-300',
              i === featureIndex ? 'w-8 bg-gradient-to-r from-[#FFA000] to-[#FF8F00]' : 'w-3 bg-blue-700'
            )}
          />
        ))}
      </div>

      {/* Navigation */}
      <div className="flex justify-between pt-4 border-t border-blue-400/20">
        <Button
          variant="outline"
          className="border-blue-300/30 text-blue-100 hover:bg-blue-500/10"
          onClick={onBack}
          disabled={featureIndex === 0}
        >
          <ChevronLeft className="mr-2 h-4 w-4" /> Back
        </Button>
        <Button
          className={cn(
            'bg-gradient-to-r from-[#FFA000] to-[#FF8F00] hover:from-[#FF8F00] hover:to-[#FF6F00] text-white shadow-lg',
            isLast && 'px-6'
          )}
          onClick={onNext}
        >
          {isLast ? (
            <>
              Finish <CheckCircle2 className="ml-2 h-5 w-5" />
            </>
          ) : (
            <>
              Next <ChevronRight className="ml-2 h-4 w-4" />
            </>
          )}
        </Button>
      </div>
    </div>
  )
}

function CompleteStep({ onNext }: { onNext: () => void }) {
  return (
    <div className="space-y-8 text-center py-12 animate-fade-in-up">
      {/* Success Icon */}
      <div className="flex justify-center mb-8">
        <div className="relative">
          <div className="absolute inset-0 bg-green-500 opacity-20 blur-3xl rounded-full animate-pulse" />
          <div className="relative flex h-24 w-24 items-center justify-center rounded-full bg-gradient-to-br from-green-500 to-emerald-600 shadow-2xl">
            <CheckCircle2 className="h-12 w-12 text-white" />
          </div>
        </div>
      </div>

      {/* Title */}
      <div>
        <h1 className="text-4xl font-bold text-white mb-3">You're All Set!</h1>
        <p className="text-xl text-blue-100">
          Your executive dashboard is ready to use
        </p>
      </div>

      {/* Completion List */}
      <div className="grid grid-cols-2 gap-4 max-w-md mx-auto mb-8">
        {[
          { icon: '✓', label: 'Profile configured' },
          { icon: '✓', label: 'Security settings enabled' },
          { icon: '✓', label: 'Notifications configured' },
          { icon: '✓', label: 'Dashboard tour completed' },
        ].map((item, i) => (
          <div
            key={i}
            className="flex items-center gap-3 p-3 rounded-lg bg-blue-500/10 border border-blue-400/20 animate-fade-in-up"
            style={{ animationDelay: `${i * 0.1}s` }}
          >
            <span className="text-xl">{item.icon}</span>
            <span className="text-white text-sm">{item.label}</span>
          </div>
        ))}
      </div>

      {/* CTA Button */}
      <Button
        size="lg"
        className="h-14 px-8 bg-gradient-to-r from-[#FFA000] to-[#FF8F00] hover:from-[#FF8F00] hover:to-[#FF6F00] text-white font-medium shadow-lg hover:shadow-xl transition-all duration-300 animate-pulse-gold"
        onClick={onNext}
      >
        <span className="flex items-center justify-center gap-2 text-lg">
          <LayoutDashboard className="h-5 w-5" />
          Go to Dashboard
          <ChevronRight className="h-5 w-5" />
        </span>
      </Button>

      {/* Footer */}
      <p className="text-sm text-blue-200 mt-8">
        You can always change these settings later
      </p>
    </div>
  )
}
