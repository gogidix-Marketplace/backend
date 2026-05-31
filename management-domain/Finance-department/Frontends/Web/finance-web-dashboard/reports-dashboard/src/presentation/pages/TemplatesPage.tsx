import { useNavigate } from 'react-router-dom'
import {
  TrendingUp,
  Scale,
  DollarSign,
  BarChart3,
  Globe,
  Shield,
  FileText,
  Palette,
  Plus,
  Clock,
} from 'lucide-react'
import { useReportsStore } from '@shared/store/reportsStore'
import { cn } from '@shared/utils/cn'

const ICON_MAP: Record<string, React.ElementType> = {
  TrendingUp,
  Scale,
  DollarSign,
  BarChart3,
  Globe,
  Shield,
  FileText,
  Palette,
}

const CATEGORY_COLORS: Record<string, string> = {
  financial: 'bg-blue-50 text-blue-700',
  operational: 'bg-green-50 text-green-700',
  compliance: 'bg-purple-50 text-purple-700',
  custom: 'bg-amber-50 text-amber-700',
}

export default function TemplatesPage() {
  const { templates } = useReportsStore()
  const navigate = useNavigate()

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Report Templates</h1>
          <p className="text-gray-500 mt-1">Choose a template to generate a new financial report</p>
        </div>
        <button className="flex items-center gap-2 px-4 py-2.5 bg-amber-600 hover:bg-amber-700 text-white rounded-lg font-medium transition-colors">
          <Plus size={18} />
          Create Custom Template
        </button>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
        {templates.map((template) => {
          const Icon = ICON_MAP[template.icon] || FileText
          const categoryColor = CATEGORY_COLORS[template.category] || 'bg-gray-50 text-gray-700'

          return (
            <div
              key={template.id}
              className="bg-white rounded-xl border border-gray-200 hover:border-amber-300 hover:shadow-md transition-all p-5 flex flex-col"
            >
              <div className="flex items-start justify-between mb-3">
                <div className="p-2.5 bg-amber-50 rounded-lg">
                  <Icon size={22} className="text-amber-600" />
                </div>
                <span className={cn('text-xs font-medium px-2 py-0.5 rounded-full', categoryColor)}>
                  {template.category}
                </span>
              </div>

              <h3 className="font-semibold text-gray-900 mb-1">{template.name}</h3>
              <p className="text-sm text-gray-500 mb-4 flex-1">{template.description}</p>

              <div className="space-y-3">
                <div className="flex flex-wrap gap-1">
                  {template.sections.slice(0, 3).map((section) => (
                    <span
                      key={section}
                      className="text-xs bg-gray-100 text-gray-600 px-2 py-0.5 rounded"
                    >
                      {section}
                    </span>
                  ))}
                  {template.sections.length > 3 && (
                    <span className="text-xs text-gray-400 px-1 py-0.5">
                      +{template.sections.length - 3} more
                    </span>
                  )}
                </div>

                <div className="flex items-center justify-between pt-3 border-t border-gray-100">
                  <div className="flex items-center gap-1 text-xs text-gray-400">
                    <Clock size={12} />
                    Last used: {new Date(template.lastUsed).toLocaleDateString()}
                  </div>
                  <button
                    onClick={() => navigate('/generate')}
                    className="px-3 py-1.5 text-sm font-medium text-amber-600 hover:text-white hover:bg-amber-600 border border-amber-300 hover:border-amber-600 rounded-lg transition-colors"
                  >
                    Use Template
                  </button>
                </div>
              </div>
            </div>
          )
        })}
      </div>
    </div>
  )
}
