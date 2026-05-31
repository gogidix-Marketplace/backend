import { useState } from 'react';
import { Search, BookOpen, FileText, Video, MessageSquare } from 'lucide-react';

const categories = [
  { id: 'getting-started', name: 'Getting Started', icon: BookOpen, articles: 24 },
  { id: 'troubleshooting', name: 'Troubleshooting', icon: FileText, articles: 56 },
  { id: 'billing', name: 'Billing & Payments', icon: FileText, articles: 18 },
  { id: 'api', name: 'API Documentation', icon: Video, articles: 42 },
  { id: 'faqs', name: 'FAQs', icon: MessageSquare, articles: 35 },
];

const popularArticles = [
  { id: 1, title: 'How to reset your password', category: 'Getting Started', views: 15420 },
  { id: 2, title: 'Understanding your billing statement', category: 'Billing', views: 12350 },
  { id: 3, title: 'API authentication guide', category: 'API', views: 9870 },
  { id: 4, title: 'Troubleshooting login issues', category: 'Troubleshooting', views: 8650 },
  { id: 5, title: 'Exporting your data', category: 'Getting Started', views: 7430 },
];

export function KnowledgeBasePage() {
  const [searchQuery, setSearchQuery] = useState('');

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Knowledge Base</h1>
        <p className="text-gray-500 mt-1">Find answers to common questions and helpful resources</p>
      </div>

      {/* Search */}
      <div className="relative">
        <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
        <input
          type="text"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          placeholder="Search for articles, guides, and FAQs..."
          className="w-full pl-12 pr-4 py-4 border border-gray-300 rounded-xl focus:ring-2 focus:ring-primary-500 focus:border-primary-500 outline-none text-lg"
        />
      </div>

      {/* Categories */}
      <div>
        <h2 className="text-lg font-semibold text-gray-900 mb-4">Browse by Category</h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {categories.map((category) => (
            <button
              key={category.id}
              className="bg-white rounded-xl p-6 shadow-sm border border-gray-100 hover:shadow-md hover:border-primary-200 transition-all text-left"
            >
              <category.icon className="w-8 h-8 text-primary-600 mb-3" />
              <h3 className="font-semibold text-gray-900">{category.name}</h3>
              <p className="text-sm text-gray-500 mt-1">{category.articles} articles</p>
            </button>
          ))}
        </div>
      </div>

      {/* Popular Articles */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-100">
          <h2 className="text-lg font-semibold text-gray-900">Popular Articles</h2>
        </div>
        <div className="divide-y divide-gray-100">
          {popularArticles.map((article) => (
            <button
              key={article.id}
              className="w-full px-6 py-4 flex items-center justify-between hover:bg-gray-50 transition-colors text-left"
            >
              <div className="flex-1">
                <h3 className="font-medium text-gray-900">{article.title}</h3>
                <p className="text-sm text-gray-500 mt-1">{article.category}</p>
              </div>
              <span className="text-sm text-gray-400">{article.views.toLocaleString()} views</span>
            </button>
          ))}
        </div>
      </div>

      {/* Contact Support */}
      <div className="bg-gradient-to-r from-primary-50 to-primary-100 rounded-xl p-8 text-center">
        <h2 className="text-xl font-bold text-gray-900 mb-2">Can't find what you're looking for?</h2>
        <p className="text-gray-600 mb-4">Our support team is here to help you 24/7</p>
        <button className="inline-flex items-center gap-2 px-6 py-3 bg-primary-600 text-white font-medium rounded-lg hover:bg-primary-700 transition-colors">
          <MessageSquare className="w-4 h-4" />
          Contact Support
        </button>
      </div>

      {/* Live Chat Widget Placeholder */}
      <div className="fixed bottom-6 right-6 z-50">
        <button className="w-14 h-14 bg-primary-600 rounded-full shadow-lg flex items-center justify-center text-white hover:bg-primary-700 transition-colors">
          <MessageSquare className="w-6 h-6" />
        </button>
      </div>
    </div>
  );
}
