import { useState } from 'react'
import { Link } from 'react-router-dom'
import {
  Search,
  BookOpen,
  FileText,
  Plus,
  Edit,
  Trash2,
  Eye,
  ThumbsUp,
  ThumbsDown,
  Star,
  ChevronRight,
  Filter,
} from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@shared/components/ui/select'
import { cn, formatNumber } from '@shared/utils/cn'
import { mockKBArticles, mockKBCategories } from '@shared/data/mockData'

export default function KnowledgePage() {
  const [searchQuery, setSearchQuery] = useState('')
  const [selectedCategory, setSelectedCategory] = useState<string>('all')
  const [viewMode, setViewMode] = useState<'grid' | 'list'>('grid')

  const filteredArticles = mockKBArticles.filter((article) => {
    const matchesSearch =
      searchQuery === '' ||
      article.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
      article.content.toLowerCase().includes(searchQuery.toLowerCase()) ||
      article.tags.some(tag => tag.toLowerCase().includes(searchQuery.toLowerCase()))

    const matchesCategory = selectedCategory === 'all' || article.category === selectedCategory

    return matchesSearch && matchesCategory && article.status === 'published'
  })

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div className="page-header">
          <h1 className="page-title">Knowledge Base</h1>
          <p className="page-description">
            Manage help articles and documentation
          </p>
        </div>
        <Button className="gap-2">
          <Plus className="h-4 w-4" />
          New Article
        </Button>
      </div>

      {/* Search and Filters */}
      <Card>
        <CardContent className="p-6">
          <div className="flex flex-col gap-4 md:flex-row">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
              <Input
                placeholder="Search articles..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-10 h-12"
              />
            </div>
            <Select value={selectedCategory} onValueChange={setSelectedCategory}>
              <SelectTrigger className="w-[200px] h-12">
                <SelectValue placeholder="All Categories" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Categories</SelectItem>
                {mockKBCategories.map((cat) => (
                  <SelectItem key={cat.id} value={cat.name}>
                    {cat.name}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            <div className="flex gap-2 border rounded-md p-1">
              <Button
                variant={viewMode === 'grid' ? 'default' : 'ghost'}
                size="icon"
                onClick={() => setViewMode('grid')}
              >
                <Filter className="h-4 w-4" />
              </Button>
              <Button
                variant={viewMode === 'list' ? 'default' : 'ghost'}
                size="icon"
                onClick={() => setViewMode('list')}
              >
                <FileText className="h-4 w-4" />
              </Button>
            </div>
          </div>
        </CardContent>
      </Card>

      <div className="grid gap-6 lg:grid-cols-4">
        {/* Categories Sidebar */}
        <Card className="lg:col-span-1">
          <CardHeader>
            <CardTitle className="text-base">Categories</CardTitle>
          </CardHeader>
          <CardContent className="p-2">
            <div className="space-y-1">
              <button
                className={cn(
                  'w-full flex items-center justify-between rounded-md px-3 py-2 text-sm font-medium transition-colors',
                  selectedCategory === 'all'
                    ? 'bg-[#1976D2] text-white'
                    : 'hover:bg-slate-100 dark:hover:bg-slate-800'
                )}
                onClick={() => setSelectedCategory('all')}
              >
                <span>All Articles</span>
                <Badge variant={selectedCategory === 'all' ? 'secondary' : 'outline'}>
                  {mockKBCategories.reduce((sum, cat) => sum + cat.articleCount, 0)}
                </Badge>
              </button>
              {mockKBCategories.map((category) => (
                <button
                  key={category.id}
                  className={cn(
                    'w-full flex items-center justify-between rounded-md px-3 py-2 text-sm font-medium transition-colors',
                    selectedCategory === category.name
                      ? 'bg-[#1976D2] text-white'
                      : 'hover:bg-slate-100 dark:hover:bg-slate-800'
                  )}
                  onClick={() => setSelectedCategory(category.name)}
                >
                  <div className="flex items-center gap-2">
                    <BookOpen className="h-4 w-4" />
                    <span>{category.name}</span>
                  </div>
                  <Badge variant={selectedCategory === category.name ? 'secondary' : 'outline'}>
                    {category.articleCount}
                  </Badge>
                </button>
              ))}
            </div>
          </CardContent>
        </Card>

        {/* Articles Grid/List */}
        <div className="lg:col-span-3 space-y-4">
          <div className="flex items-center justify-between">
            <p className="text-sm text-muted-foreground">
              Showing {filteredArticles.length} articles
            </p>
            <Select defaultValue="updated">
              <SelectTrigger className="w-[160px]">
                <SelectValue placeholder="Sort by" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="updated">Recently Updated</SelectItem>
                <SelectItem value="views">Most Viewed</SelectItem>
                <SelectItem value="helpful">Most Helpful</SelectItem>
                <SelectItem value="title">Title A-Z</SelectItem>
              </SelectContent>
            </Select>
          </div>

          {viewMode === 'grid' ? (
            <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
              {filteredArticles.map((article) => (
                <Card key={article.id} className="group hover:shadow-md transition-shadow">
                  <CardHeader className="pb-3">
                    <div className="flex items-start justify-between gap-2">
                      <Badge variant="outline" className="shrink-0">
                        {article.category}
                      </Badge>
                      <div className="flex gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
                        <Button variant="ghost" size="icon" className="h-8 w-8">
                          <Edit className="h-3 w-3" />
                        </Button>
                        <Button variant="ghost" size="icon" className="h-8 w-8 text-red-600">
                          <Trash2 className="h-3 w-3" />
                        </Button>
                      </div>
                    </div>
                    <Link to={`/knowledge/${article.slug}`}>
                      <CardTitle className="text-base hover:text-[#1976D2] transition-colors line-clamp-2">
                        {article.title}
                      </CardTitle>
                    </Link>
                    <CardDescription className="line-clamp-2">{article.excerpt}</CardDescription>
                  </CardHeader>
                  <CardContent className="space-y-3">
                    <div className="flex flex-wrap gap-1">
                      {article.tags.slice(0, 3).map((tag) => (
                        <Badge key={tag} variant="secondary" className="text-xs">
                          {tag}
                        </Badge>
                      ))}
                    </div>
                    <div className="flex items-center justify-between text-xs text-muted-foreground">
                      <div className="flex items-center gap-3">
                        <span className="flex items-center gap-1">
                          <Eye className="h-3 w-3" />
                          {formatNumber(article.views)}
                        </span>
                        <span className="flex items-center gap-1">
                          <ThumbsUp className="h-3 w-3" />
                          {article.helpfulCount}
                        </span>
                      </div>
                      <span>Updated {new Date(article.updatedAt).toLocaleDateString()}</span>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          ) : (
            <div className="space-y-2">
              {filteredArticles.map((article) => (
                <Card key={article.id} className="group">
                  <CardContent className="p-4">
                    <div className="flex items-start gap-4">
                      <div className="h-10 w-10 rounded-lg bg-slate-100 dark:bg-slate-800 flex items-center justify-center">
                        <FileText className="h-5 w-5 text-muted-foreground" />
                      </div>
                      <div className="flex-1 min-w-0">
                        <div className="flex items-start justify-between gap-2">
                          <div className="flex-1">
                            <Link to={`/knowledge/${article.slug}`}>
                              <h3 className="font-medium hover:text-[#1976D2] transition-colors">
                                {article.title}
                              </h3>
                            </Link>
                                <p className="text-sm text-muted-foreground line-clamp-1 mt-1">
                                  {article.excerpt}
                                </p>
                              </div>
                              <div className="flex gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
                                <Button variant="ghost" size="icon" className="h-8 w-8">
                                  <Edit className="h-3 w-3" />
                                </Button>
                                <Button variant="ghost" size="icon" className="h-8 w-8 text-red-600">
                                  <Trash2 className="h-3 w-3" />
                                </Button>
                              </div>
                            </div>
                            <div className="flex items-center gap-4 mt-2 text-xs text-muted-foreground">
                              <Badge variant="outline" className="text-xs">
                                {article.category}
                              </Badge>
                              <span>{formatNumber(article.views)} views</span>
                              <span>{article.helpfulCount} helpful</span>
                              <span>Updated {new Date(article.updatedAt).toLocaleDateString()}</span>
                            </div>
                          </div>
                        </div>
                      </CardContent>
                    </Card>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
