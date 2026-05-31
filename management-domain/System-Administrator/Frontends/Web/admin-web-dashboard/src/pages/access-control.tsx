import { useState } from 'react'
import { Search, Plus, Filter, MoreHorizontal, UserPlus, Shield, Edit, Trash2 } from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@shared/components/ui/table'
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from '@shared/components/ui/tabs'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@shared/components/ui/dropdown-menu'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@shared/components/ui/dialog'
import { Label } from '@shared/components/ui/label'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@shared/components/ui/select'
import { Checkbox } from '@shared/components/ui/checkbox'
import { cn, formatDateTime } from '@shared/utils/cn'
import { mockUsers, mockRoles } from '@shared/data/mockData'

export default function AccessControlPage() {
  const [searchQuery, setSearchQuery] = useState('')
  const [selectedTab, setSelectedTab] = useState('users')

  const filteredUsers = mockUsers.filter(user =>
    user.displayName.toLowerCase().includes(searchQuery.toLowerCase()) ||
    user.email.toLowerCase().includes(searchQuery.toLowerCase())
  )

  const filteredRoles = mockRoles.filter(role =>
    role.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
    role.description.toLowerCase().includes(searchQuery.toLowerCase())
  )

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div className="page-header">
          <h1 className="page-title">Access Control</h1>
          <p className="page-description">
            Manage users, roles, and permissions across the system
          </p>
        </div>
        <div className="flex gap-2">
          <Button variant="outline">
            <Filter className="mr-2 h-4 w-4" />
            Filter
          </Button>
          <Button variant="admin">
            <Plus className="mr-2 h-4 w-4" />
            Add User
          </Button>
        </div>
      </div>

      {/* Search */}
      <div className="relative">
        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
        <Input
          placeholder="Search users, roles, permissions..."
          className="pl-9"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </div>

      {/* Tabs */}
      <Tabs value={selectedTab} onValueChange={setSelectedTab}>
        <TabsList>
          <TabsTrigger value="users">Users ({mockUsers.length})</TabsTrigger>
          <TabsTrigger value="roles">Roles ({mockRoles.length})</TabsTrigger>
          <TabsTrigger value="permissions">Permissions</TabsTrigger>
        </TabsList>

        {/* Users Tab */}
        <TabsContent value="users">
          <Card>
            <CardHeader>
              <CardTitle>User Management</CardTitle>
              <CardDescription>Manage user accounts and their access levels</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>User</TableHead>
                    <TableHead>Role</TableHead>
                    <TableHead>Department</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Last Login</TableHead>
                    <TableHead className="text-right">Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {filteredUsers.map((user) => (
                    <TableRow key={user.id}>
                      <TableCell>
                        <div className="flex items-center gap-3">
                          <div className="h-8 w-8 rounded-full bg-admin-blue/10 flex items-center justify-center">
                            <span className="text-sm font-medium text-admin-blue">
                              {user.firstName[0]}{user.lastName[0]}
                            </span>
                          </div>
                          <div>
                            <p className="font-medium">{user.displayName}</p>
                            <p className="text-xs text-muted-foreground">{user.email}</p>
                          </div>
                        </div>
                      </TableCell>
                      <TableCell>
                        <Badge variant="outline">{user.role.replace(/_/g, ' ')}</Badge>
                      </TableCell>
                      <TableCell>
                        <span className="text-sm capitalize">{user.department.replace('-', ' ')}</span>
                      </TableCell>
                      <TableCell>
                        <Badge variant={user.isActive ? 'success' : 'secondary'}>
                          {user.isActive ? 'Active' : 'Inactive'}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        <span className="text-sm text-muted-foreground">
                          {user.lastLogin ? formatDateTime(user.lastLogin) : 'Never'}
                        </span>
                      </TableCell>
                      <TableCell className="text-right">
                        <DropdownMenu>
                          <DropdownMenuTrigger asChild>
                            <Button variant="ghost" size="icon">
                              <MoreHorizontal className="h-4 w-4" />
                            </Button>
                          </DropdownMenuTrigger>
                          <DropdownMenuContent align="end">
                            <DropdownMenuItem>
                              <Edit className="mr-2 h-4 w-4" />
                              Edit User
                            </DropdownMenuItem>
                            <DropdownMenuItem>
                              <Shield className="mr-2 h-4 w-4" />
                              Manage Permissions
                            </DropdownMenuItem>
                            <DropdownMenuItem>
                              <UserPlus className="mr-2 h-4 w-4" />
                              Reset Password
                            </DropdownMenuItem>
                            <DropdownMenuSeparator />
                            <DropdownMenuItem className="text-destructive">
                              <Trash2 className="mr-2 h-4 w-4" />
                              Delete User
                            </DropdownMenuItem>
                          </DropdownMenuContent>
                        </DropdownMenu>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Roles Tab */}
        <TabsContent value="roles">
          <Card>
            <CardHeader>
              <div className="flex items-center justify-between">
                <div>
                  <CardTitle>Role Management</CardTitle>
                  <CardDescription>Define roles and their associated permissions</CardDescription>
                </div>
                <Dialog>
                  <DialogTrigger asChild>
                    <Button variant="admin">
                      <Plus className="mr-2 h-4 w-4" />
                      Create Role
                    </Button>
                  </DialogTrigger>
                  <DialogContent className="max-w-2xl">
                    <DialogHeader>
                      <DialogTitle>Create New Role</DialogTitle>
                      <DialogDescription>
                        Create a custom role with specific permissions
                      </DialogDescription>
                    </DialogHeader>
                    <div className="grid gap-4 py-4">
                      <div className="grid gap-2">
                        <Label htmlFor="role-name">Role Name</Label>
                        <Input id="role-name" placeholder="e.g., Department Manager" />
                      </div>
                      <div className="grid gap-2">
                        <Label htmlFor="role-description">Description</Label>
                        <Input id="role-description" placeholder="Brief description of the role" />
                      </div>
                      <div className="grid gap-2">
                        <Label>Permissions</Label>
                        <div className="border rounded-md p-4 space-y-3 max-h-60 overflow-y-auto">
                          {['view:dashboard', 'view:users', 'create:users', 'edit:users', 'delete:users', 'view:incidents', 'create:incidents', 'resolve:incidents'].map((permission) => (
                            <div key={permission} className="flex items-center space-x-2">
                              <Checkbox id={permission} />
                              <Label htmlFor={permission} className="text-sm font-normal">
                                {permission}
                              </Label>
                            </div>
                          ))}
                        </div>
                      </div>
                    </div>
                    <DialogFooter>
                      <Button variant="outline">Cancel</Button>
                      <Button variant="admin">Create Role</Button>
                    </DialogFooter>
                  </DialogContent>
                </Dialog>
              </div>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Role</TableHead>
                    <TableHead>Description</TableHead>
                    <TableHead>Users</TableHead>
                    <TableHead>Type</TableHead>
                    <TableHead className="text-right">Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {filteredRoles.map((role) => (
                    <TableRow key={role.id}>
                      <TableCell className="font-medium">{role.name.replace(/_/g, ' ')}</TableCell>
                      <TableCell>
                        <span className="text-sm text-muted-foreground">{role.description}</span>
                      </TableCell>
                      <TableCell>
                        <Badge variant="outline">{role.userCount} users</Badge>
                      </TableCell>
                      <TableCell>
                        {role.isSystemRole ? (
                          <Badge variant="secondary">System</Badge>
                        ) : (
                          <Badge variant="info">Custom</Badge>
                        )}
                      </TableCell>
                      <TableCell className="text-right">
                        <DropdownMenu>
                          <DropdownMenuTrigger asChild>
                            <Button variant="ghost" size="icon">
                              <MoreHorizontal className="h-4 w-4" />
                            </Button>
                          </DropdownMenuTrigger>
                          <DropdownMenuContent align="end">
                            <DropdownMenuItem>
                              <Edit className="mr-2 h-4 w-4" />
                              Edit Role
                            </DropdownMenuItem>
                            <DropdownMenuItem>
                              <Shield className="mr-2 h-4 w-4" />
                              Manage Permissions
                            </DropdownMenuItem>
                            {!role.isSystemRole && (
                              <>
                                <DropdownMenuSeparator />
                                <DropdownMenuItem className="text-destructive">
                                  <Trash2 className="mr-2 h-4 w-4" />
                                  Delete Role
                                </DropdownMenuItem>
                              </>
                            )}
                          </DropdownMenuContent>
                        </DropdownMenu>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Permissions Tab */}
        <TabsContent value="permissions">
          <Card>
            <CardHeader>
              <CardTitle>Permission Matrix</CardTitle>
              <CardDescription>Overview of all system permissions and their assignments</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {['User Management', 'Incident Management', 'Security', 'Monitoring', 'Configuration', 'Compliance'].map((category) => (
                  <div key={category} className="border rounded-lg">
                    <div className="flex items-center justify-between p-4 bg-slate-50 dark:bg-slate-800">
                      <h4 className="font-medium">{category}</h4>
                      <Badge variant="outline">{Math.floor(Math.random() * 10) + 3} permissions</Badge>
                    </div>
                    <div className="p-4 grid grid-cols-2 md:grid-cols-3 gap-3">
                      {['view', 'create', 'edit', 'delete', 'manage'].map((action) => (
                        <div key={action} className="flex items-center gap-2 text-sm">
                          <Checkbox id={`${category}-${action}`} />
                          <Label htmlFor={`${category}-${action}`} className="font-normal text-muted-foreground">
                            {action}:{category.toLowerCase().replace(' ', '_')}
                          </Label>
                        </div>
                      ))}
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}
