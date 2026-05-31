import React, { useState, useCallback } from 'react';
import {
  Box,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Grid,
  Card,
  CardMedia,
  CardActions,
  IconButton,
  Typography,
  TextField,
  InputAdornment,
  Chip,
  Checkbox,
  Toolbar,
  Tooltip,
} from '@mui/material';
import {
  Search,
  Upload,
  Delete,
  Close,
  InsertDriveFile,
  Image,
  VideoLibrary,
  PictureAsPdf,
} from '@mui/icons-material';
import { useDropzone } from 'react-dropzone';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getMediaFiles, uploadMediaFile, deleteMediaFile } from '@/services/mediaApi';
import { queryKeys } from '@/services/api';
import { useSnackbar } from '@/contexts/SnackbarContext';

interface MediaLibraryProps {
  open: boolean;
  onClose: () => void;
  onSelect: (files: string[]) => void;
  multiple?: boolean;
  accept?: string[];
  maxSize?: number;
}

const MediaLibrary: React.FC<MediaLibraryProps> = ({
  open,
  onClose,
  onSelect,
  multiple = false,
  accept,
  maxSize = 10 * 1024 * 1024,
}) => {
  const [search, setSearch] = useState('');
  const [selectedIds, setSelectedIds] = useState<string[]>([]);
  const [uploading, setUploading] = useState(false);
  const [uploadProgress, setUploadProgress] = useState(0);

  const { showSuccess, showError } = useSnackbar();
  const queryClient = useQueryClient();

  const { data: mediaData, isLoading } = useQuery({
    queryKey: queryKeys.media.all(),
    queryFn: () => getMediaFiles({ limit: 100 }),
    enabled: open,
  });

  const deleteMutation = useMutation({
    mutationFn: deleteMediaFile,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.media.all() });
      showSuccess('File deleted successfully');
    },
    onError: () => {
      showError('Failed to delete file');
    },
  });

  const onDrop = useCallback(async (acceptedFiles: File[]) => {
    setUploading(true);
    try {
      for (const file of acceptedFiles) {
        await uploadMediaFile(file, (progress) => {
          setUploadProgress(progress);
        });
      }
      queryClient.invalidateQueries({ queryKey: queryKeys.media.all() });
      showSuccess(`${acceptedFiles.length} file(s) uploaded successfully`);
    } catch (error) {
      showError('Failed to upload file(s)');
    } finally {
      setUploading(false);
      setUploadProgress(0);
    }
  }, [queryClient, showSuccess, showError]);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: accept?.reduce((acc, type) => ({ ...acc, [type]: [] }), {}) || {
      'image/*': ['.png', '.jpg', '.jpeg', '.gif', '.webp'],
    },
    maxSize,
    multiple: true,
  });

  const handleSelect = () => {
    onSelect(selectedIds);
    handleClose();
  };

  const handleClose = () => {
    setSelectedIds([]);
    setSearch('');
    onClose();
  };

  const toggleSelection = (id: string) => {
    if (multiple) {
      setSelectedIds((prev) =>
        prev.includes(id) ? prev.filter((x) => x !== id) : [...prev, id]
      );
    } else {
      setSelectedIds([id]);
    }
  };

  const handleDelete = (id: string, e: React.MouseEvent) => {
    e.stopPropagation();
    if (window.confirm('Are you sure you want to delete this file?')) {
      deleteMutation.mutate(id);
    }
  };

  const getFileIcon = (mimeType: string) => {
    if (mimeType.startsWith('image/')) return <Image />;
    if (mimeType.startsWith('video/')) return <VideoLibrary />;
    if (mimeType === 'application/pdf') return <PictureAsPdf />;
    return <InsertDriveFile />;
  };

  const filteredFiles = mediaData?.files?.filter((file) =>
    file.name.toLowerCase().includes(search.toLowerCase()) ||
    file.tags?.some((tag) => tag.toLowerCase().includes(search.toLowerCase()))
  ) || [];

  return (
    <Dialog
      open={open}
      onClose={handleClose}
      maxWidth="lg"
      fullWidth
      PaperProps={{
        sx: { height: '80vh', display: 'flex', flexDirection: 'column' },
      }}
    >
      <DialogTitle>Media Library</DialogTitle>
      <DialogContent sx={{ flex: 1, overflow: 'hidden', display: 'flex', flexDirection: 'column' }}>
        <Box
          {...getRootProps()}
          sx={{
            border: '2px dashed',
            borderColor: isDragActive ? 'primary.main' : 'divider',
            borderRadius: 2,
            p: 3,
            mb: 2,
            textAlign: 'center',
            cursor: 'pointer',
            bgcolor: isDragActive ? 'action.hover' : 'transparent',
            transition: 'all 0.2s',
          }}
        >
          <input {...getInputProps()} />
          <Upload sx={{ fontSize: 48, color: 'text.secondary', mb: 1 }} />
          <Typography variant="body1" gutterBottom>
            {isDragActive ? 'Drop files here' : 'Drag & drop files here, or click to browse'}
          </Typography>
          <Typography variant="caption" color="text.secondary">
            Max file size: {maxSize / 1024 / 1024}MB
          </Typography>
          {uploading && (
            <Box sx={{ mt: 2 }}>
              <Typography variant="body2">Uploading... {uploadProgress}%</Typography>
            </Box>
          )}
        </Box>

        <TextField
          fullWidth
          placeholder="Search files..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <Search />
              </InputAdornment>
            ),
          }}
          sx={{ mb: 2 }}
        />

        {isLoading ? (
          <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
            <Typography>Loading media...</Typography>
          </Box>
        ) : (
          <Grid container spacing={2} sx={{ overflowY: 'auto', flex: 1 }}>
            {filteredFiles.map((file) => (
              <Grid item xs={6} sm={4} md={3} key={file.id}>
                <Card
                  onClick={() => toggleSelection(file.id)}
                  sx={{
                    cursor: 'pointer',
                    position: 'relative',
                    border: selectedIds.includes(file.id)
                      ? '2px solid'
                      : '1px solid',
                    borderColor: selectedIds.includes(file.id)
                      ? 'primary.main'
                      : 'divider',
                  }}
                >
                  {file.mimeType.startsWith('image/') ? (
                    <CardMedia
                      component="img"
                      image={file.thumbnailUrl || file.url}
                      alt={file.name}
                      sx={{
                        height: 120,
                        objectFit: 'cover',
                      }}
                    />
                  ) : (
                    <Box
                      sx={{
                        height: 120,
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        bgcolor: 'action.hover',
                      }}
                    >
                      {getFileIcon(file.mimeType)}
                    </Box>
                  )}
                  <Checkbox
                    checked={selectedIds.includes(file.id)}
                    sx={{
                      position: 'absolute',
                      top: 8,
                      left: 8,
                    }}
                    onClick={(e) => {
                      e.stopPropagation();
                      toggleSelection(file.id);
                    }}
                  />
                  <CardActions
                    sx={{
                      position: 'absolute',
                      top: 8,
                      right: 8,
                      padding: 0,
                    }}
                  >
                    <Tooltip title="Delete">
                      <IconButton
                        size="small"
                        onClick={(e) => handleDelete(file.id, e)}
                        sx={{ bgcolor: 'background.paper' }}
                      >
                        <Delete fontSize="small" />
                      </IconButton>
                    </Tooltip>
                  </CardActions>
                  <Box sx={{ p: 1 }}>
                    <Typography
                      variant="caption"
                      noWrap
                      title={file.name}
                    >
                      {file.name}
                    </Typography>
                    <Typography variant="caption" display="block" color="text.secondary">
                      {(file.size / 1024).toFixed(1)} KB
                    </Typography>
                    {file.tags?.slice(0, 2).map((tag) => (
                      <Chip
                        key={tag}
                        label={tag}
                        size="small"
                        sx={{ fontSize: 8, height: 20, mr: 0.5, mt: 0.5 }}
                      />
                    ))}
                  </Box>
                </Card>
              </Grid>
            ))}
          </Grid>
        )}

        {filteredFiles.length === 0 && !isLoading && (
          <Box
            sx={{
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              justifyContent: 'center',
              py: 8,
            }}
          >
            <InsertDriveFile sx={{ fontSize: 64, color: 'text.disabled' }} />
            <Typography variant="h6" color="text.secondary" sx={{ mt: 2 }}>
              No media files found
            </Typography>
          </Box>
        )}
      </DialogContent>
      <DialogActions>
        <Toolbar sx={{ justifyContent: 'space-between', width: '100%', pl: 0 }}>
          <Typography variant="caption" color="text.secondary">
            {selectedIds.length} file(s) selected
          </Typography>
          <Box>
            <Button onClick={handleClose}>Cancel</Button>
            <Button
              onClick={handleSelect}
              variant="contained"
              disabled={selectedIds.length === 0}
            >
              {multiple ? 'Select Files' : 'Select File'}
            </Button>
          </Box>
        </Toolbar>
      </DialogActions>
    </Dialog>
  );
};

export default MediaLibrary;
