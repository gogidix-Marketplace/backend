import * as React from 'react'
import { cn } from '@shared/utils/cn'
import { Button } from '@shared/components/ui/button'
import { Progress } from '@shared/components/ui/progress'
import { Upload, X, File, Image as ImageIcon, FileText, Film, Music } from 'lucide-react'

/**
 * FormUpload - File upload with preview
 */

export interface FileWithPreview extends File {
  preview?: string
  id: string
  progress?: number
  error?: string
}

export interface FormUploadProps {
  value?: FileWithPreview[]
  onChange?: (files: FileWithPreview[]) => void
  accept?: string
  multiple?: boolean
  maxSize?: number // In bytes
  maxFiles?: number
  disabled?: boolean
  label?: string
  error?: string
  hint?: string
  required?: boolean
  className?: string
  variant?: 'default' | 'dropzone' | 'compact'
}

const getFileIcon = (type: string) => {
  if (type.startsWith('image/')) return ImageIcon
  if (type.startsWith('video/')) return Film
  if (type.startsWith('audio/')) return Music
  if (type.startsWith('text/') || type.includes('pdf')) return FileText
  return File
}

const formatFileSize = (bytes: number): string => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i]
}

export function FormUpload({
  value = [],
  onChange,
  accept,
  multiple = false,
  maxSize = 10 * 1024 * 1024, // 10MB
  maxFiles = 5,
  disabled = false,
  label,
  error,
  hint,
  required,
  className,
  variant = 'default',
}: FormUploadProps) {
  const inputRef = React.useRef<HTMLInputElement>(null)
  const [isDragging, setIsDragging] = React.useState(false)

  const handleFileSelect = (files: FileList | null) => {
    if (!files) return

    const newFiles: FileWithPreview[] = Array.from(files)
      .slice(0, maxFiles - value.length)
      .map((file) => ({
        ...file,
        id: Math.random().toString(36).substr(2, 9),
        preview: file.type.startsWith('image/')
          ? URL.createObjectURL(file)
          : undefined,
      }))

    // Validate file sizes
    const oversized = newFiles.filter((f) => f.size > maxSize)
    if (oversized.length > 0) {
      oversized.forEach((f) => {
        Object.defineProperty(f, 'error', {
          value: `File exceeds maximum size of ${formatFileSize(maxSize)}`,
          writable: false,
        })
      })
    }

    onChange?.([...value, ...newFiles].slice(0, maxFiles))
  }

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault()
    setIsDragging(false)
    handleFileSelect(e.dataTransfer.files)
  }

  const handleDragOver = (e: React.DragEvent) => {
    e.preventDefault()
    setIsDragging(true)
  }

  const handleDragLeave = () => {
    setIsDragging(false)
  }

  const removeFile = (id: string) => {
    onChange?.(value.filter((f) => f.id !== id))
  }

  const handleClick = () => {
    inputRef.current?.click()
  }

  const renderDropzone = () => {
    return (
      <div
        onClick={disabled ? undefined : handleClick}
        onDrop={disabled ? undefined : handleDrop}
        onDragOver={disabled ? undefined : handleDragOver}
        onDragLeave={disabled ? undefined : handleDragLeave}
        className={cn(
          'relative flex flex-col items-center justify-center rounded-lg border-2 border-dashed p-8 text-center transition-colors',
          isDragging && 'border-primary bg-primary/5',
          !disabled && 'cursor-pointer hover:border-primary/50 hover:bg-muted/50',
          disabled && 'cursor-not-allowed opacity-50',
          error && 'border-destructive',
          className
        )}
      >
        <input
          ref={inputRef}
          type="file"
          accept={accept}
          multiple={multiple}
          onChange={(e) => handleFileSelect(e.target.files)}
          disabled={disabled}
          className="hidden"
        />

        <div className="mx-auto flex h-12 w-12 items-center justify-center rounded-full bg-primary/10">
          <Upload className="h-6 w-6 text-primary" />
        </div>

        <div className="mt-4 space-y-1">
          <p className="text-sm font-medium">
            {multiple ? 'Drop files here or click to upload' : 'Drop a file here or click to upload'}
          </p>
          <p className="text-xs text-muted-foreground">
            Maximum file size: {formatFileSize(maxSize)}
            {maxFiles > 1 && ` • Up to ${maxFiles} files`}
          </p>
        </div>

        {variant === 'default' && (
          <Button
            type="button"
            variant="outline"
            size="sm"
            className="mt-4"
            disabled={disabled}
            onClick={(e) => {
              e.stopPropagation()
              handleClick()
            }}
          >
            Browse Files
          </Button>
        )}
      </div>
    )
  }

  const renderFileList = () => {
    if (value.length === 0) return null

    return (
      <div className="space-y-2 mt-4">
        {value.map((file) => {
          const FileIcon = getFileIcon(file.type)

          return (
            <div
              key={file.id}
              className={cn(
                'flex items-center gap-3 rounded-lg border p-3',
                file.error && 'border-destructive bg-destructive/5'
              )}
            >
              {file.preview ? (
                <img
                  src={file.preview}
                  alt={file.name}
                  className="h-10 w-10 rounded object-cover"
                />
              ) : (
                <div className="flex h-10 w-10 items-center justify-center rounded bg-muted">
                  <FileIcon className="h-5 w-5 text-muted-foreground" />
                </div>
              )}

              <div className="flex min-w-0 flex-1">
                <p className="truncate text-sm font-medium">{file.name}</p>
                <p className="text-xs text-muted-foreground">
                  {' • '}
                  {file.progress !== undefined ? (
                    <span>Uploading {file.progress}%</span>
                  ) : (
                    formatFileSize(file.size)
                  )}
                </p>
                {file.error && (
                  <p className="text-xs text-destructive block">{file.error}</p>
                )}
              </div>

              {file.progress !== undefined && file.progress < 100 && (
                <div className="w-24">
                  <Progress value={file.progress} className="h-1" />
                </div>
              )}

              <Button
                type="button"
                variant="ghost"
                size="sm"
                disabled={disabled}
                onClick={() => removeFile(file.id)}
              >
                <X className="h-4 w-4" />
              </Button>
            </div>
          )
        })}
      </div>
    )
  }

  return (
    <div className="space-y-2">
      {label && (
        <label className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
          {label}
          {required && <span className="text-destructive ml-1">*</span>}
        </label>
      )}

      {variant === 'compact' ? (
        <div className="flex gap-2">
          <input
            ref={inputRef}
            type="file"
            accept={accept}
            multiple={multiple}
            onChange={(e) => handleFileSelect(e.target.files)}
            disabled={disabled}
            className="hidden"
          />
          <Button
            type="button"
            variant="outline"
            disabled={disabled || value.length >= maxFiles}
            onClick={handleClick}
          >
            <Upload className="mr-2 h-4 w-4" />
            Choose Files
          </Button>
          {value.length > 0 && (
            <span className="text-sm text-muted-foreground py-2">
              {value.length} file{value.length !== 1 ? 's' : ''} selected
            </span>
          )}
        </div>
      ) : (
        renderDropzone()
      )}

      {renderFileList()}

      {error && <p className="text-sm text-destructive">{error}</p>}
      {hint && !error && <p className="text-sm text-muted-foreground">{hint}</p>}
    </div>
  )
}
