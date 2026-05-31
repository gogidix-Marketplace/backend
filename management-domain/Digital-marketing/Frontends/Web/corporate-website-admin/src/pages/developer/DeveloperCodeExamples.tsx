import React from 'react';
import { Box, Grid, Card, CardContent, Typography, Chip, Stack } from '@mui/material';
import { Code as CodeIcon } from '@mui/icons-material';
import PageHeader from '@/components/common/PageHeader';

const codeExamples = [
  {
    title: 'Authentication Flow',
    language: 'JavaScript',
    category: 'Authentication',
    code: 'const response = await fetch("/api/auth/login", {\n  method: "POST",\n  body: JSON.stringify({ email, password })\n});',
  },
  {
    title: 'Fetch Products',
    language: 'JavaScript',
    category: 'Products',
    code: 'const products = await client.products.list({\n  limit: 10,\n  page: 1\n});',
  },
  {
    title: 'Create Page',
    language: 'Python',
    category: 'Content',
    code: 'page = client.pages.create(\n  title="My Page",\n  content="<p>Hello World</p>"\n)',
  },
  {
    title: 'Handle Webhooks',
    language: 'Node.js',
    category: 'Webhooks',
    code: 'app.post("/webhooks", (req, res) => {\n  const event = req.body;\n  console.log("Received:", event);\n});',
  },
];

const DeveloperCodeExamples: React.FC = () => {
  return (
    <Box>
      <PageHeader
        title="Code Examples"
        subtitle="Sample code snippets and implementation examples"
      />

      <Grid container spacing={3}>
        {codeExamples.map((example, index) => (
          <Grid item xs={12} md={6} key={index}>
            <Card>
              <CardContent>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', mb: 2 }}>
                  <Box>
                    <Typography variant="h6">{example.title}</Typography>
                    <Stack direction="row" spacing={1} sx={{ mt: 1 }}>
                      <Chip label={example.language} size="small" />
                      <Chip label={example.category} size="small" variant="outlined" />
                    </Stack>
                  </Box>
                  <CodeIcon color="action" />
                </Box>
                <Box
                  sx={{
                    bgcolor: 'background.default',
                    p: 2,
                    borderRadius: 1,
                    fontFamily: 'monospace',
                    fontSize: 12,
                    overflow: 'auto',
                  }}
                >
                  <pre style={{ margin: 0 }}>{example.code}</pre>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default DeveloperCodeExamples;
