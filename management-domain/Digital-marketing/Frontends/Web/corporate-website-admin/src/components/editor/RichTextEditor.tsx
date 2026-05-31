import React, { useCallback } from 'react';
import { useEditor, EditorContent } from '@tiptap/react';
import StarterKit from '@tiptap/starter-kit';
import Image from '@tiptap/extension-image';
import Link from '@tiptap/extension-link';
import Table from '@tiptap/extension-table';
import TableRow from '@tiptap/extension-table-row';
import TableCell from '@tiptap/extension-table-cell';
import TableHeader from '@tiptap/extension-table-header';
import TextAlign from '@tiptap/extension-text-align';
import Color from '@tiptap/extension-color';
import TextStyle from '@tiptap/extension-text-style';
import {
  Box,
  Toolbar,
  ButtonGroup,
  Button,
  Divider,
  Paper,
  IconButton,
} from '@mui/material';
import {
  FormatBold,
  FormatItalic,
  FormatUnderlined,
  StrikethroughS,
  Code,
  FormatListBulleted,
  FormatListNumbered,
  FormatQuote,
  Undo,
  Redo,
  Link as LinkIcon,
  Image as ImageIcon,
  TableChart,
  FormatAlignLeft,
  FormatAlignCenter,
  FormatAlignRight,
  FormatAlignJustify,
} from '@mui/icons-material';
import { uploadMediaFile } from '@/services/mediaApi';

interface RichTextEditorProps {
  content: string;
  onChange: (content: string) => void;
  placeholder?: string;
  editable?: boolean;
  minHeight?: string | number;
}

const MenuBar: React.FC<{ editor: any; onImageUpload: (file: File) => void }> = ({ editor, onImageUpload }) => {
  const handleImageUpload = () => {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = 'image/*';
    input.onchange = async (e) => {
      const file = (e.target as HTMLInputElement).files?.[0];
      if (file) {
        onImageUpload(file);
      }
    };
    input.click();
  };

  if (!editor) {
    return null;
  }

  return (
    <Toolbar variant="dense" sx={{ borderBottom: '1px solid', borderColor: 'divider', flexWrap: 'wrap' }}>
      <ButtonGroup size="small" sx={{ mr: 1 }}>
        <Button
          onClick={() => editor.chain().focus().toggleBold().run()}
          disabled={!editor.can().chain().focus().toggleBold().run()}
          color={editor.isActive('bold') ? 'primary' : 'inherit'}
          variant={editor.isActive('bold') ? 'contained' : 'outlined'}
        >
          <FormatBold />
        </Button>
        <Button
          onClick={() => editor.chain().focus().toggleItalic().run()}
          disabled={!editor.can().chain().focus().toggleItalic().run()}
          color={editor.isActive('italic') ? 'primary' : 'inherit'}
          variant={editor.isActive('italic') ? 'contained' : 'outlined'}
        >
          <FormatItalic />
        </Button>
        <Button
          onClick={() => editor.chain().focus().toggleStrike().run()}
          disabled={!editor.can().chain().focus().toggleStrike().run()}
          color={editor.isActive('strike') ? 'primary' : 'inherit'}
          variant={editor.isActive('strike') ? 'contained' : 'outlined'}
        >
          <StrikethroughS />
        </Button>
        <Button
          onClick={() => editor.chain().focus().toggleCode().run()}
          disabled={!editor.can().chain().focus().toggleCode().run()}
          color={editor.isActive('code') ? 'primary' : 'inherit'}
          variant={editor.isActive('code') ? 'contained' : 'outlined'}
        >
          <Code />
        </Button>
      </ButtonGroup>

      <Divider orientation="vertical" flexItem sx={{ mx: 1 }} />

      <ButtonGroup size="small" sx={{ mr: 1 }}>
        <Button
          onClick={() => editor.chain().focus().toggleHeading({ level: 1 }).run()}
          color={editor.isActive('heading', { level: 1 }) ? 'primary' : 'inherit'}
          variant={editor.isActive('heading', { level: 1 }) ? 'contained' : 'outlined'}
        >
          H1
        </Button>
        <Button
          onClick={() => editor.chain().focus().toggleHeading({ level: 2 }).run()}
          color={editor.isActive('heading', { level: 2 }) ? 'primary' : 'inherit'}
          variant={editor.isActive('heading', { level: 2 }) ? 'contained' : 'outlined'}
        >
          H2
        </Button>
        <Button
          onClick={() => editor.chain().focus().toggleHeading({ level: 3 }).run()}
          color={editor.isActive('heading', { level: 3 }) ? 'primary' : 'inherit'}
          variant={editor.isActive('heading', { level: 3 }) ? 'contained' : 'outlined'}
        >
          H3
        </Button>
      </ButtonGroup>

      <Divider orientation="vertical" flexItem sx={{ mx: 1 }} />

      <ButtonGroup size="small" sx={{ mr: 1 }}>
        <Button
          onClick={() => editor.chain().focus().toggleBulletList().run()}
          color={editor.isActive('bulletList') ? 'primary' : 'inherit'}
          variant={editor.isActive('bulletList') ? 'contained' : 'outlined'}
        >
          <FormatListBulleted />
        </Button>
        <Button
          onClick={() => editor.chain().focus().toggleOrderedList().run()}
          color={editor.isActive('orderedList') ? 'primary' : 'inherit'}
          variant={editor.isActive('orderedList') ? 'contained' : 'outlined'}
        >
          <FormatListNumbered />
        </Button>
        <Button
          onClick={() => editor.chain().focus().toggleBlockquote().run()}
          color={editor.isActive('blockquote') ? 'primary' : 'inherit'}
          variant={editor.isActive('blockquote') ? 'contained' : 'outlined'}
        >
          <FormatQuote />
        </Button>
      </ButtonGroup>

      <Divider orientation="vertical" flexItem sx={{ mx: 1 }} />

      <ButtonGroup size="small" sx={{ mr: 1 }}>
        <Button
          onClick={() => editor.chain().focus().setTextAlign('left').run()}
          color={editor.isActive({ textAlign: 'left' }) ? 'primary' : 'inherit'}
          variant={editor.isActive({ textAlign: 'left' }) ? 'contained' : 'outlined'}
        >
          <FormatAlignLeft />
        </Button>
        <Button
          onClick={() => editor.chain().focus().setTextAlign('center').run()}
          color={editor.isActive({ textAlign: 'center' }) ? 'primary' : 'inherit'}
          variant={editor.isActive({ textAlign: 'center' }) ? 'contained' : 'outlined'}
        >
          <FormatAlignCenter />
        </Button>
        <Button
          onClick={() => editor.chain().focus().setTextAlign('right').run()}
          color={editor.isActive({ textAlign: 'right' }) ? 'primary' : 'inherit'}
          variant={editor.isActive({ textAlign: 'right' }) ? 'contained' : 'outlined'}
        >
          <FormatAlignRight />
        </Button>
        <Button
          onClick={() => editor.chain().focus().setTextAlign('justify').run()}
          color={editor.isActive({ textAlign: 'justify' }) ? 'primary' : 'inherit'}
          variant={editor.isActive({ textAlign: 'justify' }) ? 'contained' : 'outlined'}
        >
          <FormatAlignJustify />
        </Button>
      </ButtonGroup>

      <Divider orientation="vertical" flexItem sx={{ mx: 1 }} />

      <ButtonGroup size="small" sx={{ mr: 1 }}>
        <Button
          onClick={() => editor.chain().focus().toggleLink().run()}
          color={editor.isActive('link') ? 'primary' : 'inherit'}
          variant={editor.isActive('link') ? 'contained' : 'outlined'}
        >
          <LinkIcon />
        </Button>
        <Button onClick={handleImageUpload}>
          <ImageIcon />
        </Button>
        <Button
          onClick={() => editor.chain().focus().insertTable({ rows: 3, cols: 3, withHeaderRow: true }).run()}
        >
          <TableChart />
        </Button>
      </ButtonGroup>

      <Divider orientation="vertical" flexItem sx={{ mx: 1 }} />

      <ButtonGroup size="small">
        <Button
          onClick={() => editor.chain().focus().undo().run()}
          disabled={!editor.can().chain().focus().undo().run()}
        >
          <Undo />
        </Button>
        <Button
          onClick={() => editor.chain().focus().redo().run()}
          disabled={!editor.can().chain().focus().redo().run()}
        >
          <Redo />
        </Button>
      </ButtonGroup>
    </Toolbar>
  );
};

const RichTextEditor: React.FC<RichTextEditorProps> = ({
  content,
  onChange,
  placeholder = 'Start typing...',
  editable = true,
  minHeight = 300,
}) => {
  const editor = useEditor({
    extensions: [
      StarterKit.configure({
        heading: {
          levels: [1, 2, 3, 4],
        },
      }),
      Image.configure({
        inline: true,
        allowBase64: false,
      }),
      Link.configure({
        openOnClick: false,
        HTMLAttributes: {
          target: '_blank',
        },
      }),
      Table.configure({
        resizable: true,
      }),
      TableRow,
      TableHeader,
      TableCell,
      TextAlign.configure({
        types: ['heading', 'paragraph'],
      }),
      Color,
      TextStyle,
    ],
    content,
    editable,
    onUpdate: ({ editor }) => {
      onChange(editor.getHTML());
    },
    editorProps: {
      attributes: {
        class: 'rich-text-editor',
      },
    },
  });

  const handleImageUpload = useCallback(async (file: File) => {
    try {
      const response = await uploadMediaFile(file);
      if (editor && response.file.url) {
        editor.chain().focus().setImage({ src: response.file.url }).run();
      }
    } catch (error) {
      console.error('Failed to upload image:', error);
    }
  }, [editor]);

  return (
    <Paper
      variant="outlined"
      sx={{
        '& .ProseMirror': {
          minHeight,
          padding: 2,
          outline: 'none',
          '&:focus': {
            outline: 'none',
          },
          '& p.is-editor-empty:first-child::before': {
            color: 'text.disabled',
            content: 'attr(data-placeholder)',
            float: 'left',
            height: 0,
            pointerEvents: 'none',
          },
          '& table': {
            borderCollapse: 'collapse',
            tableLayout: 'fixed',
            width: '100%',
            margin: '0',
            overflow: 'hidden',
            td, th: {
              minWidth: '1em',
              border: '1px solid #e2e8f0',
              padding: '3px 5px',
              verticalAlign: 'top',
              position: 'relative',
              '> *': {
                marginBottom: 0,
              },
            },
            th: {
              fontWeight: 'bold',
              textAlign: 'left',
              backgroundColor: '#f1f5f9',
            },
            '.selectedCell:after': {
              zIndex: 2,
              position: 'absolute',
              content: '""',
              left: 0, right: 0, top: 0, bottom: 0,
              background: 'rgba(200, 200, 255, 0.4)',
              pointerEvents: 'none',
            },
            '.column-resize-handle': {
              position: 'absolute',
              right: -2,
              top: 0,
              bottom: -2,
              width: 4,
              backgroundColor: '#adf',
              pointerEvents: 'none',
            },
          },
          '& img': {
            maxWidth: '100%',
            height: 'auto',
          },
          '& a': {
            color: 'primary.main',
            textDecoration: 'underline',
          },
        },
      }}
    >
      {editable && <MenuBar editor={editor} onImageUpload={handleImageUpload} />}
      <EditorContent
        editor={editor}
        placeholder={placeholder}
      />
    </Paper>
  );
};

export default RichTextEditor;
