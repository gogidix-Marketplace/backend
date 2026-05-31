import React from 'react';
import {
  Box,
  List,
  ListItem,
  ListItemAvatar,
  Avatar,
  ListItemText,
  Typography,
  Divider,
} from '@mui/material';
import {
  Article as ArticleIcon,
  Comment as CommentIcon,
  Person as PersonIcon,
  ShoppingCart as CartIcon,
} from '@mui/icons-material';

interface ActivityItem {
  id: string;
  type: 'content' | 'comment' | 'user' | 'order';
  title: string;
  description: string;
  time: string;
  avatar?: string;
}

const activities: ActivityItem[] = [
  {
    id: '1',
    type: 'content',
    title: 'New blog post published',
    description: 'How to Build Scalable React Applications',
    time: '5 minutes ago',
  },
  {
    id: '2',
    type: 'user',
    title: 'New user registered',
    description: 'john@example.com',
    time: '15 minutes ago',
  },
  {
    id: '3',
    type: 'order',
    title: 'Demo request received',
    description: 'Acme Corporation - Enterprise Plan',
    time: '1 hour ago',
  },
  {
    id: '4',
    type: 'comment',
    title: 'New comment on blog',
    description: 'Great article! Very helpful.',
    time: '2 hours ago',
  },
  {
    id: '5',
    type: 'content',
    title: 'Page updated',
    description: 'Pricing page - updated plans',
    time: '3 hours ago',
  },
];

const getIcon = (type: ActivityItem['type']) => {
  switch (type) {
    case 'content':
      return <ArticleIcon />;
    case 'comment':
      return <CommentIcon />;
    case 'user':
      return <PersonIcon />;
    case 'order':
      return <CartIcon />;
    default:
      return <ArticleIcon />;
  }
};

const getColor = (type: ActivityItem['type']) => {
  switch (type) {
    case 'content':
      return 'primary.main';
    case 'comment':
      return 'secondary.main';
    case 'user':
      return 'success.main';
    case 'order':
      return 'warning.main';
    default:
      return 'primary.main';
  }
};

const RecentActivityTable: React.FC = () => {
  return (
    <List sx={{ py: 0 }}>
      {activities.map((activity, index) => (
        <React.Fragment key={activity.id}>
          <ListItem
            alignItems="flex-start"
            sx={{ px: 0, py: 1.5 }}
          >
            <ListItemAvatar>
              <Avatar sx={{ bgcolor: getColor(activity.type) }}>
                {getIcon(activity.type)}
              </Avatar>
            </ListItemAvatar>
            <ListItemText
              primary={
                <Typography variant="body2" fontWeight={600}>
                  {activity.title}
                </Typography>
              }
              secondary={
                <Box>
                  <Typography variant="body2" color="text.secondary">
                    {activity.description}
                  </Typography>
                  <Typography variant="caption" color="text.disabled">
                    {activity.time}
                  </Typography>
                </Box>
              }
            />
          </ListItem>
          {index < activities.length - 1 && <Divider />}
        </React.Fragment>
      ))}
    </List>
  );
};

export default RecentActivityTable;
