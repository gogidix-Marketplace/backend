import * as React from 'react'
import { Upload as UploadIcon, File, X, Check, AlertCircle } from 'lucide-react'
import { cn } from '@lib/utils'
import { Button } from './button'
import { Progress } from './progress'

export interface FileUploadProps {
  onUpload?: (files: File[]) => Promise<{ success: boolean; url?: string; error?: string }[]>
  accept?: string
  multiple?: boolean
  maxSize?: number // in bytes
  maxFiles?: number
  disabled?: boolean
  className?: string
}

export interface UploadedFile {
  file: File
  id: string
  url?: string
  progress: number
  status: 'pending' | 'uploading' | 'success' | 'error'
  error?: string
}

export function FileUpload({
  onUpload,
  accept = '*/*',
  multiple = false,
  maxSize = 10 * 1024 * 1024, // 10MB default
  maxFiles = 5,
  disabled = false,
  className,
}: FileUploadProps) {
  const [uploadedFiles, setUploadedFiles] = React.useState<UploadedFile[]>([])
  const [isDragging, setIsDragging] = React.useState(false)
  const inputRef = React.useRef<HTMLInputElement>(null)

  const validateFile = (file: File): { valid: boolean; error?: string } => {
    if (file.size > maxSize) {
      return {
        valid: false,
        error: `File size exceeds ${formatFileSize(maxSize)}`,
      }
    }

    if (accept !== '*/*') {
      const acceptedTypes = accept.split(',').map((type) => type.trim())
      const fileType = file.type
      const fileExtension = '.' + file.name.split('.').pop()

      const isValid = acceptedTypes.some((type) => {
        if (type.startsWith('.')) {
          return fileExtension === type
        }
        return fileType.match(type)
      })

      if (!isValid) {
        return { valid: false, error: 'File type not accepted' }
      }
    }

    return { valid: true }
  }

  const handleFileSelect = (files: FileList) => {
    const remainingSlots = maxFiles - uploadedFiles.length
    const filesToProcess = Array.from(files).slice(0, remainingSlots)

    const newFiles: UploadedFile[] = []

    for (const file of filesToProcess) {
      const validation = validateFile(file)
      if (!validation.valid) {
        newFiles.push({
          file,
          id: Math.random().toString(36),
          progress: 0,
          status: 'error',
          error: validation.error,
        })
        continue
      }

      newFiles.push({
        file,
        id: Math.random().toString(36),
        progress: 0,
        status: 'pending',
      })
    }

    setUploadedFiles((prev) => [...prev, ...newFiles])

    // Upload files
    newFiles.forEach(async (uploadedFile) => {
      if (uploadedFile.status === 'error') return

      setUploadedFiles((prev) =>
        prev.map((f) =>
          f.id === uploadedFile.id ? { ...f, status: 'uploading' } : f
        )
      )

      if (onUpload) {
        try {
          // Simulate upload progress
          const progressInterval = setInterval(() => {
            setUploadedFiles((prev) =>
              prev.map((f) => {
                if (f.id === uploadedFile.id && f.status === 'uploading') {
                  const newProgress = Math.min(f.progress + 10, 90)
                  return { ...f, progress: newProgress }
                }
                return f
              })
            })
          }, 100)

          const results = await onUpload([uploadedFile.file])
          clearInterval(progressInterval)

          const result = results[0]
          if (result.success) {
            setUploadedFiles((prev) =>
              prev.map((f) =>
                f.id === uploadedFile.id
                  ? { ...f, progress: 100, status: 'success', url: result.url }
                  : f
              )
            )
          } else {
            setUploadedFiles((prev) =>
              prev.map((f) =>
                f.id === uploadedFile.id
                  ? { ...f, status: 'error', error: result.error || 'Upload failed' }
                  : f
              )
            )
          }
        } catch (error) {
          setUploadedFiles((prev) =>
            prev.map((f) =>
              f.id === uploadedFile.id
                ? { ...f, status: 'error', error: 'Upload failed' }
                : f
            )
          )
        }
      } else {
        // Simulate successful upload without backend
        const progressInterval = setInterval(() => {
          setUploadedFiles((prev) =>
            prev.map((f) => {
              if (f.id === uploadedFile.id && f.status === 'uploading') {
                const newProgress = Math.min(f.progress + 20, 100)
                if (newProgress === 100) {
                  clearInterval(progressInterval)
                  return { ...f, progress: 100, status: 'success' }
                }
                return { ...f, progress: newProgress }
              }
              return f
            })
          )
        }, 100)
      }
    })
  }

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault()
    setIsDragging(false)

    if (disabled) return

    const files = e.dataTransfer.files
    handleFileSelect(files)
  }

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      handleFileSelect(e.target.files)
    }
    // Reset input value to allow selecting the same file again
    e.target.value = ''
  }

  const handleRemoveFile = (id: string) => {
    setUploadedFiles((prev) => prev.filter((f) => f.id !== id))
  }

  const getFileIcon = (file: File) => {
    const extension = file.name.split('.').pop()?.toLowerCase()

    // Image files
    if (['jpg', 'jpeg', 'png', 'gif', 'webp', 'svg'].includes(extension || '')) {
      return '🖼️'
    }
    // PDF
    if (extension === 'pdf') {
      return '📄'
    }
    // Documents
    if (['doc', 'docx', 'txt'].includes(extension || '')) {
      return '📝'
    }
    // Spreadsheets
    if (['xls', 'xlsx', 'csv'].includes(extension || '')) {
      return '📊'
    }
    // Archives
    if (['zip', 'rar', '7z'].includes(extension || '')) {
      return '📦'
    }

    return '📎'
  }

  return (
    <div className={cn('w-full', className)}>
      {/* Drop Zone */}
      <div
        onDrop={handleDrop}
        onDragOver={(e) => {
          e.preventDefault()
          setIsDragging(true)
        }}
        onDragLeave={() => setIsDragging(false)}
        className={cn(
          'relative border-2 border-dashed rounded-lg p-6 text-center transition-colors',
          isDragging
            ? 'border-primary-500 bg-primary-50'
            : 'border-gray-300 hover:border-gray-400',
          disabled && 'opacity-50 cursor-not-allowed'
        )}
      >
        <input
          ref={inputRef}
          type="file"
          accept={accept}
          multiple={multiple}
          onChange={handleInputChange}
          disabled={disabled || uploadedFiles.length >= maxFiles}
          className="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
        />

        <UploadIcon className="mx-auto h-12 w-12 text-gray-400 mb-3" />
        <p className="text-sm text-gray-600 mb-1">
          {isDragging ? 'Drop files here' : 'Drag and drop files here'}
        </p>
        <p className="text-xs text-gray-500">
          or{' '}
          <button
            type="button"
            onClick={() => inputRef.current?.click()}
            disabled={disabled || uploadedFiles.length >= maxFiles}
            className="text-primary-600 hover:text-primary-700 font-medium underline"
          >
            browse
          </button>
        </p>
        <p className="text-xs text-gray-400 mt-2">
          Max file size: {formatFileSize(maxSize)}
          {maxFiles > 1 && ` • Max ${maxFiles} files`}
        </p>
      </div>

      {/* File List */}
      {uploadedFiles.length > 0 && (
        <div className="mt-4 space-y-2">
          {uploadedFiles.map((uploadedFile) => (
            <div
              key={uploadedFile.id}
              className="flex items-center gap-3 p-3 bg-white border border-gray-200 rounded-lg"
            >
              <span className="text-2xl">{getFileIcon(uploadedFile.file)}</span>

              <div className="flex-1 min-w-0">
                <p className="text-sm font-medium text-gray-900 truncate">
                  {uploadedFile.file.name}
                </p>
                <p className="text-xs text-gray-500">
                  {formatFileSize(uploadedFile.file.size)}
                </p>

                {/* Progress Bar */}
                {uploadedFile.status === 'uploading' && (
                  <Progress value={uploadedFile.progress} className="h-1 mt-1" />
                )}

                {/* Error Message */}
                {uploadedFile.status === 'error' && (
                  <p className="text-xs text-red-600 flex items-center gap-1 mt-1">
                    <AlertCircle className="h-3 w-3" />
                    {uploadedFile.error}
                  </p>
                )}
              </div>

              {/* Status Icon */}
              <div className="flex-shrink-0">
                {uploadedFile.status === 'uploading' && (
                  <div className="h-5 w-5 animate-spin rounded-full border-2 border-primary-600 border-t-transparent" />
                )}
                {uploadedFile.status === 'success' && (
                  <Check className="h-5 w-5 text-green-600" />
                )}
                {uploadedFile.status === 'error' && (
                  <AlertCircle className="h-5 w-5 text-red-600" />
                )}
              </div>

              {/* Remove Button */}
              <button
                type="button"
                onClick={() => handleRemoveFile(uploadedFile.id)}
                disabled={uploadedFile.status === 'uploading'}
                className="flex-shrink-0 p-1 text-gray-400 hover:text-gray-600 disabled:opacity-50"
              >
                <X className="h-5 w-5" />
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

function formatFileSize(bytes: number): string {
  if (bytes === 0) return '0 Bytes'

  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))

  return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i]
}
